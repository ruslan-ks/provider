package com.provider.controller.command.impl;

import com.provider.constants.params.TariffParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class DownloadTariffPdfCommand extends FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(DownloadTariffPdfCommand.class);

    public DownloadTariffPdfCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public @NotNull CommandResult execute() throws DBException, ServletException, IOException, CommandParamException {
        final String tariffIdParam = getRequiredParam(TariffParams.ID);
        final int tariffId = CommandUtil.parseIntParam(tariffIdParam);

        final String locale = getLocale();

        final TariffService tariffService = serviceFactory.getTariffService();
        final Optional<TariffDto> tariffDtoOptional = tariffService.findTariffFullInfoById(tariffId, locale);

        if (tariffDtoOptional.isPresent()) {
            response.setContentType("application/pdf");
            try (var os = response.getOutputStream()) {
                tariffService.writeTariffPdf(tariffDtoOptional.get(), os);
                logger.debug("PDF document written! TariffDto: {}", tariffDtoOptional.get());
            }
        }
        return CommandResult.NO_VIEW;
    }
}
