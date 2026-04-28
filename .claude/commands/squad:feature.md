---
name: squad:feature
description: Start a new feature with full orchestration - creates GitHub issue, worktrees, and assigns agents
---

# Squad Feature Command

## Initialization

Display "FEATURE DEV" header.

---

## Feature Lifecycle

Feature Development Lifecycle:
1. Define Feature
2. Create Issue
3. Setup Worktree
4. Capability Gap Analysis
5. Assign Agents
6. Development

---

## Phase 1: Feature Definition (20%)

**Q1: Feature Name**

Ask: "Feature name (kebab-case)?"

Example: `user-authentication`, `payment-integration`

**Q2: Feature Description**

Ask: "Describe the feature"

Collect a clear description for the GitHub issue body.

**Q3: Feature Type**

Ask: "Feature type?" with options "New Feature", "Enhancement", "Bug Fix", "Refactor", "Documentation"

**Q4: Priority**

Ask: "Priority?" with options "High (blocks other work)", "Medium (planned)", "Low (nice to have)"

---

## Phase 2: Create Issue (40%)

**IMPORTANT:** Use the configured issue tracking provider from PROJECT.md, NOT just GitHub.

### Step 1: Detect Configured Provider

```bash
# Check PROJECT.md for configured provider
provider=$(grep "issue_tracking:" PROJECT.md 2>/dev/null | sed 's/.*issue_tracking:[[:space:]]*//' | tr '[:upper:]' '[:lower:]')

# Default to local if not set
provider="${provider:-local}"
```

### Step 2: Load Credentials

```bash
# Load credentials from .env.local
if [[ -f ".env.local" ]]; then
    set -a
    source .env.local
    set +a
fi
```

### Step 3: Create Issue in Configured Provider

**Use the create-issue.sh helper script:**

```bash
# This script automatically uses the configured provider
.claude/scripts/integrations/create-issue.sh \
    --title "{feature_name}" \
    --body "{description}" \
    --type "{feature_type}" \
    --priority "{priority}"
```

**Or handle each provider manually:**

**GitHub Issues:**
```bash
if [[ "$provider" == "github" ]]; then
    gh issue create \
        --title "{feature_name}" \
        --body "{description}" \
        --label "{type},{priority}"
fi
```

**Linear:**
```bash
if [[ "$provider" == "linear" ]]; then
    # Use LINEAR_API_KEY from .env.local
    curl -X POST "https://api.linear.app/graphql" \
        -H "Authorization: $LINEAR_API_KEY" \
        -H "Content-Type: application/json" \
        -d '{
            "query": "mutation { issueCreate(input: { teamId: \"...\", title: \"...\", description: \"...\" }) { success issue { identifier url } } }"
        }'
fi
```

**Jira:**
```bash
if [[ "$provider" == "jira" ]]; then
    # Use JIRA_URL, JIRA_USER, JIRA_TOKEN from .env.local
    curl -X POST "$JIRA_URL/rest/api/2/issue" \
        -u "$JIRA_USER:$JIRA_TOKEN" \
        -H "Content-Type: application/json" \
        -d '{"fields": {"project": {"key": "..."}, "summary": "...", "issuetype": {"name": "Story"}}}'
fi
```

**Local (fallback):**
```bash
if [[ "$provider" == "local" || -z "$provider" ]]; then
    # Create local feature file
    mkdir -p docs/features/{feature-slug}
    # Create FEATURE.md with tracking info
fi
```

### Step 4: Display Result

Based on provider, show appropriate success message:

**For GitHub:**
- GitHub Issue created
- Issue #XX: {title}
- URL: {github_url}

**For Linear:**
- Linear Issue created
- Issue {identifier}: {title}
- URL: {linear_url}

**For Jira:**
- Jira Issue created
- Issue {key}: {title}
- URL: {jira_url}

**For Local:**
- Local tracking initialized
- Feature: {title}
- File: docs/features/{slug}/FEATURE.md

### Error Handling

If issue creation fails:

1. **Check credentials:**
   ```bash
   # Verify integration is configured
   .claude/scripts/integrations/setup-integration.sh status
   ```

2. **Offer to set up integration:**
   Ask: "Integration not configured. Set up {provider} now?"

3. **Fall back to local tracking:**
   If user declines, create local FEATURE.md instead

---

## Phase 3: Worktree Setup (60%)

**Q5: Repository Selection** (if multi-repo)

Ask: "Which repositories?" (multi-select)

List available repositories from project config.

For each selected repo:
1. Create worktree with branch `feature/{feature-name}`
2. Link to GitHub issue

Display success:
- Worktree created
- Branch: feature/{name}
- Path: {worktree_path}

---

## Phase 4: Capability Gap Analysis (70%)

**Before assigning agents, analyze if the team has all required capabilities:**

### Step 1: Identify Required Capabilities

Based on the feature requirements, determine:
- **Technologies needed** (e.g., React Native, GraphQL, Redis, Stripe, etc.)
- **Domain expertise** (e.g., payments, authentication, ML/AI, blockchain)
- **Infrastructure needs** (e.g., Kubernetes, AWS Lambda, CDN setup)

### Step 2: Scan Existing Agents & Skills

```bash
# Discover available agents
ls -la .claude/agents/*/

# Discover available skills
ls -la .claude/skills/
```

### Step 3: Identify Gaps

Compare required capabilities against available agents/skills.

Display capability analysis table:
| Capability | Status | Resource |
|------------|--------|----------|
| Frontend React | Available | senior-frontend-engineer |
| Payment Integration | MISSING | Need: payment-specialist agent |
| Stripe SDK | MISSING | Need: stripe skill |

### Step 4: Suggest Missing Resources

If gaps are identified, ask the user:

```
I've identified some capability gaps for this feature:

Missing Agents:
- [agent-name]: [reason needed]

Missing Skills:
- [skill-name]: [reason needed]

Would you like me to create these before proceeding?
```

**Options:**
1. "Yes, create all missing agents and skills"
2. "Create agents only"
3. "Create skills only"
4. "Skip - proceed with existing capabilities"

### Step 5: Create Missing Resources (if confirmed)

If user confirms:
- For missing agents → Execute `/squad:agent` for each
- For missing skills → Execute `/squad:skill` for each

Display success:
- Capabilities ready
- Created: {count} new agents
- Created: {count} new skills

---

## Phase 5: Agent Assignment (85%)

**Q6: Agent Selection**

Analyze feature requirements and suggest agents (including newly created ones).

Display recommended agents table:
| Agent | Role | Reason |
|-------|------|--------|
| senior-frontend | Frontend Dev | UI components needed |
| senior-backend | Backend Dev | API endpoints needed |

Ask: "Assign agents?" (multi-select)

Options populated from `.claude/agents/` directory (includes any newly created agents).

---

## Phase 6: Ready to Develop (100%)

Display completion:
- Feature ready for development!
- Issue: #{issue_number}
- Branch: feature/{name}
- Worktree: {path}
- Agents: {count} assigned

---

## Related Commands

- `/squad:status` - View feature progress
- `/squad:worktree` - Manage worktrees
- `/squad:agent` - Create custom agents
