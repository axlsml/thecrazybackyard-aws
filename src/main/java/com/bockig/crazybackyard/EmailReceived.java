package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.EmailReader;
import com.bockig.crazybackyard.model.Image;
import com.bockig.crazybackyard.model.MetaData;
import com.bockig.crazybackyard.model.S3FileReceivedHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

public class EmailReceived implements S3FileReceivedHandler {

    private static final Logger LOG = LogManager.getLogger(EmailReceived.class);

    @Override
    public void receiveObject(S3Object object, AmazonS3 s3Client) throws Exception {
        EmailReceivedConfig config = EmailReceivedConfig.load();
        Optional<EmailReader> reader = EmailReader.create(object.getObjectContent());
        if (reader.isPresent()) {
            EmailReader email = reader.get();
            Consumer<Image> writeImageToTargetBucket = i -> putImage(i, email, s3Client, config);
            LOG.info("received new email from '{}' with subject '{}'", email.sender(), email.subject());
            email.images().forEach(writeImageToTargetBucket);
        }
    }

    private void putImage(Image image, EmailReader email, AmazonS3 client, EmailReceivedConfig config) {
        try (InputStream is = image.inputStream()) {
            client.putObject(config.getTargetBucket(), image.getFilename(), is, MetaData.create(email.metaData(), config.getHours()));
        } catch (IOException e) {
            LOG.error("cannot write image {}", image.getFilename(), e);
        }
    }

}
