# CLAUDE.local.md — 个人开发环境覆盖

> 本文件不纳入版本控制（`.claude/.gitignore` 已排除）。
> 与 `settings.local.json` 配合使用，可安全提交只含 `settings.json` + `CLAUDE.md`。

---

## 开发环境

| 项目 | 值 |
|------|-----|
| OS | Windows 11 |
| Shell | PowerShell 5.1 |
| IDE | IntelliJ IDEA 2026.1+ |
| JDK | GraalVM JDK 25 — `C:\Program Files\GraalVM\graalvm-jdk-25` |
| Gradle | 9.6（使用 Wrapper `./gradlew`） |
| 容器 | Docker Desktop（WSL2 后端） |
| 本地 Maven | `C:\Users\Lenovo\.m2\repository` |
| Gradle 缓存 | `C:\Users\Lenovo\.gradle\caches` |
| 镜像 | 阿里云 mirror 已在 `build.gradle` / `settings.gradle` 中配置 |

## 本地中间件地址

| 服务 | 地址 | 凭据 |
|------|------|------|
| MySQL | `localhost:3306` | root / root |
| Redis | `localhost:6379` | — |
| Nacos | `localhost:8848` | nacos / nacos |
| Sentinel | `localhost:8080` | — |
| Seata | `localhost:8091` | — |
| Kafka | `localhost:9092` | — |
| Elasticsearch | `localhost:9200` | — |
| Skywalking OAP | `localhost:11800` | — |
| Prometheus | `localhost:9090` | — |
| Grafana | `localhost:3000` | admin / admin |

## 本地常用命令

```powershell
# 快速编译指定模块
./gradlew :taotao-cloud-order-domain:compileJava

# 单个测试
./gradlew :taotao-cloud-order-domain:test --tests "*OrderTest*"

# 本地启动（dev 环境）
./gradlew :taotao-cloud-order-assembly:bootRun --args='--spring.profiles.active=dev'

# 格式化
./gradlew spotlessApply

# 快速构建（跳过测试）
./gradlew build -x test
```

## 个人偏好

- **语言**：中文
- **测试策略**：领域层单元测试 → 集成测试 → 接口测试
- **代码风格**：阿里规范 + Spotless 配置
- **调试**：启用 SQL 日志（`logging.level.sql=debug`）
- **模型**：优先 Java `record` + Lombok 减少样板代码
- **AI 温度**：0.3（精确优先）

## IDEA 插件

- TaoTaoToolKit（项目专属）
- PlantUML（领域模型可视化）
- MapStruct Support
- MyBatisX
- Lombok
- Gradle Toolkit
- SpotBugs
- CheckStyle-IDEA

## settings.local.json 对应覆盖

如需调整 Claude Code 行为，编辑 `.claude/settings.local.json`：

```json
{
  "model": "claude-sonnet-4-20250514",
  "custom": {
    "temperature": 0.3,
    "java.jvmArgs": "--enable-preview --add-exports=..."
  }
}
```

不要修改 `settings.json`（纳入版本控制），个人覆盖都写在 `settings.local.json` 中。
