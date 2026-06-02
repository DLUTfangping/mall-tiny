# mall-tiny Project Configuration

## Project Identity

| Field | Value |
|-------|-------|
| **Name** | mall-tiny |
| **Description** | mall-tiny 电商后台 (Spring Boot + Vue 后台管理系统) |
| **Type** | Full-Stack Application (Multi-repo) |
| **Language** | Java 1.8 (Backend) + JavaScript (Frontend) |
| **Framework** | Spring Boot 2.7.5 (Backend) + Vue 2.6 (Frontend) |
| **Repository** | Multi-repo (api + frontend) |
| **Frontend Path** | `/Users/pikaqiu/Documents/front/mall-admin-web` |

## Tech Stack

### Backend

| Category | Technology |
|---------|------------|
| Backend | Spring Boot 2.7.5 |
| ORM | MyBatis Plus 3.5.1 |
| Database | MySQL 8.0.29 |
| Connection Pool | Druid 1.2.14 |
| Cache | Redis |
| Security | Spring Security + JWT |
| API Docs | Swagger 3.0.0 |
| Build | Maven |

### Frontend

| Category | Technology |
|---------|------------|
| Framework | Vue 2.6 |
| UI Library | Element UI 2.3 |
| HTTP Client | Axios |
| Charts | ECharts |
| Build | Webpack |
| State | Vuex |

## Project Modules

| Module | Description |
|-------|-------------|
| `ums` | 用户管理模块 (User Management) |
| `mgs` | 医疗机构模块 (Medical Group System) |
| `fms` | 财务模块 (Financial Management) |
| `com` | 通用模块 (Common) |
| `classify` | 分类模块 (Classification) |
| `medicine` | 药材管理模块 (药品、库存、入库、出库、预警等) |

## Repositories

| Repository | Path | Description |
|------------|------|-------------|
| **api** (backend) | `/Users/pikaqiu/Documents/backend/mall-tiny` | Spring Boot 后端服务 |
| **frontend** | `/Users/pikaqiu/Documents/front/mall-admin-web` | Vue 2.6 + Element UI 前端 |

## Integrations

| Category | Integration | Status |
|----------|-------------|--------|
| Project Management | GitHub Issues & Projects | Available (未配置API) |
| CI/CD | GitHub Actions | Available |
| Notifications | None | - |
| Advanced | MCP Protocol | Skipped |

## Infrastructure

- Docker remote: `http://47.98.195.104:2375`
- Docker registry: `http://47.98.195.104:5000`
- Maven mirror: Aliyun repository

## Development Team

| Role | Agent |
|------|-------|
| Orchestrator | prime-orchestrator |
| Architect | solution-architect |
| Product Manager | product-manager |
| QA Engineer | qa-engineer |
| DevOps | devops-engineer |

## Git Workflow

- **Main branch**: master
- **Development branch**: dev
- **Commit format**: Conventional commits (feat, fix, refactor, docs, test, chore, perf, ci)

## Build & Run

```bash
# Build
./mvnw clean package -DskipTests

# Run
java -jar target/mall-tiny-1.0.0-SNAPSHOT.jar

# Docker build
docker build -t mall-tiny .
```

## Medicine Module Features

### 有效期预警颜色配置
- 可配置多个时间段及对应颜色
- 支持启用/禁用配置
- 表: `medicine_validity_warning_config`

### 药材库存
- 批次视图和药品总库存视图
- 有效期颜色预警显示
- 库存预警（最高/最低库存）

### 药材入库/出库
- 药品下拉表格选择（对齐样式）
- 批号管理
- 入库/出库明细