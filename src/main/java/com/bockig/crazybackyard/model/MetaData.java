package com.bockig.crazybackyard.model;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MetaData {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    static final String UTC = "utc";
    static final String FROM = "from";
    static final String SUBJECT = "subject";

    private MetaData() {
    }

    public static String buildStatusText(Map<String, String> userMetadata) throws Exception {
        String utc = userMetadata.get(UTC);
        if (utc != null) {
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(utc)), ZoneId.of("Europe/Berlin"));
            return String.format("Gotcha buddy! (%s) #trailcam", time.format(TIME_FORMAT));
        }
        return "Gotcha buddy! #trailcam";
    }

    public static ObjectMetadata create(Map<String, String> meta) {
        ObjectMetadata metaData = new ObjectMetadata();
        meta.forEach(metaData::addUserMetadata);
        return metaData;
    }
}
