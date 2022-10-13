<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.HtmlRegex" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Tariffs management - Provider</title>
</head>
<body>
    <pro:header/>
    <div class="container-md my-5 p-sm-3">
        <div class="row">
            <form class="col p-3 border-0 shadow">
                <h5><fmt:message key="service.addService"/></h5>
                <hr>
                <div class="mb-3 row">
                    <label for="serviceNameIn" class="col-sm-3 col-form-label">
                        <fmt:message key="service.name"/></label>
                    <div class="col-sm-9">
                        <input type="text" pattern="${HtmlRegex.REGULAR_TEXT}" class="form-control" id="serviceNameIn"
                               required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="serviceDescriptionIn" class="col-sm-3 col-form-label">
                        <fmt:message key="service.description"/></label>
                    <div class="col-sm-9">
                        <input type="text" pattern="${HtmlRegex.REGULAR_TEXT}" class="form-control"
                               id="serviceDescriptionIn" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <div class="col-md-9"></div>
                    <div class="col-md-3">
                        <input type="submit" class="btn btn-primary" value="<fmt:message key="service.addService"/>">
                    </div>
                </div>
            </form>
            <div class="col">
                <table class="table table-striped">
                    <tr>
                        <th><fmt:message key="service.id"/></th>
                        <th><fmt:message key="service.name"/></th>
                        <th><fmt:message key="service.description"/></th>
                    </tr>
                    <c:forEach var="service" items="${requestScope[RequestAttributes.SERVICES]}">
                        <tr>
                            <td>${service.id}</td>
                            <td>${service.name}</td>
                            <td>${service.description}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
