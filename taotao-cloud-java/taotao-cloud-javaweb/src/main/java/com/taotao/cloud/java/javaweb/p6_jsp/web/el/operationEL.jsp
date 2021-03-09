<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EL表达式</title>
</head>
<body>
    <%
        request.setAttribute("nums",1234);
        request.setAttribute("ss","b");
    %>
        <h1>empty运算符</h1>
        <h1>${empty ss}</h1>
    <hr/>
    <h1>算术运算符</h1>
    <h1>${nums + 5 } </h1>
    <h1>${nums - 5 } </h1>
    <h1>${nums * 5 } </h1>
    <h1>${nums div 5 } </h1>
    <h1>${nums mod 5 } </h1>
    <hr/>
    <h1>关系运算符</h1>
    <h1>${nums eq 1234}</h1>
    <h1>${nums ne 1234}</h1>
    <h1>${nums gt 1234}</h1>
    <h1>${nums lt 1234}</h1>
    <h1>${nums ge 1234}</h1>
    <h1>${nums le 1234}</h1>
    <h1>逻辑运算符</h1>
    <h1>${nums > 1000 and nums !=1200}</h1>
    <h1>${nums > 1000 or nums == 2000}</h1>
    <h1>${not(num > 1234)}</h1>
</body>
</html>
