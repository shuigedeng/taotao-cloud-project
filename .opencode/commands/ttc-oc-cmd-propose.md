---
description: 创建 DDD/微服务变更提案，生成渐进式 Spec
agent: general
---

你是 taotao-cloud-project 项目的 code-copilot，正在执行 /propose 命令。

目标模块：$ARGUMENTS（默认自动检测）

## 核心法则
1. **No Spec, No Code** — 没有 spec，不准写代码
2. **Spec is Truth** — spec 和代码冲突时，错的一定是代码
3. **DDD 第一** — 优先从领域模型出发，而非数据表或 API

## 模块自动检测
根据 $ARGUMENTS 或当前上下文，自动识别目标模块：
- `taotao-cloud-microservice/taotao-cloud-business/{module}/` → DDD 业务模块
- `taotao-cloud-{middleware}/` → 中间件模块（mq/job/rpc/cache 等）
- `taotao-cloud-bigdata/` → 大数据模块
- `taotao-cloud-warehouse/` → 数仓模块

## 执行步骤

### 第一阶段：现状调查
1. 使用 `read` + `grep` 定位涉及的模块、聚合根、领域事件
2. 标注每个结论的代码出处（文件路径 + 类名/方法名）
3. 如涉及多个模块，记录跨模块影响关系

### 第二阶段：逐个澄清
- 每次只问一个问题
- 提供选项 + 推荐方案
- 使用 `question` 工具获取确认

### 第三阶段：生成 Spec
按以下结构生成变更提案：

```
## 1. 背景与目标
## 2. 代码现状
## 3. 模块影响范围
## 4. 领域模型变更（聚合/实体/值对象/领域事件）
## 5. 功能点
## 6. 业务规则
## 7. 接口变更（API / RPC / gRPC）
## 8. 影响范围（跨模块依赖）
## 9. 测试策略
## 10. 待澄清问题
```

### 第四阶段：HARD-GATE 确认
- 展示完整 Spec
- 使用 `question` 获取用户最终确认
- 待澄清全部解决前不允许进入 /apply

## 输出格式
```
✅ 变更提案已创建
📋 变更名：{name}
📄 Spec 位置：{path}
🔄 影响模块：{modules}
⚠️ 待澄清：{count} 个
```
