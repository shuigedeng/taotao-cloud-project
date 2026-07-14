
**`.claude/rules/architecture.md`**
```markdown
# 架构规范

## 分层职责

### Controller 层
- **职责**: HTTP 请求解析、参数校验、响应封装
- **禁止**: 业务逻辑、直接调用 Repository、事务管理
- **示例**:
```java
@GetMapping("/{id}")
public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.findById(id));
}
Service 层
职责: 业务逻辑、事务管理、权限控制

禁止: SQL 拼接、直接返回 Entity

示例:

java
@Transactional(readOnly = true)
public UserResponse findById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    return UserResponse.from(user);
}
Repository 层
职责: 数据访问、查询封装

禁止: 业务逻辑、返回 DTO

示例:

java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
依赖方向
text
Controller → Service ← Repository
Service 可以依赖多个 Repository

Controller 只能依赖 Service

Repository 不能依赖 Service
