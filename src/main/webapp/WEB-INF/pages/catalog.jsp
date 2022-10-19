<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.provider.constants.attributes.RequestAttributes" %>
<%@ page import="com.provider.constants.Paths" %>
<%@ page import="com.provider.constants.params.PaginationParams" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Catalog - Provider</title>
</head>
<body>
    <pro:header/>
    <div class="container">
        <div class="row">
            <c:forEach var="tariffDto" items="${requestScope[RequestAttributes.TARIFFS]}">
                <div class="col-lg-4 col-md-6 my-3">
                    <pro:tariffCard tariffDto="${tariffDto}"/>
                </div>
            </c:forEach>
        </div>
    </div>
    <pro:paginationNav pageCount="${requestScope[RequestAttributes.PAGE_COUNT]}"
                       href="${Paths.CATALOG_PAGE}"
                       pageParam="${PaginationParams.PAGE_NUMBER}"/>
</body>
</html>
