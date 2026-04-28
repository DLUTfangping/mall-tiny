#!/usr/bin/env bash
# Warning Messages Hook Script
# Provides configurable warning messages for Claude hooks
# Usage: warning-messages.sh <message-type> [context...]

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_DIR="${SCRIPT_DIR}/../../config"
MESSAGES_FILE="${CONFIG_DIR}/messages.toml"

# Default messages (used if config file not found)
declare -A DEFAULT_MESSAGES=(
    ["tdd_reminder"]="Warning: No test file found. Consider TDD - write tests first!"
    ["push_quality"]="Quality check: Ensure tests pass before pushing."
    ["deploy_gate"]="Deployment requested - ensure all quality gates pass."
    ["file_written"]="File written: %s"
    ["test_reminder"]="Remember to test your changes!"
    ["auto_save"]="Auto-save reminder: Your work is being saved automatically"
    ["commit_reminder"]="Remember to commit your work!"
    ["backup_reminder"]="Remember to backup your writing to cloud storage!"
)

# Parse TOML config file for a specific key
get_config_message() {
    local key="$1"
    local default="${DEFAULT_MESSAGES[$key]:-}"

    if [[ -f "$MESSAGES_FILE" ]]; then
        # Simple TOML parsing - look for key = "value" or key = 'value'
        local value=$(grep -E "^${key}\s*=" "$MESSAGES_FILE" 2>/dev/null | head -1 | sed -E 's/^[^=]+=\s*["'"'"']?([^"'"'"']*)["'"'"']?\s*$/\1/')
        if [[ -n "$value" ]]; then
            echo "$value"
            return 0
        fi
    fi

    echo "$default"
}

# Message handlers
tdd_reminder() {
    local file_path="${1:-}"
    local msg=$(get_config_message "tdd_reminder")

    if [[ -n "$file_path" ]]; then
        # Check if file is a Python file and if test exists
        if [[ "$file_path" == *.py ]]; then
            local test_file="tests/test_$(basename "$file_path")"
            if [[ ! -f "$test_file" ]]; then
                echo "$msg"
            fi
        fi
    else
        echo "$msg"
    fi
}

push_quality() {
    local msg=$(get_config_message "push_quality")
    echo "$msg"
}

deploy_gate() {
    local msg=$(get_config_message "deploy_gate")
    echo "$msg"
}

file_written() {
    local file_path="${1:-unknown}"
    local template=$(get_config_message "file_written")
    printf "$template\n" "$file_path"
}

test_reminder() {
    local msg=$(get_config_message "test_reminder")
    echo "$msg"
}

auto_save() {
    local msg=$(get_config_message "auto_save")
    echo "$msg"
}

commit_reminder() {
    local msg=$(get_config_message "commit_reminder")
    echo "$msg"
}

backup_reminder() {
    local msg=$(get_config_message "backup_reminder")
    echo "$msg"
}

# Check if a message should be shown based on config
should_show() {
    local key="$1"
    local enabled_key="${key}_enabled"

    if [[ -f "$MESSAGES_FILE" ]]; then
        local enabled=$(grep -E "^${enabled_key}\s*=" "$MESSAGES_FILE" 2>/dev/null | head -1 | sed -E 's/^[^=]+=\s*//' | tr '[:upper:]' '[:lower:]')
        if [[ "$enabled" == "false" ]]; then
            return 1
        fi
    fi
    return 0
}

# Main handler
main() {
    local message_type="${1:-}"
    shift || true

    case "$message_type" in
        tdd-reminder|tdd_reminder)
            should_show "tdd_reminder" && tdd_reminder "$@"
            ;;
        push-quality|push_quality)
            should_show "push_quality" && push_quality "$@"
            ;;
        deploy-gate|deploy_gate)
            should_show "deploy_gate" && deploy_gate "$@"
            ;;
        file-written|file_written)
            should_show "file_written" && file_written "$@"
            ;;
        test-reminder|test_reminder)
            should_show "test_reminder" && test_reminder "$@"
            ;;
        auto-save|auto_save)
            should_show "auto_save" && auto_save "$@"
            ;;
        commit-reminder|commit_reminder)
            should_show "commit_reminder" && commit_reminder "$@"
            ;;
        backup-reminder|backup_reminder)
            should_show "backup_reminder" && backup_reminder "$@"
            ;;
        list)
            echo "Available message types:"
            for key in "${!DEFAULT_MESSAGES[@]}"; do
                echo "  - $key"
            done
            ;;
        *)
            echo "Usage: warning-messages.sh <message-type> [context...]"
            echo ""
            echo "Message types:"
            echo "  tdd-reminder [file_path]  - TDD test file reminder"
            echo "  push-quality              - Pre-push quality check"
            echo "  deploy-gate               - Deployment gate warning"
            echo "  file-written <path>       - File write notification"
            echo "  test-reminder             - General test reminder"
            echo "  auto-save                 - Auto-save reminder"
            echo "  commit-reminder           - Git commit reminder"
            echo "  backup-reminder           - Backup reminder"
            echo "  list                      - List all message types"
            exit 1
            ;;
    esac
}

main "$@"
