# Workflow Hints

Contextual help messages displayed during various operations.
Edit this file to customize the guidance shown to users.

---

## Worktree Operations

### After Creating Feature Worktree

```
Next steps:
1. Agents will work in these isolated worktrees
2. Changes will be committed to feature branches
3. PRs will be created when feature is ready
4. Run `worktree-manager.sh feature-status {feature}` to check progress
```

### After Listing Worktrees

```
Tip: Use `worktree-manager.sh cleanup-merged` to remove worktrees for merged branches.
```

### After Cleanup

```
Cleaned up {count} merged worktree(s).
Consider running `git fetch --prune` to clean up remote references.
```

---

## Feature Development

### After Feature Creation

```
Feature {name} is ready for development!

Workflow:
1. Implement the feature in the worktree
2. Write tests as you develop
3. Commit changes frequently
4. Create a PR when ready for review
5. Address review feedback
6. Merge when approved
```

### After PR Creation

```
PR #{number} created successfully!

Next steps:
1. Wait for CI checks to pass
2. Request reviews from team members
3. Address any feedback
4. Merge when approved and checks pass
```

---

## Issue Tracking

### After Issue Creation

```
Issue #{number} created and linked.

The issue will be automatically updated with:
- Commit messages mentioning this branch
- PR status changes
- Test results
- Deployment notifications
```

### After Stage Completion

```
Stage "{stage}" marked complete!

Issue #{number} has been updated with stage details.
Label changed to: {new_label}
```

---

## Git Operations

### Before Push

```
Pre-push checklist:
- [ ] Tests passing locally
- [ ] No console.log or debug statements
- [ ] Code has been reviewed (if applicable)
- [ ] Branch is up to date with base
```

### After Successful Deploy

```
Deployment to {environment} complete!

Post-deployment checklist:
- [ ] Verify deployment in {environment}
- [ ] Check monitoring for errors
- [ ] Validate critical user flows
- [ ] Update status page if applicable
```

---

## Agent Operations

### After Agent Creation

```
Agent "{name}" created successfully!

To use this agent:
- It will be available in the Task tool
- Assign it to features with /squad:feature
- View agent activity with /squad:status --agents
```

### After Skill Assignment

```
Skill "{skill}" assigned to {count} agent(s).

The agents will now apply this skill's knowledge when working on relevant tasks.
```

---

## Project Initialization

### After Init Complete

```
Project "{name}" initialized!

Quick start:
1. Run `/squad:feature` to start your first feature
2. Run `/squad:status` to view project status
3. Run `/squad:agent` to create custom agents

Documentation:
- Project config: PROJECT.md
- Status tracking: PROJECT_STATUS.md
- Agent directory: .claude/agents/
```

### For Existing Codebase

```
Existing codebase detected!

Detected stack:
{detected_stack}

Squad has been configured to work with your existing:
- Build system
- Test framework
- CI/CD pipeline

Run `/squad:status` to see the current project state.
```

---

## Error Recovery

### When Build Fails

```
Build failed. Common fixes:
1. Check for missing dependencies: npm install / pip install
2. Verify TypeScript/linting errors: npm run lint
3. Check for environment variables
4. Review recent changes in git log
```

### When Tests Fail

```
Tests failed. To debug:
1. Run failing tests in isolation
2. Check test output for specific failures
3. Verify test data/fixtures are correct
4. Look for timing/race conditions in async tests
```

### When Deploy Fails

```
Deployment failed. Recovery steps:
1. Check deployment logs for specific error
2. Verify environment configuration
3. Roll back if necessary: git revert HEAD
4. Check cloud provider status page
```

---

## Sprint Operations

### Sprint Start

```
Sprint {name} started!

Sprint planning checklist:
- [ ] Review and prioritize backlog
- [ ] Assign tasks to team members
- [ ] Set sprint goals
- [ ] Schedule standups and reviews
```

### Sprint End

```
Sprint {name} complete!

Sprint review checklist:
- [ ] Demo completed features
- [ ] Review sprint metrics
- [ ] Conduct retrospective
- [ ] Plan next sprint
```

---

## Custom Hints

Add your project-specific hints below:

```
# Example: Your team's PR review process
Our PR Review Process:
1. Self-review before requesting reviews
2. At least 2 approvals required
3. All comments must be resolved
4. Squash merge to main
```
