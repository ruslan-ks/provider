<%@ tag body-content="empty" %>
<%@ tag import="com.provider.constants.attributes.AppAttributes" %>
<%@ tag import="com.provider.constants.attributes.SessionAttributes" %>
<%@ tag import="com.provider.constants.params.UserSettingsParams" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>

<%-- Conviniant variables --%>
<c:set var="user" value="${sessionScope[SessionAttributes.SIGNED_USER]}" scope="page"/>
<c:set var="settings" value="${sessionScope[SessionAttributes.USER_SETTINGS]}" scope="page"/>

<fmt:setLocale value="${settings.locale}"/>
<fmt:setBundle basename="LabelsBundle" scope="request"/>

<include:bootstrapStyles/>

<header class="navbar navbar-expand-md bg-dark navbar-dark">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">Provider</a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <nav class="collapse navbar-collapse" id="navMenu">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/" class="nav-link"><fmt:message key="nav.main"/></a>
                </li>
                <li class="nav-item"><a href="${Paths.CATALOG_PAGE}"
                                        class="nav-link"><fmt:message key="nav.catalog"/></a></li>
                <c:choose>
                    <c:when test="${not empty user}">
                        <li class="nav-item dropdown">
                            <a href="#" class="nav-link dropdown-toggle" id="navbarScrollingDropdown" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                    ${sessionScope[SessionAttributes.SIGNED_USER].name}
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
                                <c:if test="${pro:isAdminOrHigher(user)}">
                                    <li><a href="${pageContext.request.contextPath}/${Paths.USERS_MANAGEMENT_PAGE}"
                                           class="dropdown-item"><fmt:message key="nav.manageUsers"/></a></li>
                                    <li><a href="${pageContext.request.contextPath}/${Paths.TARIFFS_MANAGEMENT_PAGE}"
                                           class="dropdown-item"><fmt:message key="nav.manageTariffs"/></a></li>
                                </c:if>
                                <li><a href="${pageContext.request.contextPath}/${Paths.USER_PANEL_PAGE}"
                                       class="dropdown-item"><fmt:message key="nav.userPanel"/></a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/${Paths.SIGN_OUT}">
                                    <fmt:message key="nav.signOut"/></a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/${Paths.SIGN_IN_JSP}"
                               class="nav-link"><fmt:message key="nav.signIn"/></a>
                        </li>
                    </c:otherwise>
                </c:choose>
                <li class="nav-item">
                    <select name="${UserSettingsParams.LOCALE}" id="localeSelect" class="form-select"
                            aria-label="language" onchange="handleLanguageSelectChange()">
                        <c:forEach var="entry" items="${applicationScope[AppAttributes.LOCALE_LANG_MAP]}">
                            <c:set var="selected" value="${entry.key eq settings.locale ? 'selected' : ''}"/>
                            <option value="${entry.key}" ${selected}>${entry.value}</option>
                        </c:forEach>
                    </select>
                </li>
            </ul>
        </nav>
    </div>
</header>

<%-- Show request messages --%>
<pro:messages/>

<include:bootstrapScripts/>

<script type="text/javascript">
    function handleLanguageSelectChange() {
        // Reload the page with a language parameter set
        const localeSelect = document.querySelector('#localeSelect');
        const parser = new URL(window.location);
        parser.searchParams.set('${UserSettingsParams.LOCALE}', localeSelect.value);
        window.location = parser.href;
    }
</script>