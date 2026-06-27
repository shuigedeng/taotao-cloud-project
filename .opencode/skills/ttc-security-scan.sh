#!/bin/bash
# taotao-cloud-project 安全扫描脚本
# 用于检查 DDD 项目中的常见安全问题
# 用法: bash .opencode/skills/security-scan.sh [模块目录]
# 示例: bash .opencode/skills/security-scan.sh taotao-cloud-microservice/taotao-cloud-business/taotao-cloud-order

BASE_DIR="${1:-.}"
echo "================================================"
echo "  taotao-cloud-project 安全扫描"
echo "  目标: $BASE_DIR"
echo "================================================"

HAS_WARNINGS=0

# 1. 检查敏感信息泄露
echo ""
echo "[1/4] 检查敏感信息泄露..."
if [ -d "$BASE_DIR/src/main/resources" ]; then
    found=$(grep -r "password\|secret\|token\|jwt-secret\|access-key" \
        "$BASE_DIR/src/main/resources" \
        --include="*.yml" --include="*.yaml" --include="*.properties" \
        2>/dev/null | grep -v "ENC(" | grep -v "#" || true)
    if [ -n "$found" ]; then
        echo "  ⚠️  发现明文敏感信息:"
        echo "$found"
        HAS_WARNINGS=1
    else
        echo "  ✅ 未发现明文敏感信息"
    fi
else
    echo "  ℹ️  未找到 resources 目录"
fi

# 2. 检查 SQL 注入风险
echo ""
echo "[2/4] 检查 SQL 注入风险..."
if [ -d "$BASE_DIR/src/main/java" ]; then
    found=$(grep -rn "nativeQuery\|createNativeQuery\|sql:\|@Select\|@Update\|@Delete\|@Insert" \
        "$BASE_DIR/src/main/java" \
        --include="*.java" 2>/dev/null || true)
    if [ -n "$found" ]; then
        echo "  ℹ️  发现 SQL 注解（确认使用参数绑定）"
    else
        echo "  ✅ 未发现原生 SQL"
    fi
fi

# 3. 检查权限控制
echo ""
echo "[3/4] 检查权限控制注解..."
if [ -d "$BASE_DIR/src/main/java" ]; then
    count=$(grep -rn "@PreAuthorize\|@Secured" \
        "$BASE_DIR/src/main/java" \
        --include="*.java" 2>/dev/null | wc -l || echo 0)
    if [ "$count" -gt 0 ]; then
        echo "  ✅ 发现 $count 处权限控制"
    else
        echo "  ⚠️  未发现权限控制注解"
        HAS_WARNINGS=1
    fi
fi

# 4. 检查 domain 层持久化操作
echo ""
echo "[4/4] 检查 domain 层持久化操作..."
if [ -d "$BASE_DIR/src/main/java" ]; then
    domain_dirs=$(find "$BASE_DIR/src/main/java" -type d -name "domain" 2>/dev/null)
    for dd in $domain_dirs; do
        found=$(grep -rn "\.save\|\.update\|\.delete\|\.findById\|@Table\|@Entity" \
            "$dd" \
            --include="*.java" 2>/dev/null || true)
        if [ -n "$found" ]; then
            echo "  ⚠️  domain 层发现持久化引用（违反 DDD 规范）"
            echo "$found"
            HAS_WARNINGS=1
        fi
    done
fi

echo ""
echo "================================================"
if [ $HAS_WARNINGS -eq 0 ]; then
    echo "  ✅ 安全扫描通过，未发现问题"
else
    echo "  ⚠️  扫描完成，存在需要关注的问题"
fi
echo "================================================"
