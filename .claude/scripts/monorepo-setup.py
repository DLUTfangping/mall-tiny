#!/usr/bin/env python3
"""
Monorepo Framework Setup for Multi-Agent Squad
Supports Nx and TurboRepo monorepo architectures
"""

import os
import sys
import json
import subprocess
import shutil
from pathlib import Path
from typing import Dict, List, Optional, Tuple

class MonorepoSetup:
    """Handles monorepo framework setup and configuration"""

    def __init__(self, project_path: str = "project"):
        self.project_path = Path(project_path)
        self.root_path = Path.cwd()

        # Framework configurations
        self.frameworks = {
            "nx": {
                "name": "Nx",
                "description": "Full-featured monorepo framework with code generators, dependency graph, and distributed task execution",
                "best_for": ["Large teams", "Multi-stack projects", "Enterprise", "Angular/React ecosystems"],
                "pros": [
                    "Code generators for consistent scaffolding",
                    "Visual dependency graph",
                    "Distributed task execution",
                    "Supports multiple languages (JS, Python, Go, etc.)",
                    "7x faster than Turborepo for large repos (benchmarked)"
                ],
                "cons": [
                    "Steeper learning curve",
                    "More complex configuration",
                    "Heavier setup"
                ],
                "install_cmd": "npx create-nx-workspace@latest",
                "config_files": ["nx.json", "project.json"]
            },
            "turborepo": {
                "name": "TurboRepo",
                "description": "Lightweight, high-performance task runner focused on speed and simplicity",
                "best_for": ["Small to medium teams", "JS/TS projects", "Vercel/Next.js", "Quick setup"],
                "pros": [
                    "Simple 20-line configuration",
                    "Fast setup (15 minutes)",
                    "Tight Vercel integration",
                    "Minimal learning curve",
                    "Excellent for Next.js projects"
                ],
                "cons": [
                    "No code generators",
                    "No dependency visualization built-in",
                    "No distributed execution",
                    "JavaScript/TypeScript only"
                ],
                "install_cmd": "npx create-turbo@latest",
                "config_files": ["turbo.json", "pnpm-workspace.yaml"]
            }
        }

        # Preset configurations
        self.nx_presets = {
            "ts": "Empty TypeScript workspace",
            "react": "React application",
            "react-native": "React Native application",
            "angular": "Angular application",
            "next": "Next.js application",
            "nest": "NestJS backend",
            "express": "Express backend",
            "node": "Node.js application",
            "web-components": "Web Components library"
        }

        self.turbo_templates = {
            "basic": "Basic starter with apps and packages",
            "with-nextjs": "Next.js application with shared packages",
            "with-vite": "Vite applications with shared packages",
            "with-npm": "npm workspaces configuration",
            "with-pnpm": "pnpm workspaces configuration (recommended)",
            "with-yarn": "Yarn workspaces configuration"
        }

    def interactive_setup(self) -> Dict:
        """Main interactive setup flow"""
        print("\n" + "=" * 60)
        print("  MONOREPO FRAMEWORK SETUP")
        print("=" * 60)

        # Step 1: Explain options
        print("\n📚 Available Monorepo Frameworks:\n")

        for key, framework in self.frameworks.items():
            print(f"  [{key.upper()}] {framework['name']}")
            print(f"      {framework['description']}")
            print(f"      Best for: {', '.join(framework['best_for'])}")
            print()

        # Step 2: Get framework choice
        framework = self._ask_framework_choice()
        if not framework:
            return {"status": "cancelled"}

        # Step 3: Get package manager preference
        package_manager = self._ask_package_manager()

        # Step 4: Get project structure
        structure = self._ask_project_structure(framework)

        # Step 5: Get initial packages
        initial_packages = self._ask_initial_packages(framework)

        # Step 6: Confirm and execute
        config = {
            "framework": framework,
            "package_manager": package_manager,
            "structure": structure,
            "initial_packages": initial_packages
        }

        if self._confirm_setup(config):
            return self._execute_setup(config)

        return {"status": "cancelled"}

    def _ask_framework_choice(self) -> Optional[str]:
        """Ask user to choose a framework"""
        print("\n" + "-" * 40)
        print("FRAMEWORK SELECTION")
        print("-" * 40)

        print("\n🤔 Which framework would you like to use?\n")
        print("  [1] Nx")
        print("      → Choose if: Large team, multiple languages, need code generators")
        print("      → Setup time: ~10-15 minutes")
        print()
        print("  [2] TurboRepo")
        print("      → Choose if: Small team, JS/TS only, want simplicity")
        print("      → Setup time: ~5 minutes")
        print()
        print("  [3] Help me decide")
        print()
        print("  [0] Cancel")

        while True:
            choice = input("\nEnter choice (0-3): ").strip()

            if choice == "0":
                return None
            elif choice == "1":
                return "nx"
            elif choice == "2":
                return "turborepo"
            elif choice == "3":
                return self._framework_decision_wizard()
            else:
                print("Please enter 1, 2, 3, or 0")

    def _framework_decision_wizard(self) -> Optional[str]:
        """Help user decide which framework to use"""
        print("\n" + "-" * 40)
        print("DECISION HELPER")
        print("-" * 40)

        score = {"nx": 0, "turborepo": 0}

        questions = [
            {
                "question": "How large is your team?",
                "options": {
                    "1": ("1-3 developers", {"turborepo": 2}),
                    "2": ("4-10 developers", {"nx": 1, "turborepo": 1}),
                    "3": ("10+ developers", {"nx": 2})
                }
            },
            {
                "question": "What languages will you use?",
                "options": {
                    "1": ("JavaScript/TypeScript only", {"turborepo": 2}),
                    "2": ("JS/TS + Python/Go/other", {"nx": 2}),
                    "3": ("Multiple backend languages", {"nx": 2})
                }
            },
            {
                "question": "How important is quick setup?",
                "options": {
                    "1": ("Very important - I want it working in minutes", {"turborepo": 2}),
                    "2": ("Somewhat - willing to spend time for features", {"nx": 1}),
                    "3": ("Not important - I want the best tooling", {"nx": 2})
                }
            },
            {
                "question": "Do you need code generation (scaffolding)?",
                "options": {
                    "1": ("Yes, definitely", {"nx": 2}),
                    "2": ("Nice to have", {"nx": 1}),
                    "3": ("No, I'll manage manually", {"turborepo": 1})
                }
            },
            {
                "question": "Are you using Vercel or Next.js heavily?",
                "options": {
                    "1": ("Yes, it's core to our stack", {"turborepo": 2}),
                    "2": ("We use it but it's not primary", {"turborepo": 1}),
                    "3": ("No", {"nx": 1})
                }
            }
        ]

        for q in questions:
            print(f"\n{q['question']}")
            for key, (label, _) in q['options'].items():
                print(f"  [{key}] {label}")

            while True:
                answer = input("\nYour choice: ").strip()
                if answer in q['options']:
                    _, scores = q['options'][answer]
                    for framework, points in scores.items():
                        score[framework] += points
                    break
                print("Please enter a valid option")

        # Calculate recommendation
        if score["nx"] > score["turborepo"]:
            recommendation = "nx"
        elif score["turborepo"] > score["nx"]:
            recommendation = "turborepo"
        else:
            recommendation = "turborepo"  # Default to simpler option on tie

        print(f"\n✨ Based on your answers, I recommend: {self.frameworks[recommendation]['name']}")
        print(f"   Score: Nx={score['nx']}, TurboRepo={score['turborepo']}")

        confirm = input(f"\nUse {self.frameworks[recommendation]['name']}? (Y/n): ").strip().lower()
        if confirm == "n":
            return self._ask_framework_choice()

        return recommendation

    def _ask_package_manager(self) -> str:
        """Ask user for package manager preference"""
        print("\n" + "-" * 40)
        print("PACKAGE MANAGER")
        print("-" * 40)

        print("\nWhich package manager do you prefer?\n")
        print("  [1] pnpm (Recommended - faster, disk efficient)")
        print("  [2] npm")
        print("  [3] yarn")
        print("  [4] bun")

        options = {"1": "pnpm", "2": "npm", "3": "yarn", "4": "bun"}

        while True:
            choice = input("\nEnter choice (1-4) [1]: ").strip() or "1"
            if choice in options:
                return options[choice]
            print("Please enter 1, 2, 3, or 4")

    def _ask_project_structure(self, framework: str) -> Dict:
        """Ask about project structure"""
        print("\n" + "-" * 40)
        print("PROJECT STRUCTURE")
        print("-" * 40)

        structure = {
            "apps_dir": "apps",
            "packages_dir": "packages",
            "namespace": ""
        }

        # Get namespace
        print("\n📦 Package Namespace")
        print("   This prefix will be used for all internal packages")
        print("   Example: @mycompany/ui, @mycompany/utils")

        namespace = input("\nEnter namespace (e.g., @mycompany) [skip]: ").strip()
        if namespace:
            if not namespace.startswith("@"):
                namespace = f"@{namespace}"
            structure["namespace"] = namespace

        # Confirm structure
        print(f"\n📁 Default structure:")
        print(f"   {self.project_path}/")
        print(f"   ├── {structure['apps_dir']}/       # Applications")
        print(f"   ├── {structure['packages_dir']}/   # Shared packages")
        print(f"   └── ...")

        custom = input("\nCustomize folder names? (y/N): ").strip().lower()
        if custom == "y":
            apps = input(f"Apps folder name [{structure['apps_dir']}]: ").strip()
            if apps:
                structure["apps_dir"] = apps
            packages = input(f"Packages folder name [{structure['packages_dir']}]: ").strip()
            if packages:
                structure["packages_dir"] = packages

        return structure

    def _ask_initial_packages(self, framework: str) -> List[Dict]:
        """Ask about initial packages to create"""
        print("\n" + "-" * 40)
        print("INITIAL PACKAGES")
        print("-" * 40)

        packages = []

        if framework == "nx":
            print("\n📦 Nx Presets Available:\n")
            for i, (key, desc) in enumerate(self.nx_presets.items(), 1):
                print(f"   [{i}] {key}: {desc}")

            print("\n   [0] Skip - I'll add packages later")

            choice = input("\nSelect preset (0-9): ").strip()
            if choice != "0" and choice.isdigit():
                idx = int(choice) - 1
                keys = list(self.nx_presets.keys())
                if 0 <= idx < len(keys):
                    preset = keys[idx]
                    name = input(f"Application name [my-app]: ").strip() or "my-app"
                    packages.append({
                        "type": "app",
                        "preset": preset,
                        "name": name
                    })
        else:
            print("\n📦 TurboRepo Templates Available:\n")
            for i, (key, desc) in enumerate(self.turbo_templates.items(), 1):
                print(f"   [{i}] {key}: {desc}")

            print("\n   [0] Skip - I'll configure manually")

            choice = input("\nSelect template (0-6): ").strip()
            if choice != "0" and choice.isdigit():
                idx = int(choice) - 1
                keys = list(self.turbo_templates.keys())
                if 0 <= idx < len(keys):
                    packages.append({
                        "type": "template",
                        "template": keys[idx]
                    })

        # Ask about common shared packages
        print("\n📚 Common Shared Packages:")
        print("   Would you like to create any of these?")

        common_packages = [
            ("ui", "Shared UI components library"),
            ("utils", "Common utility functions"),
            ("config", "Shared configuration (ESLint, TSConfig, etc.)"),
            ("types", "Shared TypeScript types/interfaces")
        ]

        for name, desc in common_packages:
            create = input(f"   Create '{name}' ({desc})? (y/N): ").strip().lower()
            if create == "y":
                packages.append({
                    "type": "package",
                    "name": name,
                    "description": desc
                })

        return packages

    def _confirm_setup(self, config: Dict) -> bool:
        """Show configuration summary and confirm"""
        print("\n" + "=" * 60)
        print("  CONFIGURATION SUMMARY")
        print("=" * 60)

        framework = self.frameworks[config["framework"]]

        print(f"\n  Framework:       {framework['name']}")
        print(f"  Package Manager: {config['package_manager']}")
        print(f"  Project Path:    {self.project_path}")

        if config["structure"]["namespace"]:
            print(f"  Namespace:       {config['structure']['namespace']}")

        print(f"\n  Structure:")
        print(f"    └── {config['structure']['apps_dir']}/")
        print(f"    └── {config['structure']['packages_dir']}/")

        if config["initial_packages"]:
            print(f"\n  Initial Packages:")
            for pkg in config["initial_packages"]:
                if pkg["type"] == "app":
                    print(f"    - App: {pkg['name']} ({pkg['preset']})")
                elif pkg["type"] == "template":
                    print(f"    - Template: {pkg['template']}")
                else:
                    print(f"    - Package: {pkg['name']}")

        print("\n" + "-" * 60)

        confirm = input("\nProceed with setup? (Y/n): ").strip().lower()
        return confirm != "n"

    def _execute_setup(self, config: Dict) -> Dict:
        """Execute the monorepo setup"""
        print("\n" + "=" * 60)
        print("  EXECUTING SETUP")
        print("=" * 60)

        result = {
            "status": "success",
            "framework": config["framework"],
            "created_files": [],
            "commands_run": [],
            "errors": []
        }

        try:
            # Create project directory if it doesn't exist
            self.project_path.mkdir(parents=True, exist_ok=True)

            if config["framework"] == "nx":
                self._setup_nx(config, result)
            else:
                self._setup_turborepo(config, result)

            # Generate configuration documentation
            self._generate_monorepo_docs(config, result)

        except Exception as e:
            result["status"] = "error"
            result["errors"].append(str(e))

        return result

    def _setup_nx(self, config: Dict, result: Dict) -> None:
        """Set up Nx monorepo"""
        print("\n🔧 Setting up Nx workspace...")

        workspace_name = self.project_path.name or "workspace"
        pm = config["package_manager"]

        # Generate nx.json configuration
        nx_config = {
            "$schema": "./node_modules/nx/schemas/nx-schema.json",
            "namedInputs": {
                "default": ["{projectRoot}/**/*", "sharedGlobals"],
                "production": ["default", "!{projectRoot}/**/?(*.)+(spec|test).[jt]s?(x)?(.snap)", "!{projectRoot}/tsconfig.spec.json"],
                "sharedGlobals": []
            },
            "targetDefaults": {
                "build": {
                    "dependsOn": ["^build"],
                    "inputs": ["production", "^production"],
                    "cache": True
                },
                "test": {
                    "inputs": ["default", "^production"],
                    "cache": True
                },
                "lint": {
                    "inputs": ["default"],
                    "cache": True
                }
            },
            "defaultBase": "main"
        }

        # Write nx.json
        nx_json_path = self.project_path / "nx.json"
        with open(nx_json_path, "w") as f:
            json.dump(nx_config, f, indent=2)
        result["created_files"].append(str(nx_json_path))
        print(f"  ✓ Created nx.json")

        # Generate package.json
        package_json = {
            "name": workspace_name,
            "version": "0.0.0",
            "private": True,
            "workspaces": [
                f"{config['structure']['apps_dir']}/*",
                f"{config['structure']['packages_dir']}/*"
            ],
            "scripts": {
                "build": "nx run-many -t build",
                "test": "nx run-many -t test",
                "lint": "nx run-many -t lint",
                "dev": "nx run-many -t dev",
                "graph": "nx graph",
                "affected": "nx affected",
                "affected:build": "nx affected -t build",
                "affected:test": "nx affected -t test"
            },
            "devDependencies": {
                "nx": "latest",
                "@nx/js": "latest",
                "typescript": "~5.3.0"
            }
        }

        if pm == "pnpm":
            package_json["packageManager"] = "pnpm@9.0.0"

        pkg_json_path = self.project_path / "package.json"
        with open(pkg_json_path, "w") as f:
            json.dump(package_json, f, indent=2)
        result["created_files"].append(str(pkg_json_path))
        print(f"  ✓ Created package.json")

        # Create pnpm-workspace.yaml if using pnpm
        if pm == "pnpm":
            workspace_yaml = f"""packages:
  - '{config['structure']['apps_dir']}/*'
  - '{config['structure']['packages_dir']}/*'
"""
            workspace_yaml_path = self.project_path / "pnpm-workspace.yaml"
            with open(workspace_yaml_path, "w") as f:
                f.write(workspace_yaml)
            result["created_files"].append(str(workspace_yaml_path))
            print(f"  ✓ Created pnpm-workspace.yaml")

        # Create directory structure
        apps_dir = self.project_path / config["structure"]["apps_dir"]
        packages_dir = self.project_path / config["structure"]["packages_dir"]
        apps_dir.mkdir(exist_ok=True)
        packages_dir.mkdir(exist_ok=True)

        # Create .gitkeep files
        (apps_dir / ".gitkeep").touch()
        (packages_dir / ".gitkeep").touch()

        print(f"  ✓ Created {config['structure']['apps_dir']}/ directory")
        print(f"  ✓ Created {config['structure']['packages_dir']}/ directory")

        # Create tsconfig.base.json
        tsconfig = {
            "compileOnSave": False,
            "compilerOptions": {
                "rootDir": ".",
                "sourceMap": True,
                "declaration": False,
                "moduleResolution": "node",
                "emitDecoratorMetadata": True,
                "experimentalDecorators": True,
                "importHelpers": True,
                "target": "ES2022",
                "module": "ESNext",
                "lib": ["ES2022", "DOM"],
                "skipLibCheck": True,
                "skipDefaultLibCheck": True,
                "baseUrl": ".",
                "paths": {}
            },
            "exclude": ["node_modules", "tmp"]
        }

        tsconfig_path = self.project_path / "tsconfig.base.json"
        with open(tsconfig_path, "w") as f:
            json.dump(tsconfig, f, indent=2)
        result["created_files"].append(str(tsconfig_path))
        print(f"  ✓ Created tsconfig.base.json")

        # Create initial packages
        namespace = config["structure"].get("namespace", "")
        for pkg in config.get("initial_packages", []):
            if pkg["type"] == "package":
                self._create_shared_package(pkg, packages_dir, namespace, "nx", result)

        print(f"\n✅ Nx workspace created!")
        print(f"\n📋 Next steps:")
        print(f"   1. cd {self.project_path}")
        print(f"   2. {pm} install")
        print(f"   3. npx nx generate @nx/react:app my-app  # Add React app")
        print(f"   4. npx nx graph  # View dependency graph")

    def _setup_turborepo(self, config: Dict, result: Dict) -> None:
        """Set up TurboRepo monorepo"""
        print("\n🔧 Setting up TurboRepo workspace...")

        workspace_name = self.project_path.name or "workspace"
        pm = config["package_manager"]

        # Generate turbo.json configuration
        turbo_config = {
            "$schema": "https://turbo.build/schema.json",
            "globalDependencies": ["**/.env.*local"],
            "tasks": {
                "build": {
                    "dependsOn": ["^build"],
                    "inputs": ["$TURBO_DEFAULT$", ".env*"],
                    "outputs": [".next/**", "!.next/cache/**", "dist/**"]
                },
                "dev": {
                    "cache": False,
                    "persistent": True
                },
                "test": {
                    "dependsOn": ["^build"],
                    "inputs": ["$TURBO_DEFAULT$"],
                    "outputs": ["coverage/**"]
                },
                "lint": {
                    "dependsOn": ["^build"],
                    "outputs": []
                },
                "check-types": {
                    "dependsOn": ["^build"],
                    "outputs": []
                }
            }
        }

        # Write turbo.json
        turbo_json_path = self.project_path / "turbo.json"
        with open(turbo_json_path, "w") as f:
            json.dump(turbo_config, f, indent=2)
        result["created_files"].append(str(turbo_json_path))
        print(f"  ✓ Created turbo.json")

        # Generate package.json
        package_json = {
            "name": workspace_name,
            "version": "0.0.0",
            "private": True,
            "workspaces": [
                f"{config['structure']['apps_dir']}/*",
                f"{config['structure']['packages_dir']}/*"
            ],
            "scripts": {
                "build": "turbo run build",
                "dev": "turbo run dev",
                "test": "turbo run test",
                "lint": "turbo run lint",
                "check-types": "turbo run check-types",
                "clean": "turbo run clean",
                "format": "prettier --write \"**/*.{ts,tsx,md}\""
            },
            "devDependencies": {
                "turbo": "latest",
                "prettier": "^3.0.0",
                "typescript": "~5.3.0"
            }
        }

        if pm == "pnpm":
            package_json["packageManager"] = "pnpm@9.0.0"

        pkg_json_path = self.project_path / "package.json"
        with open(pkg_json_path, "w") as f:
            json.dump(package_json, f, indent=2)
        result["created_files"].append(str(pkg_json_path))
        print(f"  ✓ Created package.json")

        # Create pnpm-workspace.yaml if using pnpm
        if pm == "pnpm":
            workspace_yaml = f"""packages:
  - '{config['structure']['apps_dir']}/*'
  - '{config['structure']['packages_dir']}/*'
"""
            workspace_yaml_path = self.project_path / "pnpm-workspace.yaml"
            with open(workspace_yaml_path, "w") as f:
                f.write(workspace_yaml)
            result["created_files"].append(str(workspace_yaml_path))
            print(f"  ✓ Created pnpm-workspace.yaml")

        # Create directory structure
        apps_dir = self.project_path / config["structure"]["apps_dir"]
        packages_dir = self.project_path / config["structure"]["packages_dir"]
        apps_dir.mkdir(exist_ok=True)
        packages_dir.mkdir(exist_ok=True)

        # Create .gitkeep files
        (apps_dir / ".gitkeep").touch()
        (packages_dir / ".gitkeep").touch()

        print(f"  ✓ Created {config['structure']['apps_dir']}/ directory")
        print(f"  ✓ Created {config['structure']['packages_dir']}/ directory")

        # Create tsconfig.json base
        tsconfig = {
            "$schema": "https://json.schemastore.org/tsconfig",
            "display": "Default",
            "compilerOptions": {
                "composite": False,
                "declaration": True,
                "declarationMap": True,
                "esModuleInterop": True,
                "forceConsistentCasingInFileNames": True,
                "inlineSources": False,
                "isolatedModules": True,
                "moduleResolution": "Bundler",
                "noUnusedLocals": False,
                "noUnusedParameters": False,
                "preserveWatchOutput": True,
                "skipLibCheck": True,
                "strict": True,
                "strictNullChecks": True
            },
            "exclude": ["node_modules"]
        }

        tsconfig_path = self.project_path / "tsconfig.json"
        with open(tsconfig_path, "w") as f:
            json.dump(tsconfig, f, indent=2)
        result["created_files"].append(str(tsconfig_path))
        print(f"  ✓ Created tsconfig.json")

        # Create initial packages
        namespace = config["structure"].get("namespace", "")
        for pkg in config.get("initial_packages", []):
            if pkg["type"] == "package":
                self._create_shared_package(pkg, packages_dir, namespace, "turborepo", result)

        print(f"\n✅ TurboRepo workspace created!")
        print(f"\n📋 Next steps:")
        print(f"   1. cd {self.project_path}")
        print(f"   2. {pm} install")
        print(f"   3. Create apps in {config['structure']['apps_dir']}/")
        print(f"   4. turbo run dev  # Run all apps")

    def _create_shared_package(self, pkg: Dict, packages_dir: Path, namespace: str, framework: str, result: Dict) -> None:
        """Create a shared package"""
        pkg_name = pkg["name"]
        full_name = f"{namespace}/{pkg_name}" if namespace else pkg_name
        pkg_dir = packages_dir / pkg_name
        pkg_dir.mkdir(exist_ok=True)

        # Create package.json
        package_json = {
            "name": full_name,
            "version": "0.0.0",
            "private": True,
            "main": "./src/index.ts",
            "types": "./src/index.ts",
            "exports": {
                ".": {
                    "types": "./src/index.ts",
                    "default": "./src/index.ts"
                }
            },
            "scripts": {
                "build": "tsc",
                "lint": "eslint src/",
                "check-types": "tsc --noEmit"
            },
            "devDependencies": {
                "typescript": "~5.3.0"
            }
        }

        with open(pkg_dir / "package.json", "w") as f:
            json.dump(package_json, f, indent=2)

        # Create src directory and index.ts
        src_dir = pkg_dir / "src"
        src_dir.mkdir(exist_ok=True)

        index_content = f"""/**
 * {pkg['description']}
 * Package: {full_name}
 */

export const {pkg_name} = {{
  version: '0.0.0',
}};

// Add your exports here
"""

        with open(src_dir / "index.ts", "w") as f:
            f.write(index_content)

        # Create tsconfig.json
        tsconfig = {
            "extends": "../../tsconfig.json" if framework == "turborepo" else "../../tsconfig.base.json",
            "compilerOptions": {
                "outDir": "./dist",
                "rootDir": "./src"
            },
            "include": ["src/**/*"]
        }

        with open(pkg_dir / "tsconfig.json", "w") as f:
            json.dump(tsconfig, f, indent=2)

        result["created_files"].append(str(pkg_dir))
        print(f"  ✓ Created package: {full_name}")

    def _generate_monorepo_docs(self, config: Dict, result: Dict) -> None:
        """Generate documentation for the monorepo setup"""
        framework = config["framework"]
        pm = config["package_manager"]

        if framework == "nx":
            doc_content = f"""# Monorepo Structure (Nx)

## Overview
This project uses **Nx** for monorepo management.

## Structure
```
{self.project_path.name}/
├── {config['structure']['apps_dir']}/       # Applications
├── {config['structure']['packages_dir']}/   # Shared packages
├── nx.json              # Nx configuration
├── tsconfig.base.json   # Base TypeScript config
└── package.json         # Root dependencies
```

## Common Commands

### Development
```bash
{pm} run dev                    # Run all apps in dev mode
npx nx serve <app-name>         # Run specific app
npx nx dev <app-name>           # Alternative dev command
```

### Building
```bash
{pm} run build                  # Build all projects
npx nx build <app-name>         # Build specific app
npx nx affected:build           # Build only affected projects
```

### Testing
```bash
{pm} run test                   # Run all tests
npx nx test <app-name>          # Test specific app
npx nx affected:test            # Test only affected projects
```

### Code Generation
```bash
# Generate new application
npx nx generate @nx/react:app my-app
npx nx generate @nx/next:app my-next-app
npx nx generate @nx/node:app my-api

# Generate new library
npx nx generate @nx/js:lib my-lib
npx nx generate @nx/react:lib my-components
```

### Dependency Graph
```bash
npx nx graph                    # View interactive dependency graph
npx nx graph --affected         # Show only affected projects
```

## Adding Dependencies
```bash
# Add to specific project
{pm} add <package> --filter=<project-name>

# Add to root
{pm} add -D <package> -w
```

## Best Practices
1. Use `npx nx affected` commands in CI to only build/test changed code
2. Keep shared code in `{config['structure']['packages_dir']}/`
3. Use code generators for consistent project structure
4. Run `npx nx graph` regularly to understand dependencies
"""
        else:
            doc_content = f"""# Monorepo Structure (TurboRepo)

## Overview
This project uses **TurboRepo** for monorepo management.

## Structure
```
{self.project_path.name}/
├── {config['structure']['apps_dir']}/       # Applications
├── {config['structure']['packages_dir']}/   # Shared packages
├── turbo.json           # Turbo configuration
├── tsconfig.json        # Base TypeScript config
└── package.json         # Root dependencies
```

## Common Commands

### Development
```bash
{pm} run dev                    # Run all apps in dev mode
{pm} run dev --filter=<app>     # Run specific app
turbo run dev --filter=!docs    # Run all except docs
```

### Building
```bash
{pm} run build                  # Build all projects
turbo run build --filter=<app>  # Build specific app
turbo run build --filter=...<pkg>  # Build package and dependents
```

### Testing
```bash
{pm} run test                   # Run all tests
turbo run test --filter=<app>   # Test specific app
```

### Linting
```bash
{pm} run lint                   # Lint all projects
turbo run lint --filter=<app>   # Lint specific project
```

## Filter Syntax
```bash
--filter=<app>           # Specific app
--filter=...<pkg>        # Package and everything that depends on it
--filter=<pkg>...        # Package and all its dependencies
--filter=!<app>          # Everything except app
--filter=./apps/*        # All apps
```

## Adding Dependencies
```bash
# Add to specific package
{pm} add <package> --filter=<package-name>

# Add to root
{pm} add -D <package> -w
```

## Creating New Packages

### New App
1. Create folder in `{config['structure']['apps_dir']}/`
2. Add `package.json` with name and scripts
3. Reference shared packages with `workspace:*`

### New Package
1. Create folder in `{config['structure']['packages_dir']}/`
2. Add `package.json` with exports field
3. Import in apps using package name

## Best Practices
1. Use `workspace:*` for internal dependencies
2. Define clear `exports` in package.json
3. Use `turbo run` instead of running npm scripts directly
4. Configure task dependencies in `turbo.json`
"""

        docs_dir = self.project_path / "docs"
        docs_dir.mkdir(exist_ok=True)

        doc_path = docs_dir / "MONOREPO.md"
        with open(doc_path, "w") as f:
            f.write(doc_content)

        result["created_files"].append(str(doc_path))
        print(f"  ✓ Created documentation: docs/MONOREPO.md")


def main():
    """Main entry point"""
    import argparse

    parser = argparse.ArgumentParser(description="Set up monorepo framework")
    parser.add_argument("--project-path", default="project", help="Project directory path")
    parser.add_argument("--framework", choices=["nx", "turborepo"], help="Framework to use")
    parser.add_argument("--package-manager", choices=["pnpm", "npm", "yarn", "bun"], default="pnpm")
    parser.add_argument("--non-interactive", action="store_true", help="Run without prompts")

    args = parser.parse_args()

    setup = MonorepoSetup(args.project_path)

    if args.non_interactive and args.framework:
        # Non-interactive mode
        config = {
            "framework": args.framework,
            "package_manager": args.package_manager,
            "structure": {"apps_dir": "apps", "packages_dir": "packages", "namespace": ""},
            "initial_packages": []
        }
        result = setup._execute_setup(config)
    else:
        # Interactive mode
        result = setup.interactive_setup()

    if result["status"] == "success":
        print("\n" + "=" * 60)
        print("  SETUP COMPLETE!")
        print("=" * 60)
        print(f"\n  Created {len(result['created_files'])} files")
        print(f"  Framework: {result['framework']}")

        if result.get("errors"):
            print(f"\n  Warnings:")
            for error in result["errors"]:
                print(f"    - {error}")
    elif result["status"] == "cancelled":
        print("\n❌ Setup cancelled.")
    else:
        print("\n❌ Setup failed!")
        for error in result.get("errors", []):
            print(f"   - {error}")


if __name__ == "__main__":
    main()
