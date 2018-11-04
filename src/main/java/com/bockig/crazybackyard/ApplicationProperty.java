package com.bockig.crazybackyard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class ApplicationProperty {

    private static final Logger LOG = LogManager.getLogger(ApplicationProperty.class);

    private String key;
    private String value;

    private ApplicationProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    static List<ApplicationProperty> create(Function<String, String> retrieveKey, String... keys) {
        return Arrays.stream(keys)
                .map(key -> new ApplicationProperty(key, retrieveKey.apply(key)))
                .collect(Collectors.toList());
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
