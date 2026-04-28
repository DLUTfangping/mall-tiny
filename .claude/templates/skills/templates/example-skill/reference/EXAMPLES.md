# Examples

> Concrete input/output pairs help Claude understand desired style and detail level

## Contents

- [Basic Examples](#basic-examples)
- [Advanced Examples](#advanced-examples)
- [Common Patterns](#common-patterns)
- [Edge Cases](#edge-cases)

---

## Basic Examples

### Example 1: Simple Processing

**Input:**
```json
{
  "type": "text",
  "content": "Hello, world!"
}
```

**Output:**
```json
{
  "status": "success",
  "result": {
    "processed": true,
    "word_count": 2,
    "characters": 13
  }
}
```

### Example 2: File Processing

**Input:**
```bash
python scripts/process.py input.txt --format json
```

**Output:**
```
Processing input.txt...
✓ Parsed 150 lines
✓ Validated structure
✓ Generated output.json
Done in 0.3s
```

---

## Advanced Examples

### Example 3: Batch Processing with Validation

**Scenario:** Process multiple files with validation loop

**Input:**
```python
files = ["data1.csv", "data2.csv", "data3.csv"]
for file in files:
    result = process_file(file)
    if not validate(result):
        print(f"Validation failed for {file}")
        # Fix and retry
```

**Expected Output:**
```
Processing data1.csv... OK
Processing data2.csv... OK
Processing data3.csv... VALIDATION ERROR
  - Missing required field: 'timestamp'
  - Invalid format in row 45
Retrying data3.csv with fixes... OK
All files processed successfully.
```

### Example 4: Complex Workflow

**Scenario:** End-to-end data pipeline

```
Task Progress:
- [x] Step 1: Load raw data
- [x] Step 2: Clean and normalize
- [x] Step 3: Validate schema
- [ ] Step 4: Transform to target format
- [ ] Step 5: Export and verify
```

---

## Common Patterns

### Pattern 1: Configuration-First

Always load configuration before processing:

```python
# Good
config = load_config("settings.yaml")
result = process(data, config=config)

# Avoid
result = process(data)  # Uses defaults, may not match requirements
```

### Pattern 2: Validate-Transform-Validate

```python
# 1. Validate input
assert validate_input(raw_data), "Invalid input"

# 2. Transform
transformed = transform(raw_data)

# 3. Validate output
assert validate_output(transformed), "Transform produced invalid output"
```

### Pattern 3: Progressive Enhancement

Start simple, add complexity only when needed:

```python
# Start with basic processing
result = basic_process(data)

# Add options only if required
if needs_advanced:
    result = enhance(result, options=advanced_options)
```

---

## Edge Cases

### Edge Case 1: Empty Input

**Input:**
```json
{}
```

**Expected Behavior:**
```
Warning: Empty input received
Returning default structure with status: 'no_data'
```

### Edge Case 2: Large Files

**Input:** File > 100MB

**Expected Behavior:**
```
Large file detected (150MB)
Switching to streaming mode...
Progress: [████████████████████] 100%
Completed in 45s (memory efficient)
```

### Edge Case 3: Malformed Data

**Input:**
```json
{"broken": json here}
```

**Expected Behavior:**
```
Error: JSON parse error at line 1, column 12
Suggestion: Check for missing quotes or brackets
```
