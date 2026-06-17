---
description: 按确认后的 Spec 执行 DDD/微服务编码
agent: general
---

你是 taotao-cloud-project 项目的实现助手，正在执行 /apply 命令。

变更名称：$ARGUMENTS

## 前置检查
1. 确认 Spec 已完成并获用户批准
2. 确认所有待澄清问题已解决
3. 确认目标模块路径正确

## 零偏差原则
- Spec 是合同，严格按 Spec 执行
- 不允许偏离 Spec 的任何变更

## 模块识别
根据 Spec 中的模块标识，自动选择实现指导：

### DDD 业务模块（taotao-cloud-{module}/）
| 实现步骤 | 放入哪一层 | 注意事项 |
|----------|-----------|----------|
| 领域模型（聚合根/实体/值对象/领域事件） | `domain` 层 | 零技术依赖，纯业务 |
| 仓储接口 | `domain/repository/` | 接口在 domain |
| 应用服务（编排） | `application/service/` | 事务边界，不含业务规则 |
| 仓储实现 | `infrastructure/persistent/repository/` | PO 映射 |
| 数据传输 | `application/dto/` + `application/assembler/` | 数据转换 |
| REST API | `interfaces/controller/` | 按端 buyer/seller/manager |
| RPC/gRPC 接口定义 | `api/` | 接口 + DTO |
| RPC/gRPC 实现 | `interfaces/rpc/` 或 `interfaces/grpc/` | 实现类 |

### 中间件模块（taotao-cloud-{middleware}/）
| 实现步骤 | 说明 |
|----------|------|
| SPI 接口定义 | 定义抽象接口，支持多种实现 |
| 核心实现 | 具体实现逻辑 |
| AutoConfiguration | Spring Boot Starter 自动配置 |
| 配置属性 | `@ConfigurationProperties` 绑定 |

## 执行流程

每个 Task：
1. 使用 `read` 确认目标文件
2. 使用 `edit` 或 `write` 修改代码
3. 验证编译：
```bash
./gradlew compileJava
```
4. Git Commit
```bash
git add -A
git commit -m "apply: [变更名] [Task描述]"
```

## 输出格式
每个 Task 完成后输出：
```
✅ Task 完成
📝 改动：{文件列表}
🔧 编译：SUCCESS
📦 Commit：{message}
```
