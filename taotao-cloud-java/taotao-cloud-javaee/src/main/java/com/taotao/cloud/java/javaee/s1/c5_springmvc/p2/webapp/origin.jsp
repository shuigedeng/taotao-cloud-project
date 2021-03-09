<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/3
  Time: 9:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/js/jquery-2.1.0.js"></script>
</head>
<body>


    <input type="button" value="ajax" onclick="cross_origin();">
    <input type="button" value="ajax2" onclick="cross_origin2();">
    <script>

        function cross_origin(){
            $.ajax({
                type:"get",
                url:"http://localhost:9999/origin/test1",
                xhrFields: {
                    // 跨域携带cookie
                    withCredentials: true
                },
                success:function(ret){
                    console.log("ret:"+ret);
                }
            });
        }

        function cross_origin2(){
            $.ajax({
                type:"get",
                url:"http://localhost:9999/origin/test2",
                xhrFields: {
                    // 跨域携带cookie
                    withCredentials: true
                },
                success:function(ret){
                    console.log("ret:"+ret);
                }
            });
        }
    </script>
</body>
</html>
