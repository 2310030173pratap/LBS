package com.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("LibraryLogger");

    static {
        try {
            Path logsDir = Paths.get("logs");
            Files.createDirectories(logsDir);

            FileHandler fileHandler = new FileHandler(logsDir.resolve("library.log").toString(), true);
            fileHandler.setFormatter(new SimpleFormatter());

            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LoggerUtil() {
    }

    public static void logInfo(String message) {
        LOGGER.info(message);
    }

    public static void logError(String message, Throwable throwable) {
        if (throwable == null) {
            LOGGER.log(Level.SEVERE, message);
        } else {
            LOGGER.log(Level.SEVERE, message, throwable);
        }
    }
}