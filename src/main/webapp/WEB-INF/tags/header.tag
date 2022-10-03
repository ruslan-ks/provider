<%@ tag body-content="empty" %>
<%@ tag import="com.provider.constants.attributes.AppAttributes" %>
<%@ tag import="com.provider.constants.attributes.SessionAttributes" %>
<%@ tag import="com.provider.constants.attributes.RequestAttributes" %>
<%@ tag import="com.provider.constants.params.UserSettingsParams" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<%@ taglib prefix="func" uri="http://functions.provider.com" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>

<include:bootstrapStyles/>

<jsp:useBean id="userSettings" beanName="${SessionAttributes.USER_SETTINGS}" scope="session"
             type="com.provider.entity.settings.UserSettings"/>
<jsp:useBean id="languageInfo" beanName="${AppAttributes.LANGUAGE_INFO}" scope="application"
             type="com.provider.localization.LanguageInfo"/>
<c:set var="languages" value="${languageInfo.supportedLanguages}" scope="page"/>

<%-- TODO: move this default language-setting logic to a filter --%>
<c:set var="selectedLanguage"
       value="<%= userSettings.getLanguage().orElseGet(languageInfo::getDefaultLanguage) %>" scope="page"/>
<fmt:setLocale value="${requestScope[RequestAttributes.LOCALE]}" />

<%-- Just a conviniant short name for user --%>
<c:set var="user" value="${sessionScope[SessionAttributes.SIGNED_USER]}" scope="page"/>
<header class="navbar navbar-expand-md bg-dark navbar-dark">
    <div class="container">
        <a href="#" class="navbar-brand">Provider</a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <fmt:bundle basename="LabelsBundle">
            <nav class="collapse navbar-collapse" id="navMenu">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.main"/></a></li>
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.catalogue"/></a></li>
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.about"/></a></li>
                    <c:choose>
                        <c:when test="${not empty user}">
                            <li class="nav-item dropdown">
                                <a href="#" class="nav-link dropdown-toggle" id="navbarScrollingDropdown" role="button"
                                   data-bs-toggle="dropdown" aria-expanded="false">
                                        ${sessionScope[SessionAttributes.SIGNED_USER].name}
                                </a>
                                <ul class="dropdown-menu" aria-labelledby="navbarScrollingDropdown">
                                    <c:if test="${func:isAdminOrHigher(user)}">
                                        <li><a href="${pageContext.request.contextPath}/${Paths.USERS_MANAGEMENT_PAGE}"
                                               class="dropdown-item"><fmt:message key="nav.manageUsers"/></a></li>
                                    </c:if>
                                    <c:if test="${func:isRoot(user)}">
                                        <li><a href="#"
                                               class="dropdown-item">Root page placeholder</a></li>
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
                        <select name="${UserSettingsParams.LANGUAGE}" id="langSelect" class="form-select"
                                aria-label="language" onchange="handleLanguageSelectChange()">
                            <c:forEach var="lang" items="${pageScope.languages}">
                                <option value="${lang}"
                                        <c:if test="${lang.equals(selectedLanguage)}">selected</c:if>
                                >${lang}</option>
                            </c:forEach>
                        </select>
                    </li>
                </ul>
            </nav>
        </fmt:bundle>
    </div>
</header>

<%-- Show request messages --%>
<pro:messages hey="HEY" hello="HELLO"/>

<include:bootstrapScripts/>

<script type="text/javascript">
    function handleLanguageSelectChange() {
        // Reload the page with a language parameter set
        const languageSelect = document.querySelector('#langSelect');
        const parser = new URL(window.location);
        parser.searchParams.set('${UserSettingsParams.LANGUAGE}', languageSelect.value);
        window.location = parser.href;
    }
</script>