<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.UsersManagementParams" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pro" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>Users management</title>
</head>
<body>
    <pro:header/>
    <c:set var="users" value="${requestScope[RequestAttributes.USERS]}" scope="page"/>
    <div class="container-sm w-75 mx-auto my-sm-5 p-sm-3 rounded rounded-1 shadow">
        <table class="table table-striped my-sm-2">
            <tr>
                <th>id</th>
                <th>login</th>
                <th>name</th>
                <th>surname</th>
                <th>phone</th>
                <th>status</th>
            </tr>
            <c:forEach var="user" items="${users}" varStatus="status">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                    <td>${user.phone}</td>
                    <td>${user.status}</td>
                </tr>
            </c:forEach>
        </table>
        <nav class="mx-auto" aria-label="Page navigation example">
            <ul class="pagination mx-auto">
                <li class="page-item"><a class="page-link" href="#">Previous</a></li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item"><a class="page-link" href="#">Next</a></li>
            </ul>
        </nav>
    </div>
</body>
</html>
