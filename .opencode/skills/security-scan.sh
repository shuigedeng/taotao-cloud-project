#!/bin/bash
# 安全扫描脚本

echo "🔒 SpringBoot 安全扫描开始..."

# 1. 检查敏感信息泄露
echo "1. 检查敏感信息..."
grep -r "password\|secret\|token\|key" src/main/resources/application*.yml \
  | grep -v "ENC(" \
  && echo "⚠️  警告：发现明文敏感信息！"

# 2. 检查 SQL 注入风险
echo "2. 检查 SQL 注入..."
grep -r "nativeQuery" src/main/java \
  | grep -v "@Query(nativeQuery" \
  && echo "⚠️  警告：发现原生 SQL 查询，请确认已使用参数绑定！"

# 3. 检查权限注解
echo "3. 检查权限控制..."
grep -r "@PreAuthorize\|@Secured" src/main/java \
  || echo "⚠️  警告：未发现权限控制注解！"

# 4. 检查 XSS 防护
echo "4. 检查 XSS 防护..."
grep -r "HtmlUtils.htmlEscape\|Jsoup.clean" src/main/java \
  || echo "⚠️  建议：添加 XSS 过滤器"

echo "✅ 扫描完成"
