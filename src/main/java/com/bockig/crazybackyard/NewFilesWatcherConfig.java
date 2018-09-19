package com.bockig.crazybackyard;

import com.bockig.crazybackyard.model.Config;
import com.bockig.crazybackyard.model.SystemProperty;

import java.util.ArrayList;
import java.util.List;

class NewFilesWatcherConfig extends Config {

    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";
    private static final String WATCH_DIRECTORY = "CRAZYBACKYARD_WATCH_DIRECTORY";

    NewFilesWatcherConfig(List<SystemProperty> properties) {
        super(properties);
    }

    static NewFilesWatcherConfig load() {
        return new NewFilesWatcherConfig(new ArrayList<>(SystemProperty.create(TARGET_BUCKET, WATCH_DIRECTORY, HOURS)));
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
