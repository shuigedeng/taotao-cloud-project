---
description: 部署应用到指定环境（dev/test/pre/pro）
agent: general
---

你是 taotao-cloud-project 项目的部署助手，正在执行 /deploy 命令。

目标模块：$ARGUMENTS
- 格式：`{module} {environment}`，如 `taotao-cloud-order dev`
- 默认环境：dev

## 部署流程

### 1. 运行测试
```bash
./gradlew test
```
测试失败则中止部署。

### 2. 打包
DDD 单体模块：
```bash
./gradlew :{module}-assembly:bootJar
```

微服务模块：
```bash
./gradlew :{module}:bootJar
```

### 3. 启动（指定环境）
```bash
java --enable-preview \
  -jar {module}-assembly/build/libs/{module}-assembly-*.jar \
  --spring.profiles.active={environment}
```

### 4. 健康检查
```bash
curl -f http://localhost:{port}/actuator/health
```

### 5. 输出部署报告
```
🔄 部署报告
📦 模块：{module}
🌐 环境：{environment}
⏱ 时间：{timestamp}
📐 JAR 大小：{size}
💚 健康检查：PASS/FAIL
```
