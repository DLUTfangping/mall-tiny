---
# ============================================================================
# SKILL FRONTMATTER (REQUIRED)
# ============================================================================
# These fields are loaded at startup for skill discovery (~100 tokens)
#
# name: (REQUIRED)
#   - Maximum 64 characters
#   - Must contain only: lowercase letters, numbers, hyphens
#   - Cannot contain: XML tags, reserved words ("anthropic", "claude")
#   - Use gerund form (verb + -ing): "processing-pdfs", "analyzing-data"
#
# description: (REQUIRED)
#   - Maximum 1024 characters
#   - Must be non-empty, no XML tags
#   - Write in THIRD PERSON (not "I can help you..." or "You can use...")
#   - Include BOTH: what it does AND when to use it
#   - Include trigger terms users would mention
#
# allowed-tools: (OPTIONAL - Claude Code only)
#   - Restricts which tools Claude can use when skill is active
#   - Useful for read-only skills, limited scope, security-sensitive workflows
#   - Example: Read, Grep, Glob (for read-only access)

name: example-skill-name
description: Brief description of what this Skill does. Include trigger terms and contexts for when Claude should use it, such as when users mention specific keywords or file types.
# allowed-tools: Read, Grep, Glob  # Uncomment to restrict tools
---

# Example Skill Name

> **Progressive Disclosure**: This file is loaded when the skill is triggered.
> Keep under 500 lines. Move detailed content to separate reference files.

## Overview

[One-paragraph description of what this skill does and its main purpose.]

## Quick Start

[Provide the most common usage pattern immediately. Users should be able to use the skill with just this section.]

```python
# Example code for the most common use case
def quick_example():
    """Minimal example to get started."""
    pass
```

## Core Instructions

### Step 1: [First Action]

[Clear, actionable instruction]

### Step 2: [Second Action]

[Clear, actionable instruction]

### Step 3: [Third Action]

[Clear, actionable instruction]

## Workflows

### Basic Workflow

Copy this checklist and track progress:

```
Task Progress:
- [ ] Step 1: [Description]
- [ ] Step 2: [Description]
- [ ] Step 3: [Description]
- [ ] Step 4: Verify output
```

**Step 1: [Description]**
[Detailed instructions]

**Step 2: [Description]**
[Detailed instructions]

**Step 3: [Description]**
[Detailed instructions]

**Step 4: Verify output**
[Validation instructions - always include verification!]

### Advanced Workflow

[For complex operations, provide conditional guidance]

1. Determine the task type:
   - **Type A?** → Follow "Type A workflow" in [reference/type-a.md](reference/type-a.md)
   - **Type B?** → Follow "Type B workflow" in [reference/type-b.md](reference/type-b.md)

## Utility Scripts

> Scripts execute without loading into context - only output consumes tokens.

**scripts/analyze.py**: [Brief description]
```bash
python scripts/analyze.py input.txt > output.json
```

**scripts/validate.py**: [Brief description]
```bash
python scripts/validate.py output.json
# Returns: "OK" or lists errors
```

## Advanced Features

For detailed information, see:
- **API Reference**: [reference/REFERENCE.md](reference/REFERENCE.md)
- **Examples**: [reference/EXAMPLES.md](reference/EXAMPLES.md)
- **Troubleshooting**: [reference/TROUBLESHOOTING.md](reference/TROUBLESHOOTING.md)

## Requirements

### Dependencies

```bash
pip install package-name-1 package-name-2
```

### Environment

- Python 3.8+
- [Other requirements]

## Notes

- [Important consideration 1]
- [Important consideration 2]
- [Common pitfall to avoid]

## Version History

- v1.0.0 (YYYY-MM-DD): Initial release
