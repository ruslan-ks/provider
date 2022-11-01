package com.provider.controller;

import com.provider.constants.Paths;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.exception.IllegalCommandException;
import com.provider.controller.command.impl.FrontCommandFactoryImpl;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

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
        final Optional<CommandResult> result = handleRequest(request, response);
        if (result.isPresent()) {
            if (result.get() == CommandResult.NO_VIEW) {
                return;
            }
            logger.debug("Forward after GET Request - Front Controller Servlet");
            request.getRequestDispatcher(result.get().getViewLocation())
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("POST Request - Front Controller Servlet");
        final Optional<CommandResult> result = handleRequest(request, response);
        if (result.isPresent()) {
            if (result.get() == CommandResult.NO_VIEW) {
                return;
            }
            logger.debug("Redirect after POST Request - Front Controller Servlet");
            response.sendRedirect(result.get().getViewLocation());
        }
    }

    /**
     * Handles all incoming requests.
     * Obtains appropriate command via frontCommandFactory.getCommand(), then executes it.
     * @param request incoming request
     * @param response resulting response
     */
    private Optional<CommandResult> handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            final FrontCommand frontCommand = frontCommandFactory.getCommand(request, response, getServletConfig());
            return Optional.of(frontCommand.execute());
        } catch (IllegalCommandException | CommandParamException ex) {
            logger.warn("Controller caught exception", ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forwardToBadRequestPage(request, response);
        } catch (DBException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Front controller servlet caught exception", ex);
            forwardToInternalServerErrorPage(request, response);
        }
        return Optional.empty();
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
}
