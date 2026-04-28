---
name: squad:worktree
description: List, create, clean up, and manage git worktrees across all project repositories
---

# Squad Worktree Command

## Initialization

Display "WORKTREE MANAGER" header.

---

## Available Operations

Worktree Operations:
- List - Show all worktrees
- Create - New worktree
- Remove - Delete worktree
- Cleanup - Remove merged
- Sync - Update status

---

## Subcommand: List

`/squad:worktree list` or `/squad:worktree`

Display: "Scanning worktrees..."

Display all worktrees across repositories in a table:
| Repository | Branch | Path | Status | Age |

Status indicators:
- `active` - Currently in use
- `stale` - No commits in 7+ days
- `merged` - Branch merged to main
- `diverged` - Branch has diverged

---

## Subcommand: Create

`/squad:worktree create [branch-name] [repo]`

**Q1: Branch Name** (if not provided)

Ask: "Branch name?"

**Q2: Repository** (if multi-repo and not provided)

Ask: "Repository?" with available repo options

**Q3: Base Branch**

Ask: "Base branch?" with options "main (Recommended)", "develop"

Execute:
```bash
git worktree add ../{branch-name} -b {branch-name}
```

Display success:
- Worktree created
- Branch: {branch}
- Path: {path}

---

## Subcommand: Remove

`/squad:worktree remove [branch-name]`

**Q1: Select Worktree** (if not provided)

Ask: "Remove which worktree?" with worktree options

**Safety Check:**

Ask: "Remove worktree '{branch}'? This cannot be undone."

Execute:
```bash
git worktree remove {path}
git branch -d {branch}  # if merged
```

Display success:
- Worktree removed
- Branch: {branch}
- Path cleaned up

---

## Subcommand: Cleanup

`/squad:worktree cleanup`

Finds all merged worktrees and offers to remove them.

Display: "Finding merged worktrees..."

Display table of merged worktrees safe to remove:
| Branch | Merged To | Age |

**Confirm Cleanup:**

Ask: "Remove {count} merged worktrees?"

Display success:
- Cleanup complete
- Removed: {count} worktrees
- Freed: {space}

---

## Subcommand: Sync

`/squad:worktree sync`

Updates worktree status from git state.

Display: "Syncing worktree status..."

Actions:
1. Check each worktree for uncommitted changes
2. Update merge status
3. Calculate divergence from base branch
4. Update PROJECT_STATUS.md

Display success:
- Sync complete
- Worktrees: {count}
- Stale: {stale_count}
- Merged: {merged_count}

---

## Safety Features

- Prevents removal of worktrees with uncommitted changes
- Warns before removing unmerged branches
- Backs up branch refs before deletion
- Validates worktree paths exist

---

## Related Commands

- `/squad:feature` - Creates worktrees automatically
- `/squad:status` - Shows worktree status
