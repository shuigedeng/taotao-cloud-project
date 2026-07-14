---
description: 代码审查 - 检查 SpringBoot 代码质量
parameters:
  - name: scope
    type: string
    enum: [controller, service, repository, all]
    default: all
  - name: strict
    type: boolean
    default: true
---

# 代码审查命令

执行代码审查，检查范围：`{{scope}}`

## 检查清单

### 架构规范
- [ ] Controller 层是否只处理 HTTP 转换
- [ ] Service 层是否有事务注解
- [ ] Repository 是否使用了正确的查询方法
- [ ] Entity 和 DTO 是否分离

### 代码质量
- [ ] 是否有重复代码
- [ ] 方法长度是否超过 50 行
- [ ] 循环复杂度是否过高（>10）
- [ ] 是否正确处理了空值

### 性能问题
- [ ] 是否存在 N+1 查询
- [ ] 批量操作是否使用了批处理方法
- [ ] 是否避免了 SELECT *
- [ ] 缓存策略是否合理

### 安全问题
- [ ] 输入参数是否校验
- [ ] SQL 注入防护
- [ ] 权限控制是否完整
- [ ] 敏感数据是否脱敏

## 输出格式
```markdown
## 代码审查报告

### 🔴 严重问题（必须修复）
- [文件:行号] 问题描述 + 修复建议

### 🟡 警告（建议修复）
- [文件:行号] 问题描述 + 优化方案

### 🟢 优化建议
- 建议内容

### ✅ 通过项
- 列举做得好的地方
