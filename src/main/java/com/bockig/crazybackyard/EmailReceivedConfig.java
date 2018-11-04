package com.bockig.crazybackyard;

import java.util.ArrayList;
import java.util.List;

class EmailReceivedConfig extends Config {

    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";

    private EmailReceivedConfig(List<ApplicationProperty> properties) {
        super(properties);
    }

    static EmailReceivedConfig load() {
        return new EmailReceivedConfig(new ArrayList<>(ApplicationProperty.create(System::getenv, TARGET_BUCKET, HOURS)));
    }

    String getTargetBucket() {
        return propertyValue(TARGET_BUCKET);
    }

    String getHours() {
        return propertyValue(HOURS);
    }
}
