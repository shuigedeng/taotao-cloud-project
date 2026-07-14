**`.claude/commands/test.md`**
```markdown
---
description: 运行测试并生成报告
parameters:
  - name: module
    type: string
    description: 测试模块（controller/service/repository）
    required: false
  - name: coverage
    type: boolean
    default: true
  - name: parallel
    type: boolean
    default: true
---

# 测试执行

## 执行步骤

### 1. 清理并编译
```bash
./mvnw clean compile
2. 运行测试
{% if module %}

bash
./mvnw test -Dtest=*{{module}}*Test
{% else %}

bash
./mvnw test -DforkCount={{ parallel ? '1C' : '1' }}
{% endif %}

3. 生成覆盖率报告（如果需要）
{% if coverage %}

bash
./mvnw jacoco:report
# 报告位置: target/site/jacoco/index.html
{% endif %}

4. 输出测试结果
测试统计
总测试数: {{total}}

通过: {{passed}}

失败: {{failed}}

跳过: {{skipped}}

耗时: {{duration}}ms

覆盖率报告
指令覆盖率: {{instructionCoverage}}%

分支覆盖率: {{branchCoverage}}%

行覆盖率: {{lineCoverage}}%

方法覆盖率: {{methodCoverage}}%

失败测试详情
{% for failure in failures %}

{{failure.className}}.{{failure.methodName}}

错误: {{failure.message}}

堆栈: {{failure.stackTrace | truncate(200)}}
{% endfor %}
