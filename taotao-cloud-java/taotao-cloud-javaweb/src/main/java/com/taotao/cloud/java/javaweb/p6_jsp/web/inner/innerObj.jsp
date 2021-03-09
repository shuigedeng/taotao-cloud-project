<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!--四大作用域对象 -->
    <%
        pageContext.setAttribute("pageContextScope","aaa");
        String aaa = (String) pageContext.getAttribute("pageContextScope");
        String bbb = (String)request.getAttribute("requestScope");
        String ccc = (String)session.getAttribute("sessionScope");
        String ddd = (String)application.getAttribute("servletContextScope");
    %>
<h1>pageContextScope:<%=aaa%></h1>
<h1>requestScope:<%=bbb%></h1>
<h1>sessionScope:<%=ccc%></h1>
<h1>applicationScope:<%=ddd%></h1>
</body>
</html>
