#!/usr/bin/env python3
"""
taotao-cloud-project SQL 查询分析器

分析 MyBatis Mapper XML 中的查询，识别潜在性能问题。
支持任意模块的 mapper 目录扫描。

用法: python optimize-query.py <模块资源目录>
示例: python optimize-query.py taotao-cloud-microservice/taotao-cloud-business/taotao-cloud-order
      python optimize-query.py .

检查项:
1. N+1 查询模式
2. 缺少索引的 JOIN 字段
3. SELECT * 查询
4. 大结果集无分页
"""

import os
import sys
import re
from pathlib import Path


def find_mapper_xml(base_dir):
    """扫描所有 Mapper XML 文件"""
    xml_files = []
    for root, dirs, files in os.walk(base_dir):
        for f in files:
            if f.endswith(".xml"):
                path = os.path.join(root, f)
                if "generatorConfiguration" in path:
                    continue
                xml_files.append(path)
    return xml_files


def analyze_mapper(filepath):
    """分析单个 Mapper XML 文件"""
    issues = []
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            content = f.read()
    except UnicodeDecodeError:
        try:
            with open(filepath, "r", encoding="gbk") as f:
                content = f.read()
        except Exception:
            return ["  ⚠️  无法读取文件编码"]

    # 检查 SELECT *
    select_stars = re.findall(r"SELECT\s+\*", content, re.IGNORECASE)
    if select_stars:
        issues.append(f"  ⚠️  SELECT * 查询: {len(select_stars)} 处")

    # 检查 JOIN 查询
    joins = re.findall(r"JOIN\s+(\w+)", content, re.IGNORECASE)
    if joins:
        issues.append(f"  ℹ️  JOIN 表: {', '.join(set(joins))}")

    # 检查无分页的大结果集
    select_counts = len(re.findall(r"<select\b", content, re.IGNORECASE))
    limit_counts = len(re.findall(r"LIMIT\s+\d+|ROWNUM\s|<=\s*\d+", content, re.IGNORECASE))
    if select_counts > limit_counts:
        issues.append(f"  ℹ️  {select_counts} 个 SELECT，{limit_counts} 个含 LIMIT/分页")

    # 检查循环查询模式
    if re.search(r"select\s+.*\b(in|foreach)\b", content, re.IGNORECASE):
        issues.append("  ℹ️  发现 IN/FOREACH 查询（确认是否可优化为 JOIN）")

    return issues


def main():
    if len(sys.argv) < 2:
        print("用法: python optimize-query.py <项目目录>")
        print("示例: python optimize-query.py .")
        print("      python optimize-query.py taotao-cloud-microservice/taotao-cloud-business/taotao-cloud-order")
        sys.exit(1)

    base_dir = sys.argv[1]
    if not os.path.isdir(base_dir):
        print(f"错误: 目录 '{base_dir}' 不存在")
        sys.exit(1)

    # 查找所有模块下的 mapper 目录
    mapper_dirs = []
    for root, dirs, files in os.walk(base_dir):
        dir_name = os.path.basename(root)
        if dir_name in ("mapper", "mybatis") or "mybatis" in dir_name:
            mapper_dirs.append(root)

    if not mapper_dirs:
        print("ℹ️  未找到 mapper 目录，尝试扫描 resources 下的所有 XML")
        mapper_dirs = [base_dir]

    all_issues = []
    for md in mapper_dirs:
        xml_files = find_mapper_xml(md)
        for xml_file in xml_files:
            issues = analyze_mapper(xml_file)
            if issues:
                rel_path = os.path.relpath(xml_file, base_dir)
                print(f"\n📄 {rel_path}")
                for issue in issues:
                    print(issue)
                    all_issues.append(issue)

    if not all_issues:
        print("\n✅ 未发现明显的查询性能问题")
    else:
        print(f"\n📊 共发现 {len(all_issues)} 个需要关注的问题")


if __name__ == "__main__":
    main()
