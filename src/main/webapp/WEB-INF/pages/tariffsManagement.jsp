<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.ServiceParams" %>
<%@ page import="com.provider.constants.params.PaginationParams" %>
<%@ page import="com.provider.constants.Regex" %>
<%@ page import="com.provider.constants.Paths" %>
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
            <form method="post" action="${pageContext.request.contextPath}/${Paths.ADD_SERVICE}"
                  class="col p-3 border-0 shadow">
                <h5><fmt:message key="service.addService"/></h5>
                <hr>
                <div class="mb-3 row">
                    <label for="serviceNameIn" class="col-sm-3 col-form-label">
                        <fmt:message key="service.name"/></label>
                    <div class="col-sm-9">
                        <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${ServiceParams.NAME}"
                               class="form-control" id="serviceNameIn" required>
                    </div>
                </div>
                <div class="mb-3 row">
                    <label for="serviceDescriptionIn" class="col-sm-3 col-form-label">
                        <fmt:message key="service.description"/></label>
                    <div class="col-sm-9">
                        <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${ServiceParams.DESCRIPTION}"
                               class="form-control" id="serviceDescriptionIn" required>
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

        <table class="table table-striped my-sm-2">
            <tr>
                <th>id</th>
                <th>Title</th>
                <th>Description</th>
                <th>Price</th>
                <th>Status</th>
                <th>Duration</th>
                <th>Services</th>
            </tr>
            <c:forEach var="tariffDto" items="${requestScope[RequestAttributes.TARIFFS]}">
                <tr>
                    <td>${tariffDto.tariff.id}</td>
                    <td>${tariffDto.tariff.title}</td>
                    <td>${tariffDto.tariff.description}</td>
                    <td>${tariffDto.tariff.usdPrice}</td>
                    <td>${tariffDto.tariff.status}</td>
                    <td>${tariffDto.duration.months} months ${tariffDto.duration.minutes} min</td>
                    <td>
                        <c:forEach var="service" items="${tariffDto.services}">
                            [${service.id}] ${service.name}<br>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <pro:paginationNav pageCount="${requestScope[RequestAttributes.PAGE_COUNT]}"
                           href="${pageContext.request.contextPath}/${Paths.TARIFFS_MANAGEMENT_PAGE}"
                           pageParam="${PaginationParams.PAGE_NUMBER}"/>
    </div>
</body>
</html>
