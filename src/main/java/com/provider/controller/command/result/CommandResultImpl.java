package com.provider.controller.command.result;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandResultImpl implements CommandResult {
    private final String viewLocation;

    // key - message param name, value - param value
    private final Queue<Pair<MessageType, String>> messages = new LinkedList<>();

    CommandResultImpl(@NotNull String viewLocation) {
        this.viewLocation = viewLocation;
    }

    public static CommandResultImpl of(@NotNull String viewLocation) {
        return new CommandResultImpl(viewLocation);
    }

    @Override
    public @NotNull String getViewLocation() {
        return viewLocation;
    }

    @Override
    public @NotNull CommandResultImpl addMessage(@NotNull MessageType messageType, @NotNull String message) {
        messages.add(Pair.of(messageType, message));
        return this;
    }

    @Override
    public @NotNull Queue<Pair<MessageType, String>> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "CommandResultImpl{" +
                "viewLocation='" + viewLocation + '\'' +
                ", messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandResultImpl that = (CommandResultImpl) o;
        return Objects.equals(viewLocation, that.viewLocation)
                && Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewLocation, messages);
    }
}
