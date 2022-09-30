<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.UsersManagementParams" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="prov" uri="http://provider.com" %>

<html>
<head>
    <title>Users management</title>
</head>
<body>
    <pro:header/>
    <c:set var="users" value="${requestScope[RequestAttributes.USERS]}" scope="page"/>
    <fmt:bundle basename="LabelsBundle">
        <div class="container-md mx-md-auto my-md-5 p-sm-3 rounded rounded-1 shadow">
            <table class="table table-striped my-sm-3">
                <tr>
                    <th><fmt:message key="user.id"/></th>
                    <th><fmt:message key="user.login"/></th>
                    <th><fmt:message key="user.name"/></th>
                    <th><fmt:message key="user.surname"/></th>
                    <th><fmt:message key="user.phone"/></th>
                    <th><fmt:message key="user.status"/></th>
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
            <prov:paginationNav pageNumber="${param[UsersManagementParams.PAGE_NUMBER]}"
                                pageCount="${requestScope[RequestAttributes.PAGE_COUNT]}"
                                href="${pageContext.request.contextPath}/${Paths.USERS_MANAGEMENT_PAGE}"
                                pageParam="${UsersManagementParams.PAGE_NUMBER}"/>
        </div>
    </fmt:bundle>
</body>
</html>
