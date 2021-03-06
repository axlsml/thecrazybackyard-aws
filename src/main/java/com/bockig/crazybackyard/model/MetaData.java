package com.bockig.crazybackyard.model;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class MetaData {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");
    private static final String HASH_TAGS = "#wildkamera #wildlife #nature #hunting #trailcam";
    private static final String MAIN_TEXT = "Hey buddy!";

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
            return String.format("%1$s (%2$s) %3$s", MAIN_TEXT, time.format(TIME_FORMAT), HASH_TAGS);
        }
        return String.format("%1$s %2$s", MAIN_TEXT, HASH_TAGS);
    }

    static boolean isNowActive(Map<String, String> userMetadata) {
        return Hours.checkActive(userMetadata.get(ACTIVE_HOURS));
    }

    private static Map<String, String> buildHours(String hours) {
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
