package com.bockig.crazybackyard;

import java.util.List;

abstract class Config {

    static final String HOURS = "CRAZYBACKYARD_ACTIVE_HOURS";

    private List<ApplicationProperty> properties;

    Config(List<ApplicationProperty> properties) {
        this.properties = properties;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        properties.forEach(ApplicationProperty::failIfMissing);
    }

    String propertyValue(String key) {
        return properties.stream()
                .filter(p -> p.getKey().equals(key))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .getValue();
    }

}
