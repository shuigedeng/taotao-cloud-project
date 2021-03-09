<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <%--<form action="${pageContext.request.contextPath}/param/test3">
        id: <input type="text" name="id"> <br>
        name:<input type="text" name="name"><br>
        gender:<input type="text" name="gender"><br>
        birth:<input type="text" name="birth"><br>
        <input type="checkbox" name="hobby" value="football"> 足球
        <input type="checkbox" name="hobby" value="basketball"> 篮球
        <input type="checkbox" name="hobby" value="volleyball"> 排球 <br>
        <input type="submit" value="提交">
    </form>--%>

    <form action="${pageContext.request.contextPath}/param/test4" method="post">
        id: <input type="text" name="users[0].id"> <br>
        name:<input type="text" name="users[0].name"><br>
        gender:<input type="text" name="users[0].gender"><br>
        <hr>
        id: <input type="text" name="users[1].id"> <br>
        name:<input type="text" name="users[1].name"><br>
        gender:<input type="text" name="users[1].gender"><br>

        <input type="submit" value="提交">
    </form>
</body>
</html>
