---
name: squad:status
description: View comprehensive status of all active features, worktrees, agents, and GitHub issues
---

# Squad Status Command

## Initialization

Display "PROJECT STATUS" header.

---

## Status Report Components

This command gathers and displays:
1. Active Features - All features currently in development
2. Worktree Status - State of each git worktree
3. Agent Assignments - Who's working on what
4. GitHub Issues - Linked issues and PRs
5. Blockers - What's holding up progress
6. Recent Completions - What was finished recently

---

## Data Gathering

Display: "Gathering project status..."

Collect data from:
- `PROJECT_STATUS.md` - Project tracking file
- `.claude/agents/` - Agent configurations
- `git worktree list` - Active worktrees
- `gh issue list` - Open GitHub issues
- `gh pr list` - Open pull requests

---

## Display Format

### Project Overview

Display a key-value summary:
- Name: {project_name}
- Active Features: {feature_count}
- Open PRs: {pr_count}
- Agents: {agent_count}

### Active Features

For each feature, display:
- Feature name
- Issue: #{issue_number}
- Branch: {branch}
- Status: {status}
- Assigned: {agents}

### Worktree Status

Display table with columns:
- Branch
- Path
- Status
- Age

### Open Pull Requests

Display table with columns:
- #
- Title
- Status
- Reviews

### Blockers (if any)

Display warning with blocker details.

### Recent Completions

Display list of recently completed items.

---

## Subcommands

### Feature-specific status

`/squad:status --feature {name}`

Shows detailed status for a single feature including:
- All commits on the feature branch
- Test results
- Code review status
- Deployment readiness

### Agent activity

`/squad:status --agents`

Shows what each agent is currently working on.

### Quick summary

`/squad:status --summary`

One-line status per feature:
- feature-auth: Ready for review
- feature-api: In development

---

## Related Commands

- `/squad:feature` - Start new feature
- `/squad:worktree` - Manage worktrees
- `/squad:agent` - Create agents
