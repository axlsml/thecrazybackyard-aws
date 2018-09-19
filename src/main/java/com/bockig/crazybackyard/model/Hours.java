package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.util.Strings;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hours {

    private static final Pattern HOURS_PATTERN = Pattern.compile("(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)");

    static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Berlin");

    private Hours() {
    }

    static boolean checkActive(Clock clock, String hoursString) {
        if (Strings.isBlank(hoursString)) {
            return false;
        }
        String trimmed = hoursString.trim().replaceAll("\n ", "").toLowerCase();
        Matcher matcher = HOURS_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            LocalTime from = LocalTime.parse(matcher.group(1));
            LocalTime to = LocalTime.parse(matcher.group(2));
            LocalTime now = LocalTime.now(clock);
            boolean overMidnight = to.isBefore(from);
            return overMidnight ? !now.isAfter(to) : (!now.isBefore(from) && !now.isAfter(to));
        } else {
            return "open".equals(trimmed);
        }
    }

    public static boolean checkActive(String hoursString) {
        return checkActive(Clock.system(DEFAULT_ZONE), hoursString);
    }
}
