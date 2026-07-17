# 项目编码规范 — taotao-cloud-project

> 全项目通用编码规范，涵盖 Java（DDD 单体 + 微服务）、Gradle 构建、数据库、前端等。

---

## 1. 通用编码规范

### 1.1 Java 版本与特性
- **JDK 25**（预览特性），所有编译/测试/执行均需 `--enable-preview`
- 使用 `var` 仅限局部变量，且右侧类型明确时
- Record 优先于简单 POJO（不可变数据传输）
- 使用 `instanceof` 模式匹配简化类型检查
- 使用 `switch` 表达式替代传统 switch

### 1.2 命名规范
- 类名：PascalCase（`OrderService`, `CreateOrderCommand`）
- 方法名：camelCase（`findById`, `createOrder`）
- 常量：UPPER_SNAKE_CASE（`MAX_RETRY_COUNT`）
- 包名：全小写（`com.taotao.cloud.order.domain`）
- 命令/查询命名：`{动词}{名词}{Command|Query}`（`CreateOrderCommand`）

### 1.3 代码风格
- 缩进：4 空格（非 Tab）
- 编码：UTF-8
- 花括号：K&R 风格（左括号不换行）
- 遵循阿里代码规范 + Checkstyle + Spotless 配置

### 1.4 工具链
- IDE：IntelliJ IDEA 2026.1+
- 构建：Gradle 9.5（Wrapper）
- 代码质量：Checkstyle + SpotBugs + PMD + Spotless + OWASP Dependency Check
- 测试：JUnit 5 + JaCoCo（覆盖率）

---

## 2. DDD 分层规范（微服务业务模块）

适用于 `taotao-cloud-microservice/taotao-cloud-business/*/` 下基于 DDD 的业务模块。

### 2.1 模块依赖规则

```
api  ←  interfaces  ←  application  →  facade
                          ↓
                     domain  ←  infrastructure
```

- `domain`：零外部依赖，不依赖 Spring、不依赖数据库
- `application`：依赖 `domain`，可依赖 `facade` 接口，不依赖 `infrastructure`
- `infrastructure`：依赖 `domain` 实现仓储，依赖 `application` 实现事件订阅
- `interfaces`：依赖 `application`，不直接依赖 `infrastructure`
- `api`：纯 DTO + 接口定义，不依赖任何业务模块

### 2.2 包结构规范

```
com.taotao.cloud.order.{module}/
├── aggregate/     # 聚合根（@AggregateRoot）
├── entity/        # 实体（@Entity）
├── valobj/        # 值对象（@ValueObject | @Embeddable）
├── event/         # 领域事件（extends DomainEvent）
├── repository/    # 仓储接口
└── service/       # 领域服务（@DomainService）
```

### 2.3 聚合根规范
```java
@AggregateRoot
public class OrderAgg {
    // 聚合内实体用对象引用
    private List<OrderItem> items;
    // 跨聚合用 ID 引用
    private Long customerId;

    // 业务行为方法（不是 setter）
    public void addItem(ProductId productId, Money price, int quantity) {
        // 校验业务规则 → 修改内部状态 → 注册领域事件
        registerEvent(new OrderItemAddedEvent(this.id, productId));
    }

    // 无参构造（JPA 要求），protected
    protected OrderAgg() {}

    // 静态工厂方法
    public static OrderAgg create(...) { ... }
}
```

### 2.4 禁止违反的依赖
```java
// ❌ 禁止：Controller 直接调用 Repository
@Autowired private OrderRepository orderRepository;

// ❌ 禁止：Application Service 直接调用 Mapper
@Autowired private OrderMapper orderMapper;

// ❌ 禁止：Domain Service 注入 Repository
@Autowired private OrderRepository orderRepository;

// ✅ 正确：Application Service 通过仓储接口操作持久化
private final OrderDomainRepository orderRepository;
```

---

## 3. 值对象规范

```java
@Embeddable
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    // 构造时自验证
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("金额不能为负数");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // 只有 getter，无 setter
    // 覆写 equals/hashCode（基于所有属性）
}
```

---

## 4. Controller 规范

```java
@RestController
@RequestMapping("/{role}/order/order")
// role = buyer | seller | manager
public class OrderBuyerController extends BusinessController {
    // HTTP 解析 + 参数校验 + Result 封装
    // 禁止业务逻辑

    @GetMapping("/page")
    public Result<PageResult<OrderSimpleResult>> page(OrderPageQuery query) {
        return Result.success(orderQueryService.pageQuery(query));
    }

    @PostMapping("/{orderSn}/cancel")
    public Result<Void> cancel(@PathVariable String orderSn, @RequestParam String reason) {
        orderCommandService.cancel(orderSn, reason);
        return Result.success();
    }
}
```

---

## 5. 枚举规范

```java
public enum OrderStatusEnum {
    PENDING("待付款"),
    PAID("已付款"),
    DELIVERED("已发货"),
    RECEIVED("已收货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;
    // ...
}
```

---

## 6. 领域事件规范

```java
// 事件定义在 domain/event/
public class OrderCreatedEvent extends DomainEvent {
    private final Long orderId;
    // 不可变，构造时赋值
}

// 事件在聚合根内 registerEvent()
// 仓储 save() 时自动 flush 发布
// 订阅在 infrastructure/event/
```

---

## 7. MapStruct + Assembler 规范

```java
// Assembler 在 infrastructure/assembler/
// 职责：Domain Entity ←→ Persistence PO 双向映射

@Mapper(componentModel = "spring")
public interface OrderAssembler {
    OrderPo toPo(Order order);
    Order toDomain(OrderPo po);
}
```

---

## 8. 构建与测试

```bash
# 全量构建
./gradlew build

# 运行所有测试
./gradlew test

# 运行指定模块测试
./gradlew :taotao-cloud-order-domain:test

# 代码质量
./gradlew checkstyleMain spotlessCheck pmdMain spotbugsMain

# 本地启动（DDD 单体）
./gradlew :taotao-cloud-order-assembly:bootRun --args='--spring.profiles.active=dev'

# 本地启动（微服务）
./gradlew :taotao-cloud-auth-assembly:bootRun --args='--spring.profiles.active=dev'
```

---

## 9. 数据库规范

### 9.1 表必备字段
```sql
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`create_by` bigint DEFAULT NULL COMMENT '创建人ID',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT NULL COMMENT '更新人ID',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`is_deleted` tinyint(1) DEFAULT 0 COMMENT '删除标记',
`tenant_id` bigint DEFAULT 0 COMMENT '租户ID',
`version` int DEFAULT 0 COMMENT '乐观锁'
```

### 9.2 禁止
- 循环中查询数据库（N+1 问题）
- `SELECT *`
- 在 Java 代码中拼接 SQL
- 跨聚合直接操作其他聚合的数据表

---

## 10. 安全规范

### 10.1 配置安全
- 禁止明文存储密码/密钥/Token
- 敏感信息使用环境变量 `System.getenv()` 或加密配置
- 使用 Jasypt 加密（`ENC(...)`）存储敏感配置

### 10.2 API 安全
- 统一 OAuth2 / JWT 认证
- 接口权限使用 `@PreAuthorize` 注解
- 防重放、防 XSS、防 SQL 注入
- 接口限流使用 Sentinel

### 10.3 依赖安全
- 定期执行 OWASP Dependency Check
- 禁止使用已知漏洞的依赖版本

---

## 11. 中间件开发规范

适用于 `taotao-cloud-mq`、`taotao-cloud-job`、`taotao-cloud-rpc`、`taotao-cloud-cache` 等自研中间件。

- 提供统一 Starter 封装，遵循 Spring Boot AutoConfiguration 规范
- 接口抽象化：定义 SPI 接口，支持多种实现（如 MQ 支持 Kafka/RocketMQ/Pulsar）
- 配置规范化：使用 `@ConfigurationProperties` 绑定配置前缀
- 提供完整测试用例
- 文档齐全（README + API 注释）
