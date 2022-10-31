<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ page import="com.provider.constants.params.ServiceParams" %>
<%@ page import="com.provider.constants.params.PaginationParams" %>
<%@ page import="com.provider.constants.params.TariffParams" %>
<%@ page import="com.provider.constants.params.CommandParams" %>
<%@ page import="com.provider.constants.params.EditParams" %>
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
    <c:set var="settings" value="${sessionScope[SessionAttributes.USER_SETTINGS]}" scope="page"/>
    <div class="container-md my-5 p-sm-3">
        <div class="row">
            <%-- New service form --%>
            <form method="post" action="${pageContext.request.contextPath}/${Paths.ADD_SERVICE}"
                  class="col p-3 border-0 shadow needs-validation" novalidate>
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

            <%-- Services table --%>
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
            <%-- New tariff form --%>
            <form method="post" action="${pageContext.request.contextPath}/${Paths.ADD_TARIFF}"
                  enctype="multipart/form-data" id="addTariffForm" class="row needs-validation" novalidate>
                <div class="row">
                    <h5><fmt:message key="tariff.add.addTariff"/></h5>
                    <hr>
                </div>
                <div class="col-md-7">
                    <div class="mb-3 row">
                        <label for="tariffTitleIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.title"/></label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.TITLE}"
                                   class="form-control" id="tariffTitleIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffDescIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.description"/></label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.DESCRIPTION}"
                                   class="form-control" id="tariffDescIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffUsdPriceIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.price"/></label>
                        <div class="col-sm-10">
                            <input type="number" step="0.01" min="0" name="${TariffParams.USD_PRICE}" placeholder="$"
                                   class="form-control" id="tariffUsdPriceIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffStatusSelect"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.status"/></label>
                        <div class="col-sm-10">
                            <select name="${TariffParams.STATUS}" class="form-select" id="tariffStatusSelect">
                                <c:forEach var="status" items="${pro:allTariffStatuses()}">
                                    <option value="${status}">${status}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label class="col-sm-2 col-form-label"><fmt:message key="tariff.duration"/></label>
                        <div class="col-sm-5">
                            <input type="number" min="0" max="12" name="${TariffParams.DURATION_MONTHS}"
                                   placeholder="<fmt:message key="tariff.duration.months"/>" class="form-control"
                                   aria-label="Duration months" required>
                        </div>
                        <div class="col-sm-5">
                            <input type="number" min="0" name="${TariffParams.DURATION_MINUTES}"
                                   placeholder="<fmt:message key="tariff.duration.minutes"/>"
                                   class="form-control" aria-label="Duration minutes" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="formFile" class="form-label"><fmt:message key="tariff.add.selectImage"/></label>
                        <input type="file" name="${TariffParams.IMAGE}" class="form-control" id="formFile" required>
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <h6><fmt:message key="tariff.add.selectServices"/></h6>
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
                        <input class="btn btn-primary w-100" type="submit"
                               value="<fmt:message key="tariff.add.addTariff"/>">
                    </div>
                </div>
            </form>
        </div>
        <br>

        <table class="table table-striped my-sm-2">
            <tr>
                <th><fmt:message key="tariff.id"/></th>
                <th><fmt:message key="tariff.image"/></th>
                <th><fmt:message key="tariff.title"/></th>
                <th><fmt:message key="tariff.description"/></th>
                <th><fmt:message key="tariff.price"/></th>
                <th><fmt:message key="tariff.status"/></th>
                <th><fmt:message key="tariff.duration"/></th>
                <th><fmt:message key="services"/></th>
                <th></th>
            </tr>
            <c:forEach var="tariffDto" items="${requestScope[RequestAttributes.TARIFFS]}">
                <tr>
                    <td>${tariffDto.tariff.id}</td>
                    <td><img src="${pro:tariffImagePath(tariffDto.tariff.imageFileName, pageContext.servletContext)}"
                             alt="Image" style="width: 100px; height: 80px;"></td>
                    <td>${tariffDto.tariff.title}</td>
                    <td>${tariffDto.tariff.description}</td>
                    <td>${tariffDto.tariff.usdPrice}</td>
                    <td>${tariffDto.tariff.status}</td>
                    <td>${tariffDto.duration.months} <fmt:message key="tariff.duration.months"/>
                            ${tariffDto.duration.minutes} <fmt:message key="tariff.duration.min"/></td>
                    <td>
                        <c:forEach var="service" items="${tariffDto.services}">
                            [${service.id}] ${service.name}<br>
                        </c:forEach>
                    </td>
                    <td>
                        <form action="">
                            <input type="text" name="${CommandParams.COMMAND}"
                                   value="${CommandParams.EDIT_TARIFF_PAGE}" readonly hidden aria-label="command">
                            <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}"
                                   readonly hidden aria-label="tariff id">
                            <input type="text" name="${EditParams.LOCALE}" aria-label="edit locale"
                                   value="${settings.locale}" readonly hidden>
                            <input type="submit" class="btn btn-link" value="<fmt:message key="tariff.editBtn"/>">
                        </form>
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
        tariffForm.addEventListener("submit", (event) => {
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
        }, false);
    </script>
    <script src="${pageContext.request.contextPath}/public/js/validation.js"></script>
</body>
</html>
