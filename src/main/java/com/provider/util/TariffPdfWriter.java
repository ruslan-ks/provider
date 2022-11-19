package com.provider.util;

import com.provider.entity.dto.TariffDto;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public interface TariffPdfWriter {
    /**
     * Writes tariff data in pdf format
     * @param tariffDto tariff data to be written
     * @param os output stream where tariff data will be written
     */
    void write(@NotNull TariffDto tariffDto, @NotNull OutputStream os);
}
