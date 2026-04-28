#!/usr/bin/env bash
# Stack Discovery Hook Script
# Scans existing project to detect tech stack and suggest relevant options
# Usage: discover-stack.sh [--json] [--category frontend|backend|database|...]

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CONFIG_DIR="${SCRIPT_DIR}/../../config"
PROJECT_ROOT="${PROJECT_ROOT:-$(pwd)}"

# Output format
OUTPUT_JSON=false
CATEGORY=""

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --json) OUTPUT_JSON=true; shift ;;
        --category) CATEGORY="$2"; shift 2 ;;
        *) shift ;;
    esac
done

# Detection results
declare -A DETECTED

# Helper: Check if file exists (supports glob patterns)
file_exists() {
    local pattern="$1"
    compgen -G "$PROJECT_ROOT/$pattern" > /dev/null 2>&1
}

# Helper: Check if package.json contains dependency
has_npm_dep() {
    local dep="$1"
    if [[ -f "$PROJECT_ROOT/package.json" ]]; then
        grep -q "\"$dep\"" "$PROJECT_ROOT/package.json" 2>/dev/null
    else
        return 1
    fi
}

# Helper: Check if requirements.txt contains package
has_pip_dep() {
    local dep="$1"
    if [[ -f "$PROJECT_ROOT/requirements.txt" ]]; then
        grep -qi "^$dep" "$PROJECT_ROOT/requirements.txt" 2>/dev/null
    else
        return 1
    fi
}

# Helper: Check if pyproject.toml contains package
has_pyproject_dep() {
    local dep="$1"
    if [[ -f "$PROJECT_ROOT/pyproject.toml" ]]; then
        grep -qi "$dep" "$PROJECT_ROOT/pyproject.toml" 2>/dev/null
    else
        return 1
    fi
}

# Detect Frontend Framework
detect_frontend() {
    # React
    if has_npm_dep "react" || has_npm_dep "@types/react"; then
        if has_npm_dep "next"; then
            DETECTED[frontend]="nextjs"
            DETECTED[frontend_name]="Next.js"
        else
            DETECTED[frontend]="react"
            DETECTED[frontend_name]="React"
        fi
        return 0
    fi

    # Vue
    if has_npm_dep "vue" || file_exists "vue.config.js"; then
        if has_npm_dep "nuxt"; then
            DETECTED[frontend]="nuxt"
            DETECTED[frontend_name]="Nuxt"
        else
            DETECTED[frontend]="vue"
            DETECTED[frontend_name]="Vue"
        fi
        return 0
    fi

    # Angular
    if has_npm_dep "@angular/core" || file_exists "angular.json"; then
        DETECTED[frontend]="angular"
        DETECTED[frontend_name]="Angular"
        return 0
    fi

    # Svelte
    if has_npm_dep "svelte" || file_exists "svelte.config.js"; then
        if has_npm_dep "@sveltejs/kit"; then
            DETECTED[frontend]="sveltekit"
            DETECTED[frontend_name]="SvelteKit"
        else
            DETECTED[frontend]="svelte"
            DETECTED[frontend_name]="Svelte"
        fi
        return 0
    fi

    # SolidJS
    if has_npm_dep "solid-js"; then
        DETECTED[frontend]="solid"
        DETECTED[frontend_name]="SolidJS"
        return 0
    fi

    return 1
}

# Detect Backend Framework
detect_backend() {
    # Node.js frameworks
    if has_npm_dep "express"; then
        DETECTED[backend]="nodejs-express"
        DETECTED[backend_name]="Express"
        DETECTED[language]="javascript"
        return 0
    fi

    if has_npm_dep "fastify"; then
        DETECTED[backend]="nodejs-fastify"
        DETECTED[backend_name]="Fastify"
        DETECTED[language]="javascript"
        return 0
    fi

    if has_npm_dep "@nestjs/core"; then
        DETECTED[backend]="nodejs-nestjs"
        DETECTED[backend_name]="NestJS"
        DETECTED[language]="typescript"
        return 0
    fi

    # Bun
    if file_exists "bun.lockb" || file_exists "bunfig.toml"; then
        DETECTED[backend]="bun"
        DETECTED[backend_name]="Bun"
        DETECTED[language]="typescript"
        return 0
    fi

    # Python frameworks
    if has_pip_dep "fastapi" || has_pyproject_dep "fastapi"; then
        DETECTED[backend]="python-fastapi"
        DETECTED[backend_name]="FastAPI"
        DETECTED[language]="python"
        return 0
    fi

    if has_pip_dep "django" || has_pyproject_dep "django" || file_exists "manage.py"; then
        DETECTED[backend]="python-django"
        DETECTED[backend_name]="Django"
        DETECTED[language]="python"
        return 0
    fi

    if has_pip_dep "flask" || has_pyproject_dep "flask"; then
        DETECTED[backend]="python-flask"
        DETECTED[backend_name]="Flask"
        DETECTED[language]="python"
        return 0
    fi

    # Go
    if file_exists "go.mod"; then
        DETECTED[backend]="go"
        DETECTED[backend_name]="Go"
        DETECTED[language]="go"
        return 0
    fi

    # Rust
    if file_exists "Cargo.toml"; then
        DETECTED[backend]="rust"
        DETECTED[backend_name]="Rust"
        DETECTED[language]="rust"
        return 0
    fi

    return 1
}

# Detect Database
detect_database() {
    # PostgreSQL
    if has_npm_dep "pg" || has_pip_dep "psycopg" || has_pip_dep "asyncpg"; then
        DETECTED[database]="postgresql"
        DETECTED[database_name]="PostgreSQL"
        return 0
    fi

    # MySQL
    if has_npm_dep "mysql2" || has_pip_dep "mysql"; then
        DETECTED[database]="mysql"
        DETECTED[database_name]="MySQL"
        return 0
    fi

    # MongoDB
    if has_npm_dep "mongoose" || has_npm_dep "mongodb" || has_pip_dep "pymongo"; then
        DETECTED[database]="mongodb"
        DETECTED[database_name]="MongoDB"
        return 0
    fi

    # SQLite
    if file_exists "*.sqlite" || file_exists "*.db" || has_npm_dep "better-sqlite3"; then
        DETECTED[database]="sqlite"
        DETECTED[database_name]="SQLite"
        return 0
    fi

    # Supabase
    if has_npm_dep "@supabase/supabase-js"; then
        DETECTED[database]="supabase"
        DETECTED[database_name]="Supabase"
        return 0
    fi

    # Firebase
    if has_npm_dep "firebase" || file_exists "firebase.json"; then
        DETECTED[database]="firebase"
        DETECTED[database_name]="Firebase"
        return 0
    fi

    # Prisma (ORM detection)
    if has_npm_dep "prisma" || file_exists "prisma/schema.prisma"; then
        DETECTED[orm]="prisma"
        DETECTED[orm_name]="Prisma"
        return 0
    fi

    # Drizzle
    if has_npm_dep "drizzle-orm"; then
        DETECTED[orm]="drizzle"
        DETECTED[orm_name]="Drizzle"
        return 0
    fi

    return 1
}

# Detect Monorepo Tool
detect_monorepo() {
    if file_exists "nx.json"; then
        DETECTED[monorepo]="nx"
        DETECTED[monorepo_name]="Nx"
        return 0
    fi

    if file_exists "turbo.json"; then
        DETECTED[monorepo]="turborepo"
        DETECTED[monorepo_name]="Turborepo"
        return 0
    fi

    if file_exists "pnpm-workspace.yaml"; then
        DETECTED[monorepo]="pnpm"
        DETECTED[monorepo_name]="pnpm workspaces"
        return 0
    fi

    if file_exists "lerna.json"; then
        DETECTED[monorepo]="lerna"
        DETECTED[monorepo_name]="Lerna"
        return 0
    fi

    return 1
}

# Detect Testing Framework
detect_testing() {
    # Vitest
    if has_npm_dep "vitest" || file_exists "vitest.config.*"; then
        DETECTED[testing]="vitest"
        DETECTED[testing_name]="Vitest"
        return 0
    fi

    # Jest
    if has_npm_dep "jest" || file_exists "jest.config.*"; then
        DETECTED[testing]="jest"
        DETECTED[testing_name]="Jest"
        return 0
    fi

    # Pytest
    if has_pip_dep "pytest" || file_exists "pytest.ini"; then
        DETECTED[testing]="pytest"
        DETECTED[testing_name]="pytest"
        return 0
    fi

    # Playwright
    if has_npm_dep "playwright" || file_exists "playwright.config.*"; then
        DETECTED[testing_e2e]="playwright"
        DETECTED[testing_e2e_name]="Playwright"
        return 0
    fi

    # Cypress
    if has_npm_dep "cypress" || file_exists "cypress.config.*"; then
        DETECTED[testing_e2e]="cypress"
        DETECTED[testing_e2e_name]="Cypress"
        return 0
    fi

    return 1
}

# Detect CI/CD
detect_cicd() {
    if file_exists ".github/workflows"; then
        DETECTED[cicd]="github-actions"
        DETECTED[cicd_name]="GitHub Actions"
    fi

    if file_exists ".gitlab-ci.yml"; then
        DETECTED[cicd]="gitlab-ci"
        DETECTED[cicd_name]="GitLab CI"
    fi

    if file_exists ".circleci"; then
        DETECTED[cicd]="circleci"
        DETECTED[cicd_name]="CircleCI"
    fi
}

# Detect Deployment Target
detect_deployment() {
    if file_exists "vercel.json"; then
        DETECTED[deployment]="vercel"
        DETECTED[deployment_name]="Vercel"
    fi

    if file_exists "netlify.toml"; then
        DETECTED[deployment]="netlify"
        DETECTED[deployment_name]="Netlify"
    fi

    if file_exists "Dockerfile" || file_exists "docker-compose.yml"; then
        DETECTED[docker]="true"
        DETECTED[docker_name]="Docker"
    fi

    if file_exists "fly.toml"; then
        DETECTED[deployment]="fly"
        DETECTED[deployment_name]="Fly.io"
    fi

    if file_exists "railway.json"; then
        DETECTED[deployment]="railway"
        DETECTED[deployment_name]="Railway"
    fi
}

# Detect Development Tools
detect_devtools() {
    # TypeScript
    if file_exists "tsconfig.json" || has_npm_dep "typescript"; then
        DETECTED[typescript]="true"
    fi

    # ESLint
    if file_exists ".eslintrc*" || file_exists "eslint.config.*" || has_npm_dep "eslint"; then
        DETECTED[eslint]="true"
    fi

    # Prettier
    if file_exists ".prettierrc*" || has_npm_dep "prettier"; then
        DETECTED[prettier]="true"
    fi

    # Husky
    if file_exists ".husky" || has_npm_dep "husky"; then
        DETECTED[husky]="true"
    fi
}

# Run all detections
run_detections() {
    detect_frontend
    detect_backend
    detect_database
    detect_monorepo
    detect_testing
    detect_cicd
    detect_deployment
    detect_devtools
}

# Output results
output_results() {
    if [[ "$OUTPUT_JSON" == "true" ]]; then
        echo "{"
        local first=true
        for key in "${!DETECTED[@]}"; do
            if [[ "$first" == "true" ]]; then
                first=false
            else
                echo ","
            fi
            echo -n "  \"$key\": \"${DETECTED[$key]}\""
        done
        echo ""
        echo "}"
    else
        echo "Detected Project Stack"
        echo "======================"
        echo ""

        if [[ -n "${DETECTED[frontend]}" ]]; then
            echo "Frontend:   ${DETECTED[frontend_name]}"
        fi
        if [[ -n "${DETECTED[backend]}" ]]; then
            echo "Backend:    ${DETECTED[backend_name]}"
        fi
        if [[ -n "${DETECTED[language]}" ]]; then
            echo "Language:   ${DETECTED[language]}"
        fi
        if [[ -n "${DETECTED[database]}" ]]; then
            echo "Database:   ${DETECTED[database_name]}"
        fi
        if [[ -n "${DETECTED[orm]}" ]]; then
            echo "ORM:        ${DETECTED[orm_name]}"
        fi
        if [[ -n "${DETECTED[monorepo]}" ]]; then
            echo "Monorepo:   ${DETECTED[monorepo_name]}"
        fi
        if [[ -n "${DETECTED[testing]}" ]]; then
            echo "Testing:    ${DETECTED[testing_name]}"
        fi
        if [[ -n "${DETECTED[testing_e2e]}" ]]; then
            echo "E2E Tests:  ${DETECTED[testing_e2e_name]}"
        fi
        if [[ -n "${DETECTED[cicd]}" ]]; then
            echo "CI/CD:      ${DETECTED[cicd_name]}"
        fi
        if [[ -n "${DETECTED[deployment]}" ]]; then
            echo "Deployment: ${DETECTED[deployment_name]}"
        fi

        echo ""
        echo "Development Tools:"
        [[ "${DETECTED[typescript]}" == "true" ]] && echo "  - TypeScript"
        [[ "${DETECTED[eslint]}" == "true" ]] && echo "  - ESLint"
        [[ "${DETECTED[prettier]}" == "true" ]] && echo "  - Prettier"
        [[ "${DETECTED[husky]}" == "true" ]] && echo "  - Husky (Git hooks)"
        [[ "${DETECTED[docker]}" == "true" ]] && echo "  - Docker"
    fi
}

# Main
main() {
    run_detections

    if [[ -n "$CATEGORY" ]]; then
        # Output specific category only
        case "$CATEGORY" in
            frontend) echo "${DETECTED[frontend]:-unknown}" ;;
            backend) echo "${DETECTED[backend]:-unknown}" ;;
            database) echo "${DETECTED[database]:-unknown}" ;;
            language) echo "${DETECTED[language]:-unknown}" ;;
            *) echo "unknown" ;;
        esac
    else
        output_results
    fi
}

main
