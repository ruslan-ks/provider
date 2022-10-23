<%@ attribute name="tariffDto" type="com.provider.entity.dto.TariffDto" required="true" rtexprvalue="true" %>
<%@ tag dynamic-attributes="dynamicAttributes" body-content="empty" %>
<%@ tag import="com.provider.constants.params.TariffParams"%>
<%@ tag import="com.provider.constants.Paths"%>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="card border border-0 shadow rounded rounded-3" <pro:attributes map="${dynamicAttributes}"/>>
    <img src="${pro:tariffImagePath(tariffDto.tariff.imageFileName, pageContext.servletContext)}" class="card-img-top"
         alt="Tariff image">
    <div class="card-body">
        <h4 class="card-title">${tariffDto.tariff.title}</h4>
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
        <form method="post" action="${Paths.SUBSCRIBE}">
            <input type="number" name="${TariffParams.ID}" value="${tariffDto.tariff.id}" aria-label="tariff id"
                   readonly hidden>
            <input type="submit" value="<fmt:message key="tariffCard.subscribeBtn"/>" class="btn btn-success w-100">
        </form>
        <div class="row">
            <div class="col-lg-5 ms-auto">
                <a href="#" class="card-link3"><fmt:message key="tariffCard.downloadPdfBtn"/></a>
            </div>
        </div>
    </div>
</div>
