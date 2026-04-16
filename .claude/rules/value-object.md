**`.claude/rules/value-object.md`**
```markdown
# 值对象设计规范

## 核心特性

### 1. 不可变性
```java
@ValueObject
public final class Email {
    private final String value;
    
    public Email(String value) {
        if (!isValid(value)) {
            throw new DomainException("Invalid email: " + value);
        }
        this.value = value;
    }
    
    private boolean isValid(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    public String getValue() { return value; }
    
    // 操作返回新对象
    public Email normalize() {
        return new Email(value.toLowerCase().trim());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
2. 自验证
值对象在构造时必须验证自身有效性：

java
public class Money {
    public Money(BigDecimal amount, Currency currency) {
        // 验证1: 金额不能为null
        if (amount == null) {
            throw new DomainException("Amount cannot be null");
        }
        
        // 验证2: 金额不能为负数
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Amount cannot be negative");
        }
        
        // 验证3: 货币不能为null
        if (currency == null) {
            throw new DomainException("Currency cannot be null");
        }
        
        this.amount = amount;
        this.currency = currency;
    }
}
3. 行为内聚
值对象应该包含业务行为：

java
public class Address {
    private final String province;
    private final String city;
    private final String street;
    private final String zipCode;
    
    // 业务行为
    public boolean isInSameCity(Address other) {
        return this.province.equals(other.province) 
            && this.city.equals(other.city);
    }
    
    public String format() {
        return String.format("%s %s %s", province, city, street);
    }
    
    public String toGeoCode() {
        // 生成地理编码
        return GeoEncoder.encode(format());
    }
}
常见值对象模式
1. 范围值对象
java
public class PriceRange {
    private final Money min;
    private final Money max;
    
    public PriceRange(Money min, Money max) {
        if (min.compareTo(max) > 0) {
            throw new DomainException("Min price cannot be greater than max");
        }
        this.min = min;
        this.max = max;
    }
    
    public boolean contains(Money price) {
        return price.compareTo(min) >= 0 && price.compareTo(max) <= 0;
    }
}
2. 枚举值对象
java
public class OrderStatus {
    public static final OrderStatus PENDING = new OrderStatus("PENDING");
    public static final OrderStatus PAID = new OrderStatus("PAID");
    public static final OrderStatus SHIPPED = new OrderStatus("SHIPPED");
    public static final OrderStatus DELIVERED = new OrderStatus("DELIVERED");
    public static final OrderStatus CANCELLED = new OrderStatus("CANCELLED");
    
    private final String value;
    
    private OrderStatus(String value) {
        this.value = value;
    }
    
    public boolean canTransitionTo(OrderStatus target) {
        // 状态转换规则
        if (this == PENDING && target == PAID) return true;
        if (this == PAID && target == SHIPPED) return true;
        if (this == SHIPPED && target == DELIVERED) return true;
        return false;
    }
    
    public String getValue() { return value; }
}
3. 复合值对象
java
public class FullName {
    private final String firstName;
    private final String lastName;
    
    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getDisplayName() {
        return lastName + " " + firstName;
    }
    
    public String getInitials() {
        return String.valueOf(firstName.charAt(0)) + lastName.charAt(0);
    }
}
JPA映射值对象
嵌入式值对象
java
@Embeddable
public class Address {
    private String province;
    private String city;
    private String street;
    
    // 无参构造器（JPA要求）
    protected Address() {}
    
    // 业务构造器
    public Address(String province, String city, String street) {
        this.province = province;
        this.city = city;
        this.street = street;
    }
}

// 使用
@Entity
public class Order {
    @Embedded
    private Address shippingAddress;
}
集合值对象
java
// 使用AttributeConverter转换复杂值对象
@Converter
public class MoneyConverter implements AttributeConverter<Money, String> {
    @Override
    public String convertToDatabaseColumn(Money money) {
        return money.getAmount() + "|" + money.getCurrency();
    }
    
    @Override
    public Money convertToEntityAttribute(String dbData) {
        String[] parts = dbData.split("\\|");
        return new Money(new BigDecimal(parts[0]), Currency.getInstance(parts[1]));
    }
}
