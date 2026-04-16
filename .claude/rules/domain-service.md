
**`.claude/rules/domain-service.md`**
```markdown
# 领域服务设计规范

## 何时使用领域服务

### 适用场景
1. **跨聚合的业务逻辑**
2. **无状态的计算服务**
3. **外部领域概念**

### 不适用场景
1. **应该属于聚合根的行为**
2. **纯粹的技术性操作**
3. **应用层的用例编排**

```java
// ✅ 正确：跨聚合的业务逻辑
@DomainService
public class TransferService {
    public void transfer(Account from, Account to, Money amount) {
        if (!from.canWithdraw(amount)) {
            throw new DomainException("余额不足");
        }
        
        from.withdraw(amount);
        to.deposit(amount);
        
        // 注册领域事件
        DomainEventPublisher.publish(new MoneyTransferredEvent(from.getId(), to.getId(), amount));
    }
}

// ❌ 错误：应该属于聚合根
@DomainService
public class OrderAmountCalculator {
    public Money calculate(Order order) {
        // 这个逻辑应该放在Order聚合内
        return order.getItems().stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.ZERO, Money::add);
    }
}
领域服务实现规范
1. 无状态设计
java
@Service
@DomainService
public class DiscountCalculator {
    // 只依赖其他无状态服务
    private final UserLevelService userLevelService;
    private final CouponValidator couponValidator;
    
    public DiscountCalculator(UserLevelService userLevelService, 
                              CouponValidator couponValidator) {
        this.userLevelService = userLevelService;
        this.couponValidator = couponValidator;
    }
    
    // 方法不修改自身状态
    public Money calculateDiscount(Order order, User user, Coupon coupon) {
        Money baseDiscount = calculateBaseDiscount(user);
        Money couponDiscount = calculateCouponDiscount(coupon, order);
        return baseDiscount.add(couponDiscount);
    }
}
2. 业务语义明确
java
@DomainService
public class InventoryService {
    // 方法名表达业务意图
    public boolean isAvailable(ProductId productId, int quantity) {
        return checkInventory(productId, quantity);
    }
    
    public void reserveStock(OrderId orderId, List<OrderItem> items) {
        // 明确的业务操作
        for (OrderItem item : items) {
            reserveProduct(item.getProductId(), item.getQuantity());
        }
    }
    
    public void releaseReservedStock(OrderId orderId) {
        // 对应的逆操作
        releaseReservation(orderId);
    }
}
3. 异常处理
java
@DomainService
@Slf4j
public class PaymentService {
    private final PaymentGateway paymentGateway;
    
    public PaymentResult pay(Order order, PaymentMethod paymentMethod) {
        try {
            Money amount = order.getTotalAmount();
            PaymentResponse response = paymentGateway.charge(
                paymentMethod, amount
            );
            
            return PaymentResult.success(response.getTransactionId());
        } catch (PaymentException e) {
            log.error("Payment failed for order: {}", order.getId(), e);
            return PaymentResult.failure(e.getMessage());
        }
    }
}
领域服务测试
java
@ExtendWith(MockitoExtension.class)
class PricingServiceTest {
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private DiscountPolicy discountPolicy;
    
    @InjectMocks
    private PricingService pricingService;
    
    @Test
    void shouldCalculateOrderPriceWithDiscount() {
        // Given
        Order order = mock(Order.class);
        when(order.getItems()).thenReturn(createOrderItems());
        when(discountPolicy.calculateDiscount(any(), any()))
            .thenReturn(new Money(10));
        
        // When
        Money total = pricingService.calculateOrderPrice(order, CustomerType.VIP);
        
        // Then
        assertThat(total.getAmount()).isEqualTo(190);
    }
}
