package com.bockig.crazybackyard;

import com.bockig.crazybackyard.model.Config;
import com.bockig.crazybackyard.model.SystemProperty;
import org.apache.logging.log4j.util.Strings;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackyardPicReceivedConfig extends Config {

    private static final String CONSUMER_KEY = "CRAZYBACKYARD_TWITTER_CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "CRAZYBACKYARD_TWITTER_CONSUMER_SECRET";
    private static final String ACCESS_TOKEN = "CRAZYBACKYARD_TWITTER_ACCESS_TOKEN";
    private static final String ACCESS_SECRET = "CRAZYBACKYARD_TWITTER_ACCESS_SECRET";
    private static final String HOURS = "CRAZYBACKYARD_TWITTER_HOURS";

    private static final Pattern HOURS_PATTERN = Pattern.compile("(\\d\\d:\\d\\d)-(\\d\\d:\\d\\d)");
    public static final String EUROPE_BERLIN = "Europe/Berlin";


    private BackyardPicReceivedConfig(List<SystemProperty> properties) {
        super(properties);
    }

    static BackyardPicReceivedConfig load() {
        return new BackyardPicReceivedConfig(new ArrayList<>(
                SystemProperty.create(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_SECRET, HOURS)));
    }

    Configuration twitterConfig() {
        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setOAuthConsumerKey(propertyValue(CONSUMER_KEY))
                .setOAuthConsumerSecret(propertyValue(CONSUMER_SECRET))
                .setOAuthAccessToken(propertyValue(ACCESS_TOKEN))
                .setOAuthAccessTokenSecret(propertyValue(ACCESS_SECRET));
        return cb.build();
    }

    boolean isNowActive() {
        return checkActive(Clock.system(ZoneId.of(EUROPE_BERLIN)), propertyValue(HOURS));
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

}
