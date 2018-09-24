package com.bockig.crazybackyard.model;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MetaData {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm:ss a");

    static final String UTC = "utc";
    static final String FROM = "from";
    static final String SUBJECT = "subject";
    private static final String ACTIVE_HOURS = "active-hours";

    private MetaData() {
    }

    public static String buildStatusText(Map<String, String> userMetadata) {
        String utc = userMetadata.get(UTC);
        if (utc != null) {
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(utc)), Hours.DEFAULT_ZONE);
            return String.format("Hey buddy! (%s) #trailcam", time.format(TIME_FORMAT));
        }
        return "Hey buddy! #trailcam";
    }

    public static ObjectMetadata create(Map<String, String> meta, String hours) {
        ObjectMetadata metaData = buildHours(hours);
        meta.forEach(metaData::addUserMetadata);
        return metaData;
    }

    public static boolean isNowActive(Map<String, String> userMetadata) {
        return Hours.checkActive(userMetadata.get(ACTIVE_HOURS));
    }

    static ObjectMetadata buildHours(String hours) {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.addUserMetadata(ACTIVE_HOURS, hours);
        return metaData;
    }
}
