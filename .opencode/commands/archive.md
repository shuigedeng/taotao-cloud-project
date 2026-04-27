---
description: 归档变更并沉淀知识到知识库
agent: general
---

你是 code-copilot，正在执行 /archive 命令。

变更名称：$ARGUMENTS

## 归档流程

### 1. 读取变更记录
- 使用 `read` 工具读取 `code-copilot/changes/$1/spec.md`
- 使用 `read` 工具读取 `code-copilot/changes/$1/tasks.md`
- 使用 `read` 工具读取 `code-copilot/changes/$1/log.md`

### 2. 展示知识发现
逐条展示 `log.md` 中记录的有价值发现：
```
📚 知识发现：
   
1. [发现标题]
   - 描述：...
   - 文件：...
   - 建议：沉淀到 knowledge/
   
2. [发现标题]
   - 描述：...
```

### 3. 用户确认沉淀
使用 `question` 工具询问每条发现是否沉淀：
- 选项：沉淀 / 跳过 / 合并

### 4. 沉淀到知识库
对于确认沉淀的发现：
- 使用 `write` 工具创建知识文件
- 文件名：`code-copilot/knowledge/tech-[主题].md`
- 内容格式：
  ```
  # [知识主题]
  
  > 来源：变更 $1
  > 时间：YYYY-MM-DD
  
  ## 问题描述
  
  ## 解决方案
  
  ## 代码示例
  
  ## 相关文件
  ```

### 5. 归档变更目录
- 使用 `bash` 工具移动目录：
  ```
  mv code-copilot/changes/$1 code-copilot/changes/archive/YYYY-MM-DD-$1/
  ```

### 6. 更新 Spec 状态
- 使用 `edit` 工具更新 spec.md：
  ```
  > status: done
  ```
- 添加归档记录：
  ```
  ## 13. 确认记录（HARD-GATE）
  - **确认时间**：...
  - **确认人**：...
  - **归档时间**：YYYY-MM-DD
  - **归档路径**：code-copilot/changes/archive/YYYY-MM-DD-$1/
  ```

## 输出格式
```
🎉 归档完成
   
📂 归档路径：code-copilot/changes/archive/YYYY-MM-DD-$1/
📚 知识沉淀：[知识文件列表]
📊 变更状态：done
```