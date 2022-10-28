package com.provider.controller.command.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.CommandUtil;
import com.provider.controller.command.FrontCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
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

        // TODO: refactor
        final String locale = "en";

        final TariffService tariffService = serviceFactory.getTariffService();
        final Optional<TariffDto> tariffOptional = tariffService.findTariffFullInfoById(tariffId, locale);

        if (tariffOptional.isPresent()) {
            final Tariff tariff = tariffOptional.get().getTariff();
            response.setContentType("application/pdf");
            final PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
            final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            try (var document = new Document(pdfDocument)) {
                final var titleParagraph = new Paragraph(tariff.getTitle());
                document.add(titleParagraph);

                final var priceParagraph = new Paragraph("Price: $" + tariff.getUsdPrice());
                document.add(priceParagraph);

                final TariffDuration duration = tariffOptional.get().getDuration();
                final var durationParagraph = new Paragraph("Duration: " + duration.getMonths()
                        + " months, " + duration.getMinutes() + " minutes");
                document.add(durationParagraph);

                final var descriptionParagraph = new Paragraph(tariff.getDescription());
                document.add(descriptionParagraph);

                final var list = new List();
                for (var service : tariffOptional.get().getServices()) {
                    list.add(service.getName() + ": " + service.getDescription());
                }
                document.add(list);
            }
            logger.debug("PDF document written");
        }

        return CommandResult.NO_VIEW;
    }
}
