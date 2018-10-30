package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.IOException;
import java.io.InputStream;

public class TheCrazyBackyardTweeter {

    private static final Logger LOG = LogManager.getLogger(TheCrazyBackyardTweeter.class);

    private final Twitter twitter;
    private final Downloaded downloaded;

    public TheCrazyBackyardTweeter(Configuration configuration, Downloaded downloaded) {
        this.twitter = new TwitterFactory(configuration).getInstance();
        this.downloaded = downloaded;
    }

    public void post() throws IOException, TwitterException {
        try (InputStream is = downloaded.createInputStream()) {
            StatusUpdate update = createStatusUpdate(is);
            postStatusUpdate(update);
        }
    }

    private StatusUpdate createStatusUpdate(InputStream fis) {
        String text = MetaData.buildStatusText(downloaded.getMeta());
        StatusUpdate status = new StatusUpdate(text);
        status.setMedia(downloaded.getName(), fis);
        return status;
    }

    private void postStatusUpdate(StatusUpdate update) throws TwitterException {
        Status result = twitter.updateStatus(update);
        LOG.info("successfully posted tweet with id {}", result.getId());
    }

}
