# PROJECT KNOWLEDGE BASE

**Generated:** 2026-06-17
**Commit:** `56589ba`
**Branch:** (active branch)

## OVERVIEW

企业级微服务快速开发脚手架，基于 Spring Boot 4.1.0 / JDK 25 / Gradle 9.5。
采用 DDD（领域驱动设计）思想，提供多种便捷 starter 进行功能扩展。

包含六大模块组：**中间件模块**、**大数据模块**、**微服务业务模块**、**前端模块**、**基础设施模块**、**语言扩展模块**。

## STRUCTURE

```
taotao-cloud-project/
├── .opencode/              # OpenCode 配置（命令、技能、指令）
├── .github/                # GitHub CI/CD 配置
├── .circleci/              # CircleCI 配置
├── .gitee/                 # Gitee 配置
│
├── taotao-cloud-agent/     # Agent 模块
├── taotao-cloud-ai/        # AI 模块
├── taotao-cloud-bigdata/   # 大数据模块
├── taotao-cloud-cache/     # 本地缓存中间件
├── taotao-cloud-ccsr/      # 配置中心 & 服务注册中心
├── taotao-cloud-design-patterns/  # 设计模式
├── taotao-cloud-jdbcpool/  # JDBC 连接池中间件
├── taotao-cloud-job/       # 分布式任务调度中间件
├── taotao-cloud-microservice/  # ★ 微服务模块（核心）
├── taotao-cloud-mq/        # 分布式消息中间件
├── taotao-cloud-netty/     # Netty 组件
├── taotao-cloud-plugin/    # 插件模块
├── taotao-cloud-python/    # Python 模块
├── taotao-cloud-rpc/       # 分布式 RPC 中间件
├── taotao-cloud-scala/     # Scala 模块
├── taotao-cloud-tx/        # 分布式事务中间件
├── taotao-cloud-warehouse/ # 数仓模块
│
├── build.gradle            # 根构建脚本
├── settings.gradle         # 项目设置
├── gradle.properties       # Gradle 属性
└── gradle/                 # Gradle 配置目录
```

## MODULE DETAILS

### 中间件模块（自研中间件）

| Module | 描述 | 技术栈 |
|--------|------|--------|
| `taotao-cloud-cache` | 本地缓存中间件 | Caffeine / Redis / Hazelcast |
| `taotao-cloud-mq` | 分布式消息中间件 | Kafka / RocketMQ / Pulsar |
| `taotao-cloud-job` | 分布式任务调度中间件 | XXL-Job / PowerJob / Quartz |
| `taotao-cloud-rpc` | 分布式 RPC 中间件 | Dubbo / gRPC / Netty |
| `taotao-cloud-tx` | 分布式事务中间件 | Seata |
| `taotao-cloud-ccsr` | 配置中心 & 服务注册 | Nacos / ZooKeeper |
| `taotao-cloud-jdbcpool` | JDBC 连接池 | HikariCP |

### 微服务模块 `taotao-cloud-microservice/`

```
taotao-cloud-microservice/
├── taotao-cloud-bff/           # BFF（Backend For Frontend）
├── taotao-cloud-business/      # 核心业务模块
│   ├── taotao-cloud-auth/      # 认证授权
│   ├── taotao-cloud-member/    # 会员
│   ├── taotao-cloud-goods/     # 商品
│   ├── taotao-cloud-order/     # ★ 订单（DDD 参考实现）
│   ├── taotao-cloud-sys/       # 系统管理
│   ├── taotao-cloud-message/   # 消息通知
│   └── ...
├── taotao-cloud-gateway/       # 网关
├── taotao-cloud-monitor/       # 监控
├── taotao-cloud-open-platform/ # 开放平台
├── taotao-cloud-shell/         # Shell 工具
├── taotao-cloud-xxljob/        # XXL-Job 部署
├── taotao-cloud-data-sync/     # 数据同步
├── taotao-cloud-data-analysis/ # 数据分析
├── taotao-cloud-recommend/     # 推荐
└── taotao-cloud-generator/     # 代码生成
```

### DDD 单体服务结构（以 taotao-cloud-order 为参考）

```
taotao-cloud-order/
├── api/               # RPC/gRPC 接口 + DTO
├── application/       # 应用层：编排、事务、DTO 转换
├── assembly/          # 启动器 + 环境配置
├── common/            # 公共工具、枚举
├── domain/            # ★ 领域层（零外部依赖）
├── facade/            # 防腐层（ACL）
├── infrastructure/    # 持久化、MQ、事件
└── interfaces/        # REST / RPC / gRPC
```

### 大数据模块 `taotao-cloud-bigdata/`

| 子模块 | 描述 |
|--------|------|
| Hadoop/Hive | 离线批量处理 |
| Flink / Flink CDC / Flink CEP | 流式计算 |
| Spark Streaming | 微批处理 |
| Dolphinscheduler | 任务调度 |
| Doris / TiDB | OLAP/HTAP |
| Hudi / Paimon | 数据湖 |
| SeaTunnel | 数据同步 |

### 数仓模块 `taotao-cloud-warehouse/`

| 子模块 | 描述 |
|--------|------|
| `offline-warehouse` | 离线数仓 |
| `offline-weblog` | 离线日志分析 |
| `realtime-datalake` | 实时数据湖 |
| `realtime-warehouse` | 实时数仓 |

架构：ODS → DWD/DIM → DWS → ADS 四级分层。

## KEY TECHNOLOGIES

| Category | Technology | Version |
|----------|-----------|---------|
| JVM | JDK | 25 (preview) |
| Build | Gradle | 9.5 |
| Framework | Spring Boot | 4.1.0 |
| Cloud | Spring Cloud | 2025.1.1 |
| Cloud | Spring Cloud Alibaba | 2025.1.0.0 |
| Security | Spring Security | 7.1.0 |
| ORM | MyBatis-Plus | 3.5.16 |
| DB | MySQL | 9.6.0 |
| Search | Elasticsearch | 9.2.2 |
| Cache | Redis / Redisson | 4.3.1 |
| MQ | Kafka / RocketMQ | / |
| RPC | Dubbo / gRPC | / |
| Doc | Knife4j / Swagger | 4.5.0 / 3.0.0 |
| Monitor | Skywalking / Prometheus / ELK | / |
| Transaction | Seata | 2.6.0 |

## CONVENTIONS

### DDD 架构（参考 taotao-cloud-order）
- 分层依赖：`interfaces → application → domain ← infrastructure`
- 跨聚合通过 ID 引用，非对象引用
- 事务边界仅开在 `application/` 层
- 命令/查询命名：`{动词}{名词}{Command|Query}`
- 领域模型与持久化模型分离（domain entity ≠ PO）

### 通用 Java 规范
- JDK 25 预览特性：`--enable-preview`
- 编码：UTF-8，所有文件
- 遵循阿里代码规范 + Checkstyle + SpotBugs + PMD + Spotless
- 使用 MapStruct + Record Builder + Lombok 减少样板代码

### 代码质量门禁
```bash
./gradlew build                              # 全量编译
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain  # 质量检查
./gradlew test                               # 测试
./gradlew :{module}:assembly:bootRun --args='--spring.profiles.active=dev'  # 启动
```

## ANTI-PATTERNS
- Controller 中写业务逻辑判断
- 聚合根中注入 Repository 或 Domain Service
- Application Service 中包含业务规则判断
- 跨聚合直接操作其他聚合的内部状态
- Domain 层依赖 Spring 或数据库
- 明文敏感信息在配置文件中

## WHERE TO LOOK

| Task | Location |
|------|----------|
| 微服务业务开发 | `taotao-cloud-microservice/taotao-cloud-business/{module}/` |
| 中间件开发 | `taotao-cloud-{middleware}/` |
| DDD 参考实现 | `taotao-cloud-microservice/.../taotao-cloud-order/` |
| 大数据/数仓 | `taotao-cloud-bigdata/` 或 `taotao-cloud-warehouse/` |
| AI 模块 | `taotao-cloud-ai/` |
| 网关配置 | `taotao-cloud-microservice/taotao-cloud-gateway/` |
| CI/CD 配置 | `.github/` / `.circleci/` |
| Gradle 构建 | `build.gradle` / `settings.gradle` / `gradle/` |

## NOTES
- JDK 25 预览特性，`--enable-preview` + 大量 `--add-exports`
- `taotao-cloud-dependencies:2026.07` BOM 未开源，外部构建需要私有仓库凭据
- 四个环境配置：dev / test / pre / pro
- 代码质量门禁：Checkstyle + SpotBugs + PMD + Spotless + OWASP
- 部署支持：Shell / Docker / Docker Compose / K8s / GitHub Actions
