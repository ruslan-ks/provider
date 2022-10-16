package com.provider.controller.command.impl;

import com.provider.constants.Paths;
import com.provider.controller.command.AdminCommand;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import static com.provider.constants.params.TariffParams.*;

public class AddTariffCommand extends AdminCommand {
    public AddTariffCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        super(request, response);
    }

    @Override
    protected @NotNull CommandResult executeAccessed(@NotNull User user)
            throws DBException, ServletException, IOException, CommandParamException {

        final Map<String, String> params = getRequestParams(TITLE, DESCRIPTION, USD_PRICE, STATUS, DURATION_MONTHS,
                DURATION_MINUTES, SERVICE_IDS);
        for (var entry : params.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        return CommandResultImpl.of(Paths.TARIFFS_MANAGEMENT_PAGE);
    }
}
