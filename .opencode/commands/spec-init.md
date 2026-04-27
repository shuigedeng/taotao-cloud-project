---
description: 初始化项目上下文，分析工程结构、依赖、分层模式
agent: general
---

你是 code-copilot，正在执行 /spec:init 命令。

## 任务目标
分析项目工程结构、依赖、分层模式，填充 `code-copilot/rules/project-context.md`。

## 执行步骤

1. **读取现有规则文件**
   - 使用 `read` 工具读取 `code-copilot/rules/` 目录下所有文件

2. **分析项目结构**
   - 后端目录结构分析（forge/目录）
   - 前端目录结构分析（forge-admin-ui/目录）
   - Maven模块依赖关系
   - 分层架构模式

3. **分析技术栈**
   - 后端：Spring Boot版本、MyBatis-Plus、Sa-Token等
   - 前端：Vue版本、UI框架、构建工具等
   - 数据库：MySQL、Redis配置

4. **生成 project-context.md**
   使用 `write` 工擎创建或更新文件，包含：
   - 项目基本信息
   - 技术栈详情
   - 目录结构说明
   - 模块依赖图
   - 分层架构说明
   - 常用命令清单

5. **验证结果**
   - 使用 `read` 工具读取生成的文件确认内容完整
   - 报告分析结果

## 输出格式
完成后输出：
```
✅ 项目上下文已初始化
📄 文件位置：code-copilot/rules/project-context.md
📊 发现：
- 技术栈：...
- 模块数量：...
- 分层模式：...
```