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
<body>
<!-- 整个div会在页面显示时，以特定动画显示出来 -->
<div class="layui-anim layui-anim-up" style="height: 100px">aa</div>
<!-- 额外添加样式类：layui-anim-loop 使得动画循环运行 -->
<div class="layui-anim layui-anim-rotate layui-anim-loop"
     style="text-align:center;line-height: 100px;margin-left:50px;height: 100px;width:100px">bb</div>
</body>
</html>
