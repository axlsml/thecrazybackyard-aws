package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.Map;

public class PhotoReceived implements S3FileReceivedHandler {

    private static final Logger LOG = LogManager.getLogger(PhotoReceived.class);

    @Override
    public void receiveObject(S3Object object2, AmazonS3 s3Client) throws IOException, TwitterException {
        PhotoReceivedConfig config = PhotoReceivedConfig.load();
        Downloaded downloaded = download(object2);

        if (postDisabledCurrently(config, downloaded.getMeta())) {
            return;
        }

        new TheCrazyBackyardTweeter(config.twitterConfig()).post(downloaded);
    }

    private Downloaded download(S3Object object) {
        Downloaded downloaded = new AmazonS3Downloader(object).download();
        LOG.info("downloaded: {}", downloaded);
        return downloaded;
    }

    private static boolean postDisabledCurrently(PhotoReceivedConfig globalConfig, Map<String, String> userMetadata) {
        if (!globalConfig.isEnabled()) {
            LOG.info("disabled now (global config) - skipping");
            return true;
        }
        if (!MetaData.isNowActive(userMetadata)) {
            LOG.info("disabled now (sender-specific config) - skipping");
            return true;
        }
        return false;
    }

}
