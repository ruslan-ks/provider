<%@ tag body-content="empty" %>
<%@ tag import="com.provider.constants.attributes.RequestAttributes" %>
<%@ attribute name="user" type="com.provider.entity.user.User" rtexprvalue="true" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="card shadow rounded border-0">
    <fmt:setLocale value="${requestScope[RequestAttributes.LOCALE]}" />
    <fmt:bundle basename="LabelsBundle">
        <div class="card-header"><fmt:message key="user.profile"/></div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-3"><fmt:message key="user.name"/></div>
                <div class="col text-muted">${user.name}</div>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-3"><fmt:message key="user.surname"/></div>
                <div class="col text-muted">${user.surname}</div>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-3"><fmt:message key="user.login"/></div>
                <div class="col text-muted">${user.login}</div>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-3"><fmt:message key="user.phone"/></div>
                <div class="col text-muted">${user.phone}</div>
            </div>
        </div>
    </fmt:bundle>
</div>
