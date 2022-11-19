package com.provider.util;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.provider.entity.dto.TariffDto;
import com.provider.entity.product.Tariff;
import com.provider.entity.product.TariffDuration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public class ItextTariffPdfWriter implements TariffPdfWriter {
    private static final String FONT_RESOURCE_NAME = "fonts/LiberationSerif-Regular.ttf";

    ItextTariffPdfWriter() {}

    public static ItextTariffPdfWriter newInstance() {
        return new ItextTariffPdfWriter();
    }

    @Override
    public void write(@NotNull TariffDto tariffDto, @NotNull OutputStream os) {
        final Tariff tariff = tariffDto.getTariff();
        final PdfWriter pdfWriter = new PdfWriter(os);
        final PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        try (var document = new Document(pdfDocument)) {
            final PdfFont font = PdfFontFactory.createFont(FONT_RESOURCE_NAME);
            document.setFont(font);

            final var titleParagraph = new Paragraph(tariff.getTitle());
            document.add(titleParagraph);

            final var priceParagraph = new Paragraph("Price: $" + tariff.getUsdPrice());
            document.add(priceParagraph);

            final TariffDuration duration = tariffDto.getDuration();
            final var durationParagraph = new Paragraph("Duration: " + duration.getMonths()
                    + " months, " + duration.getMinutes() + " minutes");
            document.add(durationParagraph);

            final var descriptionParagraph = new Paragraph(tariff.getDescription());
            document.add(descriptionParagraph);

            final var list = new com.itextpdf.layout.element.List();
            for (var service : tariffDto.getServices()) {
                list.add(service.getName() + ": " + service.getDescription());
            }
            document.add(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
