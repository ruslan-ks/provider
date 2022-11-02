package com.provider.controller.command.impl;

import com.provider.constants.params.TariffParams;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TariffImageCommand extends FrontCommand {
    public TariffImageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws DBException, ServletException, IOException, CommandParamException {
        final String tariffImageFileName = getRequiredParam(TariffParams.IMAGE_FILE_NAME);

        final Path imagePath = java.nio.file.Paths.get(getImageUploadPath().toString(), tariffImageFileName);
        try (var inputStream = Files.newInputStream(imagePath);
                var outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
        }
        return CommandResult.NO_VIEW;
    }
}
