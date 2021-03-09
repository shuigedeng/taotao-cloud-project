<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>查询所有员工</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<div id="wrap">
    <div id="top_content">
        <div id="header">
            <div id="rightheader">
                <p>
                    2009/11/20
                    <br/>
                </p>
            </div>
            <div id="topheader">
                <h1 id="title">
                    <a href="#">main</a>
                </h1>
            </div>
            <div id="navigation">
            </div>
        </div>
        <div id="content">
            <p id="whereami">
            </p>
            <h1>
                Welcome!
            </h1>
            <table class="table">
                <tr class="table_header">
                    <td>
                        ID
                    </td>
                    <td>
                        Name
                    </td>
                    <td>
                        Salary
                    </td>
                    <td>
                        Age
                    </td>
                    <td>
                        Operation
                    </td>
                </tr>
                <c:forEach items="${emps}" var="emp" varStatus="e">
                    <c:if test="${e.count % 2  !=0}">
                        <tr class="row1">
                    </c:if>
                    <c:if test="${e.count % 2  ==0}">
                        <tr class="row2">
                    </c:if>
                    <td>
                            ${emp.id}
                    </td>
                    <td>
                            ${emp.name}
                    </td>
                    <td>
                            ${emp.salary}
                    </td>
                    <td>
                            ${emp.age}
                    </td>
                    <td>
                        <a href="<c:url  context='${pageContext.request.contextPath}' value='/manager/safe/deleteEmp?id=${emp.id}'/>">delete emp</a>&nbsp;<a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showEmp?id=${emp.id}'/>">update emp</a>
                    </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="5" align="center">
                        <a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showAllEmp?pageIndex=1'/>">首页</a>

                        <c:if test="${page.pageIndex > 1}">
                            <a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showAllEmp?pageIndex=${page.pageIndex - 1}' />">上一页</a>
                        </c:if>
                        <c:if test="${page.pageIndex==1}">
                            <a>上一页</a>
                        </c:if>

                        <c:if test="${page.pageIndex < page.totalPages}">
                            <a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showAllEmp?pageIndex=${page.pageIndex + 1}' />">下一页</a>
                        </c:if>
                        <c:if test="${page.pageIndex == page.totalPages}">
                            <a>下一页</a>
                        </c:if>

                        <a href="<c:url context='${pageContext.request.contextPath}' value='/manager/safe/showAllEmp?pageIndex=${page.totalPages}'/>">尾页</a>
                    </td>
                </tr>
            </table>
            <p>
                <input type="button" class="button" value="Add Employee" onclick="location='${pageContext.request.contextPath}/addEmp.jsp'"/>
            </p>
        </div>
    </div>
    <div id="footer">
        <div id="footer_bg">
            ABC@126.com
        </div>
    </div>
</div>
</body>
</html>

