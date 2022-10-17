<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.ServiceParams" %>
<%@ page import="com.provider.constants.params.PaginationParams" %>
<%@ page import="com.provider.constants.params.TariffParams" %>
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
                    <div class="col-md-5 ms-auto">
                        <input type="submit" class="btn btn-primary w-100" value="<fmt:message key="service.addService"/>">
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

        <div class="border border-0 shadow p-sm-4">
            <form method="post" action="${pageContext.request.contextPath}/${Paths.ADD_TARIFF}"
                  onsubmit="handleSubmit(event)" class="row" id="addTariffForm">
                <div class="row">
                    <h5>Add tariff</h5>
                    <hr>
                </div>
                <div class="col-md-7">
                    <div class="mb-3 row">
                        <label for="tariffTitleIn" class="col-sm-2 col-form-label">Title</label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.TITLE}"
                                   class="form-control" id="tariffTitleIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffDescIn" class="col-sm-2 col-form-label">Description</label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.DESCRIPTION}"
                                      class="form-control" id="tariffDescIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffUsdPriceIn" class="col-sm-2 col-form-label">Price</label>
                        <div class="col-sm-10">
                            <input type="number" step="0.01" min="0" name="${TariffParams.USD_PRICE}" placeholder="$"
                                   class="form-control" id="tariffUsdPriceIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffStatusSelect" class="col-sm-2 col-form-label">Status</label>
                        <div class="col-sm-10">
                            <select name="${TariffParams.STATUS}" class="form-select" id="tariffStatusSelect">
                                <c:forEach var="status" items="${pro:allTariffStatuses()}">
                                    <option value="${status}">${status}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label class="col-sm-2 col-form-label">Duration</label>
                        <div class="col-sm-5">
                            <input type="number" min="0" max="12" name="${TariffParams.DURATION_MONTHS}"
                                   placeholder="months" class="form-control" aria-label="Duration months" required>
                        </div>
                        <div class="col-sm-5">
                            <input type="number" min="0" name="${TariffParams.DURATION_MINUTES}" placeholder="minutes"
                                   class="form-control" aria-label="Duration minutes" required>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h6>Select services</h6>
                    </div>
                    <div class="row">
                        <c:forEach var="service" items="${requestScope[RequestAttributes.SERVICES]}" varStatus="status">
                            <div class="col-lg-6">
                                <input type="checkbox" name="${TariffParams.SERVICE_IDS}" value="${service.id}"
                                       class="btn-check tariff-service-check" id="tariffServiceCheckBtn${status.index}"
                                       autocomplete="off">
                                <label class="btn btn-outline-primary w-100 my-sm-2"
                                       for="tariffServiceCheckBtn${status.index}">[${service.id}] ${service.name}</label>
                                <br>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3 mx-auto">
                        <button class="btn btn-primary w-100" type="submit">Add tariff!</button>
                    </div>
                </div>
            </form>
        </div>
        <br>

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
    <script>
        const tariffForm = document.querySelector('#addTariffForm');
        const tariffServiceChecks = document.querySelectorAll('.tariff-service-check');
        function handleSubmit(event) {
            let checkedFound = false;
            for (let check of tariffServiceChecks) {
                if (check.checked) {
                    checkedFound = true;
                    break;
                }
            }
            if (checkedFound) {
                return true;
            }
            alert("At least one service must be chosen!");
            if (event.preventDefault) {
                event.preventDefault();
            }
            return false;
        }
    </script>
</body>
</html>
