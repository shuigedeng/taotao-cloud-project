<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="css/login2.css">
<link href="css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery.min.js"></script>
<title>头部</title>
    <script type="text/javascript">
        $(document).ready(function(){
            $.ajax({
                url:"${pageContext.request.contextPath}/type?method=findAll",
                type:"GET",
                dataType:"json",
                success:function(data){
                    for(var i in data){
                        var a = $("<a href='${pageContext.request.contextPath}/product?method=show&tid="+data[i].tid+"'>"+data[i].tname+"</a>");
                        $("#goodsType").append(a);

                    }
                },
                error:function(){
                    alert("失败");
                }
            })
        })
    </script>
</head>
<body>
				
 <div id="top">
    	<div id="topdiv">
            <span>
                <a href="index.jsp" id="a_top" target="_blank">小米商城</a>
                <li>|</li>
                <a href="" id="a_top">小米商城移动版</a>
                <li>|</li>
                <a href="" id="a_top">问题反馈</a>
            </span>

            <span style="float:right">
           		<c:if test="${empty loginUser}">
                    <a href="login.jsp" id="a_top">登录</a>
                    <li>|</li>
                    <a href="register.jsp" id="a_top">注册</a>
                </c:if>
       			<c:if test="${not empty loginUser}">
                    <a href="address?method=show" id="a_top">${loginUser.username}</a>
                    <li>|</li>
                    <a href="user?method=logOut" id="a_top">注销</a>
                    <li>|</li>
                    <a href="order?method=show" id="a_top">我的订单</a>
                    <li>|</li>
                    <a href="userservlet?method=getAddress" id="a_top">地址管理</a>
                </c:if>
                <li>|</li>
                <a href="" id="a_top">消息通知</a>
                <a href="${pageContext.request.contextPath}/cart?method=show&uid=${loginUser.uid}" id="shorpcar">购物车</a>
            </span>
        </div>
 </div>
<div id="second">
	    <a href="" id="seimg" style=" margin-top:23px;"><img id="logo" src="image/logo_top.png" width="55" height="54"/></a>
        <a href="" id="seimg" style=" margin-top:17px;"><img id="gif" src="image/yyymix.gif" width="180" height="66" /></a>
        <p id="goodsType">
			<!-- 根据ajax 回调函数 填写数据 到此id中 -->

        </p>
       <form class="form-inline pull-right" style="margin-top: 40px;margin-right: 10px;">
		
		  <div class="form-group">
		    <input type="text" class="form-control" style="width: 400px"  placeholder="搜索一下好东西...">
		  </div>
		  <button type="submit" class="btn btn-warning"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;搜索</button>
	  </form>
</div>
</body>
</html>