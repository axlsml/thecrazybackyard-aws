package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.common.Forward;
import com.bockig.crazybackyard.common.ForwardFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;
import twitter4j.conf.Configuration;

import java.io.IOException;
import java.io.InputStream;

public class TheCrazyBackyardTweeter implements Forward {

    private static final Logger LOG = LogManager.getLogger(TheCrazyBackyardTweeter.class);

    private final Twitter twitter;
    private final FileWithMetaData fileWithMetaData;

    public TheCrazyBackyardTweeter(Configuration configuration, FileWithMetaData fileWithMetaData) {
        this.twitter = new TwitterFactory(configuration).getInstance();
        this.fileWithMetaData = fileWithMetaData;
    }

    public void forward() throws ForwardFailedException {
        try (InputStream is = fileWithMetaData.createInputStream()) {
            StatusUpdate update = createStatusUpdate(is);
            postStatusUpdate(update);
        } catch (IOException | TwitterException e) {
            throw new ForwardFailedException("an error occurred during twitter forward", e);
        }
    }

    private StatusUpdate createStatusUpdate(InputStream fis) {
        String text = MetaData.buildStatusText(fileWithMetaData.getMeta());
        StatusUpdate status = new StatusUpdate(text);
        status.setMedia(fileWithMetaData.getName(), fis);
        return status;
    }

    private void postStatusUpdate(StatusUpdate update) throws TwitterException {
        Status result = twitter.updateStatus(update);
        LOG.info("successfully posted tweet with id {}", result.getId());
    }

}
