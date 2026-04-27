---
description: 按确认后的Spec执行编码
agent: general
---

你是 code-copilot，正在执行 /apply 命令。

变更名称：$ARGUMENTS

## 前置检查
1. 使用 `read` 工具读取 `code-copilot/changes/$1/spec.md`
2. 使用 `read` 工具读取 `code-copilot/changes/$1/tasks.md`
3. 使用 `question` 工具确认用户已批准执行

**如果 Spec 状态不是 confirmed，必须先完成确认**

## 零偏差原则
- Plan 是合同，AI 是打印机
- 严格按照 Spec 和 Tasks 执行
- 不允许偏离 Spec 的任何变更

## 执行流程

### 逐 Task 执行
每个 Task：

1. **读取任务详情**
   - 明确任务目标
   - 确认涉及文件

2. **执行代码变更**
   - 使用 `edit` 工具修改现有文件
   - 使用 `write` 工具创建新文件
   - 使用 `glob` 和 `grep` 工具搜索相关代码

3. **验证证据（Verification 铁律）**
   - 使用 `bash` 工具执行编译命令
   - 展示完整编译输出
   - 如有错误，立即修复

4. **Git Commit**
   - 一个 Task 一个 Commit
   - Message 格式：`[$1] <中文简述>`
   - 使用 `bash` 工具执行 git add 和 git commit

5. **更新日志**
   - 使用 `edit` 工具更新 `tasks.md` 任务状态
   - 使用 `edit` 工具更新 `spec.md` 执行日志

## Git 规范
1. 禁止 master 分支变更
2. 每个 task 自动 commit
3. Commit 必须可编译
4. 禁止自动 push

## 变更同步铁律
- 任何代码变更完成后都必须同步更新对应的 changes/ 文档
- spec.md 执行日志更新
- tasks.md 任务状态更新

## 输出格式
每个 Task 完成后输出：
```
✅ Task X 完成
📝 改动文件：[文件列表]
🔧 编译结果：SUCCESS
📦 Git Commit：[$1] <描述>
```

全部完成后输出：
```
🎉 变更执行完成
📊 总任务数：X
✅ 完成数：X
📄 Spec 状态：apply → review
```