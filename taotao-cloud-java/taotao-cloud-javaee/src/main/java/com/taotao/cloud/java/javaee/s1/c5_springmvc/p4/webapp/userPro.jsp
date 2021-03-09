<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <title>Hello, world!</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.4.1/dist/jquery.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.4.1/dist/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

</head>
<body>

    <table class="table table-bordered table-striped table-dark">
        <thead>
            <tr>
                <th>id</th>
                <th>username</th>
                <th>password</th>
                <th>gender</th>
                <th>registTime</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${requestScope.data.list}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.gender}</td>
                    <td>
                        <fmt:formatDate value="${user.registTime}" pattern="yyyy-MM-dd"/>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <nav aria-label="...">
        <ul class="pagination justify-content-center" >
            <c:if test="${requestScope.data.hasPreviousPage}">
                <li class="page-item">
                    <a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${requestScope.data.prePage}" tabindex="-1" aria-disabled="true">Previous</a>
                </li>
            </c:if>
            <c:if test="${!requestScope.data.hasPreviousPage}">
                <li class="page-item disabled">
                    <a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${requestScope.data.prePage}" tabindex="-1" aria-disabled="true">Previous</a>
                </li>
            </c:if>

            <%--<li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item active" aria-current="page">
                <a class="page-link" href="#">2 <span class="sr-only">(current)</span></a>
            </li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>--%>

            <c:forEach begin="1" end="${requestScope.data.pages}" var="pageNum">
                <c:if test="${pageNum==requestScope.data.pageNum}">
                    <li class="page-item active"><a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${pageNum}">${pageNum}</a></li>
                </c:if>
                <c:if test="${pageNum!=requestScope.data.pageNum}">
                    <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${pageNum}">${pageNum}</a></li>
                </c:if>
            </c:forEach>


                <c:if test="${requestScope.data.hasNextPage}">
                    <li class="page-item">
                        <a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${requestScope.data.nextPage}">Next</a>
                    </li>
                </c:if>
                <c:if test="${!requestScope.data.hasNextPage}">
                    <li class="page-item disabled">
                        <a class="page-link" href="${pageContext.request.contextPath}/users?pageNum=${requestScope.data.nextPage}">Next</a>
                    </li>
                </c:if>

        </ul>
    </nav>
</body>
</html>
