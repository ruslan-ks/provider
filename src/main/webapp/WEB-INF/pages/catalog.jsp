<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ page import="com.provider.constants.params.CommandParams" %>
<%@ page import="com.provider.constants.params.PaginationParams" %>
<%@ page import="com.provider.constants.params.CatalogParams" %>
<%@ page import="com.provider.constants.params.TariffParams" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ftm" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Catalog - Provider</title>
</head>
<body>
    <pro:header/>

    <div class="container-fluid px-5">
        <div class="row">
            <div class="col-md-2 my-3 bg-light border border-1 py-2 rounded rounded-3">
                <form action="${Paths.CONTROLLER}">
                    <input type="text" name="${CommandParams.COMMAND}" value="${CommandParams.CATALOG_PAGE}"
                           readonly hidden aria-label="Controller">
                    <h5><fmt:message key="catalog.orderBy"/></h5>
                    <c:forEach var="orderByEntry" items="${requestScope[RequestAttributes.TARIFF_ORDER_BY_FIELDS]}"
                            varStatus="status">
                        <div class="form-check">
                            <c:set var="checked"
                                   value="${orderByEntry.key eq param[CatalogParams.ORDER_BY_FIELD] ? 'checked' : ''}"/>
                            <input type="radio" name="${CatalogParams.ORDER_BY_FIELD}" value="${orderByEntry.key}"
                                   id="orderRadio${status.index}" class="form-check-input" ${checked}>
                            <label class="form-check-label" for="orderRadio${status.index}">
                                    ${orderByEntry.value}
                            </label>
                        </div>
                    </c:forEach>
                    <hr>
                    <div class="form-check">
                        <c:set var="checked" value="${param[CatalogParams.IS_ORDER_DESC] eq 'true' ? 'checked' : ''}"/>
                        <input type="checkbox" name="${CatalogParams.IS_ORDER_DESC}" value="true" class="form-check-input"
                               id="defaultCheck1" ${checked}>
                        <label class="form-check-label" for="defaultCheck1">
                            <fmt:message key="catalog.orderBy.descending"/>
                        </label>
                    </div>
                    <hr>
                    <c:forEach var="serviceCountEntry" items="${requestScope[RequestAttributes.SERVICE_COUNT_MAP]}"
                               varStatus="status">
                        <c:set var="checked"
                               value="${pro:contains(paramValues[CatalogParams.SERVICE_ID_FILTER], serviceCountEntry.key.id)
                                        ? 'checked'
                                        : ''}"/>
                        <input type="checkbox" name="${CatalogParams.SERVICE_ID_FILTER}" value="${serviceCountEntry.key.id}"
                               id="filterBtnCheck${status.index}" class="btn-check" ${checked}>
                        <label class="btn btn-outline-primary w-100" for="filterBtnCheck${status.index}">
                                ${serviceCountEntry.key.name} <span class="badge bg-dark">${serviceCountEntry.value}</span>
                        </label>
                        <br>
                    </c:forEach>
                    <input type="submit" class="btn btn-dark w-100 my-2"
                           value="<fmt:message key="catalog.orderBy.applyBtn"/>">
                </form>
            </div>
            <div class="col">
                <div class="row">
                    <c:set var="subscribedTariffIds"
                           value="${requestScope[RequestAttributes.USER_SUBSCRIBED_TARIFF_IDS]}"/>
                    <c:forEach var="tariffDto" items="${requestScope[RequestAttributes.TARIFFS]}">
                        <div class="col-lg-4 col-md-6 my-3">
                            <pro:tariffCard tariffDto="${tariffDto}">
                                <form method="post" action="${Paths.SUBSCRIBE}">
                                    <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}"
                                           aria-label="tariff id" readonly hidden>
                                    <c:set var="disabled"
                                           value="${subscribedTariffIds.contains(tariffDto.tariff.id) ? 'disabled' : ''}"/>
                                    <input type="submit" value="<fmt:message key="subscription.subscribeBtn"/>"
                                           class="btn btn-success w-100"${disabled}>
                                </form>
                            </pro:tariffCard>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

    <%--
    URL with parameters should be passed to the pro:paginationNav,
    so all the filtering and ordering params will be saved
    --%>
    <c:set var="urlWithParams"
           value="${pageContext.request.contextPath}${requestScope['jakarta.servlet.forward.servlet_path']}?${requestScope['jakarta.servlet.forward.query_string']}"/>
    <div class="row">
        <div class="col-2 col-lg-3 col-md-5 col-sm mx-auto">
            <pro:paginationNav pageCount="${requestScope[RequestAttributes.PAGE_COUNT]}"
                               href="${urlWithParams}"
                               pageParam="${PaginationParams.PAGE_NUMBER}"/>
        </div>
    </div>
    <br>
</body>
</html>
