package com.provider.controller;

import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.exception.CommandAccessException;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.exception.IllegalCommandException;
import com.provider.controller.command.impl.FrontCommandFactoryImpl;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
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
        } catch (CommandAccessException ex) {
            logger.debug("Controller caught exception", ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalCommandException | CommandParamException ex) {
            logger.debug("Controller caught exception", ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (DBException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Front controller servlet caught exception", ex);
        }
        return Optional.empty();
    }
}
