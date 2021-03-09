<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>A页面</title>
</head>
<body>
<%--    转发--%>
<jsp:forward page="B.jsp">
    <jsp:param name="name" value="gavin"/>
</jsp:forward>
</body>
</html>
