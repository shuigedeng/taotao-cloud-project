# Code Copilot — 渐进式 Spec 开发技能

适配项目：taotao-cloud-project（Gradle 9.5 + JDK 25 单体/微服务 monorepo）

## 触发条件

通过 `/propose` → `/apply` → `/review` → `/fix` → `/test` → `/archive` 命令触发。

## 核心规则

1. **No Spec No Code** — 没有确认的 Spec 不准写代码
2. **Spec is Truth** — Spec 和代码冲突时，错的一定是代码
3. **DDD 优先** — 业务模块变更从领域模型出发，而非数据库或 API

## 模块识别

自动识别 $ARGUMENTS 或当前上下文中的目标模块：
- `taotao-cloud-microservice/taotao-cloud-business/{module}/` → DDD 业务模块
- `taotao-cloud-{middleware}/` → 中间件模块
- `taotao-cloud-bigdata/` → 大数据模块
- `taotao-cloud-warehouse/` → 数仓模块
- `taotao-cloud-ai/` → AI 模块

## 工作流

### /propose — 创建变更提案
1. 调研涉及的模块（read + grep 定位）
2. 逐个澄清需求（每次一个问题，用 `question` 工具）
3. 生成 Spec，包含：领域模型变更、业务规则、接口变更、影响范围
4. 用户确认后才进入 /apply

### /apply — 按 Spec 编码
1. 严格遵循 DDD 分层实现（domain: 业务逻辑 → application: 编排 → interfaces: API）
2. 每个 Task 执行后 `./gradlew compileJava` 验证
3. 完成后提交 git commit

### /review — DDD 代码审查
1. 检查领域模型合规（聚合边界、值对象不可变、领域事件）
2. 检查架构合规（依赖方向、事务边界、分层职责）
3. 检查安全合规（敏感信息、SQL注入、权限控制）
4. 检查项目禁止项

### /fix — 修正问题
1. 增量修改，不重构式修复
2. 验证编译通过
3. 更新相关文档

### /test — 运行测试
```bash
./gradlew test
./gradlew jacocoTestReport
```

### /archive — 归档变更
1. 记录变更总结（涉及模块、领域模型、接口）
2. 如需更新 AGENTS.md（新模块/新约定/新禁止项）
3. Git commit
