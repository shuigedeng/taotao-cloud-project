<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        String newURL = response.encodeRedirectURL(request.getContextPath()+"/jstl/jstl1.jsp");
    %>
    <%=newURL%>
    <a href="<%=response.encodeRedirectURL(request.getContextPath()+"/jstl/jstl1.jsp")%>">跳转</a>
    <br/>
    <c:url context="${pageContext.request.contextPath}" value="/jstl/jstl1.jsp"></c:url>
    <a href="<c:url context='${pageContext.request.contextPath}' value='/jstl/jstl1.jsp'></c:url>">跳转2</a>
</body>
</html>
