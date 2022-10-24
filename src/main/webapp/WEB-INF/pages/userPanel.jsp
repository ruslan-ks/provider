<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.TariffParams" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Panel - Provider</title>
</head>
<body>
    <pro:header/>
    <div class="row my-5 justify-content-center me-1">
        <div class="col-md-6">
            <pro:userProfile user="${sessionScope[SessionAttributes.SIGNED_USER]}"/>
        </div>
        <div class="col-md-4">
            <pro:userAccounts accounts="${requestScope[RequestAttributes.USER_ACCOUNTS]}"/>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <c:set var="activeSubscriptions"
                   value="${requestScope[RequestAttributes.USER_ACTIVE_SUBSCRIPTION_DTOS]}"/>
            <c:forEach var="subscriptionDto" items="${activeSubscriptions}">
                <div class="col-4">
                    <pro:tariffCard tariffDto="${subscriptionDto.tariffDto}">
                        <form method="post" action="#">
                            <input type="number" name="${TariffParams.ID}" value="${subscriptionDto.tariffDto.tariff.id}"
                                   aria-label="tariff id" readonly hidden>
                            <input type="submit" value="<fmt:message key="tariffCard.unsubscribeBtn"/>"
                                   class="btn btn-danger w-100">
                        </form>
                        Last payment:
                        <strong><pro:date instant="${subscriptionDto.subscription.lastPaymentTime}"/></strong>
                        <br>
                        Next payment:
                        <strong>
                            <pro:date instant="${pro:nextPaymentTime(subscriptionDto.subscription, subscriptionDto.tariffDto.duration)}"/>
                        </strong>
                    </pro:tariffCard>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
