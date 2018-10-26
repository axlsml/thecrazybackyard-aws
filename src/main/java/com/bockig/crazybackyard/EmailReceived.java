package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.*;

import java.io.InputStream;
import java.util.function.Consumer;

public class EmailReceived implements S3FileReceivedHandler {

    @Override
    public void receiveObject(S3Object object, AmazonS3 s3Client) {
        InputStream emailInputStream = object.getObjectContent();

        Consumer<? super EmailReader> processEmail = emailReader -> processEmail(emailReader, s3Client);
        EmailReader
                .create(emailInputStream)
                .ifPresent(processEmail);
    }

    private void processEmail(EmailReader emailReader, AmazonS3 s3Client) {
        EmailReceivedConfig config = EmailReceivedConfig.load();
        ObjectMetadata metaData = MetaData.create(emailReader.metaData());

        Consumer<Image> pushToBucket = new AmazonS3PushToBucket(s3Client, config.getTargetBucket(), metaData);
        new ImageSender(emailReader, pushToBucket).pushAll();
    }

}
