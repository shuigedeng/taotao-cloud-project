<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/2
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <form action="${pageContext.request.contextPath}/upload/test1" method="post" enctype="multipart/form-data">
        file: <input type="file" name="source"> <br>
        <input type="submit" value="上传">
    </form>

</body>
</html>
