## 5. 模块化规则

**`.claude/rules/code-style.md`**
```markdown
# 代码风格规范

## 格式化规则
- 缩进: 4 个空格（不使用 Tab）
- 行宽: 120 字符
- 大括号: K&R 风格（左括号不换行）
- 缩进：4 空格
- 包命名：`com.company.project.layer.subdomain`（如 `com.shop.order.domain.model`）
- 类名：PascalCase，接口以 `I` 开头（可选）或直接名词（如 `OrderRepository`）
- 方法：小驼峰，动词开头（`validateEmail`, `calculateTotal`）
## 导入顺序
1. Java 标准库 (java.*, javax.*)
2. 第三方库 (org.*, com.*)
3. Spring 框架 (org.springframework.*)
4. 项目内部包 (com.company.project.*)
5. 静态导入

## Lombok 使用规范
```java
@Data           // 用于简单 DTO/Entity
@Builder        // 用于构建复杂对象
@Slf4j          // 日志记录
@RequiredArgsConstructor  // 依赖注入
示例代码
java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        log.info("Creating user with username: {}", request.getUsername());
        
        // 业务逻辑
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();
        
        User saved = userRepository.save(user);
        log.debug("User created with id: {}", saved.getId());
        
        return UserResponse.from(saved);
    }
}
