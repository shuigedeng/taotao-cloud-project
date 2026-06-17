---
description: 生成 OpenAPI / Swagger 文档
agent: general
---

你是 taotao-cloud-project 项目的 API 文档助手，正在执行 /swagger 命令。

目标模块：$ARGUMENTS（默认当前模块）

## 执行步骤

### 1. 确认 Knife4j/Swagger 配置
检查 `build.gradle` 中 Knife4j 和 Swagger 依赖是否正确配置。

### 2. 生成 OpenAPI 文档
```bash
./gradlew :{module}-assembly:bootRun
```

启动后访问：
- Knife4j UI：`http://localhost:{port}/doc.html`
- OpenAPI JSON：`http://localhost:{port}/v3/api-docs`

### 3. 检查 API 完整性
- 所有 Controller 是否有 `@Tag` 注解
- 所有接口方法是否有 `@Operation` 注解
- DTO 字段是否有 `@Schema` 注解
- 请求/响应体是否完整定义

### 4. 输出报告
```
📄 API 文档检查报告
📦 模块：{module}
✅ Controller 数：{count}
⚠️ 未标注接口：{unannotated}
❌ 缺失定义：{missing}
```
