
**`.opencode/command/test.md`**
```markdown
---
name: test
description: 运行测试并生成覆盖率报告
parameters:
  - name: module
    type: string
    description: 测试模块（controller/service/repository）
    required: false
  - name: coverage
    type: boolean
    default: true
---

# 测试命令

根据参数运行测试：

**如果指定模块**：
```bash
./mvnw test -Dtest=*{module}*Test

否则运行全部测试：

bash
./mvnw clean test
生成覆盖率报告（coverage=true）：

bash
./mvnw jacoco:report
# 报告位置：target/site/jacoco/index.html
输出摘要：

测试总数：{total}

通过数：{passed}

失败数：{failed}

跳过数：{skipped}

覆盖率：{coverage}%
