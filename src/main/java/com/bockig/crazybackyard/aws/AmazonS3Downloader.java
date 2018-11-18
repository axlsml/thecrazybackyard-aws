package com.bockig.crazybackyard.aws;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.bockig.crazybackyard.PhotoReceived;
import com.bockig.crazybackyard.common.SimpleFile;
import com.bockig.crazybackyard.common.FileWithMetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class AmazonS3Downloader {

    private static final Logger LOG = LogManager.getLogger(PhotoReceived.class);

    private AmazonS3Downloader() {

    }

    public static FileWithMetaData download(S3Object object) {
        String key = object.getKey();
        try (InputStream is = object.getObjectContent()) {
            byte[] bytes = IOUtils.toByteArray(is);
            Map<String, String> meta = object.getObjectMetadata().getUserMetadata();
            FileWithMetaData fileWithMetaData = new FileWithMetaData(new SimpleFile(key, bytes), meta);
            LOG.info("fileWithMetaData: {}", fileWithMetaData);
            return fileWithMetaData;
        } catch (IOException e) {
            LOG.error("cannot get s3 content", e);
            throw new RuntimeException("could not receive content from s3");
        }
    }
}
