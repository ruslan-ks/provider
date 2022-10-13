<%@ tag body-content="empty" dynamic-attributes="dynamicAttributes" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ tag import="com.provider.constants.params.ReplenishParams" %>
<%@ tag import="com.provider.constants.params.CommandParams" %>
<%@ attribute name="accounts" type="java.lang.Iterable" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>

<div class="accordion my-2 p-1" id="accordionExample" <pro:attributes map="${dynamicAttributes}"/> >
    <h6><fmt:message key="account.accounts"/></h6>
    <c:forEach var="account" items="${accounts}">
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingOne">
                <button type="button" class="accordion-button" data-bs-toggle="collapse"
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
                        <form class="col" method="get"
                              action="${pageContext.servletContext.contextPath}/${Paths.REPLENISH_PAGE}">
                            <button class="btn btn-success float-end" type="submit">
                                <fmt:message key="account.replenish"/>
                            </button>
                            <input name="${CommandParams.COMMAND}" value="${CommandParams.REPLENISH_PAGE}"
                                   type="text" readonly hidden aria-label="">
                            <input name="${ReplenishParams.CURRENCY}" value="${account.currency}"
                                   type="text" readonly hidden aria-label="">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
