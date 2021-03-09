<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/2
  Time: 22:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/js/jquery-2.1.0.js"></script>
</head>
<body>

    <input type="button" value="queryALL" onclick="queryAll();">
    <input type="button" value="queryOne" onclick="queryOne();">
    <input type="button" value="saveUser" onclick="saveUser();">
    <input type="button" value="updateUser" onclick="updateUser();">
    <input type="button" value="deleteUser" onclick="deleteUser();">
    <script>
        function queryAll(){
            $.ajax({
                type:"get",
                url:"${pageContext.request.contextPath}/users",
                success:function(ret){
                    console.log("查询所有：");
                    console.log(ret);
                }
            });
        }
        function queryOne(){
            $.ajax({
                type:"get",
                url:"${pageContext.request.contextPath}/users/100",
                success:function(ret){
                    console.log("查询单个用户：");
                    console.log(ret);
                }
            });
        }
        function saveUser(){
            var user = {name:"shine",birth:"2020-12-12 12:12:20"};
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/users",
                data:JSON.stringify(user),
                contentType:"application/json",
                success:function(ret){
                    console.log("增加用户：");
                    console.log(ret);
                }
            });
        }
        function updateUser(){
            var user = {id:1,name:"shine2",birth:"2020-12-13 12:12:20"};
            $.ajax({
                type:"put",
                url:"${pageContext.request.contextPath}/users",
                data:JSON.stringify(user),
                contentType:"application/json",
                success:function(ret){
                    console.log("更新用户：");
                    console.log(ret);
                }
            });
        }

        function deleteUser(){
            $.ajax({
                type:"get",
                url:"${pageContext.request.contextPath}/users/200",
                success:function(ret){
                    console.log("删除用户：");
                    console.log(ret);
                }
            });
        }
    </script>
</body>
</html>
