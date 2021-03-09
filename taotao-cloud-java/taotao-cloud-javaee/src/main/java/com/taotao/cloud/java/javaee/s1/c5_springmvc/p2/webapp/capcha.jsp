<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/2
  Time: 17:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <form action="${pageContext.request.contextPath}/captcha/test1">
        <img id="cap" src="${pageContext.request.contextPath}/captcha" style="width:100px" onclick="refresh();">
        <input type="text" name="captcha"/>
        <br>
        <input type="submit" value="提交">
    </form>

    <script>
        function refresh(){
            var img = document.getElementById("cap");
            img.src="${pageContext.request.contextPath}/captcha?"+new Date().getTime()
        }
    </script>
</body>
</html>
