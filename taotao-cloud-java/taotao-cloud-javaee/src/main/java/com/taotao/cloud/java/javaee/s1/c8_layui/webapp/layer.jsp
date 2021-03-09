<%--
  Created by IntelliJ IDEA.
  User: zhj
  Date: 2020/5/11
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/layui/css/layui.css">
    <script src="${pageContext.request.contextPath}/layui/layui.js"></script>
</head>
<body>

<script>
    // 导入 layer模块
    layui.use(["layer"],function(){
        var layer = layui.layer;
        //layer.msg("hello world!!");
        /*layer.msg("确定吗？",{btn:["确定！","放弃！"],
            yes:function(i){layer.close(i);layer.msg("yes!!!")},
            btn2:function(i){layer.close(i);layer.msg("no!!!")}}
        );*/
        //0-6 7种图标  0:warning  1:success  2:error  3:question  4:lock  5:哭脸  6：笑脸
        /*layer.alert("alert弹框蓝",
            {title:'alert',icon:6 },
            function(){//点击“确定”按钮时的回调
                layer.msg("好滴");
            }
        );*/
        /*layer.confirm("你确定要删除吗?",
            {shade:false,icon:3,btn:["好滴","不行"]},
            function(){layer.msg("好滴！");},
            function(){layer.msg("不行！")}
        );*/
        /*layer.open({
            type:1,// 消息框，没有确定按钮
            title:["hello","padding-left:5px"], // 标题，及标题样式
            content:layui.$("#testmain"), // dom格式
            offset:'rb',//可以在右下角显示
            shade:false //是否遮罩
        });*/

        layer.open({
            type:2,// iframe加载，需要一个url
            content:"${pageContext.request.contextPath}/nav.jsp"
        });
    });
</script>
<div id="testmain" style="display:none;padding:10px; height: 173px; width: 275px;">
    hello world!
</div>
<button type="button">click</button>
</body>
</html>
