package com.bockig.crazybackyard;

import com.bockig.crazybackyard.model.Config;
import com.bockig.crazybackyard.model.SystemProperty;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class PhotoReceivedConfig extends Config {

    private static final String CONSUMER_KEY = "CRAZYBACKYARD_TWITTER_CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "CRAZYBACKYARD_TWITTER_CONSUMER_SECRET";
    private static final String ACCESS_TOKEN = "CRAZYBACKYARD_TWITTER_ACCESS_TOKEN";
    private static final String ACCESS_SECRET = "CRAZYBACKYARD_TWITTER_ACCESS_SECRET";
    private static final String ENABLED = "CRAZYBACKYARD_TWITTER_ENABLED";

    private PhotoReceivedConfig(List<SystemProperty> properties) {
        super(properties);
    }

    static PhotoReceivedConfig load() {
        return new PhotoReceivedConfig(new ArrayList<>(
                SystemProperty.create(System::getenv, CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_SECRET, ENABLED)));
    }

    Configuration twitterConfig() {
        ConfigurationBuilder cb = new ConfigurationBuilder()
                .setOAuthConsumerKey(propertyValue(CONSUMER_KEY))
                .setOAuthConsumerSecret(propertyValue(CONSUMER_SECRET))
                .setOAuthAccessToken(propertyValue(ACCESS_TOKEN))
                .setOAuthAccessTokenSecret(propertyValue(ACCESS_SECRET));
        return cb.build();
    }

    boolean isEnabled() {
        return Boolean.parseBoolean(propertyValue(ENABLED));
    }


}
