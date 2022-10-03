<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
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
</body>
</html>
