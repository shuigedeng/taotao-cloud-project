<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <title>Hello, world!</title>
</head>
<body>

    <table width="500px" align="center" border="1px">
        <thead>
            <tr>
                <th>id</th>
                <th>username</th>
                <th>password</th>
                <th>gender</th>
                <th>registTime</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.gender}</td>
                    <td>
                        <fmt:formatDate value="${user.registTime}" pattern="yyyy-MM-dd"/>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
