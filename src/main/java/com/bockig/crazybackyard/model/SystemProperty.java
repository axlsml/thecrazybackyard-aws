package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SystemProperty {

    private static final Logger LOG = LogManager.getLogger(SystemProperty.class);

    private String key;
    private String value;

    private SystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<SystemProperty> create(String... keys) {
        List<SystemProperty> properties = new ArrayList<>();
        for (String key : keys) {
            properties.add(new SystemProperty(key, System.getenv(key)));
        }
        return properties;
    }

    void failIfMissing() {
        if (value == null) {
            String msg = "missing variable: " + key;
            LOG.error(msg);
            throw new RuntimeException(msg);
        }
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }
}
