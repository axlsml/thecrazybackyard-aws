package com.bockig.crazybackyard.model;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MetaData {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");

    static final String UTC = "utc";
    static final String FROM = "from";
    static final String SUBJECT = "subject";
    private static final String ACTIVE_HOURS = "active-hours";

    private MetaData() {
    }

    static String buildStatusText(Map<String, String> userMetadata) {
        String utc = userMetadata.get(UTC);
        if (utc != null) {
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(utc)), Hours.DEFAULT_ZONE);
            return String.format("Hey buddy! (%s) #trailcam", time.format(TIME_FORMAT));
        }
        return "Hey buddy! #trailcam";
    }

    public static boolean isNowActive(Map<String, String> userMetadata) {
        return Hours.checkActive(userMetadata.get(ACTIVE_HOURS));
    }

    public static Map<String, String> buildHours(String hours) {
        Map<String, String> meta = new HashMap<>();
        meta.put(ACTIVE_HOURS, hours);
        return meta;
    }

    public static Map<String, String> appendHours(Map<String, String> metaData, String hours) {
        Map<String, String> withHours = new HashMap<>(metaData);
        withHours.putAll(buildHours(hours));
        return withHours;
    }
}
