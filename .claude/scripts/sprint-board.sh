#!/usr/bin/env bash
# Sprint Board - Generate sprint board markdown
# Usage: sprint-board.sh [options]
#
# Examples:
#   sprint-board.sh --sprint 1 --goal "User Authentication"
#   sprint-board.sh --sprint 1 --story "US-101:User login:5:In Progress:backend-engineer"

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ============================================================================
# SPRINT BOARD GENERATION
# ============================================================================

generate_sprint_board() {
    local sprint_num="$1"
    local goal="${2:-}"
    shift 2
    local -a stories=("$@")

    cat << EOF
# Sprint $sprint_num Board

## Sprint Goal
${goal:-Define sprint goal here}

## Stories

| ID | Story | Points | Status | Assigned To |
|----|-------|--------|---------|-------------|
EOF

    # Add stories
    if [[ ${#stories[@]} -gt 0 ]]; then
        for story in "${stories[@]}"; do
            # Parse story (format: "ID:Title:Points:Status:Assigned")
            IFS=':' read -r id title points status assigned <<< "$story"
            echo "| $id | $title | $points | $status | $assigned |"
        done
    else
        echo "| - | No stories added | - | - | - |"
    fi

    cat << 'EOF'

## Columns

### To Do
- Stories not yet started

### In Progress
- Stories currently being worked on

### In Review
- Stories awaiting code review

### Done
- Completed and verified stories

## Burndown
```
Points
EOF

    # Generate placeholder burndown
    local total_points=0
    for story in "${stories[@]}"; do
        IFS=':' read -r _ _ points _ _ <<< "$story"
        total_points=$((total_points + points))
    done

    if [[ $total_points -gt 0 ]]; then
        for ((p = total_points; p >= 0; p -= 2)); do
            printf "%2d |" "$p"
            printf "*\n"
        done
        echo "   |___________"
        echo "   1 2 3 4 5 6 7 8 9 10 Days"
    else
        cat << 'EOF'
16 |*
14 | *
12 |  *
10 |   *
8  |    *
6  |     *
4  |      *
2  |       *
0  |________*
   1 2 3 4 5 6 7 8 9 10 Days
EOF
    fi

    cat << 'EOF'
```

## Impediments
- None currently

## Daily Updates
Check daily-standup.log for details
EOF
}

# Generate compact board (table only)
generate_board_compact() {
    local sprint_num="$1"
    shift
    local -a stories=("$@")

    echo "## Sprint $sprint_num Stories"
    echo ""
    echo "| ID | Story | Pts | Status | Owner |"
    echo "|----|-------|-----|--------|-------|"

    for story in "${stories[@]}"; do
        IFS=':' read -r id title points status assigned <<< "$story"
        echo "| $id | $title | $points | $status | $assigned |"
    done
}

# Generate Kanban-style board
generate_board_kanban() {
    local sprint_num="$1"
    shift
    local -a stories=("$@")

    # Categorize stories by status
    local -a todo=() in_progress=() in_review=() done=()

    for story in "${stories[@]}"; do
        IFS=':' read -r id title points status assigned <<< "$story"
        local item="$id: $title ($points pts)"

        case "$status" in
            "To Do"|"todo"|"pending")
                todo+=("$item") ;;
            "In Progress"|"in_progress"|"active")
                in_progress+=("$item") ;;
            "In Review"|"in_review"|"review")
                in_review+=("$item") ;;
            "Done"|"done"|"complete")
                done+=("$item") ;;
            *)
                todo+=("$item") ;;
        esac
    done

    cat << EOF
# Sprint $sprint_num Kanban Board

## 📋 To Do (${#todo[@]})
EOF
    for item in "${todo[@]}"; do echo "- $item"; done

    cat << EOF

## 🔄 In Progress (${#in_progress[@]})
EOF
    for item in "${in_progress[@]}"; do echo "- $item"; done

    cat << EOF

## 🔍 In Review (${#in_review[@]})
EOF
    for item in "${in_review[@]}"; do echo "- $item"; done

    cat << EOF

## ✅ Done (${#done[@]})
EOF
    for item in "${done[@]}"; do echo "- $item"; done
}

# ============================================================================
# MAIN
# ============================================================================

show_help() {
    echo "Sprint Board - Generate sprint board markdown"
    echo ""
    echo "Usage:"
    echo "  sprint-board.sh [options]"
    echo ""
    echo "Options:"
    echo "  --sprint N         Sprint number (required)"
    echo "  --goal TEXT        Sprint goal"
    echo "  --story STORY      Add story (format: 'ID:Title:Points:Status:Assigned')"
    echo "  --style TYPE       Board style: full|compact|kanban (default: full)"
    echo "  --output FILE      Output to file (default: stdout)"
    echo ""
    echo "Story format: 'ID:Title:Points:Status:Assigned'"
    echo "  Status: To Do, In Progress, In Review, Done"
    echo ""
    echo "Examples:"
    echo "  sprint-board.sh --sprint 1 --goal 'Auth feature'"
    echo "  sprint-board.sh --sprint 1 --story 'US-101:Login:5:In Progress:backend'"
    echo "  sprint-board.sh --sprint 1 --style kanban --story 'US-101:Login:5:Done:backend'"
}

# Parse arguments
SPRINT=""
GOAL=""
STYLE="full"
OUTPUT=""
declare -a STORIES=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --help|-h)
            show_help
            exit 0
            ;;
        --sprint|-n)
            SPRINT="${2:-}"
            shift 2
            ;;
        --goal|-g)
            GOAL="${2:-}"
            shift 2
            ;;
        --story|-s)
            STORIES+=("${2:-}")
            shift 2
            ;;
        --style)
            STYLE="${2:-full}"
            shift 2
            ;;
        --output|-o)
            OUTPUT="${2:-}"
            shift 2
            ;;
        *)
            shift
            ;;
    esac
done

if [[ -z "$SPRINT" ]]; then
    echo "Error: --sprint is required" >&2
    show_help
    exit 1
fi

# Generate board
output=""
case "$STYLE" in
    full)
        output=$(generate_sprint_board "$SPRINT" "$GOAL" "${STORIES[@]}")
        ;;
    compact)
        output=$(generate_board_compact "$SPRINT" "${STORIES[@]}")
        ;;
    kanban)
        output=$(generate_board_kanban "$SPRINT" "${STORIES[@]}")
        ;;
    *)
        echo "Unknown style: $STYLE" >&2
        exit 1
        ;;
esac

# Output
if [[ -n "$OUTPUT" ]]; then
    echo "$output" > "$OUTPUT"
    echo "Sprint board written to: $OUTPUT"
else
    echo "$output"
fi
