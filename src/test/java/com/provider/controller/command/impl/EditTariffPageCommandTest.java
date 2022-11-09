package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.params.EditParams;
import com.provider.constants.params.TariffParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.dto.TariffDto;
import com.provider.service.TariffService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTariffPageCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#tariffDtoStream")
    public void testExecuteAuthorized(TariffDto tariffDto)
            throws DBException, ServletException, CommandParamException, IOException {
        final String locale = "uk";
        final Map<String, String> paramMap = Map.of(
                TariffParams.ID, String.valueOf(tariffDto.getTariff().getId()),
                EditParams.LOCALE, locale
        );
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final TariffService tariffService = mock(TariffService.class);
        when(tariffService.findTariffFullInfoById(tariffDto.getTariff().getId(), locale))
                .thenReturn(Optional.of(tariffDto));
        when(serviceFactory.getTariffService()).thenReturn(tariffService);

        final EditTariffPageCommand command = new EditTariffPageCommand(request, response);
        command.setServiceFactory(serviceFactory);
        final CommandResult expectedResult = CommandResultImpl.of(Paths.EDIT_TARIFF_JSP);
        assertEquals(expectedResult, command.executeAuthorized(ADMIN));

        verify(request).setAttribute(RequestAttributes.TARIFF_DTO, tariffDto);
    }
}