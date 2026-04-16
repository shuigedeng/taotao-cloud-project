
## 4. 自定义命令

**`.opencode/command/deploy.md`**
```markdown
---
name: deploy
description: 部署应用到指定环境
parameters:
  - name: environment
    type: string
    enum: [dev, test, prod]
    required: true
  - name: skip-tests
    type: boolean
    default: false
---

# 部署命令

执行以下步骤部署 SpringBoot 应用：

1. **运行测试**（除非 `skip-tests=true`）
   ```bash
   ./mvnw clean test

打包应用

bash
./mvnw clean package -DskipTests={skip-tests}
部署到 {environment} 环境

环境变量：SPRING_PROFILES_ACTIVE={environment}

启动命令：java -jar target/*.jar --spring.profiles.active={environment}

健康检查

bash
curl -f http://localhost:8080/actuator/health
输出部署报告

部署时间：{timestamp}

JAR 大小：{size}

环境：{environment}
