## Claude 配置本质上就两件事：

它要知道什么（指导）
它能做什么（控制）

对应到文件：

文件	                    解决的问题	            性质
CLAUDE.md	            这个项目是什么、怎么协作	    指导
.claude/settings.json	什么操作允许，什么不允许	    控制

## 一个实用目录

your-project/
├── CLAUDE.md                  # 项目主指令（共享）
├── CLAUDE.local.md            # 个人覆盖（不提交）
└── .claude/
    ├── settings.json          # 控制层：权限、钩子、项目行为
    ├── settings.local.json    # 个人控制层（不提交）
    ├── rules/                 # 模块化规则
    ├── hooks/                 # 自动化脚本
    ├── commands/              # 可复用提示词工作流
    ├── skills/                # 打包的复杂工作流（按需）
    └── agents/                # 专用子代理角色（按需）

CLAUDE.md 里放什么？
    建议只放全局且稳定的信息：
    技术栈与核心依赖
    项目结构和职责边界
    常用开发命令（测试、格式化、迁移等）
    全局编码约定与禁区
    团队协作方式（语言、输出风格、决策偏好）

settings.json 里放什么？
    这里是权限和行为控制层，常见内容：
    Claude 允许执行的命令范围
    敏感文件访问限制（如 .env）
    钩子配置
    项目级行为策略（如是否允许某些自动操作）
    一句话：CLAUDE.md 负责“怎么做事”，settings.json 负责“能做什么”。

