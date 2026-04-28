#!/usr/bin/env bash
# JSON Template - Generate JSON structures for sprints, stories, and more
# Usage: json-template.sh <type> [options]
#
# Types: sprint, story, backlog, feature, agent
#
# Examples:
#   json-template.sh sprint --number 1 --goal "User Authentication"
#   json-template.sh story --id US-101 --title "User login" --points 5

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ============================================================================
# JSON TEMPLATES
# ============================================================================

# Generate sprint JSON
generate_sprint() {
    local number="${1:-1}"
    local goal="${2:-}"
    local start_date="${3:-$(date -I)}"
    local end_date="${4:-}"
    local velocity="${5:-20}"
    local status="${6:-not_started}"

    # Calculate end date if not provided (2 weeks)
    if [[ -z "$end_date" ]]; then
        end_date=$(date -I -d "$start_date + 2 weeks" 2>/dev/null || date -v+2w -I 2>/dev/null || echo "")
    fi

    cat << EOF
{
  "sprint_number": $number,
  "start_date": "$start_date",
  "end_date": "$end_date",
  "velocity_target": $velocity,
  "sprint_goal": "$goal",
  "status": "$status",
  "stories": [],
  "total_points": 0,
  "risks": [],
  "dependencies": [],
  "created_at": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
}

# Generate story JSON
generate_story() {
    local id="${1:-US-001}"
    local title="${2:-}"
    local points="${3:-0}"
    local status="${4:-todo}"
    local assigned="${5:-}"
    local description="${6:-}"

    # Escape description for JSON
    description=$(echo "$description" | sed 's/"/\\"/g' | tr '\n' ' ')

    cat << EOF
{
  "id": "$id",
  "title": "$title",
  "description": "$description",
  "points": $points,
  "status": "$status",
  "assigned_to": "$assigned",
  "acceptance_criteria": [],
  "subtasks": [],
  "created_at": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
}

# Generate backlog JSON
generate_backlog() {
    cat << EOF
{
  "product_backlog": {
    "stories": [],
    "total_points": 0,
    "prioritization": "MoSCoW",
    "last_refined": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
  },
  "sprint_backlog": {
    "current_sprint": 0,
    "stories": []
  }
}
EOF
}

# Generate feature JSON
generate_feature() {
    local name="${1:-}"
    local description="${2:-}"
    local priority="${3:-medium}"
    local issue_number="${4:-}"

    # Create slug from name
    local slug=$(echo "$name" | tr '[:upper:]' '[:lower:]' | tr ' ' '-' | sed 's/[^a-z0-9-]//g')

    cat << EOF
{
  "name": "$name",
  "slug": "$slug",
  "description": "$description",
  "priority": "$priority",
  "status": "planning",
  "issue_number": ${issue_number:-null},
  "branch": "feature/$slug",
  "assigned_agents": [],
  "stories": [],
  "created_at": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")",
  "updated_at": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
}

# Generate agent assignment JSON
generate_agent() {
    local name="${1:-}"
    local role="${2:-}"
    local status="${3:-available}"

    cat << EOF
{
  "name": "$name",
  "role": "$role",
  "status": "$status",
  "assigned_tasks": [],
  "completed_tasks": 0,
  "skills": [],
  "current_work": null
}
EOF
}

# Generate sprint plan JSON (full structure with sample data)
generate_sprint_plan() {
    local number="${1:-1}"
    local goal="${2:-Complete sprint goal}"
    local velocity="${3:-20}"

    cat << EOF
{
  "sprint_number": $number,
  "start_date": "$(date -I)",
  "end_date": "$(date -I -d '+2 weeks' 2>/dev/null || date -v+2w -I 2>/dev/null || echo '')",
  "velocity_target": $velocity,
  "sprint_goal": "$goal",
  "stories": [
    {
      "id": "US-001",
      "title": "Example Story 1",
      "points": 5,
      "acceptance_criteria": [
        "Criteria 1",
        "Criteria 2"
      ],
      "assigned_to": "engineer-1"
    },
    {
      "id": "US-002",
      "title": "Example Story 2",
      "points": 8,
      "acceptance_criteria": [
        "Criteria 1",
        "Criteria 2"
      ],
      "assigned_to": "engineer-2"
    }
  ],
  "total_points": 13,
  "risks": [
    "Example risk 1",
    "Example risk 2"
  ],
  "dependencies": [
    "Example dependency 1"
  ]
}
EOF
}

# Generate current sprint status JSON
generate_sprint_status() {
    local number="${1:-0}"
    local status="${2:-not_started}"
    local velocity="${3:-0}"

    cat << EOF
{
  "sprint_number": $number,
  "status": "$status",
  "stories": [],
  "velocity": $velocity
}
EOF
}

# ============================================================================
# MAIN
# ============================================================================

show_help() {
    echo "JSON Template - Generate JSON structures for sprints, stories, and more"
    echo ""
    echo "Usage:"
    echo "  json-template.sh <type> [options]"
    echo ""
    echo "Types:"
    echo "  sprint        Sprint configuration"
    echo "  sprint-plan   Full sprint plan with sample stories"
    echo "  sprint-status Current sprint status"
    echo "  story         User story"
    echo "  backlog       Product/sprint backlog"
    echo "  feature       Feature definition"
    echo "  agent         Agent assignment"
    echo ""
    echo "Sprint options:"
    echo "  --number N        Sprint number"
    echo "  --goal TEXT       Sprint goal"
    echo "  --start DATE      Start date (YYYY-MM-DD)"
    echo "  --end DATE        End date (YYYY-MM-DD)"
    echo "  --velocity N      Target velocity"
    echo "  --status STATUS   Sprint status"
    echo ""
    echo "Story options:"
    echo "  --id ID           Story ID (e.g., US-101)"
    echo "  --title TEXT      Story title"
    echo "  --points N        Story points"
    echo "  --status STATUS   Story status"
    echo "  --assigned NAME   Assigned to"
    echo "  --description TEXT  Story description"
    echo ""
    echo "Feature options:"
    echo "  --name NAME       Feature name"
    echo "  --description TEXT  Description"
    echo "  --priority LEVEL  Priority (low, medium, high, critical)"
    echo "  --issue N         GitHub issue number"
    echo ""
    echo "Agent options:"
    echo "  --name NAME       Agent name"
    echo "  --role ROLE       Agent role"
    echo "  --status STATUS   Agent status"
    echo ""
    echo "Common options:"
    echo "  --output FILE     Output to file"
    echo "  --pretty          Pretty print (default)"
    echo "  --compact         Compact output (no formatting)"
    echo ""
    echo "Examples:"
    echo "  json-template.sh sprint --number 1 --goal 'Auth feature'"
    echo "  json-template.sh story --id US-101 --title 'Login' --points 5"
    echo "  json-template.sh feature --name 'User Auth' --priority high"
}

# Parse arguments
TYPE=""
OUTPUT=""
COMPACT=false

# Sprint options
SPRINT_NUMBER=""
SPRINT_GOAL=""
SPRINT_START=""
SPRINT_END=""
SPRINT_VELOCITY=""
SPRINT_STATUS=""

# Story options
STORY_ID=""
STORY_TITLE=""
STORY_POINTS=""
STORY_STATUS=""
STORY_ASSIGNED=""
STORY_DESC=""

# Feature options
FEATURE_NAME=""
FEATURE_DESC=""
FEATURE_PRIORITY=""
FEATURE_ISSUE=""

# Agent options
AGENT_NAME=""
AGENT_ROLE=""
AGENT_STATUS=""

# First argument is the type
if [[ $# -gt 0 && ! "$1" =~ ^-- ]]; then
    TYPE="$1"
    shift
fi

while [[ $# -gt 0 ]]; do
    case "$1" in
        --help|-h)
            show_help
            exit 0
            ;;
        --output|-o)
            OUTPUT="${2:-}"
            shift 2
            ;;
        --compact)
            COMPACT=true
            shift
            ;;
        --pretty)
            COMPACT=false
            shift
            ;;
        # Sprint options
        --number|-n)
            SPRINT_NUMBER="${2:-}"
            shift 2
            ;;
        --goal|-g)
            SPRINT_GOAL="${2:-}"
            shift 2
            ;;
        --start)
            SPRINT_START="${2:-}"
            shift 2
            ;;
        --end)
            SPRINT_END="${2:-}"
            shift 2
            ;;
        --velocity|-v)
            SPRINT_VELOCITY="${2:-}"
            shift 2
            ;;
        # Story/common options
        --id)
            STORY_ID="${2:-}"
            shift 2
            ;;
        --title|-t)
            STORY_TITLE="${2:-}"
            shift 2
            ;;
        --points|-p)
            STORY_POINTS="${2:-}"
            shift 2
            ;;
        --status|-s)
            STORY_STATUS="${2:-}"
            SPRINT_STATUS="${2:-}"
            AGENT_STATUS="${2:-}"
            shift 2
            ;;
        --assigned|-a)
            STORY_ASSIGNED="${2:-}"
            shift 2
            ;;
        --description|-d)
            STORY_DESC="${2:-}"
            FEATURE_DESC="${2:-}"
            shift 2
            ;;
        # Feature options
        --name)
            FEATURE_NAME="${2:-}"
            AGENT_NAME="${2:-}"
            shift 2
            ;;
        --priority)
            FEATURE_PRIORITY="${2:-}"
            shift 2
            ;;
        --issue)
            FEATURE_ISSUE="${2:-}"
            shift 2
            ;;
        # Agent options
        --role)
            AGENT_ROLE="${2:-}"
            shift 2
            ;;
        *)
            shift
            ;;
    esac
done

if [[ -z "$TYPE" ]]; then
    show_help
    exit 1
fi

# Generate JSON
output=""
case "$TYPE" in
    sprint)
        output=$(generate_sprint "${SPRINT_NUMBER:-1}" "$SPRINT_GOAL" "$SPRINT_START" \
            "$SPRINT_END" "${SPRINT_VELOCITY:-20}" "${SPRINT_STATUS:-not_started}")
        ;;
    sprint-plan)
        output=$(generate_sprint_plan "${SPRINT_NUMBER:-1}" "$SPRINT_GOAL" "${SPRINT_VELOCITY:-20}")
        ;;
    sprint-status)
        output=$(generate_sprint_status "${SPRINT_NUMBER:-0}" "${SPRINT_STATUS:-not_started}" \
            "${SPRINT_VELOCITY:-0}")
        ;;
    story)
        output=$(generate_story "$STORY_ID" "$STORY_TITLE" "${STORY_POINTS:-0}" \
            "${STORY_STATUS:-todo}" "$STORY_ASSIGNED" "$STORY_DESC")
        ;;
    backlog)
        output=$(generate_backlog)
        ;;
    feature)
        output=$(generate_feature "$FEATURE_NAME" "$FEATURE_DESC" \
            "${FEATURE_PRIORITY:-medium}" "$FEATURE_ISSUE")
        ;;
    agent)
        output=$(generate_agent "$AGENT_NAME" "$AGENT_ROLE" "${AGENT_STATUS:-available}")
        ;;
    *)
        echo "Unknown type: $TYPE" >&2
        show_help
        exit 1
        ;;
esac

# Compact if requested (remove whitespace)
if [[ "$COMPACT" == true ]]; then
    output=$(echo "$output" | tr -d '\n' | tr -s ' ')
fi

# Output
if [[ -n "$OUTPUT" ]]; then
    echo "$output" > "$OUTPUT"
    echo "JSON written to: $OUTPUT" >&2
else
    echo "$output"
fi
