### 目录介绍

```
docsify-plus					#项目
|__ docs						#文档目录 可选
   |__ contents					#文档文件夹 可选
      |__ development.md		#快速开始	 页面文件
      |__ guide.md				#指南		  页面文件
   |__ zh-en					#不同语言目录 可选
      |__ _coverpage.md			#当前语言封面
      |__ _navbar.md			#当前语言导航页面
      |__ _sidebar.md			#当前语言菜单页面
      |__ README.md				#当前语言首页面
   |__ 404.md					#错误页面 可选
   |__ _coverpage.md			#默认语言封面 可选
   |__ _navbar.md				#默认语言导航 可选
   |__ _sidebar.md				#默认语言菜单 可选
   |__ index.html				#程序入口 必选 可配置
   |__ README.md				#默认语言首页 可选
|__ .nojekyll					#git配置
|__ LICENSE						#git配置
|__ README.md					#git说明页面
```



### 文件路径

你可以简单地在你的 docsify 目录中创建更多的 markdown 文件，你可以在菜单<font color="goldenrod">_sidebar.md</font>添加目录，系统菜单是以 <font color="red">相对路径 </font>访问页面

```
* [介绍](README.md)
* [快速开始](contents/development)
* [指南](contents/guide)
```

### 从侧边栏选择设置页面标题

页面的`title`标签是从*选定*的侧边栏项目名称生成的。为了更好的显示，您可以通过在文件名后指定一个字符串来自定义标题。

```
* [指南](contents/guide "文档指南")
```

