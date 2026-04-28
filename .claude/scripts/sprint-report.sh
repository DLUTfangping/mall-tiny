#!/usr/bin/env bash
# Sprint Report - Generate sprint reports with metrics
# Usage: sprint-report.sh [options]
#
# Examples:
#   sprint-report.sh --sprint 1 --velocity 20 --completed 18 --stories-done 3 --stories-total 4
#   sprint-report.sh --sprint 1 --start-date 2024-01-01 --end-date 2024-01-14

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ============================================================================
# SPRINT REPORT GENERATION
# ============================================================================

generate_sprint_report() {
    local sprint_num="$1"
    local velocity_target="${2:-20}"
    local velocity_achieved="${3:-0}"
    local stories_done="${4:-0}"
    local stories_total="${5:-0}"
    local start_date="${6:-}"
    local end_date="${7:-}"
    local defects="${8:-0}"
    local coverage="${9:-0}"
    local pr_time="${10:-0}"

    # Calculate dates if not provided
    if [[ -z "$end_date" ]]; then
        end_date=$(date -I 2>/dev/null || date +%Y-%m-%d)
    fi
    if [[ -z "$start_date" ]]; then
        # Assume 2 week sprint
        start_date=$(date -I -d "$end_date - 2 weeks" 2>/dev/null || date +%Y-%m-%d)
    fi

    # Calculate velocity percentage
    local velocity_pct=0
    if [[ $velocity_target -gt 0 ]]; then
        velocity_pct=$((velocity_achieved * 100 / velocity_target))
    fi

    # Calculate story completion
    local story_pct=0
    if [[ $stories_total -gt 0 ]]; then
        story_pct=$((stories_done * 100 / stories_total))
    fi

    cat << EOF
# Sprint $sprint_num Report

## Summary
- **Duration**: $start_date to $end_date
- **Velocity Target**: $velocity_target points
- **Velocity Achieved**: $velocity_achieved points ($velocity_pct%)
- **Stories Completed**: $stories_done/$stories_total

## Velocity Analysis
EOF

    # Velocity chart (simple bar)
    local bar_width=30
    local filled=$((velocity_pct * bar_width / 100))
    [[ $filled -gt $bar_width ]] && filled=$bar_width
    local empty=$((bar_width - filled))

    local filled_str=$(printf '%*s' $filled | tr ' ' '█')
    local empty_str=$(printf '%*s' $empty | tr ' ' '░')

    cat << EOF
\`\`\`
Target:   [$filled_str$empty_str] $velocity_pct%
Achieved: $velocity_achieved / $velocity_target points
\`\`\`

## Completed Stories
EOF

    # Placeholder for stories - in real usage, these would be passed in
    echo "_(Add completed story details here)_"
    echo ""

    if [[ $stories_done -lt $stories_total ]]; then
        cat << EOF
## Incomplete Stories
_(Add incomplete stories with % complete and reason)_

EOF
    fi

    cat << EOF
## Key Metrics
| Metric | Value | Target | Status |
|--------|-------|--------|--------|
| Velocity | $velocity_achieved pts | $velocity_target pts | $([ $velocity_pct -ge 80 ] && echo "✅" || echo "⚠️") |
| Stories | $stories_done/$stories_total | 100% | $([ $story_pct -ge 80 ] && echo "✅" || echo "⚠️") |
| Defects Found | $defects | 0 | $([ $defects -eq 0 ] && echo "✅" || echo "⚠️") |
| Code Coverage | ${coverage}% | 80% | $([ $coverage -ge 80 ] && echo "✅" || echo "⚠️") |
| PR Cycle Time | ${pr_time} days | 2 days | $([ $pr_time -le 2 ] && echo "✅" || echo "⚠️") |

## Impediments Encountered
- _(List impediments and resolutions)_

## Action Items for Next Sprint
1. _(Action item 1)_
2. _(Action item 2)_
3. _(Action item 3)_

## Team Health
- **Morale**: _(Good/Fair/Poor)_
- **Collaboration**: _(Excellent/Good/Needs Improvement)_
- **Process Adherence**: _(%%)_

---
_Generated on $(date -u +"%Y-%m-%d %H:%M UTC")_
EOF
}

# Generate compact report (summary only)
generate_report_compact() {
    local sprint_num="$1"
    local velocity_target="${2:-20}"
    local velocity_achieved="${3:-0}"
    local stories_done="${4:-0}"
    local stories_total="${5:-0}"

    local velocity_pct=0
    [[ $velocity_target -gt 0 ]] && velocity_pct=$((velocity_achieved * 100 / velocity_target))

    cat << EOF
## Sprint $sprint_num Summary

| Metric | Value |
|--------|-------|
| Velocity | $velocity_achieved / $velocity_target pts ($velocity_pct%) |
| Stories | $stories_done / $stories_total complete |
| Status | $([ $velocity_pct -ge 80 ] && echo "✅ On Track" || echo "⚠️ Needs Attention") |
EOF
}

# Generate JSON report
generate_report_json() {
    local sprint_num="$1"
    local velocity_target="${2:-20}"
    local velocity_achieved="${3:-0}"
    local stories_done="${4:-0}"
    local stories_total="${5:-0}"
    local start_date="${6:-}"
    local end_date="${7:-}"

    cat << EOF
{
  "sprint_number": $sprint_num,
  "duration": {
    "start": "${start_date:-$(date -I)}",
    "end": "${end_date:-$(date -I)}"
  },
  "velocity": {
    "target": $velocity_target,
    "achieved": $velocity_achieved,
    "percentage": $((velocity_achieved * 100 / velocity_target))
  },
  "stories": {
    "completed": $stories_done,
    "total": $stories_total,
    "percentage": $((stories_done * 100 / stories_total))
  },
  "generated_at": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
}

# ============================================================================
# MAIN
# ============================================================================

show_help() {
    echo "Sprint Report - Generate sprint reports with metrics"
    echo ""
    echo "Usage:"
    echo "  sprint-report.sh [options]"
    echo ""
    echo "Options:"
    echo "  --sprint N              Sprint number (required)"
    echo "  --velocity N            Target velocity (default: 20)"
    echo "  --completed N           Achieved velocity"
    echo "  --stories-done N        Completed stories count"
    echo "  --stories-total N       Total stories count"
    echo "  --start-date DATE       Sprint start date (YYYY-MM-DD)"
    echo "  --end-date DATE         Sprint end date (YYYY-MM-DD)"
    echo "  --defects N             Defects found (default: 0)"
    echo "  --coverage N            Code coverage % (default: 0)"
    echo "  --pr-time N             PR cycle time in days (default: 0)"
    echo "  --style TYPE            Report style: full|compact|json (default: full)"
    echo "  --output FILE           Output to file (default: stdout)"
    echo ""
    echo "Examples:"
    echo "  sprint-report.sh --sprint 1 --completed 18 --stories-done 3 --stories-total 4"
    echo "  sprint-report.sh --sprint 1 --style compact"
    echo "  sprint-report.sh --sprint 1 --style json --output report.json"
}

# Parse arguments
SPRINT=""
VELOCITY_TARGET="20"
VELOCITY_ACHIEVED="0"
STORIES_DONE="0"
STORIES_TOTAL="0"
START_DATE=""
END_DATE=""
DEFECTS="0"
COVERAGE="0"
PR_TIME="0"
STYLE="full"
OUTPUT=""

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
        --velocity|-v)
            VELOCITY_TARGET="${2:-20}"
            shift 2
            ;;
        --completed|-c)
            VELOCITY_ACHIEVED="${2:-0}"
            shift 2
            ;;
        --stories-done)
            STORIES_DONE="${2:-0}"
            shift 2
            ;;
        --stories-total)
            STORIES_TOTAL="${2:-0}"
            shift 2
            ;;
        --start-date)
            START_DATE="${2:-}"
            shift 2
            ;;
        --end-date)
            END_DATE="${2:-}"
            shift 2
            ;;
        --defects)
            DEFECTS="${2:-0}"
            shift 2
            ;;
        --coverage)
            COVERAGE="${2:-0}"
            shift 2
            ;;
        --pr-time)
            PR_TIME="${2:-0}"
            shift 2
            ;;
        --style|-s)
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

# Generate report
output=""
case "$STYLE" in
    full)
        output=$(generate_sprint_report "$SPRINT" "$VELOCITY_TARGET" "$VELOCITY_ACHIEVED" \
            "$STORIES_DONE" "$STORIES_TOTAL" "$START_DATE" "$END_DATE" \
            "$DEFECTS" "$COVERAGE" "$PR_TIME")
        ;;
    compact)
        output=$(generate_report_compact "$SPRINT" "$VELOCITY_TARGET" "$VELOCITY_ACHIEVED" \
            "$STORIES_DONE" "$STORIES_TOTAL")
        ;;
    json)
        output=$(generate_report_json "$SPRINT" "$VELOCITY_TARGET" "$VELOCITY_ACHIEVED" \
            "$STORIES_DONE" "$STORIES_TOTAL" "$START_DATE" "$END_DATE")
        ;;
    *)
        echo "Unknown style: $STYLE" >&2
        exit 1
        ;;
esac

# Output
if [[ -n "$OUTPUT" ]]; then
    echo "$output" > "$OUTPUT"
    echo "Sprint report written to: $OUTPUT"
else
    echo "$output"
fi
