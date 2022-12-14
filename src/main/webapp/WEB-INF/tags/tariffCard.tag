<%@ attribute name="tariffDto" type="com.provider.entity.dto.TariffDto" required="true" rtexprvalue="true" %>
<%@ tag dynamic-attributes="dynamicAttributes" body-content="scriptless" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ tag import="com.provider.constants.params.CommandParams" %>
<%@ tag import="com.provider.constants.params.TariffParams" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="card border border-0 shadow rounded rounded-3 my-2" <pro:attributes map="${dynamicAttributes}"/>>
    <img src="${pro:tariffImageUrl(tariffDto.tariff.imageFileName)}" class="card-img-top"
         alt="Tariff image">
    <div class="card-body pb-0">
        <div class="row">
            <div class="col">
                <h4 class="card-title">${tariffDto.tariff.title}</h4>
            </div>
        </div>
    </div>
    <ul class="list-group list-group-flush">
        <c:forEach var="service" items="${tariffDto.services}">
            <li class="list-group-item">
                ${service.name}<br>
                <small class="text-muted">${service.description}</small>
            </li>
        </c:forEach>
    </ul>
    <div class="card-body">
        <div class="row mb-1">
            <div class="col"><strong>$${tariffDto.tariff.usdPrice}</strong></div>
            <div class="col-6 ms-auto">
                <i>${tariffDto.duration.months} <fmt:message key="tariff.duration.months"/> ${tariffDto.duration.minutes}
                    <fmt:message key="tariff.duration.min"/></i>
            </div>
        </div>
        <jsp:doBody/>
        <div class="row">
            <form action="${Paths.CONTROLLER}" class="col">
                <input type="text" name="${CommandParams.COMMAND}" value="${CommandParams.DOWNLOAD_TARIFF_PDF}"
                       readonly hidden>
                <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}" readonly hidden>
                <input type="submit" class="btn btn-link p-0" value="<fmt:message key="tariffCard.downloadPdfBtn"/>">
            </form>
        </div>
    </div>
</div>
