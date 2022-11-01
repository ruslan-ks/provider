<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<html>
<head>
    <title>Something went wrong - Provider</title>
    <include:bootstrapStyles/>
</head>
<%
    final Logger logger = LoggerFactory.getLogger("com.provider.SomethingWentWrongJsp");
    logger.error("Something went wrong", exception);
%>
<body class="bg-light">
    <br>
    <br>
    <br>
    <br>
    <div class="row my-5">
        <div class="col-sm-4 mx-auto">
            <h3>Something went wrong :(</h3>
            <h5><a href="${Paths.CATALOG_PAGE}">Back to main</a></h5>
        </div>
    </div>
</body>
</html>
