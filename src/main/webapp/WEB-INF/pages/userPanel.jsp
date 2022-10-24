<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
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
                    <strong>
                        Last payment:
                        <fmt:formatDate value="${pro:toDate(subscriptionDto.subscription.lastPaymentTime)}"
                                        pattern="yyyy-MM-dd HH:mm:ss"
                                        timeZone="${sessionScope[SessionAttributes.USER_SETTINGS].timezone}"/>
                    </strong>
                    <br>
                    <strong>
                        Next payment:
                        <fmt:formatDate value="${pro:nextPaymentTime(subscriptionDto.subscription, subscriptionDto.tariffDto.duration)}"
                                pattern="yyyy-MM-dd HH:mm:ss"
                                timeZone="${sessionScope[SessionAttributes.USER_SETTINGS].timezone}"/>
                    </strong>
                    <pro:tariffCard tariffDto="${subscriptionDto.tariffDto}" isSubscribed="${true}"/>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>
