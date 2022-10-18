package com.provider.controller.upload;

import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public interface FileUploader {
    @NotNull String upload(@NotNull Part part, @NotNull Path uploadPath) throws IOException, InvalidMimeTypeException;
}
