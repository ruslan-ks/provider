<%@ tag body-content="empty" dynamic-attributes="dynamicAttributes" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ tag import="com.provider.constants.params.ReplenishParams" %>
<%@ attribute name="accounts" type="java.lang.Iterable" required="true" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="LabelsBundle">
    <div class="accordion my-2 p-1" id="accordionExample" <pro:attributes map="${dynamicAttributes}"/> >
        <h6><fmt:message key="account.accounts"/></h6>
        <c:forEach var="account" items="${accounts}">
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingOne">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                            data-bs-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                        ${account.currency}
                    </button>
                </h2>
                <div id="collapseOne" class="accordion-collapse collapse show" aria-labelledby="headingOne"
                     data-bs-parent="#accordionExample">
                    <div class="accordion-body">
                        <div class="row">
                            <div class="col"><strong>${account.currency}</strong> -
                                <fmt:message key="account.amount"/>: ${account.amount}
                            </div>
                            <form class="col" method="post"
                                  action="${pageContext.servletContext.contextPath}/${Paths.REPLENISH_PAGE}">
                                <button class="btn btn-success float-end" type="submit">
                                    <fmt:message key="account.replenish"/>
                                </button>
                                <input type="number" name="${ReplenishParams.ACCOUNT_ID}" value="${account.id}"
                                       readonly hidden aria-label="">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</fmt:bundle>
