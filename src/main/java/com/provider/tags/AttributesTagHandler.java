package com.provider.tags;

import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class AttributesTagHandler extends SimpleTagSupport {
    private Map<String, String> map;

    private static final String DELIMITER = " ";

    @Override
    public void doTag() throws IOException {
        final JspWriter writer = getJspContext().getOut();
        final String result = map.entrySet().stream()
                .map(AttributesTagHandler::entryToString)
                .collect(Collectors.joining(DELIMITER));
        writer.write(result);
    }

    private static String entryToString(Map.Entry<String, String> entry) {
        return entry.getKey() + "=" + entry.getValue();
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
