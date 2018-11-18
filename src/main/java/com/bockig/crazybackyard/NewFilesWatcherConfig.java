package com.bockig.crazybackyard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class NewFilesWatcherConfig extends Config {

    private static final Logger LOG = LogManager.getLogger(NewFilesWatcher.class);

    private static final String PROPERTIES_FILE = "watch.properties";

    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";
    private static final String WATCH_DIRECTORY = "CRAZYBACKYARD_WATCH_DIRECTORY";
    private static final String CURRENT_FOLDER = ".";

    private NewFilesWatcherConfig(List<ApplicationProperty> properties) {
        super(properties);
    }

    static NewFilesWatcherConfig load() {
        return new NewFilesWatcherConfig(new ArrayList<>(ApplicationProperty.create(loadProperties()::getProperty, TARGET_BUCKET, WATCH_DIRECTORY, HOURS)));
    }

    static NewFilesWatcherConfig load(String folder) {
        return new NewFilesWatcherConfig(new ArrayList<>(ApplicationProperty.create(loadProperties(folder)::getProperty, TARGET_BUCKET, WATCH_DIRECTORY, HOURS)));
    }

    private static Properties loadProperties(String folder) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(new File(folder, PROPERTIES_FILE))) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.error("cannot read properties {}", PROPERTIES_FILE, e);
        }
        return properties;
    }

    private static Properties loadProperties() {
        return loadProperties(CURRENT_FOLDER);
    }

    String getTargetBucket() {
        return propertyValue(TARGET_BUCKET);
    }

    String watchDirectory() {
        return propertyValue(WATCH_DIRECTORY);
    }

    String getHours() {
        return propertyValue(HOURS);
    }
}
