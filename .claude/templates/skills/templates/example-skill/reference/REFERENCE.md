# API Reference

> This file is loaded only when referenced from SKILL.md (Level 3 - progressive disclosure)

## Contents

- [Authentication and Setup](#authentication-and-setup)
- [Core Methods](#core-methods)
- [Advanced Features](#advanced-features)
- [Error Handling](#error-handling)
- [Configuration Options](#configuration-options)

---

## Authentication and Setup

### Basic Setup

```python
from example_package import Client

client = Client(
    api_key="your-api-key",
    timeout=30  # HTTP requests typically complete within 30 seconds
)
```

### Configuration Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `api_key` | str | Required | Authentication key |
| `timeout` | int | 30 | Request timeout in seconds |
| `retries` | int | 3 | Number of retry attempts |

---

## Core Methods

### `process(input_data)`

Process input data and return results.

**Parameters:**
- `input_data` (str|dict): The data to process

**Returns:**
- `dict`: Processed result with status and data

**Example:**
```python
result = client.process({"key": "value"})
print(result["status"])  # "success"
```

### `validate(data)`

Validate data against schema.

**Parameters:**
- `data` (dict): Data to validate

**Returns:**
- `bool`: True if valid, raises ValidationError otherwise

**Example:**
```python
is_valid = client.validate(my_data)
```

### `export(format)`

Export data in specified format.

**Parameters:**
- `format` (str): One of "json", "csv", "xml"

**Returns:**
- `bytes`: Exported data

---

## Advanced Features

### Batch Processing

```python
results = client.batch_process([
    {"id": 1, "data": "..."},
    {"id": 2, "data": "..."},
])
```

### Streaming

```python
for chunk in client.stream_process(large_data):
    print(chunk)
```

---

## Error Handling

### Exception Types

| Exception | Description | Recovery |
|-----------|-------------|----------|
| `ValidationError` | Invalid input data | Check input format |
| `ConnectionError` | Network issues | Retry with backoff |
| `RateLimitError` | Too many requests | Wait and retry |

### Error Handling Pattern

```python
from example_package import ValidationError, ConnectionError

try:
    result = client.process(data)
except ValidationError as e:
    print(f"Invalid data: {e.details}")
    # Fix data and retry
except ConnectionError as e:
    print(f"Connection failed: {e}")
    # Implement retry logic
```

---

## Configuration Options

### Environment Variables

```bash
EXAMPLE_API_KEY=your-key
EXAMPLE_TIMEOUT=60
EXAMPLE_DEBUG=true
```

### Config File

```yaml
# config.yaml
api:
  key: ${EXAMPLE_API_KEY}
  timeout: 60
  retries: 3
logging:
  level: INFO
```
