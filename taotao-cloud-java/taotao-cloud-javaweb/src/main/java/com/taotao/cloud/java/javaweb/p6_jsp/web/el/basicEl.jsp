<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式</title>
</head>
<body>
    <%
        request.setAttribute("key1","value1");
        session.setAttribute("key2","value2");
        application.setAttribute("key3","value3");

        request.setAttribute("key666","value6");
        session.setAttribute("key666","value7");
        application.setAttribute("key666","value8");
    %>
    <h1>通过作用域对象获取：</h1>
<!--通过作用域对象获取数据 -->
    <h1><%=request.getAttribute("key8")%></h1>
    <h1><%=session.getAttribute("key2")%></h1>
    <h1><%=application.getAttribute("key3")%></h1>

    <hr/>
    <!--通过EL表达式获取数据 -->
    <h1>${requestScope.key8}</h1>
    <h1>${sessionScope.key666}</h1>
    <h1>${applicationScope.key666}</h1>

    <hr/>
    <h1>${key666}</h1>
    <h1>${key666}</h1>
    <h1>${key666}</h1>
</body>
</html>
