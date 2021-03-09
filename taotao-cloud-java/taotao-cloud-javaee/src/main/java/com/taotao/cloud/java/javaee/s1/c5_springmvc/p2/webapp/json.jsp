<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/1
  Time: 15:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/js/jquery-2.1.0.js"></script>
</head>
<body>

    <input type="button" value="ajax" onclick="send_json();">
    <script>
        function send_json(){
            // ajax, json
            var user = {id:1,name:"shine"};
            var userJson = JSON.stringify(user);
            $.ajax({
                url:"${pageContext.request.contextPath}/json/test4",
                type:"post",
                data:userJson,
                contentType:"application/json",
                success:function(ret){
                    alert(ret);
                }
            })
        }
    </script>
</body>
</html>
