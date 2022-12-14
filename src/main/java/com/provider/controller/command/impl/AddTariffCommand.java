package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.upload.FileUploader;
import com.provider.controller.upload.ImageUploader;
import com.provider.controller.upload.InvalidMimeTypeException;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
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
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static com.provider.constants.params.TariffParams.*;

public class AddTariffCommand extends AdminCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddTariffCommand.class);

    public AddTariffCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAuthorized(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {
        final Map<String, String> params = getRequiredParams(TITLE, DESCRIPTION, USD_PRICE, STATUS, DURATION_MONTHS,
                DURATION_MINUTES);
        final Set<String> tariffServiceIdParams = getRequiredParamValues(SERVICE_IDS);

        // File uploading
        final var uploadPath = getImageUploadPath();
        final Part imagePart = request.getPart(IMAGE);
        final FileUploader fileUploader = ImageUploader.newInstance();
        final String uploadedImageFileName;
        final CommandResult commandResult = newCommandResult(Paths.TARIFFS_MANAGEMENT_PAGE);
        try {
            uploadedImageFileName = fileUploader.upload(imagePart, uploadPath);
        } catch (InvalidMimeTypeException ex) {
            logger.warn("Failed to load tariff image! Invalid MIME-type! Admin: {}", user);
            return commandResult
                    .addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_IMAGE_UPLOAD_FAIL)
                    .addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_INSERT_FAIL);
        }

        // Tariff parameters extracting and parsing
        final String title = params.get(TITLE);
        final String description = params.get(DESCRIPTION);
        final Tariff.Status status = CommandUtil.parseTariffStatusParam(params.get(STATUS));
        final BigDecimal usdPrice = CommandUtil.parseBigDecimalParam(params.get(USD_PRICE));
        final int months = CommandUtil.parseIntParam(params.get(DURATION_MONTHS));
        final int minutes = CommandUtil.parseIntParam(params.get(DURATION_MINUTES));
        final Set<Integer> serviceIds = CommandUtil.parseIntParams(tariffServiceIdParams);

        final Tariff tariff = entityFactory.newTariff(0, title, description, status, usdPrice, uploadedImageFileName);
        final TariffDuration tariffDuration = entityFactory.newTariffDuration(0, months, minutes);

        final TariffService tariffService = serviceFactory.getTariffService();
        boolean inserted = false;
        try {
            inserted = tariffService.insertTariff(tariff, tariffDuration, serviceIds);
        } catch (ValidationException ex) {
            logger.warn("Invalid tariff data format", ex);
            logger.warn("Invalid tariff data format: {}", tariff);
            commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.INVALID_TARIFF_PARAMS);
        }
        if (inserted) {
            return commandResult.addMessage(CommandResult.MessageType.SUCCESS, Messages.TARIFF_INSERT_SUCCESS);
        }
        return commandResult.addMessage(CommandResult.MessageType.FAIL, Messages.TARIFF_INSERT_FAIL);
    }
}
