<%@ tag body-content="empty" %>
<%@ tag import="com.provider.constants.attributes.AppAttributes" %>
<%@ tag import="com.provider.constants.attributes.SessionAttributes" %>
<%@ tag import="com.provider.constants.attributes.RequestAttributes" %>
<%@ tag import="com.provider.constants.params.UserSettingsParams" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<include:bootstrapStyles/>
<header class="navbar navbar-expand-md bg-dark navbar-dark">
    <div class="container">
        <a href="#" class="navbar-brand">Provider</a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <jsp:useBean id="userSettings" beanName="${SessionAttributes.USER_SETTINGS}" scope="session"
                     type="com.provider.entity.settings.UserSettings"/>
        <jsp:useBean id="languageInfo" beanName="${AppAttributes.LANGUAGE_INFO}" scope="application"
                     type="com.provider.localization.LanguageInfo"/>
        <c:set var="languages" value="${languageInfo.supportedLanguages}" scope="page"/>
        <c:set var="selectedLanguage"
               value="<%= userSettings.getLanguage().orElseGet(languageInfo::getDefaultLanguage) %>" scope="page"/>

        <fmt:setLocale value="${requestScope[RequestAttributes.LOCALE]}" />
        <fmt:bundle basename="LabelsBundle">
            <nav class="collapse navbar-collapse" id="navMenu">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.main"/></a></li>
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.catalogue"/></a></li>
                    <li class="nav-item"><a href="#" class="nav-link"><fmt:message key="nav.about"/></a></li>
                    <li class="nav-item">
                        <c:choose>
                            <c:when test="${not empty sessionScope[SessionAttributes.SIGNED_USER]}">
                                <a href="${pageContext.request.contextPath}/${initParam.userPanel}"
                                   class="nav-link">${sessionScope[SessionAttributes.SIGNED_USER].name}</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/${initParam.signIn}"
                                   class="nav-link"><fmt:message key="nav.signIn"/></a>
                            </c:otherwise>
                        </c:choose>

                    </li>
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