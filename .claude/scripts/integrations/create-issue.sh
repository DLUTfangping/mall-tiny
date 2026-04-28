#!/bin/bash
# Create Issue - Multi-provider issue creation
# Automatically uses the configured issue tracking provider

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="${PROJECT_ROOT:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}"
ENV_FILE="$PROJECT_ROOT/.env.local"
PROJECT_MD="$PROJECT_ROOT/PROJECT.md"

# Plain text output (no color formatting)

# Load credentials
load_credentials() {
    if [[ -f "$ENV_FILE" ]]; then
        set -a
        source "$ENV_FILE"
        set +a
    fi
}

# Get configured tracking provider
get_provider() {
    if [[ -f "$PROJECT_MD" ]] && grep -q "issue_tracking:" "$PROJECT_MD" 2>/dev/null; then
        grep "issue_tracking:" "$PROJECT_MD" | head -1 | sed 's/.*issue_tracking:[[:space:]]*//' | tr '[:upper:]' '[:lower:]'
    else
        echo "local"
    fi
}

# ============================================================================
# ISSUE CREATION FUNCTIONS
# ============================================================================

create_github_issue() {
    local title="$1"
    local body="$2"
    local labels="$3"

    if ! command -v gh &> /dev/null; then
        echo "GitHub CLI (gh) not installed" >&2
        return 1
    fi

    local cmd="gh issue create --title \"$title\" --body \"$body\""

    if [[ -n "$labels" ]]; then
        cmd="$cmd --label \"$labels\""
    fi

    local result=$(eval "$cmd" 2>&1)
    local issue_url=$(echo "$result" | grep -o 'https://github.com[^ ]*' || echo "")
    local issue_number=$(echo "$issue_url" | grep -o '[0-9]*$' || echo "")

    if [[ -n "$issue_number" ]]; then
        echo "{\"provider\": \"github\", \"number\": \"$issue_number\", \"url\": \"$issue_url\"}"
        return 0
    else
        echo "Failed to create GitHub issue: $result" >&2
        return 1
    fi
}

create_linear_issue() {
    local title="$1"
    local body="$2"
    local priority="$3"  # 0=No, 1=Urgent, 2=High, 3=Medium, 4=Low

    if [[ -z "$LINEAR_API_KEY" ]]; then
        echo "LINEAR_API_KEY not set. Run setup-integration.sh setup linear" >&2
        return 1
    fi

    # Default team key
    local team_key="${LINEAR_TEAM_KEY:-}"

    # If no team key, get first team
    if [[ -z "$team_key" ]]; then
        local teams=$(curl -s -X POST "https://api.linear.app/graphql" \
            -H "Authorization: $LINEAR_API_KEY" \
            -H "Content-Type: application/json" \
            -d '{"query": "{ teams { nodes { id key } } }"}')
        team_key=$(echo "$teams" | grep -o '"key":"[^"]*"' | head -1 | cut -d'"' -f4)
    fi

    if [[ -z "$team_key" ]]; then
        echo "Could not determine Linear team" >&2
        return 1
    fi

    # Get team ID
    local team_response=$(curl -s -X POST "https://api.linear.app/graphql" \
        -H "Authorization: $LINEAR_API_KEY" \
        -H "Content-Type: application/json" \
        -d "{\"query\": \"{ team(key: \\\"$team_key\\\") { id } }\"}")

    local team_id=$(echo "$team_response" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)

    # Create issue
    local priority_value="${priority:-3}"  # Default to Medium
    local escaped_body=$(echo "$body" | sed 's/"/\\"/g' | sed ':a;N;$!ba;s/\n/\\n/g')

    local mutation="{\"query\": \"mutation { issueCreate(input: { teamId: \\\"$team_id\\\", title: \\\"$title\\\", description: \\\"$escaped_body\\\", priority: $priority_value }) { success issue { id identifier number url } } }\"}"

    local result=$(curl -s -X POST "https://api.linear.app/graphql" \
        -H "Authorization: $LINEAR_API_KEY" \
        -H "Content-Type: application/json" \
        -d "$mutation")

    if echo "$result" | grep -q '"success":true'; then
        local issue_id=$(echo "$result" | grep -o '"identifier":"[^"]*"' | head -1 | cut -d'"' -f4)
        local issue_url=$(echo "$result" | grep -o '"url":"[^"]*"' | head -1 | cut -d'"' -f4)
        echo "{\"provider\": \"linear\", \"identifier\": \"$issue_id\", \"url\": \"$issue_url\"}"
        return 0
    else
        echo "Failed to create Linear issue: $result" >&2
        return 1
    fi
}

create_jira_issue() {
    local title="$1"
    local body="$2"
    local issue_type="${3:-Task}"  # Task, Story, Bug, etc.

    if [[ -z "$JIRA_URL" || -z "$JIRA_USER" || -z "$JIRA_TOKEN" ]]; then
        echo "Jira credentials not set. Run setup-integration.sh setup jira" >&2
        return 1
    fi

    local project_key="${JIRA_PROJECT_KEY:-}"

    if [[ -z "$project_key" ]]; then
        # Get first project
        local projects=$(curl -s -u "$JIRA_USER:$JIRA_TOKEN" "$JIRA_URL/rest/api/2/project")
        project_key=$(echo "$projects" | grep -o '"key":"[^"]*"' | head -1 | cut -d'"' -f4)
    fi

    if [[ -z "$project_key" ]]; then
        echo "Could not determine Jira project" >&2
        return 1
    fi

    # Escape JSON special characters
    local escaped_body=$(echo "$body" | sed 's/"/\\"/g' | sed ':a;N;$!ba;s/\n/\\n/g')

    local payload=$(cat << EOF
{
    "fields": {
        "project": {"key": "$project_key"},
        "summary": "$title",
        "description": "$escaped_body",
        "issuetype": {"name": "$issue_type"}
    }
}
EOF
)

    local result=$(curl -s -w "\n%{http_code}" -X POST "$JIRA_URL/rest/api/2/issue" \
        -u "$JIRA_USER:$JIRA_TOKEN" \
        -H "Content-Type: application/json" \
        -d "$payload")

    local http_code=$(echo "$result" | tail -1)
    local response=$(echo "$result" | head -n -1)

    if [[ "$http_code" == "201" ]]; then
        local issue_key=$(echo "$response" | grep -o '"key":"[^"]*"' | head -1 | cut -d'"' -f4)
        local issue_url="$JIRA_URL/browse/$issue_key"
        echo "{\"provider\": \"jira\", \"key\": \"$issue_key\", \"url\": \"$issue_url\"}"
        return 0
    else
        echo "Failed to create Jira issue (HTTP $http_code): $response" >&2
        return 1
    fi
}

create_local_issue() {
    local title="$1"
    local body="$2"
    local type="${3:-feature}"

    local features_dir="$PROJECT_ROOT/docs/features"
    mkdir -p "$features_dir"

    # Generate slug from title
    local slug=$(echo "$title" | tr '[:upper:]' '[:lower:]' | tr ' ' '-' | sed 's/[^a-z0-9-]//g')
    local feature_dir="$features_dir/$slug"
    mkdir -p "$feature_dir"

    local timestamp=$(date -u +"%Y-%m-%d %H:%M UTC")
    local issue_id="LOCAL-$(date +%s | tail -c 6)"

    cat > "$feature_dir/FEATURE.md" << EOF
# $title

**ID:** $issue_id
**Status:** In Progress
**Type:** $type
**Created:** $timestamp

## Description

$body

## Activity Log

### $timestamp - Created
Feature tracking initialized

EOF

    echo "{\"provider\": \"local\", \"id\": \"$issue_id\", \"path\": \"$feature_dir/FEATURE.md\"}"
    return 0
}

# ============================================================================
# MAIN
# ============================================================================

main() {
    local title=""
    local body=""
    local type=""
    local priority=""
    local labels=""
    local force_provider=""

    while [[ $# -gt 0 ]]; do
        case "$1" in
            --title|-t)
                title="$2"
                shift 2
                ;;
            --body|-b)
                body="$2"
                shift 2
                ;;
            --type)
                type="$2"
                shift 2
                ;;
            --priority|-p)
                priority="$2"
                shift 2
                ;;
            --labels|-l)
                labels="$2"
                shift 2
                ;;
            --provider)
                force_provider="$2"
                shift 2
                ;;
            --help|-h)
                echo "Create Issue - Multi-provider issue creation"
                echo ""
                echo "Usage: create-issue.sh [options]"
                echo ""
                echo "Options:"
                echo "  -t, --title <title>      Issue title (required)"
                echo "  -b, --body <body>        Issue description"
                echo "  --type <type>            Issue type (feature, bug, task, etc.)"
                echo "  -p, --priority <level>   Priority (high, medium, low)"
                echo "  -l, --labels <labels>    Labels (comma-separated)"
                echo "  --provider <provider>    Force specific provider"
                echo ""
                echo "Providers:"
                echo "  github  - GitHub Issues (uses gh CLI)"
                echo "  linear  - Linear (uses LINEAR_API_KEY)"
                echo "  jira    - Jira (uses JIRA_* credentials)"
                echo "  local   - Local markdown files"
                echo ""
                echo "The provider is automatically detected from PROJECT.md"
                echo "unless --provider is specified."
                exit 0
                ;;
            *)
                shift
                ;;
        esac
    done

    if [[ -z "$title" ]]; then
        echo "Error: --title is required" >&2
        exit 1
    fi

    load_credentials

    local provider="${force_provider:-$(get_provider)}"

    echo "Creating issue in $provider..." >&2

    case "$provider" in
        github|github-issues)
            create_github_issue "$title" "$body" "$labels"
            ;;
        linear)
            # Map priority to Linear values (1=Urgent, 2=High, 3=Medium, 4=Low)
            local linear_priority=3
            case "$priority" in
                urgent|critical) linear_priority=1 ;;
                high) linear_priority=2 ;;
                medium) linear_priority=3 ;;
                low) linear_priority=4 ;;
            esac
            create_linear_issue "$title" "$body" "$linear_priority"
            ;;
        jira)
            # Map type to Jira issue types
            local jira_type="Task"
            case "$type" in
                feature|story) jira_type="Story" ;;
                bug) jira_type="Bug" ;;
                task) jira_type="Task" ;;
                enhancement) jira_type="Task" ;;
            esac
            create_jira_issue "$title" "$body" "$jira_type"
            ;;
        local|none|*)
            create_local_issue "$title" "$body" "$type"
            ;;
    esac
}

main "$@"
