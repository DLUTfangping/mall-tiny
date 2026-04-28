---
name: squad:init
description: Start the Multi-Agent Squad project orchestration - creates project structure, deploys agents, and begins development workflow
---

# Squad Init Command

## Initialization

Display welcome message explaining this is the Multi-Agent Squad orchestration system.

---

## Question Flow

Questions follow a **dependency funnel** - each answer informs subsequent questions.

### Phase 1: Identity (17%)

**Q1: Project Foundation**

First, detect the current directory name:
```bash
CURRENT_DIR=$(basename "$(pwd)")
```

**CRITICAL:** This question collects TWO pieces of information as **TEXT INPUT** (not multiple choice):

1. **Project name** - Ask separately using text input
   - Header: "Project Name"
   - Question: "What would you like to name your project?"
   - Suggestion: Suggest current directory name as default option

2. **One-line description** - Ask separately using text input
   - Header: "Project Description"
   - Question: "Briefly describe your project in one line (this helps AI agents understand your vision):"
   - Example: "A task management app with real-time collaboration"

**IMPORTANT NOTE:** When using AskUserQuestion for the project name:
- DO NOT create a separate "Custom name" option
- The AskUserQuestion tool ALWAYS provides a built-in "Type something" option that allows users to enter custom text
- Only provide the single recommended option: "Use [directory-name] (Recommended)"
- The user can use the built-in "Type something" option if they want a different name

**Q2: Starting Point**

Ask: "Is this a new project or existing code?" with options "New project from scratch" or "Existing codebase"

Flow control:
- "Existing codebase" → Skip directory creation, analyze current structure first

---

### Phase 2: Scale & Context (33%)

**Q3: Project Type** (multi-select allowed)

**CRITICAL:**
- Header MUST be: "Project Type" (NOT "Description")
- Question MUST be: "What type of project are you building?" or "What are you building?"

Options:
- Web Application (frontend)
- API/Backend Service
- Mobile App (iOS/Android)
- CLI Tool
- Library/Package
- Full-Stack Application

**Q4: Team Scale**

Ask: "Team size?" with options "Solo developer", "Small team (2-5)", "Medium team (6-15)", "Large team (15+)"

Flow control:
- Solo → Lighter process, skip some collaboration features
- Large team → More formal workflows, branching strategies

---

### Phase 3: Architecture (50%)

**Q5: Repository Structure**

Ask: "Repository approach?" with options "Single repo (Recommended for most)", "Monorepo with workspaces", "Multi-repo"

Flow control:
- Monorepo → Ask about workspace tool (Nx, Turborepo, pnpm workspaces)
- Multi-repo → Ask about repo names and relationships

**Q5b: Monorepo Tool** (only if monorepo selected)

Ask: "Monorepo tool?" with options "Nx (Recommended)", "Turborepo", "pnpm workspaces", "Yarn workspaces"

---

### Phase 4: Tech Stack (67%)

Questions conditional on project type selected in Q3:

**For Web/Frontend:**

Ask: "Frontend framework?" with options "React (Recommended)", "Vue", "Angular", "Svelte", "Next.js"

**For API/Backend:**

Ask: "Backend framework?" with options "Node.js/Express", "Python/FastAPI", "Go", "Rust"

**For Mobile:**

Ask: "Mobile approach?" with options "React Native (Recommended)", "Flutter", "Native (iOS/Android)"

**Database** (if applicable):

Ask: "Database?" with options "PostgreSQL (Recommended)", "MySQL", "MongoDB", "SQLite", "None"

---

### Phase 5: Operations (83%)

**Q6: Integrations** (multi-category questions)

Since there are many integrations available, ask in categories:

**Q6a: Project Management Integrations** (multi-select)

Ask: "Project management integrations?"

Options:
- GitHub Issues & Projects (Recommended) - Track tasks and features
- Linear - Modern issue tracking for software teams
- Jira - Enterprise agile project management
- Other PM tools (Azure DevOps, Monday, ClickUp, Asana)

If "Other PM tools" selected, show follow-up:
- Azure DevOps - Microsoft's agile planning tools
- Monday.com - Visual project management
- ClickUp - All-in-one project management
- Asana - Work management platform

**Q6b: Communication Integrations** (multi-select)

Ask: "Communication integrations?"

Options:
- Slack (Recommended) - Build/deploy notifications
- Microsoft Teams - Enterprise communication
- Discord - Community notifications
- Email notifications - Email alerts for events

**Q6c: Documentation Integrations** (multi-select, conditional)

Only ask if scale >= "Small Team" OR intent is Production/Platform:

Ask: "Documentation integrations?"

Options:
- Notion - Docs and knowledge base
- Confluence - Atlassian team docs
- GitHub/GitLab Wiki - Repo-based docs
- None for now

**Q6d: CI/CD Integrations** (multi-select)

Ask: "CI/CD integrations?"

Options:
- GitHub Actions (Recommended) - CI/CD in your repo
- Jenkins - Open source automation
- Custom webhooks - Trigger external services
- None for now

**Q6e: Advanced Integrations** (conditional - Medium+ scale or Production intent)

Ask: "Advanced integrations?"

Options:
- MCP Protocol servers - Enhanced AI capabilities (Database, GitHub, Memory, Docker, etc.)
- Custom webhook endpoints
- Skip advanced integrations

If MCP servers selected, show available servers:
- Database (PostgreSQL Explorer)
- GitHub (Manage issues, PRs, repos)
- Project Memory (Remember context)
- Docker Management
- Jira, Linear, Notion, Confluence (additional options)

**Q7: Development Preferences** (batched multi-select)

Ask: "Development preferences?"

Options:
- TypeScript (recommended)
- ESLint + Prettier
- Git hooks (husky)
- Conventional commits
- Automated testing setup

---

### Phase 5b: Integration Setup (90%)

**CRITICAL:** This phase ONLY runs after Phase 5 questions are answered. For each selected integration that requires API keys or credentials, you MUST collect and validate them.

**Check which integrations require credentials:**

Based on user selections from Q6a-Q6e, identify integrations needing setup:

| Integration | Requires |
|-------------|----------|
| Linear | API key |
| Jira | URL, email, API token |
| Slack | Webhook URL |
| Discord | Webhook URL |
| Microsoft Teams | Webhook URL |
| GitHub Issues | gh CLI authentication |

**For each integration requiring credentials:**

1. **Display setup instructions** using AskUserQuestion with text input
2. **Validate the credentials** before proceeding
3. **Store securely** in `.env.local`
4. **Update PROJECT.md** with configured provider

**Integration Setup Flow (2 steps only):**

```javascript
// Example flow for Linear - ONLY 2 QUESTIONS
if (selectedIntegrations.includes('linear')) {
  // Question 1: Configure now or skip?
  const configure = await askUserQuestion({
    question: "Would you like to set up Linear integration now?",
    header: "Linear",
    options: [
      { label: "Yes, configure now (Recommended)", description: "Enter API key to enable issue tracking" },
      { label: "Skip for now", description: "Configure later" }
    ]
  });

  if (configure === 'skip') continue;

  // Question 2: Get the API key (show instructions, then ask)
  displayMessage(`
    To get your Linear API key:
    1. Go to https://linear.app/start-jur/settings/account/security
    2. Click "Create key" → Name it "Claude Squad"
    3. Copy the key (starts with lin_api_)
  `);

  const response = await askUserQuestion({
    question: "Type your Linear API key or 'skip' to configure later:",
    header: "Linear Key",
    options: [
      { label: "Type in chat", description: "I'll type my API key in the next message" },
      { label: "Skip", description: "Configure Linear later" }
    ]
  });

  // User types API key in chat → validate → store
  if (apiKey && apiKey.startsWith('lin_api_')) {
    const isValid = await validateLinearKey(apiKey);
    if (isValid) {
      saveToEnvLocal('LINEAR_API_KEY', apiKey);
      updateProjectConfig('issue_tracking', 'linear');
      console.log('Linear connected! Authenticated as ' + userName);
    }
  }
}
```

**Credential Collection Order:**

1. **Issue Tracking** (most important - enables feature workflow)
   - If "Linear" selected → Collect LINEAR_API_KEY
   - If "Jira" selected → Collect JIRA_URL, JIRA_USER, JIRA_TOKEN
   - If "GitHub Issues" selected → Verify `gh auth status`, prompt login if needed

2. **Notifications** (enables build/deploy alerts)
   - If "Slack" selected → Collect SLACK_WEBHOOK_URL
   - If "Discord" selected → Collect DISCORD_WEBHOOK_URL
   - If "Microsoft Teams" selected → Collect TEAMS_WEBHOOK_URL

**IMPORTANT - Simple Integration Setup Flow:**

For each integration, use a **2-step flow only**:

1. **Step 1:** Ask if user wants to configure (Yes/Skip)
2. **Step 2:** If yes, ask for the API key directly via text input

```javascript
// Example for Linear - ONLY 2 questions total
// Step 1: Ask to configure
askUserQuestion({
  question: "Would you like to set up Linear integration now?",
  header: "Linear",
  options: [
    { label: "Yes, configure now (Recommended)", description: "Enter your API key to enable issue tracking" },
    { label: "Skip for now", description: "Configure later with /squad:integration" }
  ]
});

// Step 2: If user selected "Yes", ask for the key directly
// Display instructions first, then ask for the key
displayMessage(`
To get your Linear API key:
1. Go to https://linear.app/start-jur/settings/account/security
2. Click "Create key"
3. Name it "Claude Squad" and copy the key
`);

askUserQuestion({
  question: "Paste your Linear API key (starts with lin_api_):",
  header: "Linear Key",
  options: [
    { label: "Type in chat", description: "I'll type my API key in the next message" },
    { label: "Skip", description: "Configure Linear later - continue with setup" }
  ]
});
// User then types the key directly in chat

// For Jira - still only 2 main questions, but collect 3 values
// Step 1: Ask to configure (same pattern)
// Step 2: Ask user to type: URL, email, and token (one message with all 3)
```

**CRITICAL:** Do NOT create intermediate questions like:
- "Do you have your API key ready?"
- "Would you like to enter the key now?"
- "Confirm you want to paste the key"

Just ask **configure now?** → if yes → **paste your key**

**Validation Functions:**

After collecting each credential, validate it works:

```bash
# Linear - Test API key
curl -s -X POST "https://api.linear.app/graphql" \
  -H "Authorization: $LINEAR_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{"query": "{ viewer { id name } }"}'

# Jira - Test credentials
curl -s -u "$JIRA_USER:$JIRA_TOKEN" "$JIRA_URL/rest/api/2/myself"

# Slack - Send test message
curl -s -X POST "$SLACK_WEBHOOK_URL" \
  -H "Content-Type: application/json" \
  -d '{"text": "Claude Squad connected!"}'

# GitHub - Check auth status
gh auth status
```

**Storage:**

Create/update `.env.local` in project root:

```bash
# Create .env.local if it doesn't exist
touch .env.local

# Add to .gitignore
echo ".env.local" >> .gitignore

# Store credentials
echo "LINEAR_API_KEY=lin_api_xxx" >> .env.local
```

**Skip Option:**

The "Skip" option is already included in Step 1 above. No separate skip question needed.

**Show Setup Summary:**

After all integrations are configured, display a summary table showing:
- Integration name and status (Configured/Skipped)

---

### Phase 6: Confirmation (100%)

**Display Summary**

Show a summary table with project configuration:
- Name: {project_name}
- Description: {description}
- Type: {project_type}
- Repo: {repo_structure}
- Stack: {tech_stack}
- Database: {database}

**Confirm**

Ask: "Proceed with this configuration?"

---

## Execution

On confirmation, execute setup:

1. Display: "Creating project structure..." (10%)
2. Create directories and files
3. Display: "Setting up git repository..." (30%)
4. Initialize git, create .gitignore
5. Display: "Installing dependencies..." (50%)
6. Run package manager install
7. Display: "Scaffolding applications..." (55%)
8. **Generate App Boilerplates** - See [App Boilerplate Generation](#app-boilerplate-generation) section below
9. Display: "Copying hook scripts..." (60%)
10. Copy hook scripts and utilities from resources to .claude/scripts/
    - Create directory: `mkdir -p .claude/scripts/hooks`
    - Create directory: `mkdir -p .claude/scripts/integrations`
    - Copy hooks: `cp resources/scripts/hooks/warning-messages.sh .claude/scripts/hooks/`
    - Copy hooks: `cp resources/scripts/hooks/discover-stack.sh .claude/scripts/hooks/`
    - Copy utilities: `cp resources/scripts/issue-tracker.sh .claude/scripts/`
    - Copy utilities: `cp resources/scripts/setup-git-hooks.sh .claude/scripts/`
    - Copy integrations: `cp resources/scripts/integrations/*.sh .claude/scripts/integrations/`
    - Make executable: `chmod +x .claude/scripts/hooks/*.sh .claude/scripts/*.sh .claude/scripts/integrations/*.sh`
11. Display: "Configuring Claude agents..." (70%)
12. Copy agent templates to .claude/agents/
13. Copy config files to .claude/config/
    - Create directory: `mkdir -p .claude/config`
    - Copy labels config: `cp resources/config/labels.toml .claude/config/`
    - Copy messages config: `cp resources/config/messages.toml .claude/config/`
    - Copy integrations config: `cp resources/config/integrations.json .claude/config/`
14. Display: "Finalizing integrations..." (90%)
15. **Integration Setup** (credentials collected in Phase 5b)
    - Verify `.env.local` exists with stored credentials
    - Verify `.gitignore` includes `.env.local`
    - Update PROJECT.md with configured issue_tracking and notifications providers
16. Display: "Complete!" (100%)

**Completion**

Display completion message:
- Project initialized!
- Directory: {path}
- Agents: {count} deployed
- Next: Run /squad:feature to start development

Display tree of created structure showing:
- .claude/ (agents, commands, skills)
- src/
- package.json
- README.md

---

## Error Handling

On any error, display:
- Setup failed
- Error message
- Recovery suggestion

---

## App Boilerplate Generation

**CRITICAL:** This section runs during execution step 8. For each app type selected by the user, you MUST research and run the appropriate scaffolding CLI commands.

### Research-First Approach

**DO NOT use hardcoded CLI commands.** CLI tools change frequently. Always research current documentation at runtime.

### Step 1: For Each Selected App Type, Research the CLI

Based on user selections from Phase 4 (Tech Stack), research the appropriate scaffolding command:

#### For Web Applications

**Research Query:** Use WebSearch with query based on selected framework:
- Next.js: `"create-next-app" CLI options site:nextjs.org {current_year}`
- Vite + React: `"npm create vite" react typescript template site:vite.dev {current_year}`
- Vue: `"create-vue" CLI scaffolding site:vuejs.org {current_year}`
- Svelte: `"create-svelte" CLI scaffolding site:svelte.dev {current_year}`
- Angular: `"ng new" angular CLI site:angular.io {current_year}`

**Typical Commands (verify via research):**
```bash
# Next.js (research latest flags)
npx create-next-app@latest apps/web --typescript --tailwind --eslint --app

# Vite + React (research latest templates)
npm create vite@latest apps/web -- --template react-ts

# Vue (research latest)
npm create vue@latest apps/web
```

#### For Desktop Applications

**Research Query:** Use WebSearch based on selected framework:
- Tauri: `"create-tauri-app" CLI scaffolding site:tauri.app {current_year}`
- Electron: `"create-electron-app" electron-forge CLI site:electronforge.io {current_year}`

**Typical Commands (verify via research):**
```bash
# Tauri (research latest - supports multiple frontend frameworks)
npm create tauri-app@latest apps/desktop

# Electron Forge (research latest templates)
npx create-electron-app@latest apps/desktop --template=webpack-typescript
```

#### For API/Backend Services

**Research Query:** Use WebSearch based on selected framework:
- Hono: `"create-hono" CLI scaffolding bun node site:hono.dev {current_year}`
- Fastify: `"fastify-cli" generate typescript site:fastify.dev {current_year}`
- Express: `"express-generator-typescript" CLI npx {current_year}`
- NestJS: `"nest new" CLI scaffolding site:nestjs.com {current_year}`

**Typical Commands (verify via research):**
```bash
# Hono (research latest - supports bun, node, cloudflare, etc.)
npm create hono@latest apps/api

# Fastify (research latest)
npx fastify-cli generate apps/api --lang=ts

# Express with TypeScript (research latest)
npx express-generator-typescript apps/api

# NestJS (research latest)
npx @nestjs/cli new apps/api
```

#### For Mobile Applications

**Research Query:** Use WebSearch based on selected framework:
- React Native: `"create-expo-app" OR "npx react-native" CLI {current_year}`
- Flutter: `"flutter create" CLI scaffolding {current_year}`

**Typical Commands (verify via research):**
```bash
# React Native with Expo (research latest)
npx create-expo-app@latest apps/mobile

# React Native bare (research latest)
npx react-native init apps/mobile --template react-native-template-typescript

# Flutter (research latest)
flutter create apps/mobile
```

### Step 2: Present Research Findings to User

Before running any scaffolding command, show the user what you found:

```
App Scaffolding Plan

Based on your selections and current documentation:

Web App (Next.js):
  Command: npx create-next-app@latest apps/web --typescript --tailwind --app
  Source: nextjs.org/docs (verified {date})

API (Hono):
  Command: npm create hono@latest apps/api
  Template: bun (for Bun runtime)
  Source: hono.dev/docs (verified {date})

Desktop (Tauri):
  Command: npm create tauri-app@latest apps/desktop
  Frontend: Will use your web app framework
  Source: tauri.app/v2 (verified {date})

Proceed with scaffolding? (y/n)
```

### Step 3: Execute Scaffolding Commands

**IMPORTANT:** Run scaffolding commands sequentially, not in parallel. Each may have interactive prompts.

For each app:
1. Navigate to project root (NOT into apps/ folder - let CLI create the subfolder)
2. Run the researched CLI command
3. If CLI prompts for options, select based on user's tech stack preferences
4. Verify the app was created successfully
5. Update progress

**Handle Interactive Prompts:**
- If a CLI has interactive prompts, try to find non-interactive flags first
- Common flags: `--yes`, `--default`, `--skip-install`, `-y`
- If prompts are unavoidable, inform user and let them respond

### Step 4: Post-Scaffolding Configuration

After scaffolding each app:

1. **Update package.json namespace** (if monorepo with namespace):
   ```bash
   # Update name in apps/web/package.json to use namespace
   # e.g., "name": "@myproject/web"
   ```

2. **Ensure workspace compatibility:**
   - Check that the generated package.json works with the monorepo workspace
   - Add to workspace if needed

3. **Verify build works:**
   ```bash
   cd apps/{app-name} && npm run build
   ```

### Step 5: Handle Failures

If a scaffolding command fails:

1. **Check if it's a version/compatibility issue:**
   - Research if there's a newer or different command
   - Try alternative scaffolding tools

2. **Offer manual alternative:**
   ```
   Scaffolding failed for {app-type}

   Error: {error_message}

   Options:
   1. Try alternative: {alternative_command}
   2. Create minimal boilerplate manually
   3. Skip this app (create empty folder)

   Which would you like?
   ```

3. **Minimal manual boilerplate** (last resort):
   - Create basic folder structure
   - Add minimal package.json with correct name
   - Add placeholder index file
   - Inform user they'll need to set up manually

### Example Full Flow

```
User selected:
- Web: Next.js with TypeScript + Tailwind
- API: Hono with Bun
- Desktop: Tauri

Agent actions:

1. WebSearch: "create-next-app CLI options site:nextjs.org 2025"
   → Found: npx create-next-app@latest --typescript --tailwind --eslint --app

2. WebSearch: "create-hono CLI site:hono.dev 2025"
   → Found: npm create hono@latest (prompts for template: bun, node, etc.)

3. WebSearch: "create-tauri-app CLI site:tauri.app 2025"
   → Found: npm create tauri-app@latest (prompts for frontend framework)

4. Present plan to user, get confirmation

5. Execute:
   $ npx create-next-app@latest apps/web --typescript --tailwind --eslint --app --yes
   Created apps/web

   $ cd apps/api && npm create hono@latest . -- --template bun
   Created apps/api

   $ npm create tauri-app@latest apps/desktop
   [Interactive: select "existing frontend" → point to apps/web]
   Created apps/desktop

6. Verify each app builds successfully
```

---

## Post-Initialization: Agent & Skill Discovery

**IMPORTANT:** This section ONLY runs AFTER the project initialization is complete and the user has confirmed.

### Step 1: Dynamic Discovery

Scan available agents and skills from the filesystem - do NOT use hardcoded lists.

**Scan Agents:**
```bash
# Find all agent markdown files
find .claude/agents -name "*.md" -type f 2>/dev/null

# For each agent, extract frontmatter:
# - name: agent identifier
# - description: what it does and when to use
```

**Scan Skills:**
```bash
# Find all skill directories with skill.md
find .claude/skills -name "skill.md" -type f 2>/dev/null

# For each skill, extract frontmatter:
# - name: skill identifier
# - description: what it does and when to use
```

### Step 2: Match Against Project Configuration

For each discovered agent/skill, check if its `description` matches the project config:

```javascript
function matchAgentToProject(agent, projectConfig) {
  const desc = agent.description.toLowerCase();
  const stack = projectConfig.techStack.toLowerCase();
  const type = projectConfig.type.toLowerCase();

  // Check if description keywords match project config
  const keywords = extractKeywords(desc);

  return keywords.some(keyword =>
    stack.includes(keyword) || type.includes(keyword)
  );
}

function extractKeywords(description) {
  // Extract technology/framework mentions from description
  // e.g., "React, Vue, frontend" from frontend engineer description
  // e.g., "Bun runtime, bun.sh" from bun skill description
  const techPatterns = /\b(react|vue|angular|svelte|next\.?js|node\.?js|bun|rust|python|go|typescript|javascript|frontend|backend|api|cli|mobile|desktop|tauri|flutter|electron)\b/gi;
  return [...description.matchAll(techPatterns)].map(m => m[0].toLowerCase());
}
```

### Step 3: Categorize Matches

Group discovered agents/skills into categories:

```javascript
function categorizeRecommendations(agents, skills, projectConfig) {
  const result = {
    // Core agents - always deploy these
    coreAgents: agents.filter(a =>
      ['prime-orchestrator', 'solution-architect', 'product-manager'].includes(a.name)
    ),

    // Matched agents - deploy based on project type
    matchedAgents: agents.filter(a =>
      matchAgentToProject(a, projectConfig) &&
      !result.coreAgents.includes(a)
    ),

    // Recommended agents - suggest but don't auto-deploy
    recommendedAgents: agents.filter(a =>
      isRecommendedForAnyProject(a) &&
      !result.coreAgents.includes(a) &&
      !result.matchedAgents.includes(a)
    ),

    // Matched skills - based on tech stack
    matchedSkills: skills.filter(s =>
      matchSkillToProject(s, projectConfig)
    )
  };

  return result;
}

function isRecommendedForAnyProject(agent) {
  // Agents like qa-engineer, devops-engineer are useful for most projects
  const universalAgents = ['qa-engineer', 'devops-engineer'];
  return universalAgents.includes(agent.name);
}
```

### Step 4: Display Recommendations (After Init Complete)

**Only show this AFTER the "Complete!" status is displayed.**

Display dynamically discovered and matched items:

**Agents for Your Project**

DEPLOYED (matched your project config):
   [List each agent from coreAgents + matchedAgents]
   - {agent.name} - {agent.description (truncated)}

RECOMMENDED TO ADD:
   [List each agent from recommendedAgents]
   - {agent.name} - {agent.description (truncated)}
     → /squad:agent {agent.name}

OTHER AVAILABLE:
   [List remaining unmatched agents]
   - {agent.name} - {agent.description (truncated)}
     → /squad:agent {agent.name}

Then show skills (only if matchedSkills is not empty):

**Skills Matching Your Tech Stack**

Based on: {projectConfig.techStack}

[List each skill from matchedSkills]
   - {skill.name} - {skill.description (truncated)}
     → /squad:skill {skill.name}

OTHER AVAILABLE SKILLS:
   [List remaining unmatched skills]
   - {skill.name} - {skill.description (truncated)}
     → /squad:skill {skill.name}

Create new skills with /squad:skill

### Step 5: Ask About Installation

After showing recommendations, ask using dynamically built options:

Ask: "Would you like to install any of these now?" (multi-select)

**Build options dynamically:**
```javascript
function buildInstallOptions(recommendations) {
  const options = [];

  // Add recommended agents
  recommendations.recommendedAgents.forEach(agent => {
    options.push({
      label: `${agent.name} (agent)`,
      value: { type: 'agent', name: agent.name }
    });
  });

  // Add matched skills
  recommendations.matchedSkills.forEach(skill => {
    options.push({
      label: `${skill.name} (skill)`,
      value: { type: 'skill', name: skill.name }
    });
  });

  // Always add skip option
  options.push({ label: 'Skip for now', value: null });

  return options;
}
```

**If user selects any:**
- For agents: Copy from resources/agents/ to .claude/agents/
- For skills: Copy from resources/skills/ to .claude/skills/
- Show confirmation for each

### Step 6: Custom Agent & Skill Creation Suggestions

**ALWAYS run this step** after showing existing agents/skills - regardless of whether matches were found. This helps users create domain-specific agents and skills tailored to their project.

#### 6a: Detect Specialized Domains

Analyze the project description and tech stack to identify specialized domains:

```javascript
function detectSpecializedDomains(projectConfig) {
  const { description, techStack, type } = projectConfig;
  const fullContext = `${description} ${techStack} ${type}`.toLowerCase();

  // Domain patterns with suggested agent/skill names
  const domainPatterns = [
    // Business domains
    { pattern: /\b(payment|billing|subscription|stripe|checkout)\b/gi,
      agentName: 'payments-specialist',
      skillName: 'payment-integration',
      agentDesc: 'Expert in payment processing, PCI compliance, and billing systems',
      skillDesc: 'Integrate payment providers (Stripe, PayPal) with best practices' },

    { pattern: /\b(auth|authentication|oauth|sso|identity|login)\b/gi,
      agentName: 'auth-specialist',
      skillName: 'authentication-patterns',
      agentDesc: 'Expert in authentication flows, OAuth, JWT, and security',
      skillDesc: 'Implement secure authentication with modern patterns' },

    { pattern: /\b(machine.?learning|ml|ai|neural|model|training)\b/gi,
      agentName: 'ml-engineer',
      skillName: 'ml-integration',
      agentDesc: 'Expert in ML pipelines, model deployment, and AI integration',
      skillDesc: 'Integrate ML models and AI services into applications' },

    { pattern: /\b(real.?time|websocket|socket\.io|chat|live)\b/gi,
      agentName: 'realtime-specialist',
      skillName: 'realtime-systems',
      agentDesc: 'Expert in WebSockets, real-time sync, and live updates',
      skillDesc: 'Build real-time features with WebSockets and event systems' },

    { pattern: /\b(blockchain|web3|crypto|ethereum|solidity|smart.?contract)\b/gi,
      agentName: 'web3-engineer',
      skillName: 'web3-development',
      agentDesc: 'Expert in blockchain, smart contracts, and Web3 integration',
      skillDesc: 'Develop and integrate Web3 features and smart contracts' },

    { pattern: /\b(video|audio|media|streaming|ffmpeg|transcoding)\b/gi,
      agentName: 'media-specialist',
      skillName: 'media-processing',
      agentDesc: 'Expert in media processing, streaming, and transcoding',
      skillDesc: 'Handle video/audio processing and streaming pipelines' },

    { pattern: /\b(email|notification|push|messaging|sms|twilio)\b/gi,
      agentName: 'notifications-specialist',
      skillName: 'notification-systems',
      agentDesc: 'Expert in email, push notifications, and messaging systems',
      skillDesc: 'Build robust notification and messaging systems' },

    { pattern: /\b(search|elasticsearch|algolia|fulltext|indexing)\b/gi,
      agentName: 'search-specialist',
      skillName: 'search-implementation',
      agentDesc: 'Expert in search engines, indexing, and relevance tuning',
      skillDesc: 'Implement powerful search with Elasticsearch or Algolia' },

    { pattern: /\b(analytics|tracking|metrics|dashboard|reporting)\b/gi,
      agentName: 'analytics-engineer',
      skillName: 'analytics-integration',
      agentDesc: 'Expert in analytics pipelines, tracking, and data visualization',
      skillDesc: 'Integrate analytics and build data dashboards' },

    { pattern: /\b(e.?commerce|shop|cart|inventory|product.?catalog)\b/gi,
      agentName: 'ecommerce-specialist',
      skillName: 'ecommerce-patterns',
      agentDesc: 'Expert in e-commerce systems, carts, and inventory management',
      skillDesc: 'Build e-commerce features with proven patterns' },

    { pattern: /\b(cms|content.?management|headless|sanity|strapi)\b/gi,
      agentName: 'cms-specialist',
      skillName: 'cms-integration',
      agentDesc: 'Expert in CMS architecture and content modeling',
      skillDesc: 'Integrate headless CMS with best practices' },

    { pattern: /\b(game|gaming|unity|godot|physics|multiplayer)\b/gi,
      agentName: 'game-developer',
      skillName: 'game-development',
      agentDesc: 'Expert in game development, physics, and multiplayer systems',
      skillDesc: 'Build games with modern engines and patterns' },

    { pattern: /\b(iot|embedded|sensor|arduino|raspberry|mqtt)\b/gi,
      agentName: 'iot-specialist',
      skillName: 'iot-integration',
      agentDesc: 'Expert in IoT systems, embedded devices, and protocols',
      skillDesc: 'Integrate IoT devices and sensor networks' },

    { pattern: /\b(geolocation|maps|gis|location|coordinates)\b/gi,
      agentName: 'geo-specialist',
      skillName: 'geolocation-features',
      agentDesc: 'Expert in mapping, GIS, and location-based services',
      skillDesc: 'Build location features with maps and geospatial data' }
  ];

  const detected = [];

  domainPatterns.forEach(({ pattern, agentName, skillName, agentDesc, skillDesc }) => {
    if (pattern.test(fullContext)) {
      detected.push({ agentName, skillName, agentDesc, skillDesc });
    }
  });

  return detected;
}
```

#### 6b: Filter Out Already Covered Domains

```javascript
function filterUncoveredDomains(detected, existingAgents, existingSkills) {
  return {
    // Agents not already covered
    suggestedAgents: detected.filter(d =>
      !existingAgents.some(a =>
        a.name === d.agentName ||
        a.description.toLowerCase().includes(d.agentName.replace('-', ' '))
      )
    ),
    // Skills not already covered
    suggestedSkills: detected.filter(d =>
      !existingSkills.some(s =>
        s.name === d.skillName ||
        s.description.toLowerCase().includes(d.skillName.replace('-', ' '))
      )
    )
  };
}
```

#### 6c: Display Custom Creation Suggestions

**ALWAYS show this section if any domains were detected**, even if existing agents/skills matched:

**Custom Creation Suggestions**
Based on: "{project_description}"

SUGGESTED AGENTS TO CREATE:
   Your project mentions domains that could benefit from specialized agents:

   - {agentName}
     {agentDesc}
     → /squad:agent create {agentName}

   - {agentName2}
     {agentDesc2}
     → /squad:agent create {agentName2}

SUGGESTED SKILLS TO CREATE:
   Custom skills to enhance your development workflow:

   - {skillName}
     {skillDesc}
     → /squad:skill create {skillName}

   - {skillName2}
     {skillDesc2}
     → /squad:skill create {skillName2}

#### 6d: Ask About Creating Custom Agents/Skills

```javascript
function promptCustomCreation(suggestions) {
  const options = [];

  // Add suggested agents
  suggestions.suggestedAgents.forEach(s => {
    options.push({
      label: `Create ${s.agentName} agent`,
      description: s.agentDesc,
      value: { type: 'create-agent', name: s.agentName, desc: s.agentDesc }
    });
  });

  // Add suggested skills
  suggestions.suggestedSkills.forEach(s => {
    options.push({
      label: `Create ${s.skillName} skill`,
      description: s.skillDesc,
      value: { type: 'create-skill', name: s.skillName, desc: s.skillDesc }
    });
  });

  if (options.length === 0) {
    // No domain-specific suggestions, offer generic option
    console.log(`
No specific domain patterns detected, but you can always create custom agents/skills:
  → /squad:agent create [name] - Create a specialized agent
  → /squad:skill create [name] - Create a custom skill
    `);
    return;
  }

  options.push({ label: 'Skip custom creation', value: null });

  // Present multi-select choices
  askUserQuestion(
    "Would you like to create any of these specialized agents or skills?",
    options,
    { multiSelect: true }
  );
}
```

#### 6e: Execute Creation

**If user selects any:**

For agents:
```bash
# Invoke /squad:agent create with pre-filled context
# The agent will be created in .claude/agents/{agentName}.md
# with the description and expertise pre-filled based on detection
```

For skills:
```bash
# Invoke /squad:skill create with pre-filled context
# The skill will be created in .claude/skills/{skillName}/
# with the description and patterns pre-filled based on detection
```

**Show confirmation:**
```
Created {agentName} agent in .claude/agents/{agentName}.md
  → Customize: Edit the agent file to refine expertise
  → Use: This agent will now be available for task delegation

Created {skillName} skill in .claude/skills/{skillName}/
  → Customize: Edit skill.md to add patterns and examples
  → Use: Invoke with /squad:skill {skillName}
```

---

## Related Commands

- `/squad:feature` - Start feature development
- `/squad:status` - View project status
- `/squad:agent` - Create custom agents
- `/squad:skill` - Create or install skills
