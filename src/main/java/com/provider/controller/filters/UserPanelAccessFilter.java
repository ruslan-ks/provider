package com.provider.controller.filters;

import com.provider.constants.attributes.SessionAttributes;
import com.provider.entity.user.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(filterName = "AccessFilter", urlPatterns = {"/userPanel"})
        //dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class UserPanelAccessFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(UserPanelAccessFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.debug("doFilter: filter: {}", this);

        // TODO: this cast is used in multiple Filters - refactor this
        // ClassCastException would be satisfying here
        final var httpServletRequest = (HttpServletRequest) request;
        final HttpSession session = httpServletRequest.getSession();
        final var signedUser = (User) session.getAttribute(SessionAttributes.SIGNED_USER);
        if (signedUser != null) {
            chain.doFilter(request, response);
        } else {
            final ServletContext context = request.getServletContext();
            final String signInPageUrl = context.getInitParameter("signIn");
            request.getRequestDispatcher(signInPageUrl).forward(request, response);
        }
    }
}
