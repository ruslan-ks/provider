<%@ tag body-content="empty" dynamic-attributes="dynamicAttributes" %>
<%@ tag import="com.provider.constants.Paths" %>
<%@ attribute name="code" type="java.lang.Integer" required="true" rtexprvalue="false" %>
<%@ attribute name="message" type="java.lang.String" required="true" rtexprvalue="false" %>
<%@ taglib prefix="include" tagdir="/WEB-INF/tags/include" %>
<%@ taglib prefix="pro" uri="http://provider.com" %>
<include:bootstrapStyles/>

<div class="row my-5" <pro:attributes map="${dynamicAttributes}"/>>
    <div class="col-sm-4 mx-auto">
        <h1><span style="font-size: 4rem">${code}</span> - ${message}</h1>
        <h5><a href="${Paths.CATALOG_PAGE}">Back to main</a></h5>
    </div>
</div>
