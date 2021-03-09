<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/4/21
  Time: 9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%-- 显示分页用户数据 --%>
    <table width="500px" align="center" border="1px" cellspacing="0">
        <tr>
            <th>id</th>
            <th>username</th>
            <th>password</th>
            <th>gender</th>
            <th>registTime</th>
        </tr>
        <c:forEach items="${requestScope.pageData.list}" var="user">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.password}</td>
                <td>${user.gender}</td>
                <td>${user.registTime}</td>
            </tr>
        </c:forEach>
    </table>
    <div style="text-align: center">
        <a href="${pageContext.request.contextPath}/users?pageNum=1&pageSize=2">首页</a>
        <c:if test="${requestScope.pageData.hasPreviousPage}">
            <a href="${pageContext.request.contextPath}/users?pageNum=${requestScope.pageData.prePage}&pageSize=2">上一页</a>
        </c:if>

        <c:forEach begin="1" end="${requestScope.pageData.pages}" var="i">
            <a href="${pageContext.request.contextPath}/users?pageNum=${i}&pageSize=2">
                <c:if test="${requestScope.pageData.pageNum==i}">
                    <span style="color:red">${i}</span>
                </c:if>
                <c:if test="${requestScope.pageData.pageNum!=i}">
                    <span>${i}</span>
                </c:if>
            </a>
        </c:forEach>
        <c:if test="${requestScope.pageData.hasNextPage}">
            <a href="${pageContext.request.contextPath}/users?pageNum=${requestScope.pageData.nextPage}&pageSize=2">下一页</a>
        </c:if>
        <a href="${pageContext.request.contextPath}/users?pageNum=${requestScope.pageData.pages}&pageSize=2">末页</a>
    </div>
</body>
</html>
