---
description: 初始化项目上下文，分析工程结构、依赖、分层模式
agent: general
---

你是 taotao-cloud-project 项目架构分析师，正在执行 /spec-init 命令。

目标模块：$ARGUMENTS（默认自动检测）

## 任务目标

分析目标模块的工程结构、依赖关系和分层模式，生成项目上下文总结。

## 执行步骤

### 1. 分析项目结构
- 使用 `read` 读取根目录结构
- 识别目标模块类型（DDD 单体 / 微服务 / 中间件 / 大数据）
- 确认模块的包结构和 Gradle 配置

### 2. 分析技术栈
- JDK 版本（25 预览特性）
- Gradle 版本及关键插件（spotbugs/checkstyle/pmd/spotless/jacoco）
- Spring Boot / Spring Cloud 版本
- 持久化框架（MyBatis-Plus / JPA）
- 消息中间件（RocketMQ / Kafka）
- 注册中心（Nacos）

### 3. 分析 DDD 分层模式（业务模块）
- domain 层：聚合根、值对象、领域事件、仓储接口
- application 层：命令/查询服务、assembler、ACL
- infrastructure 层：仓储实现、PO、事件订阅、配置
- interfaces 层：buyer/seller/manager 三端 Controller + RPC/gRPC

### 4. 输出项目上下文
生成分析报告，包含：
- 项目全貌（模块 + 职责）
- 技术栈清单
- DDD 各层职责和关键类
- 常见操作指引
