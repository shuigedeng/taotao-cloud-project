---
description: 归档已完成变更并更新项目上下文
agent: general
---

你是 taotao-cloud-project 项目的归档助手，正在执行 /archive 命令。

变更名称：$ARGUMENTS

## 归档流程

### 1. 记录变更总结
记录以下信息：
- 变更涉及的模块和文件
- 新增/修改的领域模型（聚合根、实体、值对象、领域事件）
- 接口变更（API / RPC / gRPC）
- 关键决策和理由

### 2. 更新 AGENTS.md（如果需要）
如果变更引入了：
- 新模块 → 更新 STRUCTURE 章节
- 新约定 → 更新 CONVENTIONS 章节
- 新禁止项 → 更新 ANTI-PATTERNS 章节

### 3. Git Commit（如未提交）
```bash
git add -A
git commit -m "archive: [变更名称]"
```

## 输出格式
```
🎉 归档完成
📂 变更：[变更名称]
📋 涉及文件：[数量] 个
🔄 涉及模块：[模块列表]
📝 知识记录：[是否更新 AGENTS.md]
```
