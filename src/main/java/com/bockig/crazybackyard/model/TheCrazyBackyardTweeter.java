package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TheCrazyBackyardTweeter {

    private static final Logger LOG = LogManager.getLogger(TheCrazyBackyardTweeter.class);

    private final Twitter twitter;

    public TheCrazyBackyardTweeter(Configuration configuration) {
        this.twitter = new TwitterFactory(configuration).getInstance();
    }

    public void post(Downloaded downloaded) throws IOException, TwitterException {
        String text = MetaData.buildStatusText(downloaded.getMeta());
        try (InputStream is = new ByteArrayInputStream(downloaded.getBytes())) {
            postStatusUpdate(twitter, text, downloaded.getName(), is);
        }
    }

    private void postStatusUpdate(Twitter twitter, String text, String filename, InputStream fis) throws TwitterException {
        StatusUpdate update = new StatusUpdate(text);
        update.setMedia(filename, fis);
        Status result = twitter.updateStatus(update);
        LOG.info("successfully posted tweet with id {}", result.getId());
    }
}
