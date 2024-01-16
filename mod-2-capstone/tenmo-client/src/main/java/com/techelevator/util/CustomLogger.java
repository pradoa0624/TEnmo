package com.techelevator.util;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.springframework.web.client.RestClientResponseException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLogger implements BasicLogger {

    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    private final Logger logger;
    private PrintWriter pw = null;
    private static final String DIRECTORY_NAME = "tenmo-client";

    // Constructor that takes a Class parameter for the logger name
    public CustomLogger(Class<?> loggerClass) {
        this.logger = Logger.getLogger(loggerClass);

        // Set up the log diretory if it doesn't exist
        setupLogDirectory();
    }

    public void log(CustomLogger.Level level, String message, RestClientResponseException restClientException) {
        // Implement your logging logic here
        // You can use the loggerClass and the provided parameters to perform the actual logging
        try {
            if (pw == null) {
                String userDir = System.getProperty("user.dir");

                if (!userDir.endsWith(DIRECTORY_NAME)) {
                    userDir += File.separator + DIRECTORY_NAME;
                }

                String logFilename = userDir + File.separator + "logs/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
                pw = new PrintWriter(new FileOutputStream(logFilename, true));
            }
            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + level + " " + message);
            if (restClientException != null) {
                restClientException.printStackTrace(pw);
            }
            pw.flush();
        } catch (FileNotFoundException e) {
            throw new BasicLoggerException(e.getMessage());
        }
    }

    public void log(CustomLogger.Level level, String message, Exception exception) {
        // Implement your logging logic here for the generic Exception case
        // You can use the loggerClass and the provided parameters to perform the actual logging
        try {
            if (pw == null) {
                String userDir = System.getProperty("user.dir");

                if (!userDir.endsWith(DIRECTORY_NAME)) {
                    userDir += File.separator + DIRECTORY_NAME;
                }

                String logFilename = userDir + File.separator + "logs/" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";
                pw = new PrintWriter(new FileOutputStream(logFilename, true));
            }
            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + level + " " + message);
            if (exception != null) {
                exception.printStackTrace(pw);
            }
            pw.flush();
        } catch (FileNotFoundException e) {
            throw new BasicLoggerException(e.getMessage());
        }
    }

    public void log(String message) {
        // Implementation to log the message
        // This might involve writing to a file, printing to console, etc.
        System.out.println(message);
    }

    // Helper method to set up the log directory
    private void setupLogDirectory() {
        try {
            String userDir = System.getProperty("user.dir");

            if (!userDir.endsWith(DIRECTORY_NAME)) {
                userDir += File.separator + DIRECTORY_NAME;
            }

            File logDirectory = new File(userDir + File.separator + "logs");

            if (!logDirectory.exists()) {
                if (logDirectory.mkdirs()) {
                    System.out.println("Log directory created: " + logDirectory.getAbsolutePath());
                } else {
                    System.err.println("Failed to create log directory: " + logDirectory.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    @Override
    public void log(Logger.Level level, Object message, Throwable throwable) {
        // Implement your logging logic here
        // You can use the loggerClass and the provided parameters to perform the actual logging
    }

    @Override
    public void log(Logger.Level level, String s, Object o, Throwable throwable) {

    }

    @Override
    public void log(String s, Logger.Level level, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void logv(Logger.Level level, String s, Object... objects) {

    }

    @Override
    public void logv(Logger.Level level, String s, Object o) {

    }

    @Override
    public void logv(Logger.Level level, String s, Object o, Object o1) {

    }

    @Override
    public void logv(Logger.Level level, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void logv(Logger.Level level, Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void logv(Logger.Level level, Throwable throwable, String s, Object o) {

    }

    @Override
    public void logv(Logger.Level level, Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void logv(Logger.Level level, Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void logv(String s, Logger.Level level, Throwable throwable, String s1, Object... objects) {

    }

    @Override
    public void logv(String s, Logger.Level level, Throwable throwable, String s1, Object o) {

    }

    @Override
    public void logv(String s, Logger.Level level, Throwable throwable, String s1, Object o, Object o1) {

    }

    @Override
    public void logv(String s, Logger.Level level, Throwable throwable, String s1, Object o, Object o1, Object o2) {

    }

    @Override
    public void logf(Logger.Level level, String s, Object... objects) {

    }

    @Override
    public void logf(Logger.Level level, String s, Object o) {

    }

    @Override
    public void logf(Logger.Level level, String s, Object o, Object o1) {

    }

    @Override
    public void logf(Logger.Level level, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void logf(Logger.Level level, Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void logf(Logger.Level level, Throwable throwable, String s, Object o) {

    }

    @Override
    public void logf(Logger.Level level, Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void logf(Logger.Level level, Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void logf(String s, Logger.Level level, Throwable throwable, String s1, Object o) {

    }

    @Override
    public void logf(String s, Logger.Level level, Throwable throwable, String s1, Object o, Object o1) {

    }

    @Override
    public void logf(String s, Logger.Level level, Throwable throwable, String s1, Object o, Object o1, Object o2) {

    }

    @Override
    public void logf(String s, Logger.Level level, Throwable throwable, String s1, Object... objects) {

    }

    @Override
    public boolean isEnabled(Logger.Level level) {

        return true;  // For simplicity, always return true
    }

    @Override
    public boolean isTraceEnabled() {

        return true;  // For simplicity, always return true
    }

    @Override
    public void trace(Object o) {

    }

    @Override
    public void trace(Object o, Throwable throwable) {

    }

    @Override
    public void trace(String s, Object o, Throwable throwable) {

    }

    @Override
    public void trace(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void tracev(String s, Object... objects) {

    }

    @Override
    public void tracev(String s, Object o) {

    }

    @Override
    public void tracev(String s, Object o, Object o1) {

    }

    @Override
    public void tracev(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void tracev(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void tracev(Throwable throwable, String s, Object o) {

    }

    @Override
    public void tracev(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void tracev(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void tracef(String s, Object... objects) {

    }

    @Override
    public void tracef(String s, Object o) {

    }

    @Override
    public void tracef(String s, Object o, Object o1) {

    }

    @Override
    public void tracef(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void tracef(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void tracef(Throwable throwable, String s, Object o) {

    }

    @Override
    public void tracef(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void tracef(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void tracef(String s, int i) {

    }

    @Override
    public void tracef(String s, int i, int i1) {

    }

    @Override
    public void tracef(String s, int i, Object o) {

    }

    @Override
    public void tracef(String s, int i, int i1, int i2) {

    }

    @Override
    public void tracef(String s, int i, int i1, Object o) {

    }

    @Override
    public void tracef(String s, int i, Object o, Object o1) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i, int i1) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i, Object o) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i, int i1, int i2) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i, int i1, Object o) {

    }

    @Override
    public void tracef(Throwable throwable, String s, int i, Object o, Object o1) {

    }

    @Override
    public void tracef(String s, long l) {

    }

    @Override
    public void tracef(String s, long l, long l1) {

    }

    @Override
    public void tracef(String s, long l, Object o) {

    }

    @Override
    public void tracef(String s, long l, long l1, long l2) {

    }

    @Override
    public void tracef(String s, long l, long l1, Object o) {

    }

    @Override
    public void tracef(String s, long l, Object o, Object o1) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l, long l1) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l, Object o) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l, long l1, long l2) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l, long l1, Object o) {

    }

    @Override
    public void tracef(Throwable throwable, String s, long l, Object o, Object o1) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    public void debug(String o, int recipientId) {
    }

    @Override
    public void debug(Object o) {

    }

    @Override
    public void debug(Object o, Throwable throwable) {

    }

    @Override
    public void debug(String s, Object o, Throwable throwable) {

    }

    @Override
    public void debug(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void debugv(String s, Object... objects) {

    }

    @Override
    public void debugv(String s, Object o) {

    }

    @Override
    public void debugv(String s, Object o, Object o1) {

    }

    @Override
    public void debugv(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void debugv(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void debugv(Throwable throwable, String s, Object o) {

    }

    @Override
    public void debugv(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void debugv(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void debugf(String s, Object... objects) {

    }

    @Override
    public void debugf(String s, Object o) {

    }

    @Override
    public void debugf(String s, Object o, Object o1) {

    }

    @Override
    public void debugf(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void debugf(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void debugf(Throwable throwable, String s, Object o) {

    }

    @Override
    public void debugf(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void debugf(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void debugf(String s, int i) {

    }

    @Override
    public void debugf(String s, int i, int i1) {

    }

    @Override
    public void debugf(String s, int i, Object o) {

    }

    @Override
    public void debugf(String s, int i, int i1, int i2) {

    }

    @Override
    public void debugf(String s, int i, int i1, Object o) {

    }

    @Override
    public void debugf(String s, int i, Object o, Object o1) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i, int i1) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i, Object o) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i, int i1, int i2) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i, int i1, Object o) {

    }

    @Override
    public void debugf(Throwable throwable, String s, int i, Object o, Object o1) {

    }

    @Override
    public void debugf(String s, long l) {

    }

    @Override
    public void debugf(String s, long l, long l1) {

    }

    @Override
    public void debugf(String s, long l, Object o) {

    }

    @Override
    public void debugf(String s, long l, long l1, long l2) {

    }

    @Override
    public void debugf(String s, long l, long l1, Object o) {

    }

    @Override
    public void debugf(String s, long l, Object o, Object o1) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l, long l1) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l, Object o) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l, long l1, long l2) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l, long l1, Object o) {

    }

    @Override
    public void debugf(Throwable throwable, String s, long l, Object o, Object o1) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(Object o) {

    }

    @Override
    public void info(Object o, Throwable throwable) {

    }

    @Override
    public void info(String s, Object o, Throwable throwable) {

    }

    @Override
    public void info(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void infov(String s, Object... objects) {

    }

    @Override
    public void infov(String s, Object o) {

    }

    @Override
    public void infov(String s, Object o, Object o1) {

    }

    @Override
    public void infov(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void infov(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void infov(Throwable throwable, String s, Object o) {

    }

    @Override
    public void infov(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void infov(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void infof(String s, Object... objects) {

    }

    @Override
    public void infof(String s, Object o) {

    }

    @Override
    public void infof(String s, Object o, Object o1) {

    }

    @Override
    public void infof(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void infof(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void infof(Throwable throwable, String s, Object o) {

    }

    @Override
    public void infof(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void infof(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void warn(Object o) {

    }

    @Override
    public void warn(Object o, Throwable throwable) {

    }

    @Override
    public void warn(String s, Object o, Throwable throwable) {

    }

    @Override
    public void warn(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void warnv(String s, Object... objects) {

    }

    @Override
    public void warnv(String s, Object o) {

    }

    @Override
    public void warnv(String s, Object o, Object o1) {

    }

    @Override
    public void warnv(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void warnv(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void warnv(Throwable throwable, String s, Object o) {

    }

    @Override
    public void warnv(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void warnv(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void warnf(String s, Object... objects) {

    }

    @Override
    public void warnf(String s, Object o) {

    }

    @Override
    public void warnf(String s, Object o, Object o1) {

    }

    @Override
    public void warnf(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void warnf(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void warnf(Throwable throwable, String s, Object o) {

    }

    @Override
    public void warnf(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void warnf(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void error(Object o) {

    }

    @Override
    public void error(Object o, Throwable throwable) {

    }

    @Override
    public void error(String s, Object o, Throwable throwable) {

    }

    @Override
    public void error(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void errorv(String s, Object... objects) {

    }

    @Override
    public void errorv(String s, Object o) {

    }

    @Override
    public void errorv(String s, Object o, Object o1) {

    }

    @Override
    public void errorv(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void errorv(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void errorv(Throwable throwable, String s, Object o) {

    }

    @Override
    public void errorv(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void errorv(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void errorf(String s, Object... objects) {

    }

    @Override
    public void errorf(String s, Object o) {

    }

    @Override
    public void errorf(String s, Object o, Object o1) {

    }

    @Override
    public void errorf(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void errorf(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void errorf(Throwable throwable, String s, Object o) {

    }

    @Override
    public void errorf(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void errorf(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void fatal(Object o) {

    }

    @Override
    public void fatal(Object o, Throwable throwable) {

    }

    @Override
    public void fatal(String s, Object o, Throwable throwable) {

    }

    @Override
    public void fatal(String s, Object o, Object[] objects, Throwable throwable) {

    }

    @Override
    public void fatalv(String s, Object... objects) {

    }

    @Override
    public void fatalv(String s, Object o) {

    }

    @Override
    public void fatalv(String s, Object o, Object o1) {

    }

    @Override
    public void fatalv(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void fatalv(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void fatalv(Throwable throwable, String s, Object o) {

    }

    @Override
    public void fatalv(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void fatalv(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void fatalf(String s, Object... objects) {

    }

    @Override
    public void fatalf(String s, Object o) {

    }

    @Override
    public void fatalf(String s, Object o, Object o1) {

    }

    @Override
    public void fatalf(String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void fatalf(Throwable throwable, String s, Object... objects) {

    }

    @Override
    public void fatalf(Throwable throwable, String s, Object o) {

    }

    @Override
    public void fatalf(Throwable throwable, String s, Object o, Object o1) {

    }

    @Override
    public void fatalf(Throwable throwable, String s, Object o, Object o1, Object o2) {

    }

    @Override
    public void log(Logger.Level level, Object o) {

    }
}
