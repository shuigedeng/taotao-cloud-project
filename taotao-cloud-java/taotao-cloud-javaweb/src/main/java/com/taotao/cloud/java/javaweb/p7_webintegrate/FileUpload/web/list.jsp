<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>文件下载页面</title>
</head>
<body>
    <table>
        <tr>
            <th>文件名</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${fileMap}" var="entry">
            <tr>
                <td>${entry.value}</td>
                <td><a href="${pageContext.request.contextPath}/downLoad?filename=${entry.key}">下载</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
