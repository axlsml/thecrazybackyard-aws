package com.bockig.crazybackyard.model;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TimestampExtractor {

    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Pic was taken on 06/09/2018 19:29:46 Battery:60%~80%
    private static final Pattern TAKEN_ON_TEXT = Pattern.compile("Pic was taken on (\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d) ");

    private TimestampExtractor() {
    }

    static Optional<ZonedDateTime> extract(String content) {
        Matcher matcher = TAKEN_ON_TEXT.matcher(StringUtils.trim(content));
        if (matcher.find()) {
            String dateStr = matcher.group(1);
            ZonedDateTime timestamp = ZonedDateTime.of(LocalDateTime.parse(dateStr, DATE_FORMAT), Hours.DEFAULT_ZONE);
            return Optional.of(timestamp);
        }
        return Optional.empty();
    }
}
