# SpringBoot DDD 架构开发规范

## 项目架构概述

本项目采用 **领域驱动设计（DDD）** 分层架构，严格遵循六边形架构思想。

### 包结构规范（必须遵守）
com.company.product/
├── domain/ # 领域层（核心业务逻辑）
│ ├── model/ # 领域模型
│ │ ├── aggregate/ # 聚合根
│ │ │ └── order/
│ │ │ ├── Order.java # 聚合根
│ │ │ ├── OrderItem.java # 实体
│ │ │ └── OrderStatus.java # 值对象
│ │ ├── valueobject/ # 值对象
│ │ │ ├── Money.java
│ │ │ ├── Address.java
│ │ │ └── Email.java
│ │ └── event/ # 领域事件
│ │ ├── OrderCreatedEvent.java
│ │ └── OrderPaidEvent.java
│ ├── service/ # 领域服务
│ │ └── PricingService.java
│ ├── repository/ # 仓储接口
│ │ └── OrderRepository.java
│ ├── specification/ # 规格模式
│ │ └── OrderSpecification.java
│ └── factory/ # 工厂
│ └── OrderFactory.java
├── application/ # 应用层（用例编排）
│ ├── service/ # 应用服务
│ │ ├── OrderApplicationService.java
│ │ └── dto/ # 应用层DTO
│ │ ├── CreateOrderCommand.java
│ │ └── OrderDto.java
│ ├── port/ # 端口（入站/出站）
│ │ ├── inbound/ # 入站端口（API接口）
│ │ │ └── OrderUseCase.java
│ │ └── outbound/ # 出站端口（SPI接口）
│ │ ├── PaymentPort.java
│ │ └── NotificationPort.java
│ └── handler/ # 事件处理器
│ └── OrderEventHandler.java
├── infrastructure/ # 基础设施层
│ ├── repository/ # 仓储实现
│ │ ├── JpaOrderRepository.java
│ │ └── mapper/ # ORM映射
│ │ └── OrderMapper.java
│ ├── config/ # 配置
│ │ ├── BeanConfiguration.java
│ │ └── JpaConfiguration.java
│ └── adapter/ # 适配器
│ ├── payment/ # 支付适配器
│ │ └── AlipayAdapter.java
│ └── notification/ # 通知适配器
│ └── SmsAdapter.java
└── interfaces/ # 接口层
├── rest/ # REST API
│ ├── OrderController.java
│ └── dto/ # REST DTO
│ ├── OrderRequest.java
│ └── OrderResponse.java
└── message/ # 消息监听
└── OrderMessageListener.java

text

## DDD 核心原则

### 1. 聚合设计原则
- **原子性**: 聚合内保证事务一致性，聚合间使用最终一致性
- **小聚合**: 一个聚合根只包含必要实体，避免性能问题
- **ID引用**: 跨聚合通过ID引用，而非对象引用
- **不变性**: 聚合根负责维护内部不变量

```java
// 聚合根示例
@AggregateRoot
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private OrderId id;  // 值对象作为ID
    
    @Embedded
    private CustomerId customerId;  // 跨聚合引用
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();
    
    @Embedded
    private Money totalAmount;
    
    private OrderStatus status;
    
    // 聚合根行为方法
    public void addItem(ProductId productId, Money price, int quantity) {
        // 业务规则校验
        if (status != OrderStatus.PENDING) {
            throw new DomainException("只能向待支付订单添加商品");
        }
        
        OrderItem item = new OrderItem(productId, price, quantity);
        items.add(item);
        recalculateTotal();
        
        // 注册领域事件
        registerEvent(new OrderItemAddedEvent(id, productId, quantity));
    }
    
    public void pay() {
        if (items.isEmpty()) {
            throw new DomainException("空订单不能支付");
        }
        this.status = OrderStatus.PAID;
        registerEvent(new OrderPaidEvent(id, totalAmount));
    }
    
    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.ZERO, Money::add);
    }
    
    // 无参构造器（JPA要求）
    protected Order() {}
    
    // 工厂方法
    public static Order create(CustomerId customerId, Address shippingAddress) {
        Order order = new Order();
        order.id = new OrderId(IdGenerator.next());
        order.customerId = customerId;
        order.status = OrderStatus.PENDING;
        order.registerEvent(new OrderCreatedEvent(order.id));
        return order;
    }
}
2. 值对象设计
不可变性: 所有字段final，无setter方法

无标识: 通过属性值判断相等性

自验证: 构造时验证数据合法性

java
@ValueObject
@Embeddable
public class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("金额不能为负数");
        }
        this.amount = amount;
        this.currency = currency;
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("货币类型不匹配");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    // 只有getter，无setter
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 &&
               currency.equals(money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
3. 领域服务
无状态: 不持有状态，只提供行为

跨聚合: 协调多个聚合的业务逻辑

业务价值: 不属于任何单一聚合

java
@DomainService
@Service
@Slf4j
public class PricingService {
    private final ProductRepository productRepository;
    private final DiscountPolicy discountPolicy;
    
    public PricingService(ProductRepository productRepository, 
                          DiscountPolicy discountPolicy) {
        this.productRepository = productRepository;
        this.discountPolicy = discountPolicy;
    }
    
    /**
     * 计算订单总价（跨聚合逻辑）
     */
    public Money calculateOrderPrice(Order order, CustomerType customerType) {
        Money subtotal = Money.ZERO;
        
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new DomainException("商品不存在"));
            Money itemPrice = product.getPrice().multiply(item.getQuantity());
            subtotal = subtotal.add(itemPrice);
        }
        
        // 应用折扣策略
        Money discount = discountPolicy.calculateDiscount(subtotal, customerType);
        return subtotal.subtract(discount);
    }
}
4. 仓储模式
接口在领域层: 业务依赖抽象

实现在基础设施: 技术细节隔离

java
// 领域层接口
public interface OrderRepository {
    Order findById(OrderId id);
    void save(Order order);
    void delete(OrderId id);
    Page<Order> findByCustomerId(CustomerId customerId, Pageable pageable);
}

// 基础设施层实现
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JpaOrderRepository jpaRepository;
    private final OrderMapper mapper;
    
    @Override
    public Order findById(OrderId id) {
        OrderPo orderPo = jpaRepository.findById(id.getValue())
            .orElseThrow(() -> new DomainException("订单不存在"));
        return mapper.toDomain(orderPo);
    }
    
    @Override
    public void save(Order order) {
        OrderPo po = mapper.toPo(order);
        jpaRepository.save(po);
        
        // 发布领域事件
        order.getDomainEvents().forEach(this::publishEvent);
        order.clearEvents();
    }
}
5. 应用服务（用例编排）
薄薄一层: 不包含业务逻辑

事务边界: 开启数据库事务

协调作用: 调用领域层和基础设施

java
@ApplicationService
@Service
@Transactional
@Slf4j
public class OrderApplicationService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final PricingService pricingService;
    private final PaymentPort paymentPort;
    private final DomainEventPublisher eventPublisher;
    
    @Override
    public OrderDto createOrder(CreateOrderCommand command) {
        log.info("Creating order for customer: {}", command.getCustomerId());
        
        // 1. 创建聚合（工厂）
        Order order = Order.create(
            new CustomerId(command.getCustomerId()),
            command.getShippingAddress()
        );
        
        // 2. 添加商品
        for (OrderItemCommand item : command.getItems()) {
            order.addItem(
                new ProductId(item.getProductId()),
                new Money(item.getPrice(), Currency.USD),
                item.getQuantity()
            );
        }
        
        // 3. 应用领域服务计算价格
        Money total = pricingService.calculateOrderPrice(
            order, 
            command.getCustomerType()
        );
        
        // 4. 保存聚合
        orderRepository.save(order);
        
        // 5. 发布领域事件
        eventPublisher.publish(order.getDomainEvents());
        
        return OrderDto.fromDomain(order);
    }
}
6. 领域事件
解耦聚合: 聚合间通过事件通信

最终一致性: 使用事件处理器实现

java
@DomainEvent
public class OrderPaidEvent extends DomainEvent {
    private final OrderId orderId;
    private final Money amount;
    private final LocalDateTime paidAt;
    
    public OrderPaidEvent(OrderId orderId, Money amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.paidAt = LocalDateTime.now();
    }
    
    // Getters...
}

// 事件处理器
@Component
@Slf4j
public class OrderEventHandler {
    @EventListener
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("Order paid: {}, amount: {}", event.getOrderId(), event.getAmount());
        // 发送通知、更新统计等
    }
}
开发流程规范
1. 战略设计阶段
事件风暴: 识别领域事件、命令、聚合

限界上下文: 划分业务边界

上下文映射: 定义上下文关系

2. 战术设计阶段
识别聚合根和值对象

定义领域服务和仓储接口

编写领域模型测试

实现应用服务和REST API

3. 代码审查要点
聚合是否保证了业务不变性

值对象是否不可变

仓储接口是否在领域层

跨聚合是否通过ID引用

领域事件是否实现最终一致性

测试策略
单元测试（领域层）
java
@Test
class OrderTest {
    @Test
    void shouldAddItemToPendingOrder() {
        Order order = Order.create(customerId, address);
        order.addItem(productId, new Money(100), 2);
        
        assertThat(order.getTotalAmount()).isEqualTo(new Money(200));
        assertThat(order.getDomainEvents()).hasSize(2);
    }
}
集成测试（应用层）
java
@SpringBootTest
@Transactional
class OrderApplicationServiceTest {
    @Test
    void shouldCreateOrder() {
        CreateOrderCommand command = createCommand();
        OrderDto result = orderService.createOrder(command);
        
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
    }
}
命名约定
聚合根: 业务名词（Order, Product, User）

值对象: 描述性名词（Money, Address, Email）

领域服务: 动词+名词（PricingService, ShippingService）

领域事件: 名词+过去式动词（OrderCreatedEvent, UserRegisteredEvent）

仓储接口: 聚合根名+Repository（OrderRepository）

应用服务: 聚合根名+ApplicationService（OrderApplicationService）

重要提醒
严禁在聚合根中注入仓储或领域服务

严禁在值对象中添加业务行为以外的逻辑

跨聚合操作必须通过应用服务协调

数据库事务只能开启在应用层

领域模型必须与持久化模型分离
