[Pdf导出测试用例](./src/test/java/cn/wisewe/docx4j/output/builder/portable/PortableExporterSpec.java)

<details>
<summary><b>1.标题正文</b></summary>

<h5>效果</h5>

<blockquote>
<h1>标题一</h1>
<h2>标题二</h2>
<h3>标题三</h3>
<h4>标题五</h4>
<h5>标题七</h5>
<h6>标题九</h6>
<p>这是正文这是正文这是正文这是正文这是正文这是正文这是正第一行</p>
<p>这是正文这是正文这是正文这是正文这是正文这是正文这是第二行</p>
<img src="./src/test/resources/b.png" style="width: 200px; height: 200px;" />
<br>
<img src="./src/test/resources/c.gif" style="width: 100px; height: 100px;" />
<br>此处有还有几张其他格式的图片省略（常见图片格式均支持）
</blockquote>


<blockquote>
说明：支持以上标题样式直接设定，其他标题样式可通过style方法自定义样式设定。
</blockquote>

```java
public void simple() throws FileNotFoundException {
    PortableExporter.fastCreate()
        .headingParagraph("标题一", Fonts.HEADING_1)
        .headingParagraph("标题二", Fonts.HEADING_2)
        .headingParagraph("标题三", Fonts.HEADING_3)
        .headingParagraph("标题五", Fonts.HEADING_5)
        .headingParagraph("标题七", Fonts.HEADING_7)
        .headingParagraph("标题九", Fonts.HEADING_9)
        .textParagraph("这是正文这是正文这是正文这是正文这是正文这是正文这是正第一行")
        .paragraph(p -> p.chunk("这是正文这是正文这是正文这是正文这是正文这是正文这是第二行", Fonts.HEADER_FOOTER.font()))
        .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/a.jpeg")), 200)
        .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/a.jpg")), 100)
        .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/b.png")))
        .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/c.gif")))
        .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/d.bmp")))
        .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "simple.pdf")));
}
```

</details>

<details>
<summary><b>2.动态分页</b></summary>
<h5>效果</h5>
<blockquote>
 <h6>张三个人信息</h6>
 <p>
姓名：张三<br>
年龄：26<br>
性别：女
</p>
 ……分页符……(分页的效果)
 <h6 >李四个人信息</h6>
  <p>
姓名：李四<br>
年龄：50<br>
性别：男
</p>
 ……分页符……(分页的效果)
 ……若干页
</blockquote>

<blockquote>
说明：添加多个文档时，每个文档之间自动添加分页符。
</blockquote>


<h5>代码</h5>

```java
public void breakPage() throws FileNotFoundException {
    PortableExporter.fastCreate()
        // 多个文档 自动添加分页符
        .documents(SpecDataFactory.tableData(), (it, d) ->
            // 分页文档
            d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                .textParagraph(String.format("姓名:%s", it.getName()))
                .textParagraph(String.format("年龄:%s", it.getAge()))
                .textParagraph(String.format("性别:%s", it.getSex()))
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "break-page.pdf")));
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
    PortableExporter.fastCreate()
        .paragraph(p ->
            p.chunk("教职工列表", Fonts.HEADING_1.font()).more(pp -> pp.setAlignment(Element.ALIGN_CENTER))
        )
        // 需要指定表格列数
        .table(3, t ->
            // 表头
            t.row(r -> r.headCells("姓名", "年龄", "性别"))
                // 数据单元格
                .rows(SpecDataFactory.tableData(), (u, r) -> r.dataCells(u::getName, u::getAge, u::getSex))
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "table.pdf")));
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
    PortableExporter.fastCreate()
        .paragraph(p ->
            p.chunk("教职工列表", Fonts.HEADING_1.font()).more(pp -> pp.setAlignment(Element.ALIGN_CENTER))
        )
        // 需要指定表格行数及列数
        .table(3, t -> {
            // 表头行列合并 从左往右从上之下渲染 合并的单元格只渲染一次
            t.row(r -> r.headCell("姓名", c -> c.rowspan(2)).headCell("其他信息", c -> c.colspan(2)))
                .row(r -> r.headCells("年龄", "性别"));
            // 数据合并
            groupBySex.forEach((key, value) -> {
                AtomicBoolean merged = new AtomicBoolean();
                int rowspan = value.size();
                t.rows(value, (it, r) -> {
                    // 前两列数据
                    r.dataCells(it::getName, it::getAge);
                    // 行合并一次 单元格也只添加一次
                    if (!merged.get()) {
                        merged.set(true);
                        r.dataCell(it::getSex, c -> c.rowspan(rowspan));
                    }
                });
            });
        })
        .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "merge-table.pdf")));
}
```

</details>

<details>
<summary><b>5.页眉页脚页码</b></summary>
<h5>效果</h5>

<blockquote>
<p style="margin-bottom: 30px; text-align: center;"> 成都中教智汇</p>
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
<p>……分页符……（分页的效果）</p>
<p style="margin-top: 100px;  text-align: center;"> 第1页/共5页</p>
<p style="margin-bottom: 30px; text-align: center;"> 成都中教智汇</p>
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
<p>……分页符……（分页的效果）</p>
<p style="margin-top: 100px;  text-align: center;">第2页/共5页</p>
……这儿共 5 页
</blockquote>

<h5>代码</h5>

```java
public void headerAndFooter() throws FileNotFoundException {
    PortableExporter.create()
        // 页眉事件
        .event(new DefaultTextHeaderHandler("成都中教智汇"))
        // 页脚事件
        .event(new DefaultPageFooterHandler("第", "页/共", "页"))
        // 事件必须在open之间设置
        .open()
        // 多个文档 自动添加分页符
        .documents(SpecDataFactory.tableData(), (it, d) ->
            // 分页文档
            d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                .table(3, t ->
                    t.row(r -> r.headCells("姓名", "年龄", "性别"))
                        .row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
                )
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "header-footer.pdf")));
}
```

</details>

<details>
<summary><b>6.文字水印</b></summary>
<h5>效果</h5>

<blockquote>
备注：所有页都有旋转45°的“成都中教智汇”文字水印
<p style="margin-bottom: 30px; text-align: center;"> 成都中教智汇</p>
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
<p>……分页符……（分页的效果）</p>
<p style="margin-top: 100px;  text-align: center;"> 第1页/共5页</p>
<p style="margin-bottom: 30px; text-align: center;"> 成都中教智汇</p>
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
<p>……分页符……（分页的效果）</p>
<p style="margin-top: 100px;  text-align: center;">第2页/共5页</p>
……这儿共 5 页
</blockquote>

<h5>代码</h5>

```java
public void watermark() throws FileNotFoundException {
    PortableExporter.create()
        // 页眉事件
        .event(new DefaultTextHeaderHandler("成都中教智汇"))
        // 页脚事件
        .event(new DefaultPageFooterHandler("第", "页/共", "页"))
        .event(new DefaultTextWatermarkHandler("成都中教智汇", 28))
        // 事件必须在open之前设置
        .open()
        // 多个文档 自动添加分页符
        .documents(SpecDataFactory.tableData(), (it, d) ->
            // 分页文档
            d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                .table(3, t ->
                    t.row(r -> r.headCells("姓名", "年龄", "性别"))
                        .row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
                )
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "watermark.pdf")));
}
```

</details>

<details>
<summary><b>7.图片(正文图片、表格图片、水印图片)</b></summary>
<h5>效果</h5>

<blockquote>
备注：所有页都有旋转45°的图片水印
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
</blockquote>

<h5>代码</h5>

```java
public void picture() throws FileNotFoundException {
    PortableExporter.create()
        // 水印图片
        .event(new DefaultPictureWatermarkHandler(new File(FileUtil.rootPath(this.getClass(), "/b.png")), 50))
        // 事件必须在open之前设置
        .open()
        .headingParagraph("教职工列表", Fonts.HEADING_3)
        // 需要指定表格行数及列数
        .table(5, t ->
            // 表头行会自动加粗
            t.row(r -> r.headCells("姓名", "年龄", "性别", "图片", "文字图片"))
                .rows(SpecDataFactory.tableData(), (p, r) ->
                    r.dataCells(p::getName, p::getAge, p::getSex)
                        // 图片单元格
                        .cell(c -> c.pictureParagraph(p.picture(), 20))
                        // 文字及图片
                        .cell(c -> c.textParagraph("我是单元格图片").pictureParagraph(p.picture(), 20))
                )
        )
        .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "picture.pdf")));
}
```

</details>