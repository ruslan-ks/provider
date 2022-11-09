package com.provider.controller.command.impl;

import com.provider.constants.Messages;
import com.provider.constants.Paths;
import com.provider.constants.params.ServiceParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.product.Service;
import com.provider.service.TariffService;
import com.provider.service.exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddServiceCommandTest extends AbstractCommandTest {
    @ParameterizedTest
    @MethodSource("com.provider.TestData#serviceStream")
    public void testPositiveExecuteAuthorized(Service service)
            throws DBException, ValidationException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_PAGE)
                .addMessage(CommandResult.MessageType.SUCCESS, Messages.SERVICE_INSERT_SUCCESS);
        testAddService(service, true, expectedResult);
    }

    @ParameterizedTest
    @MethodSource("com.provider.TestData#serviceStream")
    public void testNegativeExecuteAuthorized(Service service)
            throws DBException, ValidationException, ServletException, CommandParamException, IOException {
        final CommandResult expectedResult = CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_PAGE)
                .addMessage(CommandResult.MessageType.FAIL, Messages.SERVICE_INSERT_FAIL);
        testAddService(service, false, expectedResult);
    }

    private void testAddService(Service service, boolean insertServiceResult, CommandResult expectedCommandResult)
            throws DBException, ValidationException, ServletException, CommandParamException, IOException {
        final Map<String, String> paramMap = Map.of(
                ServiceParams.NAME, service.getName(),
                ServiceParams.DESCRIPTION, service.getDescription());
        final HttpServletRequest request = MockUtil.mockRequestWithParams(paramMap);

        final TariffService tariffService = mock(TariffService.class);
        when(tariffService.insertService(service)).thenReturn(insertServiceResult);
        when(serviceFactory.getTariffService()).thenReturn(tariffService);

        final AddServiceCommand addServiceCommand = new AddServiceCommand(request, response);
        addServiceCommand.setServiceFactory(serviceFactory);

        assertEquals(expectedCommandResult, addServiceCommand.executeAuthorized(ADMIN));
    }
}