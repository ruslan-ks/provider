package com.provider.controller.command.result;

import org.jetbrains.annotations.NotNull;

public class CommandResultImpl implements CommandResult {
    private final String viewLocation;

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
}
