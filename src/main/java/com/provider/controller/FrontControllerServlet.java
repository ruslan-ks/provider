package com.provider.controller;

import com.provider.constants.Paths;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.exception.IllegalCommandException;
import com.provider.controller.command.exception.UserAccessRightsException;
import com.provider.controller.command.impl.FrontCommandFactoryImpl;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Queue;

/**
 * The only servlet of the whole application. All requests are handled here.
 * Uses Command pattern to obtain a command for request handling, then executes this command.
 */
@WebServlet(name = "FrontControllerServlet", value = "/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class FrontControllerServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(FrontControllerServlet.class);

    private final FrontCommandFactory frontCommandFactory = FrontCommandFactoryImpl.newInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("GET Request - Front Controller Servlet");
        final Optional<String> location = handleRequest(request, response);
        if (location.isPresent()) {
            logger.debug("Forward after GET Request - Front Controller Servlet");
            request.getRequestDispatcher(location.get()).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("POST Request - Front Controller Servlet");
        final Optional<String> location = handleRequest(request, response);
        if (location.isPresent()) {
            logger.debug("Redirect after POST Request - Front Controller Servlet");
            response.sendRedirect(location.get());
        }
    }

    /**
     * Handles all incoming requests.
     * Obtains appropriate command and executes it.
     * @param request incoming request
     * @param response resulting response
     * @return resulting location
     */
    private Optional<String> handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final FrontCommand frontCommand = frontCommandFactory.getCommand(request, response, getServletConfig());
            final CommandResult result = frontCommand.execute();
            addMessages(request, result.getMessages());
            if (result != CommandResult.NO_VIEW) {
                return Optional.of(result.getViewLocation());
            }
        } catch (IllegalCommandException | CommandParamException ex) {
            logger.warn("Bad request", ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forwardToBadRequestPage(request, response);
        } catch (UserAccessRightsException ex) {
            logger.warn("Unauthorized", ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            forwardToUnauthorizedPage(request, response);
        } catch (DBException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Front controller servlet caught exception", ex);
            forwardToInternalServerErrorPage(request, response);
        }
        return Optional.empty();
    }

    private void addMessages(@NotNull HttpServletRequest request,
                             @NotNull Queue<Pair<CommandResult.MessageType, String>> messages) {
        final HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        final var messagesAttr = (Queue<Pair<CommandResult.MessageType, String>>)
                session.getAttribute(SessionAttributes.MESSAGES);
        if (messagesAttr == null) {
            session.setAttribute(SessionAttributes.MESSAGES, messages);
        } else {
            messagesAttr.addAll(messages);
        }
    }

    private void forwardToBadRequestPage(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Paths.BAD_REQUEST_ERROR_JSP).forward(request, response);
    }

    private void forwardToInternalServerErrorPage(@NotNull HttpServletRequest request,
                                                  @NotNull HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Paths.INTERNAL_SERVER_ERROR_JSP).forward(request, response);
    }

    private void forwardToUnauthorizedPage(@NotNull HttpServletRequest request,
                                           @NotNull HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Paths.UNAUTHORIZED_ERROR_JSP).forward(request, response);
    }
}
