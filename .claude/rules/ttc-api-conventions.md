
**`.claude/rules/api-conventions.md`**
```markdown
# API 设计规范

## RESTful 约定

### 资源命名
- 使用名词复数: `/users`, `/orders`, `/products`
- 避免动词: ❌ `/getUser`, ✅ `/users/{id}`

### HTTP 方法
| 方法 | 用途 | 示例 | 状态码 |
|------|------|------|--------|
| GET | 查询 | `/users/{id}` | 200 |
| POST | 创建 | `/users` | 201 |
| PUT | 全量更新 | `/users/{id}` | 200 |
| PATCH | 部分更新 | `/users/{id}` | 200 |
| DELETE | 删除 | `/users/{id}` | 204 |

### 响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "john"
  },
  "timestamp": "2024-01-01T00:00:00Z"
}
分页响应
json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "last": false
  }
}
参数校验注解
java
public class UserRequest {
    @NotNull(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
