zip导入[测试用例](./src/test/java/cn/wisewe/docx4j/input/builder/compression/CompressionImporterSpec.java)

<details>
<summary><b>简单压缩包导入</b></summary>

```java
CompressionImporter.create(new FileInputStream(FileUtil.brotherPath(this.getClass(), "/complex.zip")))
    // 文件处理
    .file((s, is) -> {
        // 解析压缩包中的excel文件
        if (FileUtil.suffix(s).equals("xlsx")) {
            ImportResult<Person> result =
                SpreadSheetImporter.create(is, false)
                    .skip(1)
                    .sheet(0)
                    .resolve(Person.class);
            System.out.println(result.hasInvalid());
            System.out.println(result.getSkip());
            System.out.println(result.getInvalidRecords());
            System.out.println(result.getValidRecords());
        }

        // 继续解压压缩文件
        if (FileUtil.suffix(s).equals("zip")) {
            CompressionImporter.create(is, false)
                // 目录处理
                .folder(ns -> System.out.printf("folder %s\n", ns))
                // 文件处理
                .file((ns, nis) -> System.out.printf("file %s\n", ns))
                .resolve();
        }
    })
    .resolve();
```

</details>
