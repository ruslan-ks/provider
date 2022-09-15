package com.provider.controller;

import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.FrontCommandFactoryImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The only servlet of the whole application. All requests are handled here.
 * Uses Command pattern to obtain a command for request handling, then executes this command.
 */
@WebServlet(name = "FrontControllerServlet", value = "/controller")
public class FrontControllerServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(FrontControllerServlet.class);

    // Factory that is used to obtain appropriate command
    final FrontCommandFactory frontCommandFactory = FrontCommandFactoryImpl.newInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    /**
     * Handles all incoming requests.
     * Obtains appropriate command via frontCommandFactory.getCommand(), then executes it.
     * @param request incoming request
     * @param response resulting response
     */
    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            final FrontCommand frontCommand = frontCommandFactory.getCommand(request, response, getServletConfig());
            frontCommand.execute();
        } catch (Exception ex) {
            logger.error("Front controller servlet caught exception", ex);
        }
    }
}
