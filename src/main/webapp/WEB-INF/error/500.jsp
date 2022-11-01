<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<html>
<head>
    <title>Something went wrong - Provider</title>
    <include:bootstrapStyles/>
</head>
<body class="bg-light">
    <br>
    <br>
    <br>
    <br>
    <pro:error code="500" message="Internal server error :("/>
</body>
</html>
