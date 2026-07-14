```markdown
---
name: event-storming
description: 事件风暴工作流，识别领域事件、命令和聚合
triggers:
  - "事件风暴"
  - "领域建模"
  - "识别聚合"
---

# 事件风暴工作流

## 步骤1：识别领域事件

询问用户以下问题：
1. 业务流程中有哪些关键事件？
2. 哪些事件会改变系统状态？
3. 哪些事件会触发其他行为？

**输出格式**：
```markdown
## 领域事件列表

1. **OrderCreated** - 订单已创建
   - 触发条件: 用户提交订单
   - 结果: 订单状态变为待支付
   - 订阅者: 库存服务、通知服务

2. **OrderPaid** - 订单已支付
   - 触发条件: 支付成功
   - 结果: 订单状态变为已支付
   - 订阅者: 物流服务、积分服务
步骤2：识别命令
每个事件通常对应一个命令：

markdown
## 命令列表

| 命令 | 触发事件 | 执行者 | 验证规则 |
|------|---------|--------|---------|
| CreateOrder | OrderCreated | 用户 | 购物车不能为空 |
| PayOrder | OrderPaid | 用户/系统 | 订单必须在待支付状态 |
| CancelOrder | OrderCancelled | 用户/管理员 | 订单未支付 |
步骤3：识别聚合
根据事件和命令识别聚合根：

markdown
## 聚合根识别

### Order聚合
- **为什么是聚合根**: 订单有独立生命周期
- **包含实体**: Order, OrderItem
- **包含值对象**: Money, Address, OrderStatus
- **命令**: CreateOrder, PayOrder, CancelOrder
- **事件**: OrderCreated, OrderPaid, OrderCancelled
- **不变性**: 
  - 只有待支付订单可以支付
  - 支付后不可修改商品
  - 已支付订单不可取消
步骤4：绘制上下文映射







输出产物
生成 docs/event-storming/ 目录：

domain-events.md - 领域事件清单

aggregates.md - 聚合设计文档

context-map.md - 上下文映射图

business-rules.md - 业务规则文档
