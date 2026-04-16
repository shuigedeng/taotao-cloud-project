## 6. 自动化工作流

**`.claude/skills/crud-generator/SKILL.md`**
```markdown
---
name: crud-generator
description: 自动生成标准 CRUD 代码（Controller, Service, Repository, DTO, Mapper）
triggers:
  - "生成CRUD"
  - "创建增删改查"
  - "新建模块"
---

# CRUD 代码生成器

## 触发条件
用户输入包含 "生成CRUD" 或 "创建增删改查" 等关键词时自动触发。

## 工作流程

### 1. 收集信息
询问用户：
- 实体名称（如 User, Product）
- 字段列表（名称 + 类型）
- 是否需要分页
- 是否需要软删除

### 2. 生成文件
按照标准包结构生成：
src/main/java/com/company/project/
├── entity/{Entity}.java
├── dto/{Entity}Request.java
├── dto/{Entity}Response.java
├── controller/{Entity}Controller.java
├── service/{Entity}Service.java
├── service/impl/{Entity}ServiceImpl.java
├── repository/{Entity}Repository.java
└── mapper/{Entity}Mapper.java

text

### 3. 代码模板示例

#### Entity 模板
```java
@Entity
@Table(name = "{{tableName}}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class {{Entity}} {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    {% for field in fields %}
    @Column(name = "{{field.name}}", nullable = false)
    private {{field.type}} {{field.name}};
    {% endfor %}
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
4. 生成数据库迁移
自动创建 Flyway 迁移脚本：

sql
-- V{{timestamp}}__create_{{tableName}}_table.sql
CREATE TABLE {{tableName}} (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    {% for field in fields %}
    {{field.name}} {{field.sqlType}} NOT NULL,
    {% endfor %}
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
