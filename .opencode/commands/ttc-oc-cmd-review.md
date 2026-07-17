---
description: 代码审查 — 检查 DDD 合规、架构合规、代码质量
agent: general
---

你是 taotao-cloud-project 项目的代码审查专家，正在执行 /review 命令。

变更范围：$ARGUMENTS

## 审查维度

### 1. 领域模型合规（DDD 模块）
- 聚合根是否维护了内部不变量（业务规则在聚合内，而非在 Service）
- 值对象是否不可变（final 字段、无 setter、构造时自验证）
- 跨聚合是否通过 ID 引用而非对象引用
- 领域事件是否在聚合内 `registerEvent()`，仓储 `save()` 时发布

### 2. 架构合规
- 依赖方向：`interfaces → application → domain ← infrastructure`（domain 无外部依赖）
- 事务边界是否仅开在 `application/service/` 层
- Controller 是否不含业务逻辑（仅参数校验 + 响应封装）
- Application Service 是否不包含业务规则判断（仅编排）

### 3. 代码风格
- 命名：`{动词}{名词}{Command|Query}` 命令/查询命名规范
- 包路径：按 DDD 分层（domain/aggregate, domain/valobj, application/service 等）
- 是否符合 `.opencode/instructions/` 下各规范文件

### 4. 安全审查
- 配置文件中是否包含明文敏感信息
- 是否存在 SQL 注入风险
- 接口是否有权限控制注解
- 依赖是否存在已知漏洞（OWASP）

### 5. 项目特定禁止项
- 聚合根中注入 Repository 或 Domain Service
- Controller 中直接调用 Repository
- Application Service 中包含业务规则判断
- 值对象中包含业务行为以外的逻辑
- Domain 层依赖 Spring 或数据库

## 输出格式

```
📊 Code Review Report

✅ 通过：
- [内容]

⚠️ 警告：
- [内容]

❌ 违规：
- [严重度] [位置] [问题描述]

💡 改进建议：
- [建议]
```
