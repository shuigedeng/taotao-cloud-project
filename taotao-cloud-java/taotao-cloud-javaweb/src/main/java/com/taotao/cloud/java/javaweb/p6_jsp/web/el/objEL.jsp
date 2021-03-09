<%@ page import="com.qf.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL获取对象</title>
</head>
<body>
    <%
        User user = new User("gavin","123456");
        request.setAttribute("user",user);
    %>

    <%
        User u = (User)request.getAttribute("user");
//        out.println(u.getUsername());
//        out.println(u.getPassword());
    %>
    <hr/>
    ${user}<br/>
    ${user.username}<br/>
    ${user.password}<br/>
</body>
</html>
