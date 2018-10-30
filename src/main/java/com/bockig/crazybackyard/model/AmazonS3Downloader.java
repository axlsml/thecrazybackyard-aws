package com.bockig.crazybackyard.model;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.bockig.crazybackyard.PhotoReceived;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class AmazonS3Downloader {

    private static final Logger LOG = LogManager.getLogger(PhotoReceived.class);

    private S3Object object;

    public AmazonS3Downloader(S3Object object) {
        this.object = object;
    }

    public Downloaded download() {
        String key = object.getKey();
        try (InputStream is = object.getObjectContent()) {
            byte[] bytes = IOUtils.toByteArray(is);
            Map<String, String> meta = object.getObjectMetadata().getUserMetadata();
            return new Downloaded(key, bytes, meta);
        } catch (IOException e) {
            LOG.error("cannot get s3 content", e);
            throw new RuntimeException("could not receive content from s3");
        }
    }
}
