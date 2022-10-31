<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.attributes.AppAttributes" %>
<%@ page import="com.provider.constants.params.EditParams" %>
<%@ page import="com.provider.constants.params.CommandParams" %>
<%@ page import="com.provider.constants.params.TariffParams" %>
<%@ page import="com.provider.constants.Regex" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Edit tariff - Provider</title>
</head>
<body>
    <pro:header/>
    <c:set var="tariffDto" value="${requestScope[RequestAttributes.TARIFF_DTO]}"/>
    <br>
    <div class="container">

        <%-- Localization nav --%>
        <ul class="nav nav-tabs">
            <c:forEach var="localeEntry" items="${applicationScope[AppAttributes.LOCALE_LANG_MAP]}">
                <li class="nav-item">
                    <form action="">
                        <input type="text" name="${CommandParams.COMMAND}"
                               value="${CommandParams.EDIT_TARIFF_PAGE}" readonly hidden aria-label="command">
                        <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}"
                               readonly hidden aria-label="tariff id">
                        <input type="text" name="${EditParams.LOCALE}" value="${localeEntry.key}" readonly hidden
                                aria-label="edit locale">
                        <c:set var="activeClass" value="${param[EditParams.LOCALE] eq localeEntry.key ? 'active' : ''}"/>
                        <input type="submit" class="nav-link ${activeClass}" aria-current="page"
                               value="${localeEntry.key}(${localeEntry.value})">
                    </form>
                </li>
            </c:forEach>
        </ul>
        <br>
        <div class="row">
            <div class="col-md-8">
                <%-- Tariff editing form --%>
                <form action="${Paths.CONTROLLER}" method="post"
                      class="needs-validation p-3 border-bottom border-dark border-1 pb-4" novalidate>
                    <input type="text" name="${CommandParams.COMMAND}" value="${CommandParams.EDIT_TARIFF}"
                           readonly hidden aria-label="command">
                    <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}" readonly hidden
                           aria-label="tariff id">
                    <input type="text" name="${EditParams.LOCALE}" value="${param[EditParams.LOCALE]}" readonly hidden
                           aria-label="edit locale">
                    <div class="mb-3 row">
                        <label for="tariffTitleIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.title"/></label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.TITLE}"
                                   value="${tariffDto.tariff.title}" class="form-control" id="tariffTitleIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffDescIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.description"/></label>
                        <div class="col-sm-10">
                            <input type="text" pattern="${Regex.REGULAR_TEXT}" name="${TariffParams.DESCRIPTION}"
                                   value="${tariffDto.tariff.description}" class="form-control" id="tariffDescIn" required>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffUsdPriceIn"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.price"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="tariffUsdPriceIn"
                                   value="$${tariffDto.tariff.usdPrice}" disabled>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="tariffStatusSelect"
                               class="col-sm-2 col-form-label"><fmt:message key="tariff.status"/></label>
                        <div class="col-sm-10">
                            <select name="${TariffParams.STATUS}" class="form-select" id="tariffStatusSelect">
                                <c:forEach var="status" items="${pro:allTariffStatuses()}">
                                    <c:set var="selected" value="${tariffDto.tariff.status eq status ? 'selected' : ''}"/>
                                    <option value="${status}" ${selected}>${status}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label class="col-sm-2 col-form-label"><fmt:message key="tariff.duration"/></label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" aria-label="Duration months"
                                   value="${tariffDto.duration.months} <fmt:message key="tariff.duration.months"/>"
                                   disabled>
                        </div>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" aria-label="Duration minutes"
                                   value="${tariffDto.duration.minutes} <fmt:message key="tariff.duration.minutes"/>"
                                   disabled>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4 ms-auto">
                            <input type="submit" value="Save" class="btn btn-primary w-100">
                        </div>
                    </div>
                </form>

                <%-- Tariff image changing form --%>
                <form action="${Paths.CONTROLLER}" method="post" enctype="multipart/form-data"
                      class="needs-validation p-3" novalidate>
                    <input type="text" name="${CommandParams.COMMAND}" value="${CommandParams.CHANGE_TARIFF_IMAGE}"
                           readonly hidden aria-label="command">
                    <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}" readonly hidden
                           aria-label="tariff id">
                    <div class="mb-3 row">
                        <div class="col">
                            <label for="formFile" class="form-label"><fmt:message key="tariff.add.selectImage"/></label>
                            <input type="file" name="${TariffParams.IMAGE}" class="form-control" id="formFile" required>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4 ms-auto">
                            <input type="submit" value="Save" class="btn btn-primary w-100">
                        </div>
                    </div>
                </form>
            </div>

            <%-- Tariff card view --%>
            <div class="col-lg-4 col-md-6">
                <pro:tariffCard tariffDto="${requestScope[RequestAttributes.TARIFF_DTO]}"/>
            </div>
        </div>
    </div>
    <br>
    <br>
    <script src="${pageContext.request.contextPath}/public/js/validation.js"></script>
</body>
</html>
