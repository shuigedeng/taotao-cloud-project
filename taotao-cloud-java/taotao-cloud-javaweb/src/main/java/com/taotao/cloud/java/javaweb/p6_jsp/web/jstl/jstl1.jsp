<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.qf.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        request.setAttribute("username","tom123");
        request.setAttribute("age",58);
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        request.setAttribute("list",list);
        List<User> users = new ArrayList<>();
        users.add(new User("tom","123456"));
        users.add(new User("marry","123456"));
        users.add(new User("jack","123456"));
        users.add(new User("gavin","123456"));
        request.setAttribute("users",users);
    %>
    ${username}
    <c:if test="${username eq 'tom'}">
        <h1>欢迎您，${username}</h1>
    </c:if>
    <c:if test="${username ne 'tom'}">
        <h1>请您重新登录！</h1>
    </c:if>
    <hr/>
    <c:choose>
        <c:when test="${age < 18}" ><h1>少年</h1></c:when>
        <c:when test="${age>=18 && age<30}"><h1>青年</h1></c:when>
        <c:when test="${age >=30 && age < 50}"><h1>中年</h1></c:when>
        <c:otherwise><h1>老年</h1></c:otherwise>
    </c:choose>
    <c:forEach var="s" items="${list}" begin="0" end="4" step="1" varStatus="i">
        <h1>${s}&nbsp;&nbsp;${i.first}&nbsp;&nbsp;${i.last}&nbsp;&nbsp;${i.count}&nbsp;&nbsp;${i.index}</h1>
    </c:forEach>
        <hr/>
    <%
        for(String s : list){
            out.println(s);
        }
    %>
    <hr/>
    <c:forEach var="user" items="${users}">
        <h1>${user.username}:${user.password}</h1>
    </c:forEach>
</body>
</html>
