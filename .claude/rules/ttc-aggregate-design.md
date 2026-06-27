## 3. DDD 模块化规则

**`.claude/rules/aggregate-design.md`**
```markdown
# 聚合设计规范

## 聚合识别原则

### 1. 事务边界
聚合内修改必须在一个事务中完成，聚合间使用最终一致性。

```java
// ✅ 正确：聚合内事务
@Aggregate
public class Order {
    public void addItem(Product product, int quantity) {
        // 校验库存（聚合内规则）
        if (product.getStock() < quantity) {
            throw new DomainException("库存不足");
        }
        this.items.add(new OrderItem(product, quantity));
        this.totalAmount = calculateTotal();
    }
}

// ❌ 错误：跨聚合事务
public class Order {
    public void addItem(Product product, int quantity) {
        // 不应该直接调用Product聚合的方法
        product.reduceStock(quantity);  
    }
}
2. 一致性规则
强一致性: 聚合内保证

最终一致性: 聚合间通过事件保证

3. 聚合大小
小聚合原则: 一个聚合根通常只包含1-3个实体

性能考虑: 避免加载过多数据

java
// ✅ 好的设计：小聚合
@Aggregate
public class Order {
    private OrderId id;
    private List<OrderItem> items;  // 只包含必要实体
    private Money totalAmount;
}

// ❌ 坏的设计：大聚合
@Aggregate
public class Order {
    private List<OrderItem> items;
    private Customer customer;  // 不应该包含Customer聚合
    private Payment payment;     // 不应该包含Payment聚合
    private Shipping shipping;   // 不应该包含Shipping聚合
}
4. 聚合根标识
使用值对象作为ID，而非基本类型：

java
// ✅ 正确
public class OrderId implements Serializable {
    private final String value;
    
    public OrderId(String value) {
        this.value = value;
    }
    // equals/hashCode
}

// ❌ 错误
public class Order {
    @Id
    private Long id;  // 基本类型无法表达业务语义
}
聚合根方法设计
命令方法（状态变更）
java
public class Order {
    // 命令方法：有业务语义
    public void submit() { ... }
    public void cancel(String reason) { ... }
    public void pay(Money amount) { ... }
    
    // 而不是
    public void setStatus(OrderStatus status) { ... }  // 贫血模型
}
查询方法（只读）
java
public class Order {
    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }
    
    public Money calculateTax(TaxPolicy policy) {
        return policy.calculate(this.totalAmount);
    }
}
不变性维护
聚合根必须保证内部不变量：

java
public class Order {
    public void addItem(OrderItem item) {
        // 不变性1: 订单必须是待支付状态
        if (status != OrderStatus.PENDING) {
            throw new DomainException("只有待支付订单可以添加商品");
        }
        
        // 不变性2: 商品数量不能超过库存
        if (item.getQuantity() > 100) {
            throw new DomainException("单次购买数量不能超过100");
        }
        
        items.add(item);
    }
}
