<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.params.CommandParams" %>
<%@ page import="com.provider.constants.params.SignInParams" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Provider - Sign in</title>
</head>
<body>
<tags:header/>
<fmt:setLocale value="${requestScope[RequestAttributes.LOCALE]}"/>
<fmt:bundle basename="LabelsBundle">
    <div class="alert alert-primary mx-auto mt-5 mb-1 w-50"><fmt:message key="signIn.howToGetRegisteredMsg"/></div>

    <form method="post" action="${initParam.controller}?${CommandParams.COMMAND}=${CommandParams.SIGN_IN}"
          class="container-sm w-50 my-2 p-4 bg-light shadow rounded">
        <h6 ><fmt:message key="signIn.enterLoginAndPass"/></h6>
        <hr>
        <div class="mb-3 row">
            <label for="loginInput" class="col-md-3 col-form-label"><fmt:message key="user.login"/></label>
            <div class="col">
                <input type="text" name="${SignInParams.LOGIN}" class="form-control" id="loginInput">
            </div>
        </div>
        <div class="mb-3 row">
            <label for="passwordInput" class="col-md-3 col-form-label"><fmt:message key="user.password"/></label>
            <div class="col">
                <input type="password" name="${SignInParams.PASSWORD}" class="form-control" id="passwordInput">
            </div>
        </div>
        <c:if test="${param[SignInParams.FAILED_TO_SIGN_IN]}">
            <div class="alert alert-danger py-1"><fmt:message key="signIn.loginOrPassFailed"/></div>
        </c:if>
        <div class="row">
            <div class="container-fluid">
                <input type="submit" class="btn btn-outline-primary float-end w-25" value="Sign in!">
            </div>
        </div>
    </form>
</fmt:bundle>
</body>
</html>
