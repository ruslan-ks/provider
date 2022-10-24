<%@ tag body-content="empty" %>
<%@ tag import="com.provider.constants.attributes.SessionAttributes" %>
<%@ attribute name="instant" type="java.time.Instant" rtexprvalue="true" required="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>

<fmt:formatDate value="${pro:toDate(instant)}" pattern="yyyy-MM-dd HH:mm:ss"
                timeZone="${sessionScope[SessionAttributes.USER_SETTINGS].timezone}"/>