package com.provider.controller.command.result;

import com.provider.constants.params.MessageParams;
import com.provider.util.ParameterizedUrl;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandResultImpl implements CommandResult {
    private final String viewLocation;

    // key - message param name, value - param value
    private final List<Pair<String, String>> messages = new ArrayList<>();

    CommandResultImpl(@NotNull String viewLocation) {
        this.viewLocation = viewLocation;
    }

    public static CommandResultImpl of(@NotNull String viewLocation) {
        return new CommandResultImpl(viewLocation);
    }

    @Override
    public @NotNull String getViewLocation() {
        final ParameterizedUrl parameterizedUrl = ParameterizedUrl.of(viewLocation);
        for (var pair : messages) {
            parameterizedUrl.addParam(pair.getLeft(), pair.getRight());
        }
        return parameterizedUrl.getString();
    }

    private @NotNull String messageTypeToParam(@NotNull MessageType messageType) {
        final Map<MessageType, String> messageTypeParamNameMap = Map.of(
                MessageType.FAIL, MessageParams.ERROR,
                MessageType.SUCCESS, MessageParams.SUCCESS
        );
        return Optional.ofNullable(messageTypeParamNameMap.get(messageType))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public @NotNull CommandResultImpl addMessage(@NotNull MessageType messageType, @NotNull String message) {
        messages.add(Pair.of(messageTypeToParam(messageType), message));
        return this;
    }
}
