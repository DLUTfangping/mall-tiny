---
name: squad:skill
description: Create a new Claude Skill for extending Claude's capabilities with domain-specific expertise
---

# Squad Skill Command

## Initialization

Display "CREATE SKILL" header.

---

## Skill Creation Workflow

Skill Creation steps:
1. Classify Type
2. Identify Category
3. Ask Questions
4. Research
5. Generate Files
6. Assign to Agents

---

## Skill Naming Convention

Skills use kebab-case: `react-hooks`, `api-design`, `code-review`

---

## Phase 1: Skill Classification (15%)

**Q1: Skill Type**

Ask: "What type of skill?" with options:
- "Hard Skills (Recommended) - Technical, measurable"
- "Soft Skills - Process, methodology, patterns"

---

## Phase 2: Category (30%)

**For Hard Skills:**

Ask: "Category?" with options:
- "Programming Language"
- "Framework/Library"
- "Tool/Platform"
- "Database"
- "Cloud/Infrastructure"

**For Soft Skills:**

Ask: "Category?" with options:
- "Development Process"
- "Communication"
- "Code Quality"
- "Architecture Patterns"
- "Team Practices"

---

## Phase 3: Skill Details (50%)

**Q2: Skill Name**

Ask: "Skill name (kebab-case)?"

**Q3: Brief Description**

Ask: "One-line description?"

**Q4: Key Concepts** (category-specific)

For Programming Language:
Ask: "Key concepts?" (multi-select) with options:
- "Syntax & Idioms"
- "Error Handling"
- "Concurrency"
- "Testing"
- "Tooling"

For Framework:
Ask: "Key concepts?" (multi-select) with options:
- "Core Concepts"
- "Best Practices"
- "Common Patterns"
- "Performance"
- "Testing"

---

## Phase 4: Research (65%)

Display: "Researching best practices..."

Actions:
1. Web search for current best practices
2. Fetch official documentation (if available)
3. Gather community patterns and anti-patterns

---

## Phase 5: Generate Files (80%)

Create skill file at `.claude/skills/{skill-name}.md`:
- Description and purpose
- Key concepts and patterns
- Best practices
- Common pitfalls
- Examples

Display success:
- Skill created
- File: .claude/skills/{name}.md

---

## Phase 6: Agent Assignment (100%)

Scan `.claude/agents/` for available agents.

Display available agents table:
| Agent | Role |
|-------|------|
| senior-frontend | Frontend Developer |
| senior-backend | Backend Developer |

**Q5: Assign to Agents**

Ask: "Assign this skill to?" (multi-select)

Update selected agent files to include the new skill.

Display completion:
- Skill ready!
- Skill: {name}
- Assigned to: {agent_count} agents
- Location: .claude/skills/{name}.md

---

## Related Commands

- `/squad:agent` - Create new agents
- `/squad:status` - View skills in use
