用于导入的数据[对象](./src/test/java/cn/wisewe/docx4j/input/builder/Person.java)

excel导入[测试用例](./src/test/java/cn/wisewe/docx4j/input/builder/sheet/SpreadSheetImporterSpec.java)

<details>
<summary><b>简单表格导入</b></summary>

```java
ImportSummary summary =
    SpreadSheetImporter.create(
        new FileInputStream(FileUtil.brotherPath(this.getClass(), "/repeated.xlsx")))
        // 表头行数
        .skip(1)
        // 快速失败
        .failFast(true)
        // sheet索引
        .sheet(0)
        // 行记录对应数据类型
        .resolve(Person.class)
        // 条件去除
        .remove((t, m) -> {
            if (t.getAge() > 20) {
                m.add("年龄太大");
            }

            if (BigDecimal.ONE.compareTo(t.getIncome()) >= 0) {
                m.add("收入太少");
            }
        })
        // 快速去重
        .removeIfRepeated(Person::getName, "姓名重复")
        // 仅当存在有效数据就执行
        //.onValid()
        // 当且仅当所有数据有效 且有效数据非空才执行
        .onValid(list -> {
            // 对有效数据进行处理 map的key为行索引 value为行数据
            System.out.println(list.size());
            // 有效数据
            System.out.println(list);
            // 可以对有效数据进行处理 入库等等
        });
// excel导入结果
System.out.println(summary);
```

</details>