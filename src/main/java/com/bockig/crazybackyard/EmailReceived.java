package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.aws.AmazonS3Downloader;
import com.bockig.crazybackyard.aws.AmazonS3PushToBucket;
import com.bockig.crazybackyard.aws.S3FileReceivedHandler;
import com.bockig.crazybackyard.common.HasInputStream;
import com.bockig.crazybackyard.model.EmailReaderFactory;
import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.model.ImageProxy;
import com.bockig.crazybackyard.model.MetaData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class EmailReceived implements S3FileReceivedHandler {

    @Override
    public void receiveObject(S3Object object, AmazonS3 s3Client) throws IOException, MessagingException {
        HasInputStream email = AmazonS3Downloader.download(object);

        new ImageProxy(EmailReaderFactory.createFromMime(email), createImageUploader(s3Client)).upload();
    }

    private Consumer<FileWithMetaData> createImageUploader(AmazonS3 s3Client) {
        EmailReceivedConfig config = EmailReceivedConfig.load();
        Function<Map<String, String>, Map<String, String>> addHoursToMetadata =
                metaData -> MetaData.appendHours(metaData, config.getHours());
        return new AmazonS3PushToBucket(s3Client, config.getTargetBucket(), addHoursToMetadata);
    }

}
