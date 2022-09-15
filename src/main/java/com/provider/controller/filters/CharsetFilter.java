package com.provider.controller.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "CharsetFilter",
        urlPatterns = "/*",
        initParams = { @WebInitParam(name = "defaultCharset", value = "UTF-8") })
public class CharsetFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CharsetFilter.class);

    private String defaultCharset;

    public void init(FilterConfig config) throws ServletException {
        defaultCharset = config.getInitParameter("defaultCharset");
        if (defaultCharset == null) {
            throw new RuntimeException("Default charset init parameter not specified");
        }
        logger.info("Filter {} was successfully initialized", this);
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        request.setCharacterEncoding(defaultCharset);

        logger.debug("Set request charset: request {}, charset{}", request, defaultCharset);

        chain.doFilter(request, response);
    }
}
