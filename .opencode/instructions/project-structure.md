# 项目结构指南 — taotao-cloud-project

## 顶层结构

```
taotao-cloud-project/           # 根项目（Gradle Root）
├── .opencode/                  # OpenCode 配置
├── .github/                    # GitHub Actions CI/CD
├── .circleci/                  # CircleCI 配置
├── .claude/                    # Claude 配置（Agent 规则）
├── .gitee/                     # Gitee 镜像配置
│
├── taotao-cloud-agent/         # 智能体模块
├── taotao-cloud-ai/            # AI 集成模块（LLM 等）
├── taotao-cloud-bigdata/       # 大数据处理模块
├── taotao-cloud-cache/         # 自研缓存中间件
├── taotao-cloud-ccsr/          # 配置中心 & 服务注册
├── taotao-cloud-design-patterns/  # 设计模式示例
├── taotao-cloud-jdbcpool/      # 自研 JDBC 连接池
├── taotao-cloud-job/           # 自研分布式任务调度
├── taotao-cloud-microservice/  # ★ 核心微服务模块
├── taotao-cloud-mq/            # 自研消息中间件
├── taotao-cloud-netty/         # Netty 网络组件
├── taotao-cloud-plugin/        # 插件系统
├── taotao-cloud-python/        # Python 应用（Django/Scrapy）
├── taotao-cloud-rpc/           # 自研 RPC 框架
├── taotao-cloud-scala/         # Scala 模块
├── taotao-cloud-tx/            # 分布式事务中间件
├── taotao-cloud-warehouse/     # 数仓模块
│
├── build.gradle                # 根构建脚本
├── settings.gradle             # 项目设置（子模块声明）
├── gradle.properties           # 全局 Gradle 属性
├── gradle/                     # Gradle 共享构建逻辑
├── gradlew / gradlew.bat       # Gradle Wrapper
├── doc/                        # 文档
├── docker/                     # Docker 部署配置
└── code-analysis/              # 代码分析配置
```

## 模块分组规则

| 层级 | 分组 | 包含模块 |
|------|------|----------|
| 单层（1层） | `oneLayerProjects` | scala, python, design-patterns, cache, jdbcpool, netty, ai |
| 双层（2层） | `twoLayerProjects` | bigdata, plugin, mq, rpc, tx, job, ccsr |
| 三层（3层） | `threeLayerProjects` | microservice, warehouse, agent |

详见 `settings.gradle` 中的 `oneLayerProjects` / `twoLayerProjects` / `threeLayerProjects` 定义。

## 微服务模块内部结构

```
taotao-cloud-microservice/
├── taotao-cloud-bff/           # 前端 BFF 层
├── taotao-cloud-business/      # 业务模块集合
│   ├── taotao-cloud-auth/      # 认证中心（OAuth2/JWT）
│   ├── taotao-cloud-member/    # 会员中心
│   ├── taotao-cloud-goods/     # 商品中心
│   ├── taotao-cloud-order/     # 订单中心（DDD 参考实现）
│   ├── taotao-cloud-sys/       # 系统管理
│   ├── taotao-cloud-message/   # 消息通知
│   └── ...
├── taotao-cloud-gateway/       # 网关（Spring Cloud Gateway）
├── taotao-cloud-monitor/       # 监控（Spring Boot Admin）
├── taotao-cloud-open-platform/ # 开放平台
├── taotao-cloud-shell/         # Shell 工具
├── taotao-cloud-xxljob/        # XXL-Job 部署
├── taotao-cloud-data-sync/     # 数据同步
├── taotao-cloud-data-analysis/ # 数据分析
├── taotao-cloud-recommend/     # 推荐系统
└── taotao-cloud-generator/     # 代码生成器
```

## DDD 业务模块结构（以 order 参考）

```
taotao-cloud-order/             # DDD 六边形架构
├── api/                        # RPC/gRPC 接口定义 + DTO
├── application/                # 应用层
│   ├── service/                #   命令/查询服务
│   ├── dto/                    #   数据传输对象
│   └── assembler/              #   DTO 装配器
├── assembly/                   # 启动器 + 环境配置
├── common/                     # 通用枚举、工具
├── domain/                     # ★ 领域层（核心）
│   ├── aggregate/              #   聚合根
│   ├── entity/                 #   实体
│   ├── valobj/                 #   值对象
│   ├── event/                  #   领域事件
│   ├── repository/             #   仓储接口
│   └── service/                #   领域服务
├── facade/                     # 防腐层（外部接口适配）
├── infrastructure/             # 基础设施层
│   ├── persistent/             #   持久化实现
│   ├── event/                  #   事件订阅/发布
│   └── config/                 #   配置
└── interfaces/                 # 接口层
    ├── controller/             #   REST Controller
    │   ├── buyer/              #     买家端
    │   ├── seller/             #     卖家端
    │   └── manager/            #     管理端
    ├── rpc/                    #   RPC 实现
    └── grpc/                   #   gRPC 实现
```

## 配置与环境

| 环境 | profile | 用途 |
|------|---------|------|
| dev | `--spring.profiles.active=dev` | 本地开发 |
| test | `--spring.profiles.active=test` | 测试环境 |
| pre | `--spring.profiles.active=pre` | 预发布 |
| pro | `--spring.profiles.active=pro` | 生产环境 |

## 构建命令速查

```bash
# 编译构建
./gradlew build

# 运行测试（全部）
./gradlew test

# 运行指定模块测试
./gradlew :taotao-cloud-order-domain:test

# 代码质量检查
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# OWASP 依赖安全扫描
./gradlew dependencyCheckAnalyze

# 本地启动
./gradlew :taotao-cloud-order-assembly:bootRun --args='--spring.profiles.active=dev'

# 发布到本地 Maven
./gradlew publishToMavenLocal

# JaCoCo 覆盖率报告
./gradlew jacocoTestReport
```
