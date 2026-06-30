# taotao-cloud-project — CLAUDE.md

企业级微服务快速开发脚手架 | JDK 25 / Gradle 9.6 / Spring Boot 4.1.0 / Spring Cloud Alibaba 2025.1.0.0

---

## 快速身份卡

| 项目 | 值 |
|------|-----|
| 类型 | Gradle 多模块 monorepo（一级 18+ 模块，含二级/三级子模块） |
| JDK | 25（预览特性，所有编译/运行必须 `--enable-preview`） |
| Gradle | 9.6 Wrapper (`distributionUrl=gradle-9.6.0-bin.zip`) |
| Spring Boot / Cloud | 4.1.0 / 2025.1.1 |
| Spring Cloud Alibaba | 2025.1.0.0 (Nacos / Sentinel / Seata) |
| ORM | MyBatis-Plus 3.5.16 + Querydsl 5.1.0 |
| BOM | `io.github.shuigedeng:taotao-cloud-dependencies:2026.08`（未开源，需私有仓库凭据） |
| 分组 ID | `io.github.shuigedeng` |
| 项目版本 | `2026.08` |
| 交流语言 | **中文** |

---

## 模块结构（来自 settings.gradle）

```
taotao-cloud-project/                      # 根项目
│
├── 一级模块（单层）：
│   ├── taotao-cloud-scala/
│   ├── taotao-cloud-python/
│   ├── taotao-cloud-design-patterns/
│   ├── taotao-cloud-cache/               # 自研缓存中间件
│   ├── taotao-cloud-jdbcpool/            # JDBC 连接池
│   ├── taotao-cloud-netty/               # Netty 组件
│   └── taotao-cloud-ai/                  # AI 模块
│
├── 二级模块（模块/子模块）：
│   ├── taotao-cloud-bigdata/             # 大数据（Flink/Spark/Hudi/Seatunnel）
│   ├── taotao-cloud-plugin/              # 插件模块
│   ├── taotao-cloud-mq/                  # 自研消息中间件
│   ├── taotao-cloud-rpc/                 # 自研 RPC 框架
│   ├── taotao-cloud-tx/                  # 分布式事务中间件
│   ├── taotao-cloud-job/                 # 自研任务调度
│   └── taotao-cloud-ccsr/               # 配置中心 & 注册中心
│
├── 三级模块（模块/层/子层）：
│   ├── taotao-cloud-microservice/        # ★ 核心微服务
│   │   ├── taotao-cloud-business/       # DDD 业务模块（auth/member/goods/order/sys…）
│   │   ├── taotao-cloud-gateway/        # Spring Cloud Gateway + Sentinel
│   │   ├── taotao-cloud-bff/            # BFF 适配层
│   │   ├── taotao-cloud-monitor/        # Spring Boot Admin
│   │   ├── taotao-cloud-open-platform/  # 开放平台
│   │   ├── taotao-cloud-data-sync/      # 数据同步
│   │   ├── taotao-cloud-data-analysis/  # 数据分析
│   │   ├── taotao-cloud-generator/      # 代码生成
│   │   ├── taotao-cloud-recommend/      # 推荐系统
│   │   ├── taotao-cloud-shell/          # Shell 控制台
│   │   └── taotao-cloud-xxljob/         # XXL-JOB 集成
│   ├── taotao-cloud-warehouse/           # 数仓（Doris/Paimon/Hudi）
│   └── taotao-cloud-agent/              # Agent 模块
│
├── gradle/libs.versions.toml             # 版本目录
├── gradle.properties                     # 全局属性
├── settings.gradle                       # 子模块声明（三种层级模式）
└── build.gradle                          # 根构建脚本
```

### DDD 业务模块内结构（参考 `taotao-cloud-business/taotao-cloud-order/`）

```
{module}/
├── api/               # RPC/gRPC 接口 + DTO（零依赖）
├── application/       # 应用层：编排、事务边界、DTO 转换
├── assembly/          # 启动器 + application.yml / logback-spring.xml
├── common/            # 公共工具、枚举、常量
├── domain/            # ★ 领域层（零外部依赖，不依赖 Spring/DB）
├── facade/            # 防腐层 ACL
├── infrastructure/    # 持久化、MQ 发布、事件总线
└── interfaces/        # REST Controller + RPC/gRPC
```

> 所有 `taotao-cloud-business/*/` 下的 DDD 模块均遵循上述结构。

---

## 架构规则（详细版在 `.claude/rules/`）

以下为**必须遵守**的核心约束。更细化的规则（聚合设计、值对象、API 规范、测试、代码风格等）定义在 `.claude/rules/*.md`，请一并查阅。

### 分层依赖

```
api  ←  interfaces  ←  application  →  facade  （防腐层）
                          ↓
                     domain  ←  infrastructure
```

### 关键约束

| 层 | 依赖 | 可依赖 Spring？ | 核心职责 |
|----|------|---------------|----------|
| `domain` | 无 | ❌ | 聚合根、实体、值对象、领域事件、仓储接口 |
| `application` | domain, facade 接口 | ✅ 事务 | 编排，不含业务规则判断 |
| `infrastructure` | domain | ✅ | 仓储实现、MQ 发布 |
| `interfaces` | application | ✅ | Controller，不含业务逻辑 |
| `api` | 无 | ❌ | DTO + 接口定义 |

### 命名规则

| 元素 | 格式 | 示例 |
|------|------|------|
| 聚合根 | 业务名词 | `OrderAgg`, `Product` |
| 值对象 | 描述性名词 | `Money`, `Address` |
| 领域事件 | 名词 + 过去式 | `OrderCreatedEvent` |
| 命令 | {动词}{名词}Command | `CreateOrderCommand` |
| 仓储接口 | {名词}Repository | `OrderRepository` |
| 应用服务 | {名词}CommandService | `OrderCommandService` |
| Controller | {名词}{端}Controller | `OrderBuyerController` |

### 禁止项

- Controller 写业务逻辑 / 聚合根注入 Repository / ApplicationService 含业务判断
- Domain 层依赖 Spring / 数据库框架
- `SELECT *` / Java 拼接 SQL / 聚合内 N+1 循环查库
- 配置明文存储密码 / 密钥 / Token

---

## 常用命令

```bash
# 编译
./gradlew build

# 全部测试
./gradlew test

# 指定模块测试（模块名见 settings.gradle）
./gradlew :taotao-cloud-order-domain:test --tests "*OrderTest*"

# 代码质量门禁
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# OWASP 依赖安全扫描
./gradlew dependencyCheckAnalyze

# 本地启动（环境: dev/test/pre/pro）
./gradlew :taotao-cloud-order-assembly:bootRun --args='--spring.profiles.active=dev'

# 代码格式化
./gradlew spotlessApply

# 发布到本地 Maven
./gradlew publishToMavenLocal

# JaCoCo 覆盖率报告
./gradlew jacocoTestReport

# 跳过测试构建
./gradlew build -x test
```

---

## 使用 .claude/ 中的现有配置

### 工作流命令（`.claude/commands/`）

| 命令 | 文件 | 用途 |
|------|------|------|
| `/ttc-review` | `commands/ttc-review.md` | DDD 代码审查 |
| `/ttc-test` | `commands/ttc-test.md` | 运行测试 |
| `/ttc-swagger` | `commands/ttc-swagger.md` | Swagger 文档生成 |
| `/ttc-deploy` | `commands/ttc-deploy.md` | 模块部署 |

通过 `command` 参数调用：`task(command="/ttc-review", prompt="...")`

### 规则文件（`.claude/rules/`）

- `ttc-architecture.md` — 架构分层规范
- `ttc-code-style.md` — 代码风格
- `ttc-api-conventions.md` — API 约定
- `ttc-aggregate-design.md` — 聚合设计
- `ttc-domain-service.md` — 领域服务
- `value-object.md` — 值对象
- `ttc-testing.md` — 测试规范

### 专用 Agent（`.claude/agents/`）

- `ttc-domain-expert` / `ttc-backend-architect` / `ttc-code-reviewer`
- `ttc-security-auditor` / `ttc-db-expert` / `ttc-aggregate-designer`

复杂任务可委托这些 Agent：`task(subagent_type="ttc-domain-expert", prompt="...")`

---

## 工作方式

1. **先问清楚** — 除非任务明确，先提问再动手
2. **查阅规则** — 涉及架构/代码生成时，先读 `.claude/rules/` 相关文件
3. **使用命令** — `.claude/commands/` 有现成工作流，优先使用
4. **质量门禁** — 改完代码后按门禁逐项验证，不留类型忽略（禁止 `as any`/`@ts-ignore`）
5. **写中文** — 所有对话、代码注释、文档使用中文
6. **简洁直接** — 不做无意义客套，用 todo 跟踪进度

---

## 注意事项

- JDK 25 预览特性：所有编译/测试/执行必须带 `--enable-preview`
- 私有 BOM `taotao-cloud-dependencies:2026.08` 需私有仓库凭据
- `taotao-cloud-starter/*` 是独立仓库（不在本 monorepo 内），只消费其发布件
- 前端在独立仓库 `taotao-cloud-ui`，基于 Vue 3 + React + Taro
- 部署支持 Shell / Docker / Docker Compose / K8s / GitHub Actions
- 代码中不得包含任何公司业务代码，仅通用技术框架封装
