
## 2. 个人配置

**`CLAUDE.local.md`**
```markdown
# 个人DDD开发配置

## 开发工具
- **IDE**: IntelliJ IDEA Ultimate with DDD插件
- **建模工具**: Miro（事件风暴）
- **文档工具**: PlantUML（领域模型图）

## 个人偏好
- **测试驱动**: 先写领域层单元测试
- **代码生成**: 使用Lombok减少样板代码
- **调试模式**: 开启SQL日志查看仓储实现

## 本地DDD设置
```yaml
ddd:
  event-storming:
    output: docs/event-storming/
  aggregate:
    max-size: 10  # 单个聚合最大实体数
  repository:
    batch-size: 100  # 批量操作大小
