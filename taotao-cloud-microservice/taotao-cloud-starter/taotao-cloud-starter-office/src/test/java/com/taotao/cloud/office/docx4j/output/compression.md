[zip导出测试用例](./src/test/java/cn/wisewe/docx4j/output/builder/compression/CompressionExporterSpec.java)

<details>
<summary><b>1.目录导出</b></summary>

##### 效果

<blockquote>
. folders.zip       <br/>
|                   <br/>
|___test-classes/   <br/>   
|                   <br/>
|___abc/            <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___1/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___2/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___3/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___4/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___5/           <br/>
|                   <br/>
|___cde/            <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___6/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___7/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___8/           <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|___9/           <br/>
|                   <br/>
</blockquote>

##### 代码

```java
public void folders() throws FileNotFoundException {
    CompressionExporter.create()
        // 文件目录
        .folder(new File(this.getClass().getResource(OutputConstants.SLASH).getPath()))
        // 自定义目录
        .folder("abc", (fn, b) ->
            b.folders(IntStream.range(1, 6).boxed().collect(Collectors.toList()), t -> fn + t, (n, nfn, t) -> {})
        )
        .folder("cde", (fn, b) ->
            b.folders(IntStream.range(6, 10).boxed().collect(Collectors.toList()), t -> fn + t, (n, nfn, t) -> {})
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "folders.zip")));
}
```

</details>

<details>
<summary><b>2.文件导出</b></summary>

##### 效果

<blockquote>    <br/> 
. files.zip     <br/>               
|               <br/> 
|___a.jpeg    <br/>           
|               <br/> 
|___a.docx    <br/>           
|               <br/> 
|___b.xlsx    <br/>           
|               <br/> 
|___c.pdf     <br/>       
|               <br/> 
</blockquote>

##### 代码

```java
public void files() throws FileNotFoundException {
    CompressionExporter.create()
        // 已有文件
        .file(new File(FileUtil.rootPath(this.getClass(), "/a.jpeg")))
        // 空的word文件
        .file(DocumentFileType.DOCX.fullName("a"), os -> DocumentBuilder.create().writeTo(os, false))
        // 空的excel文件
        .file(SpreadSheetFileType.XLSX.fullName("b"), os -> SpreadSheetBuilder.create().writeTo(os, false))
        // 空的pdf文件
        .file(PortableFileType.PDF.fullName("c"), os -> PortableBuilder.fastCreate().writeTo(os, false))
        .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "files.zip")));
}
```

</details>

<details>
<summary><b>3.复杂目录导出</b></summary>

##### 效果

<blockquote>    <br/> 
. complex.zip     <br/>               
|               <br/> 
|___cn/    <br/>           
|               <br/> 
|___test-classes/    <br/>           
|               <br/> 
|___女/    <br/>           
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___DOCX/ <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___张三.docx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___王五.docx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|  <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___赵六.docx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___PDF/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___张三.pdf <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___王五.pdf <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|  <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___赵六.pdf <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___XLSX/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___张三.xlsx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___王五.xlsx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|  <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___赵六.xlsx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___ZIP/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|___张三.zip <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|___王五.zip <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|  <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|___赵六.zip <br>
|     <br/> 
|___男/     <br/>       
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___DOCX/ <br/>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___李四.docx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___燕七.docx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___PDF/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___李四.pdf <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___燕七.pdf <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___XLSX/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___李四.xlsx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;|___燕七.xlsx <br>
|&nbsp;&nbsp;&nbsp;&nbsp;| <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;|___ZIP/ <br/> 
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|___李四.zip <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| <br>
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|___燕七.zip <br>
|<br>
|___a.jpeg  <br/>
| <br>
|___b.png <br>
| <br>
</blockquote>

##### 代码

```java
public void complex() throws FileNotFoundException {
        // 文件类型示例
        // 将数据按照性别分组 合并处理性别列 模拟sql分组 但不保证列表数据顺序
        List<String> sexList =
            SpecDataFactory.tableData().stream().map(Person::getSex).distinct().collect(Collectors.toList());
        Map<String, List<Person>> groupBySex =
            SpecDataFactory.tableData().stream().collect(Collectors.groupingBy(Person::getSex));
        CompressionExporter.create()
            // 直接添加文件
            .any(new File(FileUtil.rootPath(this.getClass(), "/a.jpeg")))
            .file(new File(FileUtil.rootPath(this.getClass(), "/b.png")))
            // 直接添加目录 test class path root
            .any(new File(this.getClass().getResource(OutputConstants.SLASH).getPath()))
            .folder(new File(this.getClass().getResource(OutputConstants.SLASH + "cn").getPath()))
            // 动态文件夹及文件
            .folders(sexList, it -> it, (u, fn, b) -> {
                // 对应性别的docx文档
                b.folder(fn + DocumentFileType.DOCX.name(), (sfn, nb) ->
                    nb.files(groupBySex.get(u), p -> DocumentFileType.DOCX.fullName(sfn + p.getName()), (p, os) ->
                        DocumentBuilder.create()
                            .header("我是页眉")
                            .footer("我是页脚")
                            .headingParagraph(p.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
                            .table(2, 3, t ->
                                t.row(r -> r.headCells("姓名", "年龄", "性别"))
                                    .row(r -> r.dataCells(p::getName, p::getAge, p::getSex))
                            )
                            .writeTo(os, false)
                    )
                );
                // 对应性别的xlsx文档
                b.folder(fn + SpreadSheetFileType.XLSX.name(), (sfn, nb) ->
                    nb.files(groupBySex.get(u), p -> SpreadSheetFileType.XLSX.fullName(sfn + p.getName()), (p, os) ->
                        SpreadSheetBuilder.create()
                            .workbook(wb ->
                                wb.sheet(s ->
                                    // 表头行
                                    s.row(r -> r.headCells("姓名", "年龄", "性别"))
                                        // 数据行
                                        .row(r -> r.dataCells(p::getName, p::getAge, p::getSex))
                                )
                            )
                            .writeTo(os, false)
                    )
                );
                // 对应性别的pdf文档
                b.folder(fn + PortableFileType.PDF.name(), (sfn, nb) ->
                    nb.files(groupBySex.get(u), p -> PortableFileType.PDF.fullName(sfn + p.getName()), (p, os) ->
                        PortableBuilder.create()
                            .event(new DefaultTextWatermarkHandler(p.getName()))
                            .open()
                            .headingParagraph(p.getName() + "个人信息", Fonts.HEADING_1)
                            .textParagraph(String.format("姓名:%s", p.getName()))
                            .textParagraph(String.format("年龄:%s", p.getAge()))
                            .textParagraph(String.format("性别:%s", p.getSex()))
                            .writeTo(os, false)
                    )
                );
                // 空压缩包
                b.folder(fn + CompressionFileType.ZIP.name(), (sfn, nb) ->
                    nb.files(groupBySex.get(u), p -> CompressionFileType.ZIP.fullName(sfn + p.getName()), (p, os) ->
                        CompressionExporter.create().writeTo(os, false)
                    )
                );
            })
            .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "complex.zip")));
    }
```

</details>
