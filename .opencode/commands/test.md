---
description: 生成单测并执行TDD流程
agent: general
---

你是 code-copilot，正在执行 /test 命令。

变更名称：$ARGUMENTS

## Red/Green TDD 原则
- 测试必须先 Red 再 Green
- 测试驱动开发，测试先行

## 执行流程

### 方式一：Spec 先行（推荐）

1. **读取相关文件**
   - 使用 `read` 工具读取 `code-copilot/changes/$1/spec.md`
   - 使用 `read` 工具读取 `code-copilot/changes/$1/tasks.md`
   - 分析涉及的类和方法

2. **生成 Test Spec**
   按照 `code-copilot/changes/templates/test-spec.md` 模板：
   ```
   # 测试策略
   
   ## 测试范围
   
   ## 测试用例
   
   ## 覆盖率目标
   
   ## Mock策略
   ```

3. **确认 Test Spec**
   使用 `question` 工具获取用户确认

### 方式二：直接生成测试

1. **分析代码**
   - 使用 `read` 工具读取相关源码
   - 确定需要测试的类和方法

2. **生成测试类**
   - 使用 `write` 工具创建测试文件
   - 遵循项目测试框架约定

### 执行测试

4. **执行测试（Red阶段）**
   - 使用 `bash` 工具执行测试命令
   - 确认测试能正确检测功能

5. **修复测试（Green阶段）**
   - 如测试失败，修复代码使测试通过
   - 使用 `bash` 工具重新执行测试

6. **验证覆盖率**
   - 使用 `bash` 工具检查测试覆盖率
   - 达到 Spec 定义的目标

## 文件操作
- 使用 `write` 工具创建 `test-spec.md`
- 使用 `write` 工具创建测试类文件

## 输出格式
```
✅ 测试完成
   
📄 Test Spec：code-copilot/changes/$1/test-spec.md
📝 测试文件：[测试类列表]
📊 测试结果：PASS
📈 覆盖率：XX%
   
Red → Green：✅
```