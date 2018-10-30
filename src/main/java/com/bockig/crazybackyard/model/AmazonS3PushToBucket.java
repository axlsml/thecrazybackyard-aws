package com.bockig.crazybackyard.model;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class AmazonS3PushToBucket implements Consumer<Image> {

    private static final Logger LOG = LogManager.getLogger(AmazonS3PushToBucket.class);

    private final AmazonS3 s3Client;
    private final String targetBucket;
    private final ObjectMetadata metaData;

    public AmazonS3PushToBucket(AmazonS3 s3Client, String targetBucket, ObjectMetadata metaData) {
        this.s3Client = s3Client;
        this.targetBucket = targetBucket;
        this.metaData = metaData;
    }

    @Override
    public void accept(Image image) {
        try (InputStream is = image.inputStream()) {
            s3Client.putObject(targetBucket, image.getFilename(), is, metaData);
        } catch (IOException e) {
            LOG.error("cannot write image {}", image.getFilename(), e);
        }
    }
}
