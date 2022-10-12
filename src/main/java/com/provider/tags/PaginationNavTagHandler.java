package com.provider.tags;

import com.provider.constants.params.PaginationParams;
import com.provider.util.AppendingParameterizedUrl;
import com.provider.util.ParameterizedUrl;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.DynamicAttributes;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.logging.log4j.util.Strings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaginationNavTagHandler extends SimpleTagSupport implements DynamicAttributes {
    // Starts from 1
    private int pageNumber = 1;

    private int pageCount;

    // href that will be added to pagination buttons with pageParam appended
    private String href;

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

        writer.write(navItem(pageNumber, String.valueOf(pageNumber), ACTIVE_STYLE_CLASS));

        for (int itemIndex = pageNumber + 1; itemIndex <= pageCount && itemIndex <= itemsCount; ++itemIndex) {
            writer.write(navItem(itemIndex, String.valueOf(itemIndex)));
        }

        writer.write(pageNumber < pageCount && pageNumber < itemsCount
                ? navItem(pageNumber + 1, NEXT)
                : disabledNavItem(NEXT));

        writer.write("</ul>");
        writer.write("</nav>");
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) {
        dynamicAttributeMap.put(localName, value);
    }

    private @NotNull String navItem(int pageNum, @NotNull String title, String... classes) {
        final ParameterizedUrl parameterizedUrl = AppendingParameterizedUrl.of(href);
        parameterizedUrl.addParam(PaginationParams.PAGE_NUMBER, String.valueOf(pageNum));
        return navItemTag(parameterizedUrl.getString(), title, classes);
    }

    private @NotNull String disabledNavItem(@NotNull String title) {
        return navItemTag("#", title, DISABLED_STYLE_CLASS);
    }

    private @NotNull String navItemTag(@NotNull String href, @NotNull String title, String... classes) {
        return String.format("<li class='page-item %s'><a class='page-link' href='%s'>%s</a></li>",
                Strings.join(Arrays.asList(classes), ' '), href, title);
    }

    @SuppressWarnings("unused")
    public void setPageNumber(int pageNumber) {
        if (pageNumber >= 1) {
            this.pageNumber = pageNumber;
        }
    }

    @SuppressWarnings("unused")
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @SuppressWarnings("unused")
    public void setHref(String href) {
        this.href = href;
    }

    @SuppressWarnings("unused")
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }
}
