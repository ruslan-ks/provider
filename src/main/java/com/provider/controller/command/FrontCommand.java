package com.provider.controller.command;

import com.provider.constants.attributes.AppAttributes;
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
import java.nio.file.Path;
import java.nio.file.Paths;
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
     *
     * @return current session
     */
    protected final @NotNull HttpSession getSession() {
        return request.getSession();
    }

    /**
     * Returns signed-in user
     * @return Optional containing signed-in user if session is present and user attribute is saved to the session
     */
    protected final @NotNull Optional<User> getSessionUser() {
        return Optional.ofNullable(getSession().getAttribute(SessionAttributes.SIGNED_USER))
                .map(obj -> (User) obj);
    }

    /**
     * Returns locale obtained from UserSettings object that resides in the session scope
     * @return locale obtained from UserSettings object that resides in the session scope
     */
    protected final @NotNull String getLocale() {
        final Optional<String> localeOptional = Optional.ofNullable(getSession()
                        .getAttribute(SessionAttributes.USER_SETTINGS))
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

    /**
     * Returns image upload path - directory where all images are uploaded
     * @return image upload path
     */
    protected @NotNull Path getImageUploadPath() {
        final var uploadPathString = (String) request.getServletContext()
                .getAttribute(AppAttributes.TARIFF_IMAGE_UPLOAD_PATH);
        return Paths.get(uploadPathString);
    }

    /**
     * Returns new PaginationHelper object
     * @return new PaginationHelper object
     * @throws CommandParamException if pagination parameters parsing fails
     */
    protected PaginationHelper getPaginationHelper() throws CommandParamException {
        return new PaginationHelper();
    }

    /**
     * Useful class for pagination implementation<br>
     * When instantiating this class, pagination request parameters are obtained and parsed.<br>
     * Pagination parameter names constants are declared in {@link com.provider.constants.params.PaginationParams}<br>
     * If no parameter found, default value is used<br><br>
     * Pagination parameters: <br>
     * - Page number: name: {@code PaginationParams.PAGE_NUMBER}, default value: 1<br>
     * - Page size: name: {@code PaginationParams.PAGE_SIZE}, default value: constructor argument - {@code defaultPageSize}
     * or 5 if no arg constructor used<br><br>
     * Page size and offset can be obtained via getters<br>
     * For appropriate pagination, {@code computeAndSetPageCountAttribute} method must be called after object creation
     */
    protected class PaginationHelper {
        private static final Logger logger = LoggerFactory.getLogger(PaginationHelper.class);

        private final int pageNumber;
        private final int pageSize;

        /**
         * Creates new PaginationHelper with default page size of 5
         * @throws CommandParamException if pagination parameters parsing fails
         */
        PaginationHelper() throws CommandParamException {
            this(5);
        }

        /**
         * Creates new PaginationHelper with page size specified
         * @param defaultPageSize default page size, used if page size request parameter is missing
         * @throws CommandParamException if pagination parameters parsing fails
         */
        PaginationHelper(int defaultPageSize) throws CommandParamException {
            if (defaultPageSize < 1) {
                throw new IllegalArgumentException("defaultPageSize < 1: " + defaultPageSize);
            }
            final String pageNumberParam = getParam(PaginationParams.PAGE_NUMBER).orElse("1");
            pageNumber = CommandUtil.parseIntParam(pageNumberParam);
            if (pageNumber <= 0) {
                logger.warn("Invalid param '{}' == {} <= 0", PaginationParams.PAGE_NUMBER, pageNumber);
                throw new CommandParamException("Invalid param: '" + PaginationParams.PAGE_NUMBER + "' == " +
                        pageNumber + " <= 0");
            }
            final String pageSizeParam = getParam(PaginationParams.PAGE_SIZE).orElse(String.valueOf(defaultPageSize));
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

        /**
         * Computes and sets page count attribute based on records count and page size
         * @param recordsCount general records count
         */
        public void computeAndSetPageCountAttribute(long recordsCount) {
            final int pageCount = (int) Math.ceil((double) recordsCount / pageSize);
            request.setAttribute(RequestAttributes.PAGE_COUNT, pageCount);
        }
    }
}
