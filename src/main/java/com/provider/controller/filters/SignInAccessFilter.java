package com.provider.controller.filters;

import com.provider.constants.Paths;
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("doFilter: filter: {}", this);
        if (request instanceof HttpServletRequest) {
            final var httpServletRequest = (HttpServletRequest) request;
            if (isSomeoneSignedIn(httpServletRequest)) {
                request.getRequestDispatcher(Paths.USER_PANEL_PAGE).forward(request, response);
                return;
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
