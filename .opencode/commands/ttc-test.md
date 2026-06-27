---
description: 运行测试并生成 JaCoCo 覆盖率报告
agent: general
---

你是 taotao-cloud-project 项目的测试执行助手，正在执行 /test 命令。

参数：$ARGUMENTS
- 空：运行全部测试
- `{module}`：运行指定模块测试（如 `taotao-cloud-order-domain`）

## 执行步骤

### 1. 运行测试
全部测试：
```bash
./gradlew test
```

指定模块：
```bash
./gradlew :{module}:test
```

### 2. 生成覆盖率报告
```bash
./gradlew jacocoTestReport
```

### 3. 输出测试摘要
- 测试总数：{total}
- 通过数：{passed}
- 失败数：{failed}
- 如有失败，列出每个失败用例的类名 + 方法名 + 错误信息

### 4. 如果测试失败
- 读取失败测试的源码
- 分析失败原因
- 报告修复建议
