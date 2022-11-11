package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResults;
import com.provider.controller.upload.FileUploader;
import com.provider.controller.upload.ImageUploader;
import com.provider.controller.upload.InvalidMimeTypeException;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.user.User;
import com.provider.service.TariffService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChangeTariffImageCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(ChangeTariffImageCommand.class);

    public ChangeTariffImageCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final int tariffId = CommandUtil.parseIntParam(getRequiredParam(TariffParams.ID));

        final TariffService tariffService = serviceFactory.getTariffService();
        final Tariff tariff = tariffService.findTariffById(tariffId)
                .orElseThrow(() -> new CommandParamException("Tariff not found! tariff id: " + tariffId));

        final CommandResult commandResult =  CommandResults.editTariffPage(tariffId, getLocale());

        // Loading new image
        final Part imagePart = request.getPart(TariffParams.IMAGE);
        final FileUploader fileUploader = ImageUploader.newInstance();
        final String uploadedImageFileName;
        try {
            uploadedImageFileName = fileUploader.upload(imagePart, getImageUploadPath());
        } catch (InvalidMimeTypeException ex) {
            logger.warn("Failed to load tariff image! Invalid MIME-type! Admin: {}", user);
            return commandResult
                    .addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_IMAGE_UPLOAD_FAIL)
                    .addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_EDIT_FAIL);
        }

        // Deleting old image
        final String oldImageFileName = tariff.getImageFileName();
        final Path oldImageFilePath = java.nio.file.Paths.get(getImageUploadPath().toString(), oldImageFileName);
        try {
            Files.delete(oldImageFilePath);
            logger.info("Deleted old image: {}", oldImageFilePath);
        } catch (NoSuchFileException ex) {
            logger.error("Failed to delete old image: {}", oldImageFilePath);
            final Path uploadedImagePath = Paths.get(getImageUploadPath().toString(), uploadedImageFileName);
            Files.delete(uploadedImagePath);
            throw ex;
        }

        tariff.setImageFileName(uploadedImageFileName);
        return tryUpdateTariff(tariff, tariffService, commandResult);
    }

    private CommandResult tryUpdateTariff(@NotNull Tariff tariff, @NotNull TariffService tariffService,
                                    @NotNull CommandResult commandResult) throws DBException {
        boolean updated = false;
        try {
            updated = tariffService.updateTariff(tariff);
        } catch (ValidationException ex) {
            logger.warn("Failed to update tariff! Invalid tariff data!", ex);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_TARIFF_PARAMS);
        }
        if (updated) {
            logger.info("Updated tariff: {}", tariff);
            return commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.TARIFF_EDIT_SUCCESS);
        }
        logger.warn("Failed to update tariff: {}", tariff);
        return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_EDIT_FAIL);
    }
}
