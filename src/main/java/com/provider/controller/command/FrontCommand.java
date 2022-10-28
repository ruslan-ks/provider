package com.provider.controller.command;

import com.provider.constants.attributes.RequestAttributes;
import com.provider.constants.attributes.SessionAttributes;
import com.provider.constants.params.PaginationParams;
import com.provider.controller.command.exception.CommandParamException;
import com.provider.controller.command.result.CommandResult;
import com.provider.controller.command.result.CommandResultImpl;
import com.provider.dao.exception.DBException;
import com.provider.entity.EntityFactory;
import com.provider.entity.SimpleEntityFactory;
import com.provider.entity.settings.UserSettings;
import com.provider.entity.user.User;
import com.provider.service.ServiceFactory;
import com.provider.service.ServiceFactoryImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Command used by the front controller servlet.
 */
public abstract class FrontCommand {
    private static final Logger logger = LoggerFactory.getLogger(FrontCommand.class);

    /**
     * Incoming request
     */
    protected final HttpServletRequest request;

    /**
     * Resulting response
     */
    protected final HttpServletResponse response;

    /**
     * Service factory that should be used to obtain service objects
     */
    protected final ServiceFactory serviceFactory = ServiceFactoryImpl.newInstance();

    /**
     * Entity factory that should be used to instantiate entities
     */
    protected final EntityFactory entityFactory = SimpleEntityFactory.newInstance();

    protected FrontCommand(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * Executes user's request. Effects request and response objects(if provided).
     * Returns result via calling forward or redirect.
     * @return CommandResult - object containing result data - page location for redirect/forward and so on
     */
    public abstract @NotNull CommandResult execute()
            throws DBException, ServletException, IOException, CommandParamException;

    /**
     * Returns current session
     * @return current session
     */
    protected final @NotNull Optional<HttpSession> getSession() {
        return Optional.ofNullable(request.getSession());
    }

    /**
     * Returns signed-in user
     * @return Optional containing signed-in user if session is present and user attribute is saved to the session
     */
    protected final @NotNull Optional<User> getSessionUser() {
        return getSession().map(s -> (User) s.getAttribute(SessionAttributes.SIGNED_USER));
    }

    /**
     * Returns locale obtained from UserSettings object that resides in the session scope
     * @return locale obtained from UserSettings object that resides in the session scope
     */
    protected final @NotNull String getLocale() {
        final Optional<String> localeOptional = getSession()
                .map(s -> s.getAttribute(SessionAttributes.USER_SETTINGS))
                .map(obj -> (UserSettings) obj)
                .map(UserSettings::getLocale);
        if (localeOptional.isEmpty()) {
            logger.warn("Failed to obtain user settings locale");
        }
        return localeOptional.orElse("en");
    }

    /**
     * Extracts and returns request parameters, works only fo parameters of type String(not for String[])
     *
     * @param requiredParamNames required parameter names
     * @return Immutable Map containing parameter name -> value[] pairs; may contain more params than required
     * @throws CommandParamException if at least one of required parameters not found
     **/
    protected final @NotNull Map<String, String> getRequiredParams(@NotNull String @NotNull... requiredParamNames)
            throws CommandParamException {
        final Map<String, String> paramMap = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));
        for (var name : requiredParamNames) {
            if (!paramMap.containsKey(name)) {
                throw new CommandParamException("Parameter '" + name + "' is null");
            }
        }
        return paramMap;
    }

    protected final @NotNull String getRequiredParam(@NotNull String name) throws CommandParamException {
        return Optional.ofNullable(request.getParameter(name))
                .orElseThrow(() -> new CommandParamException("Parameter '" + name + "' is null"));
    }

    /**
     * Returns List containing request parameter values obtained via {@code request.getParameterValues(name)}
     * @param name parameter name
     * @return {@code List<String>} containing request parameter values
     * obtained via {@code request.getParameterValues(name)} or empty list if the parameter does not exist
     * @throws CommandParamException if parameter does not exist
     */
    protected final @NotNull Set<String> getRequiredParamValues(@NotNull String name) throws CommandParamException {
        final String[] params = request.getParameterValues(name);
        if (params == null) {
            throw new CommandParamException("Param '" + name + "' is null");
        }
        return new HashSet<>(Arrays.asList(params));
    }

    /**
     * Returns Optional containing request parameter value
     * @param name request parameter name
     * @return Optional containing request parameter value if one is present, empty optional otherwise
     **/
    protected final @NotNull Optional<String> getParam(@NotNull String name) {
        return Optional.ofNullable(request.getParameter(name));
    }

    /**
     * Returns Set of request parameter values
     * @param name request parameter name
     * @return Set of parameter values obtained via {@code request.getParameterValues(name)} or empty Set, if there is
     * no such parameter
     */
    protected final @NotNull Set<String> getParamValues(@NotNull String name) {
        return Optional.ofNullable(request.getParameterValues(name))
                .map(Arrays::asList)
                .map(HashSet::new)
                .map(hs -> (Set<String>) hs)
                .orElseGet(Collections::emptySet);
    }

    /**
     * Factory method for {@code CommandResult}
     * @param location location where user will be forwarded or redirected
     * @return new {@code CommandResult} instance
     */
    protected @NotNull CommandResult newCommandResult(@NotNull String location) {
        return CommandResultImpl.of(location);
    }

    protected PaginationHelper getPaginationHelper() throws CommandParamException {
        return new PaginationHelper();
    }

    protected class PaginationHelper {
        private static final Logger logger = LoggerFactory.getLogger(PaginationHelper.class);

        private static final int DEFAULT_PAGE_SIZE = 5;

        private final int pageNumber;
        private final int pageSize;

        PaginationHelper() throws CommandParamException {
            final String pageNumberParam = getParam(PaginationParams.PAGE_NUMBER).orElse("1");
            pageNumber = CommandUtil.parseIntParam(pageNumberParam);
            if (pageNumber <= 0) {
                logger.warn("Invalid param '{}' == {} <= 0", PaginationParams.PAGE_NUMBER, pageNumber);
                throw new CommandParamException("Invalid param: '" + PaginationParams.PAGE_NUMBER + "' == " +
                        pageNumber + " <= 0");
            }
            final String pageSizeParam = getParam(PaginationParams.PAGE_SIZE).orElse(String.valueOf(DEFAULT_PAGE_SIZE));
            pageSize = CommandUtil.parseIntParam(pageSizeParam);
            if (pageSize < 1) {
                logger.warn("Invalid param '{}' == {} < 1", PaginationParams.PAGE_SIZE, pageSize);
                throw new CommandParamException("Invalid param: '" + PaginationParams.PAGE_SIZE + "' == " + pageSize +
                        " < 1");
            }
        }

        public int getPageSize() {
            return pageSize;
        }

        public long getOffset() {
            return (long) (pageNumber - 1) * pageSize;
        }

        public void computeAndSetPageCountAttribute(long recordsCount) {
            final int pageCount = (int) Math.ceil((double) recordsCount / pageSize);
            request.setAttribute(RequestAttributes.PAGE_COUNT, pageCount);
        }
    }
}
