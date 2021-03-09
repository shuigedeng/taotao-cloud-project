<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>pageContext对象的作用</title>
</head>
<body>
    <!--获取其他8个内置对象 -->
    <%
        pageContext.getRequest();
        pageContext.getResponse();
        pageContext.getSession();
        pageContext.getServletContext();
        pageContext.getOut();
        pageContext.getException();
        pageContext.getPage();
        pageContext.getServletConfig();
    %>
<!-- pageContext操作其他作用域-->
    <%
        pageContext.setAttribute("page","123");//当前JSP有效
        pageContext.setAttribute("req","aaa",PageContext.REQUEST_SCOPE);
        pageContext.setAttribute("sess","bbb",PageContext.SESSION_SCOPE);
        pageContext.setAttribute("app","ccc",PageContext.APPLICATION_SCOPE);
//        String req = (String)request.getAttribute("req");
//        String sess = (String)session.getAttribute("sess");
//        String app = (String) application.getAttribute("app");
        String req = (String) pageContext.getAttribute("req",PageContext.REQUEST_SCOPE);
        String sess = (String) pageContext.getAttribute("sess",PageContext.SESSION_SCOPE);
        String app = (String) pageContext.getAttribute("app",PageContext.APPLICATION_SCOPE);
        //pageContext、request、session、application
        String result = (String)pageContext.findAttribute("app");
    %>
    <h1>request:<%=req%></h1>
    <h1>session:<%=sess%></h1>
    <h1>application:<%=app%></h1>
    <h1>find:<%=result%></h1>
</body>
</html>
