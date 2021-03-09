<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta charset="utf-8">
	    
		<title>在线支付</title>
		
</head>
<%@ include file="header.jsp" %>
<body>
	<div class="container">
		<div class="row">
			<div class="">
				<div class="panel panel-default"  style="margin: 0 auto;width: 95%;">
				  <div class="panel-heading">
				    <h3 class="panel-title"><span class="glyphicon glyphicon-yen"></span>&nbsp;&nbsp;在线支付
				    	<span class="pull-right"><a href="${pageContext.request.contextPath }/getOrderList">返回订单列表</a>
				    	</span>
				    </h3>
				  </div>
				  <div class="panel-body">	 
				  	<form  action="http://ceshi.qfjava.cn/payment/weixinpay" method="get">
					<table class="table table-bordered table-striped table-hover">
						<tr>
							<td colspan="1">订单号:</td>
							<td colspan="3"><input type="text" class="form-control" name="orderId" value="${param.oid}" readonly="readonly"></td>
						</tr>
						<tr>
							<td colspan="1">支付金额:</td>
							<td colspan="3">
								<div class="input-group" style="width: 200px;">
							      <input type="text" class="form-control"  name="price" value="${param.omoney}">
							      <div class="input-group-addon"><span class="glyphicon glyphicon-yen"></span></div>
						      	</div>
							</td>
						</tr>
						<tr>
							<td colspan="1">订单介绍:</td>
							<td colspan="3"><input type="text" class="form-control" name="body" value="xiaomi">
							<!-- 设置 程序回传到的链接地址 -->
							<input type="hidden" class="form-control" name="url" value="http://localhost:8080/order?method=success&oid=${param.oid}">
							</td>
						</tr>
						
					</table>
					<div class="pull-right" style="margin-right: 30px;">
						<input type="submit" value="确认支付" class="btn btn-warning btn-lg">
						
					</div>
					</form>
				 </div>
			   </div>
			</div>
			
		</div>
		
	</div>
	<!-- 底部 -->
   <%@ include file="footer.jsp"%>

	
</body>
</html>