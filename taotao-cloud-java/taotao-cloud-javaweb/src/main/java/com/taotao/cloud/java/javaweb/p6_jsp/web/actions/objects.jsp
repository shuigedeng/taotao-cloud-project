<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>useBean</title>
</head>
<body>
<%--            id=对象的名字   class=全限定名--%>
    <jsp:useBean id="user" class="com.qf.entity.User"/>

    <jsp:setProperty name="user" property="username" value="tom"/>
    <jsp:setProperty name="user" property="password" value="123456"/>

    <jsp:getProperty name="user" property="username"/>
    <jsp:getProperty name="user" property="password"/>
</body>
</html>
