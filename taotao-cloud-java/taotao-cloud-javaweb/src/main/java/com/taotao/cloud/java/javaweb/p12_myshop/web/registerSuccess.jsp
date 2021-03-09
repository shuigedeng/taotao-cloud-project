<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<script type="text/javascript" src="js/jquery.min.js"></script>
		
<link rel="stylesheet" type="text/css" href="css/login.css">

<title>注册成功</title>
</head>
<body>
	<div class="regist">
		<div class="regist_center">
			<div class="regist_top">
				<div class="left fl"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;会员注册</div>
				<div class="right fr">
					<a href="index.jsp" target="_black">小米商城</a>
				</div>
				<div class="clear"></div>
				<div class="xian center"></div>
			</div>
			<div  style="margin-top: 80px;margin-left: 100px;">
					
					<p class="text-info" style="font-size: 20px;">注册成功 别忘记激活!</p>
					
					<a class="btn btn-danger" href="http://www.hao123.com/mail" target="_blank">现在激活</a>
					<a class="btn btn-default" href="${pageContext.request.contextPath }/index.jsp" target="_blank">返回主页</a>
					
			</div>
		</div>
	</div>
	
</body>
</html>