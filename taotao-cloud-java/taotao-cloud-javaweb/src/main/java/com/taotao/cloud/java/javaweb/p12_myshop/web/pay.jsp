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
				  	<form  action="${pageContext.request.contextPath}/payServlet" method="post">
					<table class="table table-bordered table-striped table-hover">
						<tr>
							<td colspan="1">订单号:</td>
							<td colspan="3"><input type="text" class="form-control" name="orderid" value="<%=request.getParameter("oid")%>" readonly="readonly"></td>
						</tr>
						<tr>
							<td colspan="1">支付金额:</td>
							<td colspan="3">
								<div class="input-group" style="width: 200px;">
							      <input type="text" class="form-control"  name="money" value="0.01">
							      <div class="input-group-addon"><span class="glyphicon glyphicon-yen"></span></div>
						      	</div>
							</td>
						</tr>
						<tr><td colspan="4" class="alert-danger"><strong>请您选择在线支付银行</strong></td></tr>
						<tr>
							<td><input type="radio" name="pd_FrpId" value="CMBCHINA-NET-B2C"> <img src="yh/bankcmb.gif" alt="招商银行" title="招商银行"></td>
							<td><input type="radio" name="pd_FrpId" value="ICBC-NET-B2C"> <img src="yh/bankicbc.gif" alt="工商银行" title="工商银行"></td>
							<td><input type="radio" name="pd_FrpId" value="ABC-NET-B2C"> <img src="yh/bankabc.gif" alt="农业银行" title="农业银行"></td>
							<td><input type="radio" name="pd_FrpId" value="CCB-NET-B2C"> <img src="yh/bankccb.gif" alt="建设银行" title="建设银行"></td>
						</tr>
						<tr>
							<td><input type="radio" name="pd_FrpId" value="CMBC-NET-B2C"> <img src="yh/bankcmbc.gif" alt="中国民生银行" title="中国民生银行"></td>
							<td><input type="radio" name="pd_FrpId" value="CEB-NET-B2C" > <img src="yh/guangda.bmp" alt="光大银行" title="光大银行"></td>
							<td><input type="radio" name="pd_FrpId" value="BOCO-NET-B2C"> <img src="yh/bankbcc.gif" alt="交通银行" title="交通银行"></td>
							<td><input type="radio" name="pd_FrpId" value="SDB-NET-B2C"> <img src="yh/banksdb.gif" alt="深圳发展银行" title="深圳发展银行"></td>
						</tr>
						<tr>
							<td><input type="radio" name="pd_FrpId" value="BCCB-NET-B2C"> <img src="yh/bankbj.gif" alt="北京银行" title="北京银行"></td>
							<td><input type="radio" name="pd_FrpId" value="CIB-NET-B2C"> <img src="yh/bankcib.gif" alt="兴业银行" title="兴业银行 "></td>
							<td><input type="radio" name="pd_FrpId" value="SPDB-NET-B2C"> <img src="yh/bankshpd.gif" alt="上海浦东发展银行" title="上海浦东发展银行 "></td>
							<td><input type="radio" name="pd_FrpId" value="BOC-NET-B2C"> <img src="yh/bankbc.gif" alt="中国银行" title="中国银行 "></td>
						</tr>
						
					</table>
					<div class="pull-right" style="margin-right: 30px;">
						<input type="submit" value="确认提交" class="btn btn-warning btn-lg">
						
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