package com.provider.tags;

import com.provider.util.ParameterizedUrlImpl;
import com.provider.util.ParameterizedUrl;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.DynamicAttributes;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaginationNavTagHandler extends SimpleTagSupport implements DynamicAttributes {
    private int pageCount;

    // href that will be added to pagination buttons with pageParam appended
    private String href;

    // page parameter used to add page number to the url params
    private String pageParam;

    // how many nav items there are displayed simultaneously
    private int itemsCount = 5;

    private final Map<String, Object> dynamicAttributeMap = new HashMap<>();

    // previous page btn title
    private static final String PREVIOUS = "<";

    // next page btn title
    private static final String NEXT = ">";

    private static final String ACTIVE_STYLE_CLASS = "active";
    private static final String DISABLED_STYLE_CLASS = "disabled";

    @Override
    public void doTag() throws IOException {
        setHref();

        final int pageNumber = getPageNumber();
        final JspWriter writer = getJspContext().getOut();

        writer.write("<nav aria-label='Page navigation example' ");
        for (var entry : dynamicAttributeMap.entrySet()) {
            writer.write(entry.getKey() + "='" + entry.getValue() + "' ");
        }
        writer.write(">");
        writer.write("<ul class='pagination mx-auto'>");

        writer.write(pageNumber > 1
                ? navItem(pageNumber - 1, PREVIOUS)
                : disabledNavItem(PREVIOUS));

        int startIndex = Math.max(1, pageNumber - 2);
        for (int itemIndex = startIndex; itemIndex <= pageCount && itemIndex <= (itemsCount + startIndex - 1); itemIndex++) {
            if (itemIndex == pageNumber) {
                writer.write(navItem(pageNumber, String.valueOf(pageNumber), ACTIVE_STYLE_CLASS));
            } else {
                writer.write(navItem(itemIndex, String.valueOf(itemIndex)));
            }
        }

        writer.write(pageNumber < pageCount && pageNumber < itemsCount
                ? navItem(pageNumber + 1, NEXT)
                : disabledNavItem(NEXT));

        writer.write("</ul>");
        writer.write("</nav>");
    }

    private void setHref() {
        final PageContext pageContext = (PageContext) getJspContext();
        final String contextPath = pageContext.getServletContext().getContextPath();
        final String servletPart = (String) pageContext.getRequest()
                .getAttribute("jakarta.servlet.forward.servlet_path");
        String queryPart = (String) pageContext.getRequest()
                .getAttribute("jakarta.servlet.forward.query_string");
        queryPart = queryPart != null && !queryPart.isBlank()
                ? "?" + queryPart
                : "";

        href = contextPath + servletPart + queryPart;
    }

    private int getPageNumber() {
        final String pageNumberString = ((PageContext) getJspContext()).getRequest().getParameter(pageParam);
        if (pageNumberString != null) {
            return Math.max(1, Integer.parseInt(pageNumberString));
        }
        return 1;
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) {
        dynamicAttributeMap.put(localName, value);
    }

    private @NotNull String navItem(int pageNum, @NotNull String title, String... classes) {
        final ParameterizedUrl parameterizedUrl = ParameterizedUrlImpl.of(href);
        parameterizedUrl.setParam(pageParam, String.valueOf(pageNum));
        return navItemTag(parameterizedUrl.getString(), title, classes);
    }

    private @NotNull String disabledNavItem(@NotNull String title) {
        return navItemTag("#", title, DISABLED_STYLE_CLASS);
    }

    private @NotNull String navItemTag(@NotNull String href, @NotNull String title, String... classes) {
        return String.format("<li class='page-item %s'><a class='page-link' href='%s'>%s</a></li>",
                String.join(" ", Arrays.asList(classes)), href, title);
    }

    @SuppressWarnings("unused")
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @SuppressWarnings("unused")
    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    @SuppressWarnings("unused")
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }
}
