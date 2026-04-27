# Code Copilot 渐进式Spec开发技能
## 触发条件
当用户输入以下命令时自动触发：
- `/propose <需求描述>`
- `/apply <变更名>`
- `/fix <变更名> [描述]`
- `/review <变更名>`
- `/test <变更名>`
- `/archive <变更名>`

## 核心规则
1. **No Spec No Code**：没有确认的Spec不准写代码
2. **Spec is Truth**：Spec和代码冲突时，错的一定是代码
3. **Reverse Sync**：执行中发现Spec与实际不符，先修Spec再修代码
4. **变更即记录**：任何代码变更完成后都必须同步更新对应的changes/文档

## 命令实现逻辑
### /propose <需求描述>
1. 生成唯一变更名（格式：yyyy-MM-dd-需求关键词）
2. 创建`code-copilot/changes/[变更名]/`目录
3. 复制templates下的spec.md、tasks.md、test-spec.md、log.md到目录
4. 填充spec.md的基础信息（需求名称、创建时间等）
5. 启动需求调研，逐个澄清待确认问题（一次只问一个）
6. 生成完整Spec后提交用户确认，全部确认后才能进入/apply阶段

### /apply <变更名>
1. 前置检查：确认Spec已完整、所有待澄清问题已解决、用户已确认
2. 解析tasks.md中的任务列表，逐Task执行
3. 每个Task完成后自动生成Git Commit，格式：`[<变更名>] <Task描述>`
4. 同步更新log.md，记录执行情况、改动文件、验证结果
5. 所有Task完成后自动触发/review流程

### /fix <变更名> [描述]
1. 定位到对应变更目录，更新spec.md/tasks.md中需要修正的内容
2. 增量修改代码，确保只修改与问题相关的部分
3. 更新log.md，记录修正内容、原因、验证结果
4. 修正完成后重新提交/review

### /review <变更名>
1. **阶段一（Spec合规审查）**：调用spec-reviewer技能，检查代码是否完全符合Spec要求
2. **阶段二（代码质量审查）**：调用code-quality-reviewer技能，检查代码规范、性能、安全等问题
3. 生成审查报告，标注所有需要修正的问题
4. 审查通过则进入/test阶段，不通过则返回/fix

### /test <变更名>
1. 生成test-spec.md测试用例
2. 遵循TDD流程：先写失败的测试，再写实现代码，直到测试通过
3. 执行所有相关测试，输出测试结果与覆盖率报告
4. 测试不通过返回/fix，通过则进入/archive阶段

### /archive <变更名>
1. 整理log.md中的知识发现，沉淀到code-copilot/knowledge/对应的分类下
2. 将变更目录移动到code-copilot/changes/archive/目录归档
3. 生成变更总结报告，展示整个开发流程的产出与经验
