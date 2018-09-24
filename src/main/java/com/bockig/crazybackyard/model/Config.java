package com.bockig.crazybackyard.model;

import java.util.List;

public abstract class Config {

    protected static final String HOURS = "CRAZYBACKYARD_ACTIVE_HOURS";

    private List<ApplicationProperty> properties;

    public Config(List<ApplicationProperty> properties) {
        this.properties = properties;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        properties.forEach(ApplicationProperty::failIfMissing);
    }

    protected String propertyValue(String key) {
        return properties.stream()
                .filter(p -> p.getKey().equals(key))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .getValue();
    }

}
