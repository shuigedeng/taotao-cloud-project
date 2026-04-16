# SpringBoot 项目全局规范

## 技术栈约束
- **Java 17** + SpringBoot 3.x + Maven
- 使用 **Lombok** 减少样板代码
- **MapStruct** 用于对象转换（版本 >= 1.5.5）
- **Spring Data JPA** 作为 ORM（避免原生 JDBC）
- **Spring Security** + JWT 进行认证授权

## 代码风格
- 包结构必须遵循：
- com.company.project/
  ├── controller/ # REST API 层
  ├── service/ # 业务逻辑层（接口+实现类）
  ├── repository/ # 数据访问层
  ├── entity/ # JPA 实体
  ├── dto/ # 数据传输对象（request/response）
  ├── mapper/ # MapStruct 映射器
  ├── config/ # 配置类
  ├── exception/ # 自定义异常
  └── util/ # 工具类


- **命名规范**：
- Controller: `XxxController`
- Service 接口: `XxxService`
- Service 实现: `XxxServiceImpl`
- Repository: `XxxRepository`
- DTO: `XxxRequest`, `XxxResponse`, `XxxDTO`

## 编码规则
1. **所有 Controller 方法必须明确返回 `ResponseEntity<T>`**
2. **Service 层必须添加事务注解 `@Transactional`**
3. **Entity 必须使用 `@DynamicUpdate` 优化更新性能**
4. **禁止在 Controller 中编写业务逻辑**
5. **所有 public 方法必须有 Javadoc 注释**
6. **使用 `@Valid` 进行参数校验，配合 `@NotNull`, `@Size` 等注解**

## 异常处理
- 统一使用全局异常处理器 `@RestControllerAdvice`
- 自定义业务异常继承 `RuntimeException`
- 错误码规范：`1000-1999` 参数错误，`2000-2999` 业务错误，`3000-3999` 系统错误

## API 设计规范
- RESTful 风格：GET（查询）、POST（创建）、PUT（全量更新）、PATCH（部分更新）、DELETE（删除）
- 分页查询统一使用 `PageRequest` + `Page<T>` 返回
- API 路径前缀：`/api/v1/`

## 测试要求
- 单元测试覆盖率 ≥ 80%
- Controller 层使用 `@WebMvcTest`
- Service 层使用 `@ExtendWith(MockitoExtension.class)`
- Repository 层使用 `@DataJpaTest`
- 使用 Testcontainers 进行集成测试

## 性能约束
- 所有数据库查询必须使用分页（单次最多 100 条）
- N+1 查询问题必须使用 `@EntityGraph` 解决
- 批量操作必须使用 `saveAll()` 而非循环调用
- Redis 缓存命中率 > 80%（使用 `@Cacheable`）

## 安全规范
- 敏感数据（密码、身份证）必须加密存储（BCrypt）
- SQL 注入防护：使用 JPA 参数绑定，禁止字符串拼接
- XSS 防护：对输入进行 HTML 转义
- 所有 API 必须进行权限验证（除登录/注册外）

## 文档要求
- 使用 SpringDoc OpenAPI 生成 API 文档
- 每个 Controller 添加 `@Tag` 注解
- 每个接口方法添加 `@Operation` 注解
- 提供 `application-dev.yml`, `application-prod.yml` 环境配置

