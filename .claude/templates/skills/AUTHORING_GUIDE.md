# Skill Authoring Guide

A comprehensive guide to creating effective Claude Skills based on official Anthropic documentation.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Skill Structure](#skill-structure)
- [Writing Effective Descriptions](#writing-effective-descriptions)
- [Progressive Disclosure](#progressive-disclosure)
- [Workflows and Feedback Loops](#workflows-and-feedback-loops)
- [Common Patterns](#common-patterns)
- [Scripts and Code](#scripts-and-code)
- [Anti-Patterns](#anti-patterns)
- [Testing and Iteration](#testing-and-iteration)
- [Checklist](#checklist)

---

## Core Concepts

### What Makes Skills Different

| Feature | Skills | Slash Commands |
|---------|--------|----------------|
| **Invocation** | Model-invoked (automatic) | User-invoked (`/command`) |
| **Discovery** | Based on description & context | Explicit user action |
| **Scope** | Broad capabilities | Specific actions |

### Key Principles

1. **Concise is key** - Context window is shared; only add what Claude doesn't know
2. **Set appropriate degrees of freedom** - Match specificity to task fragility
3. **Test with all models** - Haiku, Sonnet, Opus have different needs

---

## Skill Structure

### Required File: SKILL.md

```yaml
---
name: skill-name
description: What it does AND when to use it. Include trigger terms.
---

# Skill Name

[Instructions, workflows, and references]
```

### Field Requirements

#### `name`
- Maximum 64 characters
- Only: lowercase letters, numbers, hyphens
- No XML tags
- No reserved words: "anthropic", "claude"
- **Use gerund form**: `processing-pdfs`, `analyzing-data`, `generating-reports`

#### `description`
- Maximum 1024 characters
- Must be non-empty
- No XML tags
- **Write in third person** (not "I can help" or "You can use")
- Include BOTH what it does AND when to use it
- Include trigger terms users would mention

#### `allowed-tools` (Optional)
- Restricts which tools Claude can use
- Useful for read-only or security-sensitive skills
- Example: `Read, Grep, Glob`

---

## Writing Effective Descriptions

The description is **critical** for skill discovery. Claude uses it to choose the right skill from potentially 100+ available.

### Formula

```
[What it does] + [Specific capabilities] + [When to use it / trigger terms]
```

### Examples

**Good:**
```yaml
description: Extract text and tables from PDF files, fill forms, merge documents. Use when working with PDF files or when the user mentions PDFs, forms, or document extraction.
```

**Good:**
```yaml
description: Analyze Excel spreadsheets, create pivot tables, generate charts. Use when analyzing Excel files, spreadsheets, tabular data, or .xlsx files.
```

**Bad:**
```yaml
description: Helps with documents
description: Processes data
description: Does stuff with files
```

### Point of View

Always write in third person:

| ❌ Wrong | ✅ Correct |
|----------|-----------|
| "I can help you process PDFs" | "Processes PDF files and extracts text" |
| "You can use this for data" | "Analyzes data and generates reports" |

---

## Progressive Disclosure

Skills use a three-level loading system to minimize token usage:

### Level 1: Metadata (Always Loaded)
- `name` and `description` from YAML frontmatter
- ~100 tokens per skill
- Loaded at startup for discovery

### Level 2: Instructions (Loaded When Triggered)
- SKILL.md body content
- Target: under 5,000 tokens (500 lines)
- Loaded when skill matches request

### Level 3: Resources (Loaded As Needed)
- Reference files, scripts, documentation
- Effectively unlimited
- Loaded only when referenced

### File Organization Patterns

#### Pattern 1: High-Level Guide with References

```markdown
# PDF Processing

## Quick Start
[Basic usage - always visible when skill is triggered]

## Advanced Features
- **Form filling**: See [FORMS.md](reference/FORMS.md)
- **API reference**: See [REFERENCE.md](reference/REFERENCE.md)
- **Examples**: See [EXAMPLES.md](reference/EXAMPLES.md)
```

#### Pattern 2: Domain-Specific Organization

```
bigquery-skill/
├── SKILL.md (overview and navigation)
└── reference/
    ├── finance.md (revenue, billing metrics)
    ├── sales.md (opportunities, pipeline)
    └── product.md (API usage, features)
```

#### Pattern 3: Conditional Details

```markdown
## Creating Documents
Use docx-js for new documents. See [DOCX-JS.md](reference/DOCX-JS.md).

## Editing Documents
For simple edits, modify the XML directly.

**For tracked changes**: See [REDLINING.md](reference/REDLINING.md)
```

### Key Rules

1. **Keep SKILL.md under 500 lines** - Move details to separate files
2. **One level deep** - Don't nest references (SKILL.md → reference.md, not SKILL.md → file1.md → file2.md)
3. **Table of contents** - For files > 100 lines, add a TOC at the top

---

## Workflows and Feedback Loops

### Workflow Structure

For complex tasks, provide clear steps with a trackable checklist:

```markdown
## Workflow

Copy this checklist and track progress:

```
Task Progress:
- [ ] Step 1: Analyze input
- [ ] Step 2: Validate structure
- [ ] Step 3: Process data
- [ ] Step 4: Validate output
- [ ] Step 5: Write results
```

**Step 1: Analyze input**
[Detailed instructions]

**Step 2: Validate structure**
[Detailed instructions]

...
```

### Feedback Loops

The "validate-fix-repeat" pattern greatly improves output quality:

```markdown
## Processing Workflow

1. Make changes to the data
2. **Validate immediately**: `python scripts/validate.py output.json`
3. If validation fails:
   - Review the error message carefully
   - Fix the issues
   - Run validation again
4. **Only proceed when validation passes**
5. Write final output
```

---

## Common Patterns

### Template Pattern

Provide output format templates for consistency:

**For strict requirements:**
```markdown
## Report Structure

ALWAYS use this exact template:

```markdown
# [Title]

## Executive Summary
[One paragraph]

## Key Findings
- Finding 1
- Finding 2

## Recommendations
1. Recommendation 1
2. Recommendation 2
```
```

**For flexible guidance:**
```markdown
## Report Structure

Use this as a sensible default, adapt as needed:

[template...]

Adjust sections based on the specific context.
```

### Examples Pattern

Provide input/output pairs for clarity:

```markdown
## Commit Message Format

**Example 1:**
Input: Added user authentication with JWT
Output:
```
feat(auth): implement JWT-based authentication

Add login endpoint and token validation middleware
```

**Example 2:**
Input: Fixed bug where dates displayed incorrectly
Output:
```
fix(reports): correct date formatting

Use UTC timestamps consistently
```
```

### Conditional Workflow Pattern

Guide through decision points:

```markdown
## Document Workflow

1. Determine the task type:
   - **Creating new?** → Follow "Creation workflow"
   - **Editing existing?** → Follow "Editing workflow"

2. Creation workflow:
   [steps...]

3. Editing workflow:
   [steps...]
```

---

## Scripts and Code

### When to Use Scripts

Scripts provide:
- Deterministic operations (more reliable than generated code)
- Token efficiency (code doesn't load into context, only output)
- Consistency across uses
- Time savings

### Script Best Practices

#### 1. Handle Errors Explicitly

```python
# Good - solve the problem
def process_file(path):
    try:
        with open(path) as f:
            return f.read()
    except FileNotFoundError:
        print(f"File {path} not found, creating default")
        with open(path, 'w') as f:
            f.write('')
        return ''

# Bad - punt to Claude
def process_file(path):
    return open(path).read()  # Just fails
```

#### 2. No "Voodoo Constants"

```python
# Good - documented
REQUEST_TIMEOUT = 30  # HTTP requests typically complete within 30 seconds
MAX_RETRIES = 3       # Most intermittent failures resolve by second retry

# Bad - magic numbers
TIMEOUT = 47  # Why 47?
RETRIES = 5   # Why 5?
```

#### 3. Clear Execution vs. Reference

Make clear whether Claude should:
- **Execute**: "Run `analyze_form.py` to extract fields"
- **Read as reference**: "See `analyze_form.py` for the algorithm"

### Script Documentation

```python
#!/usr/bin/env python3
"""
Brief description of what this script does.

Usage:
    python script.py input.txt [--option]

Output:
    What the script outputs (stdout/files)
"""
```

---

## Anti-Patterns

### Avoid These

| Anti-Pattern | Problem | Solution |
|--------------|---------|----------|
| Windows paths | `scripts\helper.py` breaks on Unix | Always use forward slashes |
| Too many options | "Use pypdf or pdfplumber or PyMuPDF..." | Provide one default with escape hatch |
| Time-sensitive info | "Before August 2025, use..." | Use "old patterns" section |
| Inconsistent terminology | Mix "endpoint", "URL", "route" | Choose one term |
| Deep nesting | SKILL.md → file1 → file2 → file3 | Keep references one level deep |
| Vague descriptions | "Helps with documents" | Include specific capabilities and triggers |
| First/second person | "I can help you" / "You can use" | Write in third person |

---

## Testing and Iteration

### Evaluation-Driven Development

1. **Identify gaps** - Run Claude without the skill, document failures
2. **Create evaluations** - Build 3+ test scenarios
3. **Establish baseline** - Measure performance without skill
4. **Write minimal instructions** - Only what's needed to pass tests
5. **Iterate** - Test, refine, repeat

### Evaluation Structure

```json
{
  "skills": ["pdf-processing"],
  "query": "Extract text from this PDF",
  "files": ["test.pdf"],
  "expected_behavior": [
    "Successfully reads the PDF",
    "Extracts text from all pages",
    "Handles errors gracefully"
  ]
}
```

### Iterative Development with Claude

1. Complete a task without a skill (with Claude A)
2. Identify reusable patterns
3. Ask Claude A to create a skill capturing those patterns
4. Test with Claude B (fresh instance)
5. Bring observations back to Claude A for refinement
6. Repeat

### What to Watch For

- **Unexpected exploration paths** - Structure may not be intuitive
- **Missed connections** - Links need to be more explicit
- **Overreliance on sections** - Move frequently-read content to SKILL.md
- **Ignored content** - May be unnecessary or poorly signaled

---

## Checklist

### Before Sharing a Skill

#### Core Quality
- [ ] Description is specific and includes trigger terms
- [ ] Description includes both what it does and when to use it
- [ ] Description is in third person
- [ ] SKILL.md body is under 500 lines
- [ ] Additional details are in separate files
- [ ] No time-sensitive information
- [ ] Consistent terminology throughout
- [ ] Examples are concrete, not abstract
- [ ] File references are one level deep
- [ ] Workflows have clear steps with checklist

#### Code and Scripts
- [ ] Scripts handle errors explicitly
- [ ] No "voodoo constants" (all values documented)
- [ ] Required packages listed
- [ ] No Windows-style paths
- [ ] Validation steps included
- [ ] Feedback loops for quality-critical tasks

#### Testing
- [ ] At least 3 evaluations created
- [ ] Tested with target models (Haiku/Sonnet/Opus)
- [ ] Tested with real usage scenarios

---

## Resources

- [Claude Code Skills Documentation](https://code.claude.com/docs/en/skills)
- [Agent Skills Overview](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/overview)
- [Agent Skills Quickstart](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/quickstart)
- [Best Practices](https://platform.claude.com/docs/en/agents-and-tools/agent-skills/best-practices)
- [Skills Cookbook](https://github.com/anthropics/claude-cookbooks/tree/main/skills)
