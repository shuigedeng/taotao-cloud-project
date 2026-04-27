---
description: 两阶段代码审查（Spec合规 + 代码质量）
agent: general
---

你是 code-copilot，正在执行 /review 命令。

变更名称：$ARGUMENTS

## 两阶段审查流程

### 阶段一：Spec Compliance（Spec合规审查）

1. **读取 Spec**
   - 使用 `read` 工具读取 `code-copilot/changes/$1/spec.md`
   - 使用 `read` 工具读取 `code-copilot/changes/$1/tasks.md`

2. **检查合规性**
   - 代码是否完全遵循 Spec 定义的功能点
   - 数据/接口变更是否与 Spec 一致
   - 业务规则是否正确实现

3. **输出合规报告**
   如有偏差，列出：
   ```
   ❌ Spec偏差：
   - [偏差1]：描述 + 涉及文件
   - [偏差2]：描述 + 涉及文件
   ```
   
   如无偏差：
   ```
   ✅ Spec Compliance PASS
   ```

**阶段一 PASS 后才启动阶段二**

### 阶段二：Code Quality（代码质量审查）

1. **读取代码规则**
   - 使用 `read` 工具读取 `code-copilot/rules/coding-style.md`
   - 使用 `read` 工具读取 `.opencode/instructions/code-rules.md`

2. **检查代码质量**
   - 代码风格是否符合规范
   - 是否有冗余代码
   - 是否有潜在风险（资金/状态流转/权限变更）
   - 是否遵循项目约定

3. **读取审查标准**
   - 使用 `read` 工具读取 `code-copilot/agents/spec-reviewer.md`
   - 使用 `read` 工具读取 `code-copilot/agents/code-quality-reviewer.md`

4. **输出质量报告**
   ```
   ✅ Code Quality Report
   
   🔍 发现：
   - [发现1]：描述 + 建议
   - [发现2]：描述 + 建议
   
   ⚠️ 需关注：
   - [风险点]：描述
   
   💡 改进建议：
   - [建议1]
   ```
   

## 最终结论
```
📊 Review 结论
   
阶段一 Spec Compliance：PASS/FAIL
阶段二 Code Quality：PASS/FAIL
   
下一步建议：
- [建议]
```

更新 `spec.md` 审查结论部分。