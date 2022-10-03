<%@ tag body-content="empty"
        dynamic-attributes="dynamicAttributes" %>
<%@ tag import="com.provider.constants.params.MessageParams" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<div class="container-fluid my-sm-3" <pro:attributes map="${dynamicAttributes}"/> >
    <c:forEach var="errorMsg" items="${paramValues[MessageParams.ERROR]}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>${errorMsg}</strong>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:forEach>
    <c:forEach var="successMsg" items="${paramValues[MessageParams.SUCCESS]}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>${successMsg}</strong>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:forEach>
</div>
