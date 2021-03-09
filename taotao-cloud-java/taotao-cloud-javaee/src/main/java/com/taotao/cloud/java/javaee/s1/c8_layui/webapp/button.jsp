<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/11
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/layui/css/layui.css">
    <script src="${pageContext.request.contextPath}/layui/layui.js"></script>
</head>
<body style="padding: 10px">
    <button type="button" class="layui-btn">标准的按钮</button>
    <a href="http://www.layui.com" class="layui-btn">可跳转的按钮</a>
    <a href="http://www.layui.com" class="layui-btn layui-btn-primary">主题的按钮</a>
    <a href="http://www.layui.com" class="layui-btn layui-btn-primary layui-btn-sm">主题的按钮</a>
    <a href="http://www.layui.com" class="layui-btn layui-btn-primary layui-btn-radius ">圆角的按钮</a>
    <a href="http://www.layui.com" class="layui-btn layui-btn-primary layui-btn-sm  layui-btn-radius ">
        <i class="layui-icon layui-icon-heart-fill" style="font-size: 30px; color: #1E9FFF;"></i>
        带图标的按钮
    </a>
</body>
</html>
