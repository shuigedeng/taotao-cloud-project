<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
 <meta name="viewport" content="width=device-width, initial-scale=1">
<title>订单成功</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/login2.css">
<script type="text/javascript" src="js/jquery.min.js"></script>

</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
<hr>
	<div class="row" style="width: 30%;margin: 0 auto;padding-top: 20px">
		<div class="panel panel-success">
			<div class="panel-heading">
			    <h3 class="panel-title">订单提示</h3>
			</div>
			<div class="panel-body">
			    <h3 class="text-default"><span class="glyphicon glyphicon-ok-sign"></span>&nbsp;&nbsp;&nbsp;&nbsp;订单添加成功!!</h3>
				<hr>
				<h4>订单号:${order.id }</h4>
				<h4>订单总金额:${order.money }</h4>
				<span class="h2">立即支付</span>
				<a href="${pageContext.request.contextPath}/pay.jsp?oid=${order.id }&omoney=${order.money }" class="btn btn-warning">易宝支付</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="${pageContext.request.contextPath}/payWeixin.jsp?oid=${order.id }" class="btn btn-success">微信支付</a>
			</div>
		</div>
		
	</div>
	
</div>

<%@ include file="footer.jsp" %>
</body>
</html>