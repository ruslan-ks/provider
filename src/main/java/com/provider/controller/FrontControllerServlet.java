package com.provider.controller;

import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.FrontCommandFactory;
import com.provider.controller.command.SimpleFrontCommandFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "FrontControllerServlet", value = "/FrontControllerServlet")
public class FrontControllerServlet extends HttpServlet {
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

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        final FrontCommandFactory frontCommandFactory = SimpleFrontCommandFactory.newInstance();
        final FrontCommand frontCommand = frontCommandFactory.getCommand(request, response, getServletConfig());
        frontCommand.execute();
    }
}
