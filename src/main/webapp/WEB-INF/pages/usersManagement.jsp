<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.UsersManagementParams" %>
<%@ page import="com.provider.constants.params.UserParams" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="func" uri="http://functions.provider.com" %>

<html>
<head>
    <title>Users management</title>
</head>
<body>
    <pro:header/>
    <c:set var="users" value="${requestScope[RequestAttributes.USERS]}" scope="page"/>
    <fmt:bundle basename="LabelsBundle">
        <c:if test="${not empty param[UsersManagementParams.USER_ADDED]}">
            <div class="container">
                <c:choose>
                    <c:when test="${param[UsersManagementParams.USER_ADDED]}">
                        <div class="alert alert-success alert-dismissible fade show my-sm-2" role="alert">
                            <strong> User added successfully!</strong>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger alert-dismissible fade show my-sm-2" role="alert">
                            <strong> Failed to add new user!</strong>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <div class="container-md x-md-auto my-md-3 p-sm-3 rounded rounded-1 shadow">
            <form method="post" action="${Paths.ADD_USER}">
                <div class="mb-3 row">
                    <label for="inoutLogin" class="col-md-2 col-form-label">Login</label>
                    <div class="col-sm-10">
                        <input type="text" pattern="^[a-zA-Z0-9_]{4,}$" name="${UserParams.LOGIN}"
                               class="form-control" id="inoutLogin" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="inputPassword" class="col-md-2 col-form-label">Password</label>
                    <div class="col-sm-10">
                        <input type="password" minlength="4" name="${UserParams.PASSWORD}" class="form-control"
                               id="inputPassword" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="inputName" class="col-md-2 col-form-label">Name</label>
                    <div class="col-sm-10">
                        <input type="text" pattern="^[\w-]+$" name="${UserParams.NAME}" class="form-control"
                               id="inputName" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="inputSurname" class="col-md-2 col-form-label">Surname</label>
                    <div class="col-sm-10">
                        <input type="text" pattern="^[\w-]+$" name="${UserParams.SURNAME}" class="form-control"
                               id="inputSurname" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="inputPhone" class="col-md-2 col-form-label">Phone</label>
                    <div class="col-sm-10">
                        <input type="text" pattern="\+?([1-9][0-9])?[0-9]{6,10}$" name="${UserParams.PHONE}"
                               class="form-control" id="inputPhone" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <div class="col text-center">
                        <input type="submit" class="btn btn-primary w-25" value="Add user">
                    </div>
                </div>
            </form>
        </div>
        <div class="container-md mx-md-auto my-md-3 p-sm-3 rounded rounded-1 shadow">
            <table class="table table-striped my-sm-3">
                <tr>
                    <th><fmt:message key="user.id"/></th>
                    <th><fmt:message key="user.login"/></th>
                    <th><fmt:message key="user.name"/></th>
                    <th><fmt:message key="user.surname"/></th>
                    <th><fmt:message key="user.phone"/></th>
                    <th><fmt:message key="user.status"/></th>
                    <th></th>
                </tr>
                <c:forEach var="user" items="${users}" varStatus="status">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.login}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.phone}</td>
                        <td>${user.status}</td>
                        <td>
                            <form method="post"
                                  action="${pageContext.request.contextPath}/${Paths.UPDATE_USER_STATUS}">
                                <input type="number" name="${UserParams.ID}" value="${user.id}" aria-label="id"
                                       readonly hidden>
                                <select onchange="this.form.submit()" name="${UserParams.STATUS}" class="form-select"
                                        aria-label="User status">
                                    <c:forEach var="status" items="${func:allUserStatuses()}">
                                        <option value="${status}"
                                                <c:if test="${status eq user.status}">selected</c:if>>
                                                ${status}
                                        </option>
                                    </c:forEach>
                                </select>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <pro:paginationNav pageNumber="${param[UsersManagementParams.PAGE_NUMBER]}"
                                pageCount="${requestScope[RequestAttributes.PAGE_COUNT]}"
                                href="${pageContext.request.contextPath}/${Paths.USERS_MANAGEMENT_PAGE}"
                                pageParam="${UsersManagementParams.PAGE_NUMBER}"/>
        </div>
    </fmt:bundle>
</body>
</html>
