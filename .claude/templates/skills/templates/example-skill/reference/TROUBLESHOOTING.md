# Troubleshooting Guide

> Solutions for common issues encountered with this skill

## Contents

- [Skill Not Activating](#skill-not-activating)
- [Script Errors](#script-errors)
- [Validation Failures](#validation-failures)
- [Performance Issues](#performance-issues)

---

## Skill Not Activating

### Issue: Claude doesn't use this skill when expected

**Causes & Solutions:**

1. **Description not specific enough**
   - Check that description includes trigger terms users would mention
   - Include both what it does AND when to use it

   ```yaml
   # Bad
   description: Helps with data

   # Good
   description: Process CSV files, clean data, generate reports. Use when working with CSV files, spreadsheets, or tabular data analysis.
   ```

2. **YAML syntax error**
   - Verify opening `---` is on line 1
   - Verify closing `---` before markdown content
   - No tabs, use spaces for indentation

3. **File path incorrect**
   - Personal: `~/.claude/skills/skill-name/SKILL.md`
   - Project: `.claude/skills/skill-name/SKILL.md`

### Issue: Multiple skills conflict

**Solution:** Use distinct trigger terms in descriptions:

```yaml
# Skill 1 - Be specific about domain
description: Analyze sales data in Excel files. Use for sales reports, pipeline analysis, revenue tracking.

# Skill 2 - Different domain
description: Analyze log files and system metrics. Use for performance monitoring, debugging, diagnostics.
```

---

## Script Errors

### Issue: "File not found" errors

**Cause:** Path resolution issues

**Solutions:**
```python
# Use absolute paths or resolve from script location
import os
script_dir = os.path.dirname(os.path.abspath(__file__))
data_path = os.path.join(script_dir, "..", "data", "input.txt")

# Or use pathlib
from pathlib import Path
script_dir = Path(__file__).parent
data_path = script_dir / ".." / "data" / "input.txt"
```

### Issue: Import errors

**Cause:** Missing dependencies

**Solutions:**
1. Check requirements are installed:
   ```bash
   pip install -r requirements.txt
   ```
2. Verify package is available in code execution environment
3. Use try/except for optional dependencies:
   ```python
   try:
       import optional_package
   except ImportError:
       print("Optional feature unavailable. Install with: pip install optional_package")
   ```

### Issue: Permission denied

**Cause:** File/directory permissions

**Solutions:**
```bash
# Check permissions
ls -la path/to/file

# Fix if needed (be careful with chmod)
chmod 644 path/to/file
```

---

## Validation Failures

### Issue: Schema validation errors

**Diagnosis:**
```python
# Get detailed validation errors
from jsonschema import validate, ValidationError

try:
    validate(data, schema)
except ValidationError as e:
    print(f"Path: {list(e.path)}")
    print(f"Error: {e.message}")
    print(f"Schema rule: {e.schema}")
```

### Issue: Type mismatches

**Common causes:**
```python
# String vs number
{"count": "5"}   # Wrong - string
{"count": 5}     # Correct - number

# Null vs missing
{"field": null}  # Explicit null
{}               # Field missing entirely - may be different!

# Array vs single value
{"items": "one"}     # Single string
{"items": ["one"]}   # Array with one string
```

---

## Performance Issues

### Issue: Slow processing

**Diagnosis:**
```python
import time

start = time.time()
# ... operation ...
print(f"Operation took {time.time() - start:.2f}s")
```

**Solutions:**

1. **Large files** - Use streaming:
   ```python
   # Instead of loading entire file
   with open("large.txt") as f:
       for line in f:  # Process line by line
           process(line)
   ```

2. **Many API calls** - Batch requests:
   ```python
   # Instead of individual calls
   results = client.batch_process(all_items)
   ```

3. **Complex operations** - Cache results:
   ```python
   from functools import lru_cache

   @lru_cache(maxsize=100)
   def expensive_operation(key):
       # ... expensive computation ...
       return result
   ```

### Issue: Memory errors

**Solutions:**
```python
# Process in chunks
def process_large_file(path, chunk_size=1000):
    with open(path) as f:
        chunk = []
        for line in f:
            chunk.append(line)
            if len(chunk) >= chunk_size:
                yield process_chunk(chunk)
                chunk = []
        if chunk:  # Don't forget the last chunk
            yield process_chunk(chunk)
```

---

## Getting Help

If issues persist:

1. Check skill is properly installed: `ls .claude/skills/`
2. Verify YAML frontmatter syntax
3. Test scripts independently before using with skill
4. Review Claude Code logs for error details
