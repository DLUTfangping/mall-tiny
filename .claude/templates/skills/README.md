# Claude Skills Templates

Agent Skills are **model-invoked** modular capabilities that extend Claude's functionality. Unlike slash commands (user-invoked), Skills are autonomously activated by Claude based on request context and the Skill's description.

## Quick Start

**Recommended:** Use the `/squad:skill` command to interactively create a new skill.

**Manual:**
1. Copy the template: `cp -r .claude/templates/skills/templates/example-skill .claude/skills/my-skill`
2. Edit `my-skill/SKILL.md` with your skill's name, description, and instructions
3. Test with Claude: "What skills are available?" or ask about your skill's domain

## Directory Structure

```
.claude/templates/skills/          # Skill templates (this folder)
├── README.md                      # This file
├── AUTHORING_GUIDE.md            # Comprehensive guide for creating skills
└── templates/
    ├── minimal-skill/            # Simple single-file template
    │   └── SKILL.md
    └── example-skill/            # Full template with all components
        ├── SKILL.md              # Main skill file (required)
        ├── requirements.txt      # Python dependencies
        ├── reference/            # Additional reference files
        │   ├── REFERENCE.md      # API documentation
        │   ├── EXAMPLES.md       # Usage examples
        │   └── TROUBLESHOOTING.md
        └── scripts/              # Utility scripts
            ├── analyze.py        # Analysis script template
            ├── validate.py       # Validation script template
            └── process.py        # Processing script template

.claude/skills/                    # Your active skills (create this)
└── [your-skills]/                # Custom skills go here
```

## Skill Storage Locations

| Location | Scope | Use Case |
|----------|-------|----------|
| `~/.claude/skills/` | Personal | Individual use, not version controlled |
| `.claude/skills/` | Project | Team-shared, version controlled |

## How Skills Work

### Progressive Disclosure (3 Levels)

| Level | When Loaded | Token Cost | Content |
|-------|------------|------------|---------|
| **Level 1: Metadata** | Always (startup) | ~100 tokens | `name` + `description` from YAML |
| **Level 2: Instructions** | When triggered | <5k tokens | SKILL.md body |
| **Level 3: Resources** | As needed | Unlimited | Scripts, reference files |

### Activation Flow

1. Claude sees all skill metadata at startup
2. When your request matches a skill's description, Claude loads SKILL.md
3. If instructions reference other files, Claude reads them on-demand
4. Scripts execute without loading into context (only output consumes tokens)

### Agent Integration

When skills are assigned to agents (via `/squad:agent` or `/squad:skill`):
1. Skills are added to agent's YAML frontmatter: `skills: react, security-review`
2. Skills auto-load when the agent runs (no manual activation needed)
3. Documentation is added to agent's "## SKILLS I HAVE" section for reference

## Creating a Skill

### Minimal Skill (Single File)

```yaml
---
name: my-skill-name
description: What this skill does. Use when users mention X, Y, or Z.
---

# My Skill Name

## Instructions
1. Step one
2. Step two
3. Step three
```

### Full Skill (With Resources)

```
my-skill/
├── SKILL.md              # Main instructions
├── reference/
│   ├── api.md           # API reference (loaded as needed)
│   └── examples.md      # Examples (loaded as needed)
└── scripts/
    ├── analyze.py       # Utility script (executed, not loaded)
    └── validate.py      # Validation script
```

## Key Requirements

### YAML Frontmatter

| Field | Requirements |
|-------|-------------|
| `name` | Lowercase, numbers, hyphens only. Max 64 chars. No "anthropic" or "claude" |
| `description` | Max 1024 chars. Include what it does AND when to use it. Third person |
| `allowed-tools` | (Optional) Restrict tools: `Read, Grep, Glob` |

### Best Practices

1. **Be concise** - Only include what Claude doesn't already know
2. **Be specific** - Include trigger terms in description
3. **Use gerund naming** - `processing-pdfs`, `analyzing-data`
4. **Keep SKILL.md < 500 lines** - Move details to reference files
5. **One level deep** - Don't deeply nest file references
6. **Validate often** - Include feedback loops in workflows

## Examples

### Read-Only Skill

```yaml
---
name: code-reviewer
description: Review code for best practices and issues. Use when reviewing code, checking PRs, or analyzing code quality.
allowed-tools: Read, Grep, Glob
---

# Code Reviewer

## Checklist
1. Code organization
2. Error handling
3. Performance
4. Security
5. Test coverage
```

### Processing Skill

```yaml
---
name: processing-csvs
description: Parse, clean, and transform CSV files. Use when working with CSV files, spreadsheets, or tabular data.
---

# CSV Processing

## Quick Start
```python
import pandas as pd
df = pd.read_csv("data.csv")
```

## Workflow
1. Load CSV
2. Validate schema
3. Transform data
4. Export results
```

## Resources

- [Authoring Guide](./AUTHORING_GUIDE.md) - Comprehensive skill creation guide
- [Template](./templates/example-skill/) - Full skill template with all components
- [Claude Code Docs](https://code.claude.com/docs/en/skills) - Official documentation
- [Best Practices](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/best-practices) - Anthropic's guide
