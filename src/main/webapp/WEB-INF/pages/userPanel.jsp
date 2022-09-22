<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ taglib prefix="prov" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>User Panel - Provider</title>
</head>
<body>
    <prov:header/>
    <div class="row my-5 justify-content-center">
        <div class="col-md-6">
            <prov:userProfile user="${sessionScope[SessionAttributes.SIGNED_USER]}"/>
        </div>
    </div>
    <div class="row">User accounts: ${requestScope[RequestAttributes.USER_ACCOUNTS]}</div>
</body>
</html>
