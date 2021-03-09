<%@ page contentType="text/html;charset=GBK" pageEncoding="UTF-8" language="java" errorPage="error.jsp" session="false" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Scanner" %>
<html>
<head>
    <title>脚本的使用</title>
</head>
<body>
    <%=10/0%>
    <%
        Scanner sc = new Scanner(System.in);
        System.out.println("a");
        int a = 10;
        System.out.println(a);//打印在控制台
        out.println(a);//打印在客户端页面

    %>
    <%!
        int b = 20;
        public void play(){
            System.out.println("play...");
        }
        public int m1(){
            return 100;
        }
    %>
    <%--JSP注释 --%>
    <!-- HTML注释！-->
    <%
        out.println(b);
        play();//调用方法
        int result = m1();
        out.println(m1());
    %>
    <%=m1()%>
    <%="今天天气好"%>
<%=900%>
<%=new Date()%>
</body>
</html>
