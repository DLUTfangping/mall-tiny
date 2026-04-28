#!/bin/bash
# Setup Git Hooks for Enterprise Workflow Automation

set -euo pipefail

echo "Setting up Git hooks for enterprise workflow..."

# Create hooks directory if it doesn't exist
HOOKS_DIR=".git/hooks"
mkdir -p "$HOOKS_DIR"

# Pre-commit hook - Code quality checks
cat > "$HOOKS_DIR/pre-commit" << 'EOF'
#!/bin/bash
# Pre-commit hook: Enforce code quality before commit

echo "🔍 Running pre-commit checks..."

# Check for Python files
if git diff --cached --name-only | grep -q '\.py$'; then
    echo "→ Running Python linting..."
    if command -v flake8 &> /dev/null; then
        git diff --cached --name-only --diff-filter=ACM | grep '\.py$' | xargs flake8
    fi
    
    echo "→ Running Python tests..."
    if command -v pytest &> /dev/null; then
        pytest tests/ -v --tb=short || exit 1
    fi
fi

# Check for JavaScript/TypeScript files
if git diff --cached --name-only | grep -qE '\.(js|ts|jsx|tsx)$'; then
    echo "→ Running ESLint..."
    if command -v eslint &> /dev/null; then
        git diff --cached --name-only --diff-filter=ACM | grep -E '\.(js|ts|jsx|tsx)$' | xargs eslint
    fi
fi

# Security scan
echo "→ Running security scan..."
if command -v git-secrets &> /dev/null; then
    git secrets --pre_commit_hook -- "$@"
fi

# Check for large files
echo "→ Checking file sizes..."
for file in $(git diff --cached --name-only); do
    if [ -f "$file" ]; then
        size=$(wc -c < "$file")
        if [ $size -gt 5242880 ]; then  # 5MB
            echo "❌ Error: $file is larger than 5MB"
            exit 1
        fi
    fi
done

echo "✅ Pre-commit checks passed!"
EOF

# Pre-push hook - More comprehensive checks
cat > "$HOOKS_DIR/pre-push" << 'EOF'
#!/bin/bash
# Pre-push hook: Comprehensive checks before push

echo "🚀 Running pre-push checks..."

# Run all tests
echo "→ Running full test suite..."
if [ -f "Makefile" ] && grep -q "test:" Makefile; then
    make test || exit 1
elif [ -f "package.json" ]; then
    npm test || exit 1
elif [ -f "requirements.txt" ]; then
    pytest || exit 1
fi

# Check for uncommitted changes
if ! git diff-index --quiet HEAD --; then
    echo "⚠️  Warning: You have uncommitted changes"
    read -p "Continue push? (y/N) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Verify no merge conflicts markers
if git diff HEAD --name-only | xargs grep -l '^<<<<<<< \|^======= \|^>>>>>>> ' 2>/dev/null; then
    echo "❌ Error: Merge conflict markers found!"
    exit 1
fi

echo "✅ Pre-push checks passed!"
EOF

# Post-merge hook - Update dependencies and notify
cat > "$HOOKS_DIR/post-merge" << 'EOF'
#!/bin/bash
# Post-merge hook: Update dependencies and notify team

echo "🔄 Running post-merge tasks..."

# Update dependencies if package files changed
if git diff-tree -r --name-only --no-commit-id ORIG_HEAD HEAD | grep -q "requirements.txt"; then
    echo "→ Python dependencies changed, updating..."
    pip install -r requirements.txt
fi

if git diff-tree -r --name-only --no-commit-id ORIG_HEAD HEAD | grep -q "package.json"; then
    echo "→ Node dependencies changed, updating..."
    npm install
fi

# Notify about migration needs
if git diff-tree -r --name-only --no-commit-id ORIG_HEAD HEAD | grep -q "migrations/"; then
    echo "⚠️  Database migrations detected! Remember to run migrations."
fi

# Update agent registry if agents changed
if git diff-tree -r --name-only --no-commit-id ORIG_HEAD HEAD | grep -q ".claude/agents/"; then
    echo "🤖 Agent definitions changed. Orchestrator will reload agents."
fi

echo "✅ Post-merge tasks completed!"
EOF

# Commit-msg hook - Enforce commit message standards
cat > "$HOOKS_DIR/commit-msg" << 'EOF'
#!/bin/bash
# Commit-msg hook: Enforce conventional commit format

commit_regex='^(feat|fix|docs|style|refactor|test|chore|build|ci|perf|revert)(\(.+\))?: .{1,50}'

if ! grep -qE "$commit_regex" "$1"; then
    echo "❌ Invalid commit message format!"
    echo ""
    echo "Commit message must follow conventional format:"
    echo "  <type>(<scope>): <subject>"
    echo ""
    echo "Types: feat, fix, docs, style, refactor, test, chore, build, ci, perf, revert"
    echo ""
    echo "Example: feat(auth): add OAuth2 integration"
    echo ""
    exit 1
fi

# Check message length
first_line=$(head -n1 "$1")
if [ ${#first_line} -gt 72 ]; then
    echo "❌ Commit message first line is too long (${#first_line} > 72 characters)"
    exit 1
fi

echo "✅ Commit message validated!"
EOF

# Make all hooks executable
chmod +x "$HOOKS_DIR"/*

# Create workflow automation script
cat > ".claude/scripts/workflow-automation.sh" << 'EOF'
#!/bin/bash
# Workflow automation helpers

# Function to check if PR is ready for review
check_pr_ready() {
    echo "🔍 Checking if PR is ready for review..."
    
    # Check if all tests pass
    if ! make test > /dev/null 2>&1; then
        echo "❌ Tests are failing"
        return 1
    fi
    
    # Check code coverage
    coverage=$(pytest --cov=. --cov-report=term | grep TOTAL | awk '{print $4}' | sed 's/%//')
    if [ "$coverage" -lt 80 ]; then
        echo "❌ Code coverage is below 80% (current: $coverage%)"
        return 1
    fi
    
    # Check for TODO comments
    if grep -r "TODO\|FIXME\|XXX" --include="*.py" --include="*.js" --include="*.ts" .; then
        echo "⚠️  Warning: TODO/FIXME comments found"
    fi
    
    echo "✅ PR is ready for review!"
    return 0
}

# Function to notify about long-running PRs
check_stale_prs() {
    echo "🕒 Checking for stale PRs..."
    
    if command -v gh &> /dev/null; then
        gh pr list --json number,title,createdAt --jq '.[] | select((.createdAt | fromdateiso8601) < (now - 3*24*60*60)) | "PR #\(.number): \(.title) is over 3 days old"'
    fi
}

# Export functions
export -f check_pr_ready
export -f check_stale_prs
EOF

chmod +x .claude/scripts/workflow-automation.sh

echo "Git hooks setup complete!"
echo ""
echo "Hooks installed:"
echo "  - pre-commit   - Code quality checks"
echo "  - pre-push     - Comprehensive testing"
echo "  - post-merge   - Dependency updates"
echo "  - commit-msg   - Message format validation"
echo ""
echo "To bypass hooks (emergency only): git commit --no-verify"