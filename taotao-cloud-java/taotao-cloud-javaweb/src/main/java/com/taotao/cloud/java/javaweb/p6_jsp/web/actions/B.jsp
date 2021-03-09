<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>B页面</title>
</head>
<body>
    <h1>B页面</h1>
    <%
        String name = request.getParameter("name");
    %>
<%=name%>
</body>
</html>
