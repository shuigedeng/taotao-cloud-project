# 系统编码规范

## 目录

- [1. 前端CRUD操作通用组件](#1-前端crud操作通用组件)
- [2. 后端CRUD接口规范](#2-后端crud接口规范)
- [3. 后端接口加解密注解](#3-后端接口加解密注解)
- [4. 数据库建表规范](#4-数据库建表规范)
- [5. SQL语句编写规范](#5-sql语句编写规范)
- [6. 系统资源配置规范](#6-系统资源配置规范)

---

## 1. 前端CRUD操作通用组件

### 1.1 AiCrudPage 组件简介

`AiCrudPage` 是基于 Naive UI 封装的完整 CRUD 解决方案，集成搜索、表格、新增、编辑、删除、导入导出等功能。

### 1.2 基本使用

```vue
<template>
  <AiCrudPage
    ref="crudRef"
    :api-config="apiConfig"
    :search-schema="searchSchema"
    :columns="tableColumns"
    :edit-schema="editSchema"
    row-key="id"
    :load-detail-on-edit="true"
  />
</template>

<script setup>
import { AiCrudPage } from '@/components/ai-form'

const apiConfig = {
  list: 'get@/api/module/page',        // 列表查询
  detail: 'get@/api/module/:id',      // 详情查询
  add: 'post@/api/module',             // 新增
  update: 'put@/api/module',           // 修改
  delete: 'delete@/api/module/:id'    // 删除
}

// 搜索表单配置
const searchSchema = [
  {
    field: 'name',
    label: '名称',
    type: 'input',
    props: {
      placeholder: '请输入名称'
    }
  },
  {
    field: 'status',
    label: '状态',
    type: 'select',
    props: {
      placeholder: '请选择状态',
      clearable: true,
      options: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 }
      ]
    }
  }
]

// 表格列配置
const tableColumns = [
  {
    prop: 'name',
    label: '名称',
    width: 150
  },
  {
    prop: 'status',
    label: '状态',
    width: 100,
    render: (row) => {
      return h(NTag, {
        type: row.status === 1 ? 'success' : 'error',
        size: 'small'
      }, { default: () => row.status === 1 ? '启用' : '禁用' })
    }
  }
]

// 编辑表单配置
const editSchema = [
  {
    field: 'name',
    label: '名称',
    type: 'input',
    rules: [{ required: true, message: '请输入名称', trigger: 'blur' }],
    props: {
      placeholder: '请输入名称'
    }
  },
  {
    field: 'status',
    label: '状态',
    type: 'switch',
    defaultValue: 1,
    props: {
      checkedValue: 1,
      uncheckedValue: 0
    }
  }
]
</script>
```

### 1.3 字段类型映射

| editSchema type | 组件 | 说明 |
|----------------|------|------|
| `input` | NInput | 输入框 |
| `textarea` | NInput textarea | 文本域 |
| `select` | NSelect | 下拉选择 |
| `switch` | NSwitch | 开关 |
| `input-number` | NInputNumber | 数字输入框 |
| `date` | NDatePicker | 日期选择 |
| `daterange` | NDatePicker range | 日期范围 |
| `upload` | NUpload | 文件上传 |

### 1.4 常用属性

| 属性 | 类型 | 说明 |
|------|------|------|
| `api-config` | Object | API配置对象 |
| `search-schema` | Array | 搜索表单配置 |
| `columns` | Array | 表格列配置 |
| `edit-schema` | Array | 编辑表单配置 |
| `row-key` | String | 行键字段，默认 'id' |
| `hide-add` | Boolean | 隐藏新增按钮 |
| `hide-selection` | Boolean | 隐藏多选列 |
| `hide-toolbar` | Boolean | 隐藏工具栏 |
| `load-detail-on-edit` | Boolean | 编辑时加载详情 |
| `modal-type` | String | 弹窗类型：'modal' 或 'drawer' |

### 1.5 自定义插槽

```vue
<AiCrudPage>
  <!-- 自定义工具栏 -->
  <template #toolbar-start>
    <n-button>自定义按钮</n-button>
  </template>
  
  <!-- 自定义操作列 -->
  <template #table-action="{ row }">
    <a @click="handleCustom(row)">自定义操作</a>
  </template>
</AiCrudPage>
```

---

## 2. 后端CRUD接口规范

### 2.1 标准Controller结构

```java
@Slf4j
@RestController
@RequestMapping("/api/module")
public class ModuleController {
    
    private final ModuleService moduleService;
    
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    public RespInfo<?> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        return RespInfo.success(moduleService.lambdaQuery()
            .like(name != null, Module::getName, name)
            .eq(status != null, Module::getStatus, status)
            .page(new Page<>(pageNum, pageSize)));
    }
    
    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public RespInfo<?> getById(@PathVariable Long id) {
        return RespInfo.success(moduleService.getById(id));
    }
    
    /**
     * 新增
     */
    @PostMapping
    public RespInfo<?> save(@RequestBody Module entity) {
        moduleService.save(entity);
        return RespInfo.success();
    }
    
    /**
     * 修改
     */
    @PutMapping
    public RespInfo<?> update(@RequestBody Module entity) {
        moduleService.updateById(entity);
        return RespInfo.success();
    }
    
    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public RespInfo<?> delete(@PathVariable Long id) {
        moduleService.removeById(id);
        return RespInfo.success();
    }
}
```

### 2.2 接口命名规范

| 操作 | URL | 方法 | 说明 |
|------|-----|------|------|
| 分页查询 | `/page` | GET | 返回分页数据 |
| 列表查询 | `/list` | GET | 返回列表数据 |
| 详情查询 | `/{id}` | GET | 根据ID查询详情 |
| 新增 | `/` | POST | 新增数据 |
| 修改 | `/` | PUT | 修改数据 |
| 删除 | `/{id}` | DELETE | 根据ID删除 |

### 2.3 返回值规范

```java
// 成功返回
return RespInfo.success(data);

// 失败返回
return RespInfo.error("错误信息");

// 分页返回
return RespInfo.success(page);
```

---

## 3. 后端接口加解密注解

### 3.1 注解说明

| 注解 | 作用范围 | 说明 |
|------|---------|------|
| `@ApiDecrypt` | 方法/类 | 请求数据解密 |
| `@ApiEncrypt` | 方法/类 | 响应数据加密 |

### 3.2 使用示例

#### 方式一：方法级别

```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    /**
     * 请求解密 + 响应加密
     */
    @ApiDecrypt
    @ApiEncrypt
    @PostMapping("/login")
    public RespInfo<?> login(@RequestBody LoginDTO dto) {
        // 请求数据会自动解密
        // 响应数据会自动加密
        return RespInfo.success(userService.login(dto));
    }
    
    /**
     * 仅响应加密
     */
    @ApiEncrypt
    @GetMapping("/sensitive/{id}")
    public RespInfo<?> getSensitiveData(@PathVariable Long id) {
        return RespInfo.success(userService.getSensitiveData(id));
    }
}
```

#### 方式二：类级别

```java
@ApiDecrypt
@ApiEncrypt
@RestController
@RequestMapping("/api/secure")
public class SecureController {
    
    /**
     * 类下所有方法都应用加解密
     */
    @PostMapping("/data")
    public RespInfo<?> processData(@RequestBody DataDTO dto) {
        return RespInfo.success(service.process(dto));
    }
}
```

### 3.3 指定加密算法

```java
@ApiEncrypt(algorithm = "AES")
@ApiDecrypt(algorithm = "AES")
@PostMapping("/aes-data")
public RespInfo<?> processAesData(@RequestBody DataDTO dto) {
    return RespInfo.success(service.process(dto));
}
```

### 3.4 前端调用加密接口

```javascript
import { postEncrypt } from '@/utils/encrypt-request'

// 使用加密请求
async function login(data) {
  const res = await postEncrypt('/api/user/login', data)
  return res
}
```

---

## 4. 数据库建表规范

### 4.1 必须字段

所有业务表必须包含以下基础字段：

```sql
-- 创建人ID
`create_by` bigint DEFAULT NULL COMMENT '创建人ID',

-- 创建时间
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

-- 创建部门
`create_dept` bigint DEFAULT NULL COMMENT '创建部门ID',

-- 更新人ID
`update_by` bigint DEFAULT NULL COMMENT '更新人ID',

-- 更新时间
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

-- 租户编号（多租户系统必须）
`tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号'
```

### 4.2 实体类定义

```java
@Data
@TableName("sys_user")
public class SysUser {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 创建部门
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createDept;
    
    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 租户编号
     */
    private Long tenantId;
}
```

### 4.3 建表模板

```sql
CREATE TABLE `表名` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
    
    -- 业务字段区域
    `field1` varchar(100) NOT NULL COMMENT '字段说明',
    `field2` int DEFAULT 0 COMMENT '字段说明',
    
    -- 基础字段区域
    `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_dept` bigint DEFAULT NULL COMMENT '创建部门ID',
    `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    
    PRIMARY KEY (`id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表说明';
```

---

## 5. SQL语句编写规范

### 5.1 基本原则

**❌ 禁止：在业务层循环查询数据库**

```java
// 错误示例：循环查询
List<Long> userIds = Arrays.asList(1L, 2L, 3L);
List<User> users = new ArrayList<>();
for (Long userId : userIds) {
    User user = userMapper.selectById(userId);  // ❌ N+1问题
    users.add(user);
}
```

**✅ 正确：使用 SQL 关联查询**

```java
// 正确示例：一次查询
List<User> users = userMapper.selectList(
    new LambdaQueryWrapper<User>()
        .in(User::getId, userIds)
);
```

### 5.2 复杂查询必须写在 XML 中

**Mapper 接口：**

```java
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 查询用户及其组织信息
     */
    IPage<UserVO> selectUserWithOrg(Page<UserVO> page, 
                                      @Param("userId") Long userId,
                                      @Param("status") Integer status);
}
```

**XML 文件：** `resources/mapper/UserMapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mdframe.forge.plugin.system.mapper.UserMapper">
    
    <select id="selectUserWithOrg" resultType="com.mdframe.forge.plugin.system.domain.vo.UserVO">
        SELECT 
            u.id,
            u.user_name,
            u.real_name,
            o.org_name
        FROM sys_user u
        LEFT JOIN sys_org o ON u.org_id = o.id
        WHERE u.id = #{userId}
        <if test="status != null">
            AND u.status = #{status}
        </if>
        ORDER BY u.create_time DESC
    </select>
    
</mapper>
```

### 5.3 SQL 编写规范

1. **使用 LEFT JOIN 而非子查询**

```sql
-- ✅ 推荐
SELECT u.*, o.org_name
FROM sys_user u
LEFT JOIN sys_org o ON u.org_id = o.id

-- ❌ 不推荐
SELECT u.*, (SELECT org_name FROM sys_org WHERE id = u.org_id) as org_name
FROM sys_user u
```

2. **合理使用索引**

```sql
-- 创建索引
CREATE INDEX idx_user_status ON sys_user(status);
CREATE INDEX idx_user_org ON sys_user(org_id);

-- 复合索引（遵循最左前缀原则）
CREATE INDEX idx_tenant_status_time ON sys_user(tenant_id, status, create_time);
```

3. **避免 SELECT ***

```sql
-- ✅ 推荐：明确字段
SELECT id, user_name, real_name FROM sys_user

-- ❌ 不推荐
SELECT * FROM sys_user
```

4. **使用 EXPLAIN 分析查询**

```sql
EXPLAIN SELECT * FROM sys_user WHERE status = 1;
```

---

## 6. 系统资源配置规范

### 6.1 sys_resource 表结构

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 资源ID |
| tenant_id | bigint | 租户编号 |
| resource_name | varchar(100) | 资源名称 |
| parent_id | bigint | 父级资源ID（0为顶级） |
| resource_type | tinyint | 资源类型（1-目录，2-菜单，3-按钮，4-API接口） |
| sort | int | 排序（值越小越靠前） |
| path | varchar(255) | 资源路由（菜单/目录用） |
| component | varchar(255) | 前端组件路径（菜单用） |
| is_external | tinyint | 是否外链（0-否，1-是） |
| is_public | tinyint | 是否公开资源（0-否，1-是） |
| menu_status | tinyint | 菜单状态（0-隐藏，1-显示） |
| visible | tinyint | 显示状态（0-隐藏，1-显示） |
| perms | varchar(100) | 权限标识（如：sys:user:list） |
| icon | varchar(50) | 图标 |
| api_method | varchar(10) | API请求方法（GET/POST/PUT/DELETE） |
| api_url | varchar(255) | API接口地址 |
| keep_alive | tinyint | 是否缓存（0-否，1-是） |
| always_show | tinyint | 是否总是显示（0-否，1-是） |
| redirect | varchar(255) | 重定向地址 |
| remark | varchar(500) | 备注 |

### 6.2 资源类型说明

| resource_type | 说明 | 必填字段 |
|---------------|------|---------|
| 1 | 目录 | resource_name, path, icon |
| 2 | 菜单 | resource_name, path, component, icon |
| 3 | 按钮 | resource_name, perms |
| 4 | API接口 | resource_name, api_method, api_url |

### 6.3 INSERT 语句示例

#### 1. 目录资源

```sql
INSERT INTO sys_resource (
    tenant_id, resource_name, parent_id, resource_type, 
    sort, path, icon, visible, menu_status, create_time, update_time
) VALUES (
    1, '系统管理', 0, 1, 
    1, '/system', 'settings', 1, 1, NOW(), NOW()
);
```

#### 2. 菜单资源

```sql
INSERT INTO sys_resource (
    tenant_id, resource_name, parent_id, resource_type,
    sort, path, component, icon, visible, menu_status, keep_alive,
    create_time, update_time
) VALUES (
    1, '用户管理', 1, 2,
    1, '/system/user', 'system/user/index', 'person', 1, 1, 1,
    NOW(), NOW()
);
```

#### 3. 按钮资源

```sql
INSERT INTO sys_resource (
    tenant_id, resource_name, parent_id, resource_type,
    sort, perms, visible, create_time, update_time
) VALUES (
    1, '用户新增', 2, 3,
    1, 'sys:user:add', 1, NOW(), NOW()
);
```

#### 4. API接口资源

```sql
-- 单个接口
INSERT INTO sys_resource (
    tenant_id, resource_name, parent_id, resource_type,
    sort, api_method, api_url, visible, remark, create_time, update_time
) VALUES (
    1, '用户列表查询', 0, 4,
    1, 'POST', '/system/user/page', 1, '分页查询用户列表', NOW(), NOW()
);

-- 通配符接口
INSERT INTO sys_resource (
    tenant_id, resource_name, parent_id, resource_type,
    sort, api_url, visible, remark, create_time, update_time
) VALUES (
    1, '系统管理-所有接口', 0, 4,
    1, '/system/**', 1, '系统管理模块所有接口', NOW(), NOW()
);
```

### 6.4 通配符规则

| 通配符 | 说明 | 示例 |
|--------|------|------|
| `**` | 匹配多层路径 | `/system/**` 匹配 `/system/user/page` |
| `*` | 匹配单层路径 | `/system/user/*` 匹配 `/system/user/add` |
| 精确路径 | 精确匹配 | `/system/user/page` 仅匹配此路径 |

### 6.5 完整菜单树示例

```sql
-- 一级目录：系统管理
INSERT INTO sys_resource VALUES (1, 1, '系统管理', 0, 1, 1, '/system', NULL, 0, 0, 1, 1, NULL, NULL, 'settings', NULL, NULL, 0, 0, NULL, NULL, NOW(), NOW(), NULL);

-- 二级菜单：用户管理
INSERT INTO sys_resource VALUES (2, 1, '用户管理', 1, 2, 1, '/system/user', 'system/user/index', 0, 0, 1, 1, NULL, NULL, 'person', NULL, NULL, 1, 0, NULL, NULL, NOW(), NOW(), NULL);

-- 三级按钮：用户新增
INSERT INTO sys_resource VALUES (3, 1, '用户新增', 2, 3, 1, NULL, NULL, 0, 0, 1, 1, 'sys:user:add', NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NOW(), NOW(), NULL);

-- 三级按钮：用户编辑
INSERT INTO sys_resource VALUES (4, 1, '用户编辑', 2, 3, 2, NULL, NULL, 0, 0, 1, 1, 'sys:user:edit', NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NOW(), NOW(), NULL);

-- 三级按钮：用户删除
INSERT INTO sys_resource VALUES (5, 1, '用户删除', 2, 3, 3, NULL, NULL, 0, 0, 1, 1, 'sys:user:remove', NULL, NULL, NULL, NULL, 0, 0, NULL, NULL, NOW(), NOW(), NULL);
```

### 6.6 API权限配置示例

```sql
-- 消息管理模块API
INSERT INTO sys_resource (tenant_id, resource_name, parent_id, resource_type, sort, api_method, api_url, visible, remark, create_time, update_time) VALUES
(1, '消息列表查询', 0, 4, 1, 'POST', '/api/message/manage/page', 1, '分页查询消息列表', NOW(), NOW()),
(1, '消息详情查询', 0, 4, 2, 'GET', '/api/message/manage/{id}/detail', 1, '查询消息详情', NOW(), NOW()),
(1, '消息发送', 0, 4, 3, 'POST', '/api/message/send', 1, '发送消息', NOW(), NOW());

-- 业务类型管理API
INSERT INTO sys_resource (tenant_id, resource_name, parent_id, resource_type, sort, api_method, api_url, visible, remark, create_time, update_time) VALUES
(1, '业务类型列表', 0, 4, 1, 'GET', '/api/message/bizType/page', 1, '分页查询业务类型', NOW(), NOW()),
(1, '业务类型新增', 0, 4, 2, 'POST', '/api/message/bizType', 1, '新增业务类型', NOW(), NOW()),
(1, '业务类型修改', 0, 4, 3, 'PUT', '/api/message/bizType', 1, '修改业务类型', NOW(), NOW()),
(1, '业务类型删除', 0, 4, 4, 'DELETE', '/api/message/bizType/{id}', 1, '删除业务类型', NOW(), NOW());
```

---

## 附录：常用工具类

### A. MyBatis-Plus 使用

```java
// Lambda 查询
List<User> users = userMapper.selectList(
    new LambdaQueryWrapper<User>()
        .eq(User::getStatus, 1)
        .like(User::getUserName, "admin")
        .orderByDesc(User::getCreateTime)
);

// Lambda 更新
userMapper.update(null, 
    new LambdaUpdateWrapper<User>()
        .set(User::getStatus, 0)
        .eq(User::getId, userId)
);

// 分页查询
Page<User> page = userMapper.selectPage(
    new Page<>(pageNum, pageSize),
    new LambdaQueryWrapper<User>()
        .eq(User::getStatus, 1)
);
```

### B. Hutool 工具类

```java
// 字符串工具
StrUtil.isNotBlank(str);
StrUtil.isEmpty(str);

// 日期工具
DateUtil.parse("2024-01-01");
DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");

// JSON 工具
JSONUtil.parseObj(obj);
JSONUtil.toJsonStr(obj);
```

### C. 前端常用工具

```javascript
// 日期格式化
import { formatDateTime } from '@/utils'
formatDateTime(new Date())  // 2024-01-01 10:00:00

// 请求
import { request } from '@/utils'
const res = await request.get('/api/user/1')
const res = await request.post('/api/user', data)

// 加密请求
import { postEncrypt } from '@/utils/encrypt-request'
const res = await postEncrypt('/api/login', loginData)
```

---

## 更新日志

- 2026-04-02：初始版本，整理前端CRUD组件、后端接口、加解密注解、建表规范、SQL规范、资源配置规范
