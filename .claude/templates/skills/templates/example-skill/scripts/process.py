#!/usr/bin/env python3
"""
Process data and generate output.

This script demonstrates the complete workflow:
1. Load input
2. Validate (fail early if invalid)
3. Process/transform
4. Validate output
5. Write results

Usage:
    python process.py input.json output.json [--dry-run]

Best Practice:
    Always validate before AND after processing to catch errors early.
"""

import argparse
import json
import sys
from pathlib import Path
from typing import Any
from datetime import datetime


# ============================================================================
# PROCESSING FUNCTIONS
# ============================================================================

def load_input(file_path: Path) -> dict[str, Any]:
    """
    Load and parse input file.

    Handles errors explicitly with helpful messages.
    """
    if not file_path.exists():
        raise FileNotFoundError(
            f"Input file not found: {file_path}\n"
            f"Available files in directory: {list(file_path.parent.glob('*'))}"
        )

    try:
        return json.loads(file_path.read_text())
    except json.JSONDecodeError as e:
        raise ValueError(
            f"Invalid JSON in {file_path}\n"
            f"Error at line {e.lineno}, column {e.colno}: {e.msg}\n"
            f"Suggestion: Validate JSON syntax before processing."
        )


def validate_input(data: dict) -> tuple[bool, list[str]]:
    """
    Validate input data before processing.

    Returns:
        Tuple of (is_valid, error_messages)
    """
    errors = []

    # Check required fields
    required = ["id", "data"]
    for field in required:
        if field not in data:
            errors.append(f"Missing required field: '{field}'")

    # Check data structure
    if "data" in data and not isinstance(data["data"], (list, dict)):
        errors.append("Field 'data' must be a list or dict")

    return len(errors) == 0, errors


def process_data(data: dict) -> dict[str, Any]:
    """
    Main processing logic.

    Transform input data into output format.
    """
    result = {
        "id": data["id"],
        "processed_at": datetime.now().isoformat(),
        "source": data.get("source", "unknown"),
        "results": [],
    }

    # Process the data
    input_data = data.get("data", [])

    if isinstance(input_data, list):
        for i, item in enumerate(input_data):
            processed_item = {
                "index": i,
                "original": item,
                "transformed": transform_item(item),
            }
            result["results"].append(processed_item)
    elif isinstance(input_data, dict):
        for key, value in input_data.items():
            processed_item = {
                "key": key,
                "original": value,
                "transformed": transform_item(value),
            }
            result["results"].append(processed_item)

    result["total_processed"] = len(result["results"])
    result["status"] = "success"

    return result


def transform_item(item: Any) -> Any:
    """
    Transform a single item.

    Customize this function for your specific transformation logic.
    """
    if isinstance(item, str):
        return item.strip().upper()
    elif isinstance(item, (int, float)):
        return item * 2
    elif isinstance(item, dict):
        return {k: transform_item(v) for k, v in item.items()}
    elif isinstance(item, list):
        return [transform_item(i) for i in item]
    else:
        return item


def validate_output(data: dict) -> tuple[bool, list[str]]:
    """
    Validate output data after processing.

    Ensures the output is well-formed before writing.

    Returns:
        Tuple of (is_valid, error_messages)
    """
    errors = []

    # Check required output fields
    required = ["id", "status", "results", "processed_at"]
    for field in required:
        if field not in data:
            errors.append(f"Output missing required field: '{field}'")

    # Check status
    if data.get("status") != "success":
        errors.append(f"Processing did not complete successfully: {data.get('status')}")

    # Check results
    if "results" in data and not isinstance(data["results"], list):
        errors.append("Output 'results' must be a list")

    return len(errors) == 0, errors


def write_output(data: dict, file_path: Path) -> None:
    """
    Write output to file.

    Creates parent directories if needed.
    """
    # Ensure parent directory exists
    file_path.parent.mkdir(parents=True, exist_ok=True)

    # Write with pretty formatting
    file_path.write_text(json.dumps(data, indent=2))


# ============================================================================
# CLI INTERFACE
# ============================================================================

def main():
    parser = argparse.ArgumentParser(
        description="Process data and generate output.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
    python process.py input.json output.json
    python process.py input.json output.json --dry-run

Workflow:
    1. Load input file
    2. Validate input
    3. Process/transform data
    4. Validate output
    5. Write results (unless --dry-run)
        """
    )

    parser.add_argument(
        "input_file",
        type=Path,
        help="Path to input JSON file"
    )

    parser.add_argument(
        "output_file",
        type=Path,
        help="Path to output JSON file"
    )

    parser.add_argument(
        "--dry-run", "-n",
        action="store_true",
        help="Validate and process but don't write output"
    )

    parser.add_argument(
        "--verbose", "-v",
        action="store_true",
        help="Enable verbose output"
    )

    args = parser.parse_args()

    try:
        # Step 1: Load input
        if args.verbose:
            print(f"Loading {args.input_file}...", file=sys.stderr)
        data = load_input(args.input_file)

        # Step 2: Validate input
        if args.verbose:
            print("Validating input...", file=sys.stderr)
        is_valid, errors = validate_input(data)
        if not is_valid:
            print("INPUT VALIDATION FAILED:", file=sys.stderr)
            for error in errors:
                print(f"  - {error}", file=sys.stderr)
            sys.exit(1)

        # Step 3: Process
        if args.verbose:
            print("Processing...", file=sys.stderr)
        result = process_data(data)

        # Step 4: Validate output
        if args.verbose:
            print("Validating output...", file=sys.stderr)
        is_valid, errors = validate_output(result)
        if not is_valid:
            print("OUTPUT VALIDATION FAILED:", file=sys.stderr)
            for error in errors:
                print(f"  - {error}", file=sys.stderr)
            sys.exit(1)

        # Step 5: Write output
        if args.dry_run:
            print("Dry run - output not written")
            print(json.dumps(result, indent=2))
        else:
            if args.verbose:
                print(f"Writing {args.output_file}...", file=sys.stderr)
            write_output(result, args.output_file)
            print(f"OK - Processed {result['total_processed']} items")
            print(f"Output written to: {args.output_file}")

        sys.exit(0)

    except FileNotFoundError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(2)

    except ValueError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(2)

    except Exception as e:
        print(f"Unexpected error: {e}", file=sys.stderr)
        sys.exit(99)


if __name__ == "__main__":
    main()
