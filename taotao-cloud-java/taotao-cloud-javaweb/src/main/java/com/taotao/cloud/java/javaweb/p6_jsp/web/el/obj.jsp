<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL内置对象</title>
    <link href="${pageContext.request.contextPath}/css/xxx.css">
</head>
<body>
    <%
        String path = request.getContextPath();
    %>
    <%=path%>
    <br/>
    <a href="<%=request.getContextPath()%>/manager/safe/xxxController">Click me</a><br/>
    <a href="${pageContext.request.contextPath}/manager/safe/xxxController">Click target</a>
</body>
</html>
