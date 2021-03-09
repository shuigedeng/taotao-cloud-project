<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>获取Cookie的EL</title>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    String username = "";
    String password = "";
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                username = cookie.getValue();
            }
            if(cookie.getName().equals("password")){
                password = cookie.getValue();
            }
        }
    }
%>
<%=username%><br/>
<%=password%>
    <hr/>
    <h1>${cookie.username.value}</h1>
    <h1>${cookie.password.value}</h1>
    <input type="text" name="username" value="<%=username%>"><br/>
    <input type="text" name="password" value="<%=password%>"><br/>
    <input type="text" name="username" value="${cookie.username.value}"><br/>
    <input type="text" name="username" value="${cookie.password.value}"><br/>
</body>
 </html>
