<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.SessionAttributes" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>User Panel</title>
</head>
<body>
    <tags:header/>
    <h2>Hello, ${sessionScope[SessionAttributes.SIGNED_USER].name}</h2>

</body>
</html>
