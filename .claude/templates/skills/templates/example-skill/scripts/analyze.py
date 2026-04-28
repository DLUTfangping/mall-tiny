#!/usr/bin/env python3
"""
Analyze input data and extract structured information.

This script demonstrates best practices for skill utility scripts:
1. Handle errors explicitly (don't punt to Claude)
2. Provide clear output for Claude to use
3. Document all configuration parameters
4. Use self-documenting constants (no "voodoo constants")

Usage:
    python analyze.py input.txt [--format json|text] [--verbose]

Output:
    JSON structure with analysis results (stdout)
    Errors and warnings (stderr)
"""

import argparse
import json
import sys
from pathlib import Path
from typing import Any


# ============================================================================
# CONFIGURATION - All constants are documented (no "voodoo constants")
# ============================================================================

# Maximum file size to process in memory (10MB)
# Larger files should use streaming to avoid memory issues
MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024

# Default timeout for operations (30 seconds)
# HTTP requests typically complete within this window
DEFAULT_TIMEOUT = 30

# Retry count for transient failures (3 attempts)
# Most intermittent failures resolve by the second retry
MAX_RETRIES = 3


# ============================================================================
# MAIN FUNCTIONS
# ============================================================================

def analyze_file(file_path: Path, verbose: bool = False) -> dict[str, Any]:
    """
    Analyze a file and return structured results.

    Args:
        file_path: Path to the input file
        verbose: Enable detailed logging

    Returns:
        Dictionary with analysis results

    Raises:
        FileNotFoundError: If file doesn't exist
        ValueError: If file format is invalid
    """
    # Validate file exists
    if not file_path.exists():
        # Handle error explicitly - create helpful message for Claude
        raise FileNotFoundError(
            f"File not found: {file_path}\n"
            f"Suggestion: Check the file path and ensure the file exists."
        )

    # Check file size
    file_size = file_path.stat().st_size
    if file_size > MAX_FILE_SIZE_BYTES:
        if verbose:
            print(f"Warning: Large file ({file_size} bytes), using streaming mode",
                  file=sys.stderr)
        return analyze_file_streaming(file_path)

    # Read and analyze
    content = file_path.read_text(encoding='utf-8')

    return {
        "status": "success",
        "file": str(file_path),
        "analysis": {
            "size_bytes": file_size,
            "line_count": len(content.splitlines()),
            "word_count": len(content.split()),
            "char_count": len(content),
        },
        "metadata": {
            "encoding": "utf-8",
            "processed_fully": True,
        }
    }


def analyze_file_streaming(file_path: Path) -> dict[str, Any]:
    """
    Analyze a large file using streaming (memory efficient).

    Args:
        file_path: Path to the large input file

    Returns:
        Dictionary with analysis results
    """
    line_count = 0
    word_count = 0
    char_count = 0

    with open(file_path, 'r', encoding='utf-8') as f:
        for line in f:
            line_count += 1
            word_count += len(line.split())
            char_count += len(line)

    return {
        "status": "success",
        "file": str(file_path),
        "analysis": {
            "size_bytes": file_path.stat().st_size,
            "line_count": line_count,
            "word_count": word_count,
            "char_count": char_count,
        },
        "metadata": {
            "encoding": "utf-8",
            "processed_fully": True,
            "mode": "streaming",
        }
    }


def format_output(result: dict[str, Any], output_format: str) -> str:
    """
    Format analysis results for output.

    Args:
        result: Analysis results dictionary
        output_format: One of 'json' or 'text'

    Returns:
        Formatted string for stdout
    """
    if output_format == 'json':
        return json.dumps(result, indent=2)

    # Text format
    lines = [
        f"Analysis of: {result['file']}",
        "-" * 40,
        f"Size: {result['analysis']['size_bytes']} bytes",
        f"Lines: {result['analysis']['line_count']}",
        f"Words: {result['analysis']['word_count']}",
        f"Characters: {result['analysis']['char_count']}",
        "-" * 40,
        f"Status: {result['status']}",
    ]
    return "\n".join(lines)


# ============================================================================
# CLI INTERFACE
# ============================================================================

def main():
    parser = argparse.ArgumentParser(
        description="Analyze input data and extract structured information.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
    python analyze.py data.txt
    python analyze.py data.txt --format json
    python analyze.py data.txt --verbose
        """
    )

    parser.add_argument(
        "input_file",
        type=Path,
        help="Path to the input file to analyze"
    )

    parser.add_argument(
        "--format", "-f",
        choices=["json", "text"],
        default="json",
        help="Output format (default: json)"
    )

    parser.add_argument(
        "--verbose", "-v",
        action="store_true",
        help="Enable verbose output"
    )

    args = parser.parse_args()

    try:
        result = analyze_file(args.input_file, verbose=args.verbose)
        print(format_output(result, args.format))
        sys.exit(0)

    except FileNotFoundError as e:
        print(f"Error: {e}", file=sys.stderr)
        sys.exit(1)

    except ValueError as e:
        print(f"Validation Error: {e}", file=sys.stderr)
        sys.exit(2)

    except Exception as e:
        print(f"Unexpected Error: {e}", file=sys.stderr)
        print("Please report this issue.", file=sys.stderr)
        sys.exit(99)


if __name__ == "__main__":
    main()
