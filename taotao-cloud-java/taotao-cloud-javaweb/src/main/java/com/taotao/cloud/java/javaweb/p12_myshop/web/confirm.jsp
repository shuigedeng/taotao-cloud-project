<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>在线支付   连接易付宝</title>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="panel panel-default"  style="margin: 0 auto;width: 95%;">
	<div class="panel-heading">
		<h3 class="panel-title"><span class="glyphicon glyphicon-yen"></span>&nbsp;&nbsp;在线支付
			<span class="pull-right"><a href="${pageContext.request.contextPath }/getOrderList">返回订单列表</a></span>
		</h3>
	</div>
	 <div class="panel-body">	
	 	<form action="https://www.yeepay.com/app-merchant-proxy/node" method="post">
			<h3>订单号：${p2_Order}</h3>
			<p class="text-danger"><font size="20px"><strong>付款金额 ：${p3_Amt}</strong></font></p>
			<hr>
			<input type="hidden" name="pd_FrpId" value="${pd_FrpId }" />
			<input type="hidden" name="p0_Cmd" value="${p0_Cmd }" />
			<input type="hidden" name="p1_MerId" value="${p1_MerId }" />
			<input type="hidden" name="p2_Order" value="${p2_Order }" />
			<input type="hidden" name="p3_Amt" value="${p3_Amt }" />
			<input type="hidden" name="p4_Cur" value="${p4_Cur }" />
			<input type="hidden" name="p5_Pid" value="${p5_Pid }" />
			<input type="hidden" name="p6_Pcat" value="${p6_Pcat }" />
			<input type="hidden" name="p7_Pdesc" value="${p7_Pdesc }" />
			<input type="hidden" name="p8_Url" value="${p8_Url }" />
			<input type="hidden" name="p9_SAF" value="${p9_SAF }" />
			<input type="hidden" name="pa_MP" value="${pa_MP }" />
			<input type="hidden" name="pr_NeedResponse" value="${pr_NeedResponse }" />
			<input type="hidden" name="hmac" value="${hmac }" />
			<input type="submit" value="确认支付" class="btn btn-warning btn-lg"/>
		</form>
	 </div>
</div>

<!-- 底部 -->
   <%@ include file="footer.jsp"%>


</body>
</html>