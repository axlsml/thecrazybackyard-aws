package com.bockig.crazybackyard.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.bockig.crazybackyard.common.FileWithMetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AmazonS3PushToBucket implements Consumer<FileWithMetaData> {

    private static final Logger LOG = LogManager.getLogger(AmazonS3PushToBucket.class);

    private final AmazonS3 s3Client;
    private final String targetBucket;
    private final Function<Map<String, String>, Map<String, String>> createMetaData;

    public AmazonS3PushToBucket(AmazonS3 s3Client, String targetBucket, Function<Map<String, String>, Map<String, String>> createMetaData) {
        this.s3Client = s3Client;
        this.targetBucket = targetBucket;
        this.createMetaData = createMetaData;
    }

    @Override
    public void accept(FileWithMetaData file) {
        try (InputStream is = file.inputStream()) {
            Map<String, String> effectiveMetaData = createMetaData.apply(file.getMeta());
            s3Client.putObject(targetBucket, file.getName(), is, S3Util.toObjectMetaData(effectiveMetaData));
        } catch (IOException e) {
            LOG.error("cannot write file {}", file.getName(), e);
        }
    }
}
