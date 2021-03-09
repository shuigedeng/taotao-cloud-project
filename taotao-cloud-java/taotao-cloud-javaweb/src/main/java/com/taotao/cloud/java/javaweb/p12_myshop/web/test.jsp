<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>xxxx</title>

    <link href="css/bootstrap.min.css" rel="stylesheet">
	    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="js/jquery.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="js/bootstrap.min.js"></script> 
  </head>
  <body>
  
 <script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
			url:"${pageContext.request.contextPath}/goodstypeservlet?method=goodstypelist",
			type:"GET",
			success:function(data){
				for(var i in data){
					var a = $("<a href='${pageContext.request.contextPath}/goodsservlet?method=getGoodsListByTypeId&typeId="+data[i].id+"'>"+data[i].name+"</a>");
					$("#goodsType").append(a);
					alert("---");
				}
			},
			dataType:"json",
			error:function(){
				alert("失败");
			}
		})
	}) 

</script>
  
    <h1>你好，世界！</h1>
    
    <div id="goodsType"></div>

	<a href="#">xxx</a>
	<a href="#">bbb</a>
	<a href="#">ddd</a>
	<a href="#">ccc</a>

  </body>
</html>