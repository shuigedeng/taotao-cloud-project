[word导出测试用例](./src/test/java/cn/wisewe/docx4j/output/builder/document/DocumentExporterSpec.java)

<details>
<summary><b>1.标题正文</b></summary>

##### 效果

> # 标题一
> ## 标题二
> ### 标题三
> #### 标题五
> ##### 标题七
> ###### 标题九
> 这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文</p>


> 说明：支持以上标题样式直接设定，其他标题样式可通过style方法自定义样式设定。

##### 代码

```java
public void simple() throws IOException {
    DocumentExporter.create()
        .headingParagraph("标题一", ParagraphStyle.HEADING_1)
        .headingParagraph("标题二", ParagraphStyle.HEADING_2)
        .headingParagraph("标题三", ParagraphStyle.HEADING_3)
        .headingParagraph("标题五", ParagraphStyle.HEADING_5)
        .headingParagraph("标题七", ParagraphStyle.HEADING_7)
        .headingParagraph("标题九", ParagraphStyle.HEADING_9)
        .textParagraph("这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文")
        .writeTo(new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "simple.docx")));
}
```

</details>

<details>
<summary><b>2.动态分页</b></summary>

##### 效果

<blockquote>
 在本段落后面手动添加个分页符

 ……分页符……(Word中的分页效果)

 多个文档之间自动添加分页符

 <h6 style="text-align: center;">张三个人信息</h6>
 <table>
 	<thead>
 		<tr>
 			<th> <strong>姓名</strong> </th>
 			<th> <strong>年龄</strong> </th>
 			<th> <strong>性别</strong> </th>
 		</tr>
 	</thead>
 	<tbody>
 		<tr>
 			<td> 张三 </td>
 			<td> 26 </td>
 			<td> 女 </td>
 		</tr>
 	</tbody>
 </table>
 ……分页符……(Word中的分页效果)

 <h6 style="text-align: center;">李四个人信息</h6>
 <table>
 	<thead>
 		<tr>
 			<th> <strong>姓名</strong> </th>
 			<th> <strong>年龄</strong> </th>
 			<th> <strong>性别</strong> </th>
 		</tr>
 	</thead>
 	<tbody>
 		<tr>
 			<td> 李四 </td>
 			<td> 50 </td>
 			<td> 男 </td>
 		</tr>
 	</tbody>
 </table>
 ……分页符……(Word中的分页效果)
 ……若干页
</blockquote>

<blockquote>
说明：添加多个文档时，每个文档之间自动添加分页符。
</blockquote>


<h5>代码</h5>

```java
public void breakPage() throws FileNotFoundException {
	List<Person> people = SpecDataFactory.tableData();
	DocumentExporter.create()
		.textParagraph("在本段落后面手动添加个分页符")
		// 手动添加分页符
		.pageBreak()
		.textParagraph("多个文档之间自动添加分页符")
		// 多个文档之间 自动添加分页符
		.documents(people, (it, d) ->
			// 分页文档
			d.headingParagraph(it.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
				.table(2, 3, t ->
					t.row(r -> r.headCells("姓名", "年龄", "性别"))
						.row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
				)
		)
		.writeTo(new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "break-page.docx")));
}
```

</details>

<details>
<summary><b>3.基础表格</b></summary>
<h5>效果</h5>

<blockquote>
<h6 style="text-align: center;">教职工列表</h6>
<table>
	<thead>
		<tr>
			<th> <strong>姓名</strong> </th>
			<th> <strong>年龄</strong> </th>
			<th> <strong>性别</strong> </th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td> 张三 </td>
			<td> 26 </td>
			<td> 女 </td>
		</tr>
		<tr>
			<td> 李四 </td>
			<td> 50 </td>
			<td> 男 </td>
		</tr>
		<tr>
			<td> 王五 </td>
			<td> 18 </td>
			<td> 女 </td>
		</tr>
		<tr>
			<td> 赵六 </td>
			<td> 2 </td>
			<td> 女 </td>
		</tr>
		<tr>
			<td> 燕七 </td>
			<td> 80 </td>
			<td> 男 </td>
		</tr>
	</tbody>
</table>
</blockquote>

<h5>代码</h5>

```java
public void table() throws FileNotFoundException {
    List<Person> people = SpecDataFactory.tableData();
    DocumentExporter.create()
         // 添加副标题
        .headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
        // 添加表格，需要指定表格行数及列数
        .table(people.size() + 1, 3, t ->
               // 表头行会自动加粗
               t.row(r -> r.headCells("姓名", "年龄", "性别"))
               // 数据行正常
               .rows(people, (p, r) -> r.dataCells(p::getName, p::getAge, p::getSex))
              )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "table.docx")));
}
```

</details>

<details>
<summary><b>4.表格单元格合并</b></summary>
<h5>效果</h5>

<blockquote>
<h6 style="text-align: center;">教职工列表</h6>
<table>
	<tr>
	    <th rowspan = "2">姓名</th>
	    <th colspan="2" >其他信息</th>
	</tr >
    <tr >
	    <th>年龄</th>
        <th>性别</th>
	</tr>
	<tr >
	    <td>张三</td>
	    <td>26</td>
	    <td rowspan = "3">女</td>
	</tr>
	<tr >
	    <td>王五</td>
	    <td>18</td>
	</tr>
	<tr >
	    <td>赵六</td>
	    <td>2</td>
	</tr>
	<tr >
	    <td>李四</td>
	    <td>50</td>
	    <td rowspan = "2">男</td>
	</tr>
	<tr >
	    <td>燕七</td>
	    <td>80</td>
	</tr>
</table>
</blockquote>

<blockquote>注意：合并处理性别列模拟sql分组，<b>不保证列表数据顺序</b></blockquote>

<h5>代码</h5>

```java
public void mergeTable() throws FileNotFoundException {
    List<Person> people = SpecDataFactory.tableData();
    // 将数据按照性别分组 合并处理性别列 模拟sql分组 但不保证列表数据顺序
    Map<String, List<Person>> groupBySex = people.stream().collect(Collectors.groupingBy(Person::getSex));
    DocumentExporter.create()
        .headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
        // 需要指定表格行数及列数
        .table(people.size() + 2, 3, t -> {
            // 表头行列合并
            t.row(r -> r.cell(c -> c.boldText("姓名").rowspan(2)).cell(c -> c.boldText("其他信息").colspan(2)))
                // 合并行的数据需要补全
                .row(r -> r.headCells("姓名", "年龄", "性别"));
            groupBySex.forEach((key, value) -> {
                AtomicBoolean merged = new AtomicBoolean();
                int rowspan = value.size();
                t.rows(value, (it, r) ->
                       r.dataCells(it::getName, it::getAge)
                       .cell(c -> {
                           c.text(it::getSex);
                           // 行合并一次
                           if (!merged.get()) {
                               merged.set(true);
                               c.rowspan(rowspan);
                           }
                       })
                      );
            });
        })
        .writeTo(new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "merge-table.docx")));
}
```

</details>

<details>
<summary><b>5.简单页眉页脚</b></summary>
<h5>效果</h5>

<blockquote>
<p style="margin-bottom: 30px;"> 我是页眉</p>
<h1>标题一</h1>
<p>这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文</p>
<p style="margin-top: 100px;"> 我是页脚</p>
</blockquote>

<blockquote>说明：页眉页脚样式是左对齐，若有多页则每页均有页眉页脚。</blockquote>

<h5>代码</h5>

```java
public void simpleHeaderAndFooter() throws FileNotFoundException {
    DocumentExporter.create()
        .headingParagraph("标题一", ParagraphStyle.HEADING_1)
        .textParagraph("这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文")
        .header("我是页眉")
        .footer("我是页脚")
        .writeTo(
        new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "simple-header-foote1r.docx"))
    );
}
```

</details>

<details>
<summary><b>6.带页码的页脚</b></summary>
<h5>效果</h5>

<blockquote>
<p style="margin-bottom: 30px; text-align: center;"> 某公司职工信息</p>
<h6 style="text-align: center;">张三个人信息</h6>
<table>
	<thead>
		<tr>
			<th> <strong>姓名</strong> </th>
			<th> <strong>年龄</strong> </th>
			<th> <strong>性别</strong> </th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td> 张三 </td>
			<td> 26 </td>
			<td> 女 </td>
		</tr>
	</tbody>
</table>
<p>……分页符……（Word中的分页效果）</p>
<p style="margin-top: 100px;  text-align: center;"> 第1页/共5页</p>
<p style="margin-bottom: 30px; text-align: center;"> 某公司职工信息</p>
<h6 style="text-align: center;">李四个人信息</h6>
<table>
	<thead>
		<tr>
			<th> <strong>姓名</strong> </th>
			<th> <strong>年龄</strong> </th>
			<th> <strong>性别</strong> </th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td> 李四 </td>
			<td> 50 </td>
			<td> 男 </td>
		</tr>
	</tbody>
</table>
<p>……分页符……（Word中的分页效果）</p>
<p style="margin-top: 100px;  text-align: center;">第2页/共5页</p>
……这儿共 5 页
</blockquote>

<h5>代码</h5>

```java
public void complexHeaderAndFooter() throws FileNotFoundException {
	List<Person> people = SpecDataFactory.tableData();
	DocumentExporter.create()
		// 多个文档 自动添加分页符
		.documents(people, (it, d) ->
			// 分页文档
			d.headingParagraph(it.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
				.table(2, 3, t ->
					t.row(r -> r.headCells("姓名", "年龄", "性别"))
						.row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
				)
		)
		.header(HeaderFooterType.DEFAULT, h -> h.text("某公司职工信息"))
		.footer(HeaderFooterType.DEFAULT, f -> f.page("第", "页/共", "页"))
		.writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "complex-header-footer.docx")));
}
```

</details>

<details>
<summary><b>7.图片(正文图片、表格图片、页眉图片、页脚图片)</b></summary>
<h5>效果</h5>

<blockquote>
<p style="margin-bottom: 30px;"> 我是页眉图片
<img src="./src/test/resources/b.png" style="width: 30px; height: 30px; margin-left: 0px;" />
</p>
<h3 style="text-align: center;">教职工列表</h3>
	<table>
		<thead>
			<tr>
				<th><strong>姓名</strong></th>
				<th><strong>年龄</strong></th>
				<th><strong>性别</strong></th>
				<th><strong>图片</strong></th>
				<th><strong>文字图片</strong></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>张三</td>
				<td>26</td>
				<td>女</td>
				<td><img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
				<td>我是单元格图片 <br> <img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
			</tr>
			<tr>
				<td>李四</td>
				<td>50</td>
				<td>男</td>
				<td><img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
				<td>我是单元格图片 <br> <img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
			</tr>
			<tr>
				<td>王五</td>
				<td>18</td>
				<td>女</td>
				<td><img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
				<td>我是单元格图片 <br> <img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
			</tr>
			<tr>
				<td>赵六</td>
				<td>2</td>
				<td>女</td>
				<td><img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
				<td>我是单元格图片 <br> <img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
			</tr>
			<tr>
				<td>燕七</td>
				<td>80</td>
				<td>男</td>
				<td><img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
				<td>我是单元格图片 <br> <img src="./src/test/resources/c.gif" style="width: 50px; height: 50px;" /></td>
			</tr>
		</tbody>
	</table>
	<p>我是正文图片</p>
	<img src="./src/test/resources/c.gif" />
	<p style="margin-bottom: 30px; margin-top: 100px;"> 我是页脚图片
	<img src="./src/test/resources/b.png" style="width: 30px; height: 30px; margin-left: 0px;" />
	</p>
</blockquote>

<blockquote>说明：支持 gif、jpg、.png、.bmp图片格式，可在<b>段落</b>、<b>表格单元格</b>、<b>页眉</b>、<b>页脚</b>添加图片</blockquote>

<h5>代码</h5>

```java
public void picture() throws FileNotFoundException {
    List<Person> people = SpecDataFactory.tableData();
    DocumentExporter.create()
        .headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
        // 需要指定表格行数及列数
        .table(people.size() + 1, 5, t ->
               // 表头行会自动加粗
               t.row(r -> r.headCells("姓名", "年龄", "性别", "图片", "文字图片"))
               // 数据行正常
               .rows(people, (p, r) ->
                     r.dataCells(p::getName, p::getAge, p::getSex)
                     // 表格单元格内添加图片
                     .pictureCell(new File(FileUtil.brotherPath(DocumentExporterSpec.class, "c.gif")), 20, 20)
                     .cell(c ->
                           c.text("我是单元格图片")
                           .pictureParagraph(
                               new File(FileUtil.brotherPath(DocumentExporterSpec.class, "c.gif")),
                               20,
                               20
                           )
                          )
                    )
              )
        // 段落图片
        .textParagraph("我是正文图片")
        .pictureParagraph(new File(FileUtil.brotherPath(DocumentExporterSpec.class, "c.gif")), 400, 150)
        // 页眉图片
        .header(HeaderFooterType.DEFAULT, h ->
                h.textParagraph("我是页眉图片")
                .pictureParagraph(new File(FileUtil.brotherPath(DocumentExporterSpec.class, "b.png")), 20, 20)
               )
        // 页脚图片
        .footer(HeaderFooterType.DEFAULT, f ->
                f.textParagraph("我是页脚图片")
                .pictureParagraph(new File(FileUtil.brotherPath(DocumentExporterSpec.class, "b.png")), 20, 20)
               )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(DocumentExporterSpec.class, "picture.docx")));
}
```

</details>
