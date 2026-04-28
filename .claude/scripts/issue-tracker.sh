#!/bin/bash
# Issue Tracker Hook Script
# Manages automatic issue updates based on development events
# Now supports external templates and configurable labels

set -e

# Configuration
PROJECT_ROOT="${PROJECT_ROOT:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}"
PROJECT_MD="$PROJECT_ROOT/PROJECT.md"
PROJECT_STATUS="$PROJECT_ROOT/PROJECT_STATUS.md"
ENV_FILE="$PROJECT_ROOT/.env.local"

# Template and config paths
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_DIR="${SCRIPT_DIR}/../config"
TEMPLATES_DIR="${SCRIPT_DIR}/../templates/issue-comments"
LABELS_CONFIG="${CONFIG_DIR}/labels.toml"
MESSAGES_CONFIG="${CONFIG_DIR}/messages.toml"

# Fallback paths for deployed projects
if [[ ! -d "$CONFIG_DIR" ]]; then
    CONFIG_DIR="$PROJECT_ROOT/.claude/config"
    TEMPLATES_DIR="$PROJECT_ROOT/.claude/templates/issue-comments"
    LABELS_CONFIG="$CONFIG_DIR/labels.toml"
    MESSAGES_CONFIG="$CONFIG_DIR/messages.toml"
fi

# Load credentials from .env.local if available
# This enables integration with Linear, Jira, Slack, etc.
if [[ -f "$ENV_FILE" ]]; then
    set -a
    source "$ENV_FILE"
    set +a
fi

# Plain text output (no color formatting)

# ============================================================================
# CONFIG PARSING
# ============================================================================

# Parse TOML config for a key (simple parser)
get_toml_value() {
    local file="$1"
    local key="$2"
    local default="$3"

    if [[ -f "$file" ]]; then
        local value=$(grep -E "^${key}\s*=" "$file" 2>/dev/null | head -1 | sed -E 's/^[^=]+=\s*["'"'"']?([^"'"'"']*)["'"'"']?\s*$/\1/')
        if [[ -n "$value" ]]; then
            echo "$value"
            return 0
        fi
    fi
    echo "$default"
}

# Get label for a stage from config
get_label() {
    local stage="$1"
    local default="$2"
    get_toml_value "$LABELS_CONFIG" "stages.$stage" "${default:-$stage}"
}

# Get hook footer message from config
get_footer() {
    local default="_Automated update via hook_"
    get_toml_value "$MESSAGES_CONFIG" "hooks.auto_update_footer" "$default"
}

# ============================================================================
# TEMPLATE HANDLING
# ============================================================================

# Load and render a template
render_template() {
    local template_name="$1"
    shift
    local template_file="${TEMPLATES_DIR}/${template_name}.md"

    # Check if template exists
    if [[ ! -f "$template_file" ]]; then
        # Return empty if no template found (will use inline fallback)
        return 1
    fi

    local content=$(cat "$template_file")

    # Replace placeholders with provided variables
    # Variables are passed as key=value pairs
    while [[ $# -gt 0 ]]; do
        local var="$1"
        local key="${var%%=*}"
        local value="${var#*=}"
        content="${content//\{\{$key\}\}/$value}"
        shift
    done

    # Replace footer placeholder
    local footer=$(get_footer)
    content="${content//\{\{footer\}\}/$footer}"

    echo "$content"
}

# ============================================================================
# TRACKING MODE DETECTION
# ============================================================================

get_tracking_mode() {
    if [[ -f "$PROJECT_MD" ]] && grep -q "issue_tracking:" "$PROJECT_MD" 2>/dev/null; then
        local provider=$(grep "issue_tracking:" "$PROJECT_MD" | head -1 | sed 's/.*issue_tracking:[[:space:]]*//' | tr '[:upper:]' '[:lower:]')
        if [[ -n "$provider" && "$provider" != "none" && "$provider" != "local" ]]; then
            echo "$provider"
            return 0
        fi
    fi
    echo "local"
    return 0
}

get_current_issue() {
    if [[ -f "$PROJECT_STATUS" ]]; then
        grep -oE 'Issue: #[0-9]+' "$PROJECT_STATUS" 2>/dev/null | tail -1 | grep -oE '[0-9]+' || echo ""
    fi
}

get_current_feature() {
    if [[ -f "$PROJECT_STATUS" ]]; then
        grep -oE 'Feature: [^|]+' "$PROJECT_STATUS" 2>/dev/null | head -1 | sed 's/Feature: //' | xargs || echo ""
    fi
}

get_feature_file() {
    local feature_name="$1"
    if [[ -n "$feature_name" ]]; then
        local slug=$(echo "$feature_name" | tr '[:upper:]' '[:lower:]' | tr ' ' '-' | sed 's/[^a-z0-9-]//g')
        echo "$PROJECT_ROOT/docs/features/$slug/FEATURE.md"
    fi
}

# ============================================================================
# ISSUE OPERATIONS
# ============================================================================

update_issue() {
    local issue_number="$1"
    local comment="$2"
    local mode=$(get_tracking_mode)

    if [[ -z "$issue_number" && -z "$comment" ]]; then
        return 1
    fi

    case "$mode" in
        github)
            if [[ -n "$issue_number" ]]; then
                gh issue comment "$issue_number" --body "$comment" 2>/dev/null && \
                    echo "Updated GitHub issue #$issue_number"
            fi
            ;;
        linear)
            # Linear API update (requires LINEAR_API_KEY)
            if [[ -n "$LINEAR_API_KEY" && -n "$issue_number" ]]; then
                curl -s -X POST "https://api.linear.app/graphql" \
                    -H "Authorization: $LINEAR_API_KEY" \
                    -H "Content-Type: application/json" \
                    -d "{\"query\": \"mutation { commentCreate(input: { issueId: \\\"$issue_number\\\", body: \\\"$comment\\\" }) { success } }\"}" >/dev/null && \
                    echo "Updated Linear issue $issue_number"
            fi
            ;;
        jira)
            # Jira API update (requires JIRA_* env vars)
            if [[ -n "$JIRA_URL" && -n "$JIRA_USER" && -n "$JIRA_TOKEN" && -n "$issue_number" ]]; then
                curl -s -X POST "$JIRA_URL/rest/api/2/issue/$issue_number/comment" \
                    -u "$JIRA_USER:$JIRA_TOKEN" \
                    -H "Content-Type: application/json" \
                    -d "{\"body\": \"$comment\"}" >/dev/null && \
                    echo "Updated Jira issue $issue_number"
            fi
            ;;
        local)
            local feature=$(get_current_feature)
            local feature_file=$(get_feature_file "$feature")
            if [[ -f "$feature_file" ]]; then
                # Append to activity log in feature file
                local timestamp=$(date -u +"%Y-%m-%d %H:%M UTC")
                echo -e "\n### Activity - $timestamp\n$comment" >> "$feature_file"
                echo "Updated local feature file"
            fi
            ;;
    esac
}

update_issue_label() {
    local issue_number="$1"
    local remove_label="$2"
    local add_label="$3"
    local mode=$(get_tracking_mode)

    case "$mode" in
        github)
            if [[ -n "$issue_number" ]]; then
                [[ -n "$remove_label" ]] && gh issue edit "$issue_number" --remove-label "$remove_label" 2>/dev/null || true
                [[ -n "$add_label" ]] && gh issue edit "$issue_number" --add-label "$add_label" 2>/dev/null || true
                echo "Updated labels on #$issue_number"
            fi
            ;;
        local)
            local feature=$(get_current_feature)
            local feature_file=$(get_feature_file "$feature")
            if [[ -f "$feature_file" && -n "$add_label" ]]; then
                # Update status in feature file
                sed -i '' "s/^Status: .*/Status: $add_label/" "$feature_file" 2>/dev/null || \
                sed -i "s/^Status: .*/Status: $add_label/" "$feature_file" 2>/dev/null
                echo "Updated local status to: $add_label"
            fi
            ;;
    esac
}

# ============================================================================
# EVENT HANDLERS (Called by hooks)
# ============================================================================

on_commit() {
    local commit_msg="$1"
    local commit_hash=$(git rev-parse --short HEAD 2>/dev/null || echo "unknown")
    local issue=$(get_current_issue)

    if [[ -n "$issue" ]]; then
        local comment
        comment=$(render_template "commit" \
            "commit_hash=$commit_hash" \
            "commit_message=$commit_msg")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="**Commit:** \`$commit_hash\`
**Message:** $commit_msg

---
$footer"
        fi
        update_issue "$issue" "$comment"
    fi
}

on_push() {
    local branch=$(git branch --show-current 2>/dev/null || echo "unknown")
    local issue=$(get_current_issue)
    local commit_count=$(git rev-list origin/$branch..$branch --count 2>/dev/null || echo "0")

    if [[ -n "$issue" ]]; then
        local comment
        comment=$(render_template "push" \
            "branch=$branch" \
            "commit_count=$commit_count")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="**Branch pushed:** \`$branch\`
**Commits:** $commit_count new commit(s)

---
$footer"
        fi
        update_issue "$issue" "$comment"
    fi
}

on_pr_create() {
    local pr_number="$1"
    local pr_title="$2"
    local issue=$(get_current_issue)

    if [[ -n "$issue" ]]; then
        local comment
        comment=$(render_template "pr-created" \
            "pr_number=$pr_number" \
            "pr_title=$pr_title")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="**Pull Request Created:** #$pr_number
**Title:** $pr_title

---
$footer"
        fi
        update_issue "$issue" "$comment"

        # Update labels using config
        local old_label=$(get_label "in_development" "in-development")
        local new_label=$(get_label "in_review" "in-review")
        update_issue_label "$issue" "$old_label" "$new_label"

        # Send notification to configured channels
        notify_all "PR #$pr_number ready for review: $pr_title" "Pull Request Created" "warning" "16776960"
    fi
}

on_stage_complete() {
    local stage="$1"
    local details="$2"
    local issue=$(get_current_issue)

    # Get label from config
    local new_label=$(get_label "$stage" "$stage")

    if [[ -n "$issue" && -n "$new_label" ]]; then
        local comment
        comment=$(render_template "stage-complete" \
            "stage=${stage^}" \
            "details=$details")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="## Stage Complete: ${stage^}

$details

---
$footer"
        fi
        update_issue "$issue" "$comment"
        update_issue_label "$issue" "" "$new_label"
    fi
}

on_tests_complete() {
    local test_output="$1"
    local passed=$(echo "$test_output" | grep -oE '[0-9]+ pass' | grep -oE '[0-9]+' || echo "0")
    local failed=$(echo "$test_output" | grep -oE '[0-9]+ fail' | grep -oE '[0-9]+' || echo "0")
    local issue=$(get_current_issue)

    if [[ -n "$issue" ]]; then
        local status_emoji="+"
        [[ "$failed" -gt 0 ]] && status_emoji="x"

        local comment
        comment=$(render_template "tests" \
            "status_emoji=$status_emoji" \
            "passed=$passed" \
            "failed=$failed" \
            "test_output=$test_output")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="## $status_emoji Test Results

| Passed | Failed |
|--------|--------|
| $passed | $failed |

<details>
<summary>Test Output</summary>

\`\`\`
$test_output
\`\`\`

</details>

---
$footer"
        fi
        update_issue "$issue" "$comment"
    fi
}

on_deploy() {
    local environment="$1"
    local version="$2"
    local issue=$(get_current_issue)
    local timestamp=$(date -u +"%Y-%m-%d %H:%M UTC")

    if [[ -n "$issue" ]]; then
        local comment
        comment=$(render_template "deploy" \
            "environment=$environment" \
            "version=$version" \
            "timestamp=$timestamp")

        # Fallback if template doesn't exist
        if [[ -z "$comment" ]]; then
            local footer=$(get_footer)
            comment="## Deployment Complete

| Environment | Version | Time |
|-------------|---------|------|
| $environment | $version | $timestamp |

---
$footer"
        fi
        update_issue "$issue" "$comment"

        if [[ "$environment" == "production" ]]; then
            local old_label=$(get_label "qa_complete" "qa-complete")
            local new_label=$(get_label "deployed" "deployed")
            update_issue_label "$issue" "$old_label" "$new_label"

            # Send production deployment notification
            notify_all "Version $version deployed to production at $timestamp" "Production Deployment" "good" "3066993"
        else
            # Send staging/other deployment notification
            notify_all "Version $version deployed to $environment" "Deployment" "warning" "16776960"
        fi
    fi
}

# ============================================================================
# NOTIFICATION FUNCTIONS
# ============================================================================

send_slack_notification() {
    local message="$1"
    local title="${2:-}"
    local color="${3:-good}"  # good=green, warning=yellow, danger=red

    if [[ -z "$SLACK_WEBHOOK_URL" ]]; then
        return 0  # Silently skip if not configured
    fi

    local payload
    if [[ -n "$title" ]]; then
        payload=$(cat << EOF
{
    "attachments": [{
        "color": "$color",
        "title": "$title",
        "text": "$message",
        "footer": "Claude Squad"
    }]
}
EOF
)
    else
        payload="{\"text\": \"$message\"}"
    fi

    curl -s -X POST "$SLACK_WEBHOOK_URL" \
        -H "Content-Type: application/json" \
        -d "$payload" >/dev/null 2>&1 || true
}

send_discord_notification() {
    local message="$1"
    local title="${2:-}"
    local color="${3:-3066993}"  # Green by default (decimal color)

    if [[ -z "$DISCORD_WEBHOOK_URL" ]]; then
        return 0  # Silently skip if not configured
    fi

    local payload
    if [[ -n "$title" ]]; then
        payload=$(cat << EOF
{
    "embeds": [{
        "title": "$title",
        "description": "$message",
        "color": $color,
        "footer": {"text": "Claude Squad"}
    }]
}
EOF
)
    else
        payload="{\"content\": \"$message\"}"
    fi

    curl -s -X POST "$DISCORD_WEBHOOK_URL" \
        -H "Content-Type: application/json" \
        -d "$payload" >/dev/null 2>&1 || true
}

send_teams_notification() {
    local message="$1"
    local title="${2:-}"
    local color="${3:-0076D7}"  # Blue by default

    if [[ -z "$TEAMS_WEBHOOK_URL" ]]; then
        return 0  # Silently skip if not configured
    fi

    local payload=$(cat << EOF
{
    "@type": "MessageCard",
    "@context": "http://schema.org/extensions",
    "themeColor": "$color",
    "title": "${title:-Notification}",
    "text": "$message"
}
EOF
)

    curl -s -X POST "$TEAMS_WEBHOOK_URL" \
        -H "Content-Type: application/json" \
        -d "$payload" >/dev/null 2>&1 || true
}

# Send notification to all configured channels
notify_all() {
    local message="$1"
    local title="${2:-}"
    local slack_color="${3:-good}"
    local discord_color="${4:-3066993}"

    send_slack_notification "$message" "$title" "$slack_color"
    send_discord_notification "$message" "$title" "$discord_color"
    send_teams_notification "$message" "$title"
}

# ============================================================================
# UTILITY FUNCTIONS
# ============================================================================

init_feature_tracking() {
    local feature_name="$1"
    local issue_number="$2"

    # Update PROJECT_STATUS.md
    if [[ -f "$PROJECT_STATUS" ]]; then
        local mode=$(get_tracking_mode)
        local tracking_info=""

        if [[ "$mode" == "local" ]]; then
            local feature_file=$(get_feature_file "$feature_name")
            tracking_info="Local ($feature_file)"
        else
            tracking_info="Issue: #$issue_number"
        fi

        # Add or update current feature section
        if grep -q "## Current Feature" "$PROJECT_STATUS"; then
            sed -i '' "/## Current Feature/,/^## /c\\
## Current Feature\\
Feature: $feature_name | $tracking_info\\
" "$PROJECT_STATUS" 2>/dev/null || \
            sed -i "/## Current Feature/,/^## /c\\
## Current Feature\\
Feature: $feature_name | $tracking_info\\
" "$PROJECT_STATUS" 2>/dev/null
        else
            echo -e "\n## Current Feature\nFeature: $feature_name | $tracking_info" >> "$PROJECT_STATUS"
        fi
    fi

    echo "Feature tracking initialized: $feature_name"
}

status() {
    local mode=$(get_tracking_mode)
    local issue=$(get_current_issue)
    local feature=$(get_current_feature)

    echo "Issue Tracking Status"
    echo "Mode:    $mode"
    echo "Feature: ${feature:-None}"

    if [[ "$mode" == "local" ]]; then
        local feature_file=$(get_feature_file "$feature")
        echo "File:    ${feature_file:-N/A}"
    else
        echo "Issue:   #${issue:-None}"
    fi

    # Show config status
    if [[ -f "$LABELS_CONFIG" ]]; then
        echo "Labels:  Configured"
    else
        echo "Labels:  Using defaults"
    fi

    if [[ -d "$TEMPLATES_DIR" ]]; then
        local template_count=$(ls -1 "$TEMPLATES_DIR"/*.md 2>/dev/null | wc -l | xargs)
        echo "Templates: $template_count loaded"
    else
        echo "Templates: Using inline"
    fi
}

# ============================================================================
# MAIN
# ============================================================================

case "${1:-status}" in
    status)
        status
        ;;
    mode)
        get_tracking_mode
        ;;
    issue)
        get_current_issue
        ;;
    feature)
        get_current_feature
        ;;
    update)
        shift
        update_issue "$@"
        ;;
    label)
        shift
        update_issue_label "$@"
        ;;
    on-commit)
        shift
        on_commit "$*"
        ;;
    on-push)
        on_push
        ;;
    on-pr)
        shift
        on_pr_create "$1" "$2"
        ;;
    on-stage)
        shift
        on_stage_complete "$1" "$2"
        ;;
    on-tests)
        shift
        on_tests_complete "$*"
        ;;
    on-deploy)
        shift
        on_deploy "$1" "$2"
        ;;
    init)
        shift
        init_feature_tracking "$1" "$2"
        ;;
    *)
        # Use usage.sh if available
        if [[ -x "$SCRIPT_DIR/ui/usage.sh" ]]; then
            "$SCRIPT_DIR/ui/usage.sh" --preset issue-tracker
        else
            echo "Usage: issue-tracker.sh <command> [args]"
            echo ""
            echo "Commands:"
            echo "  status              Show tracking status"
            echo "  mode                Get tracking mode (github/linear/jira/local)"
            echo "  issue               Get current issue number"
            echo "  feature             Get current feature name"
            echo "  update <num> <msg>  Update issue with comment"
            echo "  label <num> <rm> <add>  Update issue labels"
            echo "  init <name> [num]   Initialize feature tracking"
            echo ""
            echo "Event handlers (called by hooks):"
            echo "  on-commit <msg>     Handle commit event"
            echo "  on-push             Handle push event"
            echo "  on-pr <num> <title> Handle PR creation"
            echo "  on-stage <stage> <details>  Handle stage completion"
            echo "  on-tests <output>   Handle test completion"
            echo "  on-deploy <env> <ver>  Handle deployment"
            echo ""
            echo "Configuration:"
            echo "  Labels:    $LABELS_CONFIG"
            echo "  Templates: $TEMPLATES_DIR/"
            echo "  Messages:  $MESSAGES_CONFIG"
        fi
        ;;
esac
