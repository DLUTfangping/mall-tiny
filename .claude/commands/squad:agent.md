---
name: squad:agent
description: Create a new specialized agent for your project with rich experience and domain expertise
---

# Squad Agent Command

## Initialization

Display "CREATE AGENT" header.

---

## Agent Creation Workflow

Agent Creation steps:
1. Gather Requirements
2. Select Template
3. Choose Color
4. Customize
5. Discover Skills
6. Assign Skills
7. Generate File

---

## Phase 1: Requirements (15%)

**Q1: Agent Role**

Ask: "What role will this agent fill?"

Examples: "Security Auditor", "Database Architect", "Mobile Developer"

**Q2: Agent Category**

Ask: "Category?" with options:
- "Engineering"
- "Product"
- "Architecture"
- "Quality"
- "Operations"
- "Specialized"

**Q3: Experience Level**

Ask: "Experience level?" with options:
- "Senior (10+ years)"
- "Staff/Principal (15+ years)"
- "Junior (3-5 years)"

---

## Phase 2: Template Selection (30%)

Based on category, show relevant templates.

Display available templates table:
| Template | Description |
|----------|-------------|
| senior-backend | Backend API development |
| senior-frontend | Frontend UI/UX |
| devops-engineer | CI/CD and infrastructure |

Ask: "Base template?" with template options

---

## Phase 3: Visual Identity (45%)

**Q4: Agent Color**

Ask: "Agent color?" with options:
- "Blue (default)"
- "Green"
- "Purple"
- "Orange"
- "Red"
- "Cyan"

Color is used for status displays and agent identification.

---

## Phase 4: Customization (55%)

**Q5: Domain Expertise**

Ask: "Domain expertise?" (multi-select)

Options based on category (e.g., for Engineering: "Microservices", "GraphQL", "Real-time systems")

**Q6: Tools & Technologies**

Ask: "Primary tools?" (multi-select)

Options: Language-specific tools, frameworks, platforms

---

## Phase 5: Skill Discovery (70%)

Scan `.claude/skills/` directory for available skills.

Display available skills table:
| Skill | Type | Description |
|-------|------|-------------|
| react | Hard | React 18+ patterns |
| typescript | Hard | TypeScript best practices |
| code-review | Soft | Code review methodology |

---

## Phase 6: Skill Assignment (85%)

**Q7: Select Skills**

Ask: "Assign skills to this agent?" (multi-select)

Options populated from discovered skills.

---

## Phase 7: Generation (100%)

Create agent file at `.claude/agents/{agent-name}.md`:
- Header with role and description
- Experience narrative
- Assigned skills
- Tool permissions

Display completion:
- Agent created!
- File: .claude/agents/{name}.md
- Skills: {skill_count} assigned
- Ready to use with Task tool

---

## Related Commands

- `/squad:skill` - Create new skills
- `/squad:feature` - Assign agents to features
- `/squad:status` - View agent activity
