package com.provider.controller.filters;

import com.provider.constants.attributes.SessionAttributes;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "SignInAccessFilter", urlPatterns = {"/signIn"})
public class SignInAccessFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(SignInAccessFilter.class);

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("doFilter: filter: {}", this);
        if (request instanceof HttpServletRequest) {
            final var httpServletRequest = (HttpServletRequest) request;
            if (isSomeoneSignedIn(httpServletRequest)) {
                final ServletContext context = request.getServletContext();
                final String userPanelPath = context.getInitParameter("userPanel");
                request.getRequestDispatcher(userPanelPath).forward(request, response);
            }
        } else {
            logger.error("Request object {} cannot be cast to HttServletRequest", request);
        }

        chain.doFilter(request, response);
    }

    private boolean isSomeoneSignedIn(HttpServletRequest request) {
        return request.getSession().getAttribute(SessionAttributes.SIGNED_USER) != null;
    }
}
