package com.provider.controller.upload;

import jakarta.servlet.http.Part;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class ImageUploader implements FileUploader {
    private static final Logger logger = LoggerFactory.getLogger(ImageUploader.class);

    private static final Random random = new Random(System.currentTimeMillis());

    ImageUploader() {}

    public static ImageUploader newInstance() {
        return new ImageUploader();
    }

    @Override
    public @NotNull String upload(@NotNull Part part, @NotNull Path uploadPath)
            throws IOException, InvalidMimeTypeException {
        if (!Files.exists(uploadPath)) {
            logger.info("Creating directories for file uploading: {}", uploadPath);
            Files.createDirectories(uploadPath);
        }
        final String submittedFileName = java.nio.file.Paths.get(part.getSubmittedFileName()).getFileName().toString();
        try (InputStream is = part.getInputStream()) {
            try {
                // TODO: is this toString() really necessary?
                ImageIO.read(is).toString();
                // It's an image (only BMP, GIF, JPG and PNG are recognized).
            } catch (Exception e) {
                throw new InvalidMimeTypeException("File " + submittedFileName + " is not an image");
            }
        }

        final String latinFileName = submittedFileName.replaceAll("[^A-Za-z0-9_.]+", "A");

        String uploadedFileName;
        do {
            uploadedFileName = random.nextInt(Integer.MAX_VALUE) + latinFileName;
        } while (Files.exists(Paths.get(uploadPath + File.separator + uploadedFileName)));
        part.write(uploadPath + File.separator + uploadedFileName);
        return uploadedFileName;
    }
}
