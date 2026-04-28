#!/usr/bin/env python3
"""
Validate data against schema and rules.

This script implements the validation part of the "validate-fix-repeat"
feedback loop pattern that greatly improves output quality.

Usage:
    python validate.py data.json [--schema schema.json] [--strict]

Output:
    "OK" if validation passes
    Detailed error list if validation fails (with specific fixes)

Best Practice:
    Always run validation BEFORE proceeding to the next step.
    If validation fails, fix issues and run validation again.
"""

import argparse
import json
import sys
from pathlib import Path
from typing import Any


# ============================================================================
# VALIDATION RULES
# ============================================================================

def validate_required_fields(data: dict, required: list[str]) -> list[str]:
    """
    Check that all required fields are present.

    Returns:
        List of error messages (empty if valid)
    """
    errors = []
    for field in required:
        if field not in data:
            errors.append(f"Missing required field: '{field}'")
        elif data[field] is None:
            errors.append(f"Field '{field}' cannot be null")
        elif isinstance(data[field], str) and not data[field].strip():
            errors.append(f"Field '{field}' cannot be empty")
    return errors


def validate_field_types(data: dict, type_map: dict[str, type]) -> list[str]:
    """
    Check that fields have correct types.

    Returns:
        List of error messages (empty if valid)
    """
    errors = []
    for field, expected_type in type_map.items():
        if field in data and data[field] is not None:
            if not isinstance(data[field], expected_type):
                actual_type = type(data[field]).__name__
                errors.append(
                    f"Field '{field}' has wrong type: "
                    f"expected {expected_type.__name__}, got {actual_type}"
                )
    return errors


def validate_value_ranges(data: dict, ranges: dict[str, tuple]) -> list[str]:
    """
    Check that numeric values are within acceptable ranges.

    Args:
        data: Data dictionary
        ranges: Dict mapping field names to (min, max) tuples

    Returns:
        List of error messages (empty if valid)
    """
    errors = []
    for field, (min_val, max_val) in ranges.items():
        if field in data and data[field] is not None:
            value = data[field]
            if isinstance(value, (int, float)):
                if value < min_val:
                    errors.append(
                        f"Field '{field}' value {value} is below minimum {min_val}"
                    )
                if value > max_val:
                    errors.append(
                        f"Field '{field}' value {value} is above maximum {max_val}"
                    )
    return errors


def validate_custom_rules(data: dict) -> list[str]:
    """
    Apply custom business logic validation rules.

    Add your domain-specific validation rules here.

    Returns:
        List of error messages (empty if valid)
    """
    errors = []

    # Example: If status is "completed", end_date must be present
    if data.get("status") == "completed" and not data.get("end_date"):
        errors.append(
            "When status is 'completed', 'end_date' is required"
        )

    # Example: start_date must be before end_date
    if data.get("start_date") and data.get("end_date"):
        if data["start_date"] > data["end_date"]:
            errors.append(
                "'start_date' must be before 'end_date'"
            )

    return errors


# ============================================================================
# MAIN VALIDATION
# ============================================================================

def validate_data(data: dict, strict: bool = False) -> tuple[bool, list[str]]:
    """
    Run all validation rules on data.

    Args:
        data: Data dictionary to validate
        strict: Enable strict mode (warnings become errors)

    Returns:
        Tuple of (is_valid, list of error messages)
    """
    all_errors = []

    # Define validation configuration
    required_fields = ["id", "name", "type"]
    type_map = {
        "id": (int, str),  # Can be int or string
        "name": str,
        "count": int,
        "active": bool,
    }
    value_ranges = {
        "count": (0, 1000000),
        "percentage": (0, 100),
    }

    # Run validations
    all_errors.extend(validate_required_fields(data, required_fields))

    # Type checking (handle multiple acceptable types)
    for field, types in type_map.items():
        if field in data and data[field] is not None:
            if isinstance(types, tuple):
                if not isinstance(data[field], types):
                    type_names = " or ".join(t.__name__ for t in types)
                    actual = type(data[field]).__name__
                    all_errors.append(
                        f"Field '{field}' has wrong type: "
                        f"expected {type_names}, got {actual}"
                    )
            elif not isinstance(data[field], types):
                all_errors.append(
                    f"Field '{field}' has wrong type: "
                    f"expected {types.__name__}, got {type(data[field]).__name__}"
                )

    all_errors.extend(validate_value_ranges(data, value_ranges))
    all_errors.extend(validate_custom_rules(data))

    is_valid = len(all_errors) == 0
    return is_valid, all_errors


def format_validation_result(is_valid: bool, errors: list[str]) -> str:
    """
    Format validation results for output.

    Provides clear, actionable error messages that help Claude fix issues.
    """
    if is_valid:
        return "OK"

    lines = [
        "VALIDATION FAILED",
        "=" * 40,
        f"Found {len(errors)} error(s):",
        "",
    ]

    for i, error in enumerate(errors, 1):
        lines.append(f"  {i}. {error}")

    lines.extend([
        "",
        "=" * 40,
        "Fix these issues and run validation again.",
    ])

    return "\n".join(lines)


# ============================================================================
# CLI INTERFACE
# ============================================================================

def main():
    parser = argparse.ArgumentParser(
        description="Validate data against schema and rules.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
    python validate.py data.json
    python validate.py data.json --strict

Exit codes:
    0 - Validation passed
    1 - Validation failed
    2 - Input error (file not found, invalid JSON)
        """
    )

    parser.add_argument(
        "input_file",
        type=Path,
        help="Path to JSON file to validate"
    )

    parser.add_argument(
        "--schema", "-s",
        type=Path,
        help="Path to JSON schema file (optional)"
    )

    parser.add_argument(
        "--strict",
        action="store_true",
        help="Enable strict validation mode"
    )

    args = parser.parse_args()

    # Load input file
    try:
        if not args.input_file.exists():
            print(f"Error: File not found: {args.input_file}", file=sys.stderr)
            print(f"Suggestion: Check the file path exists.", file=sys.stderr)
            sys.exit(2)

        data = json.loads(args.input_file.read_text())

    except json.JSONDecodeError as e:
        print(f"Error: Invalid JSON in {args.input_file}", file=sys.stderr)
        print(f"  Line {e.lineno}, column {e.colno}: {e.msg}", file=sys.stderr)
        print("Suggestion: Check JSON syntax (quotes, brackets, commas).",
              file=sys.stderr)
        sys.exit(2)

    # Run validation
    is_valid, errors = validate_data(data, strict=args.strict)

    # Output results
    print(format_validation_result(is_valid, errors))

    sys.exit(0 if is_valid else 1)


if __name__ == "__main__":
    main()
