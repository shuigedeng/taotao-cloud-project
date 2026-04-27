---
description: Review后修正迭代
agent: general
---

你是 code-copilot，正在执行 /fix 命令。

参数：$ARGUMENTS
- 第一个参数（$1）：变更名称
- 第二个参数（$2）：修正描述（可选）

## 增量修正 + 文档同步铁律

1. **读取当前状态**
   - 使用 `read` 工具读取 `code-copilot/changes/$1/spec.md`
   - 使用 `read` 工具读取 `code-copilot/changes/$1/tasks.md`
   - 使用 `read` 工具读取 `code-copilot/changes/$1/log.md`（如果存在）

2. **分析修正内容**
   - 根据 $2 或用户描述确定修正范围
   - 确定涉及的文件和代码

3. **执行修正**
   - 使用 `edit` 工具修改代码
   - 使用 `glob` 和 `grep` 工具搜索相关代码

4. **验证修正**
   - 使用 `bash` 工具执行编译命令
   - 展示完整编译输出
   - 确保无错误

5. **文档同步（铁律）**
   - 使用 `edit` 工具更新 `spec.md` 执行日志
   - 使用 `edit` 工具更新 `tasks.md` 任务状态
   - 创建或更新 `log.md` 记录修正过程

6. **Git Commit**
   - Message 格式：`[$1] fix: <修正描述>`
   - 使用 `bash` 工具执行 git commit

## 输出格式
```
✅ 修正完成
📝 改动文件：[文件列表]
🔧 编译结果：SUCCESS
📦 Git Commit：[$1] fix: <描述>
📄 文档已同步：spec.md, tasks.md, log.md
```