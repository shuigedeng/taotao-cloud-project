<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/11
  Time: 15:36
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
    <div class="layui-container">
        <div class="layui-row">
            <div class="layui-col-md9 layui-col-lg6 layui-bg-orange">
                你的内容 9/12
            </div>
            <div class="layui-col-md3 layui-col-lg6 layui-bg-gray">
                你的内容 3/12
            </div>
        </div>

        <%--移动设备、平板、桌面端的不同表现：--%>
        <div class="layui-row">
            <div class="layui-col-xs6 layui-col-sm6 layui-col-md4 layui-col-lg3">
                移动：6/12 | 平板：6/12 | 桌面：4/12;
            </div>
            <div class="layui-col-xs6 layui-col-sm6 layui-col-md4 layui-col-lg3">
                移动：6/12 | 平板：6/12 | 桌面：4/12;
            </div>
            <div class="layui-col-xs4 layui-col-sm12 layui-col-md4 layui-col-lg3">
                移动：4/12 | 平板：12/12 | 桌面：4/12;
            </div>
            <div class="layui-col-xs4 layui-col-sm7 layui-col-md8 layui-col-lg3">
                移动：4/12 | 平板：7/12 | 桌面：8/12;
            </div>
            <div class="layui-col-xs4 layui-col-sm5 layui-col-md4 layui-col-lg3">
                移动：4/12 | 平板：5/12 | 桌面：4/12;
            </div>
        </div>
    </div>
</body>
</html>
