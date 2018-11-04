package com.bockig.crazybackyard.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bockig.crazybackyard.model.MetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

public class S3FileUploader implements Consumer<Path> {

    private static final Logger LOG = LogManager.getLogger(S3FileUploader.class);

    private String bucketName;
    private String hours;

    public S3FileUploader(String bucketName, String hours) {
        this.bucketName = bucketName;
        this.hours = hours;
    }

    @Override
    public void accept(Path path) {
        File file = path.toFile();
        try (InputStream fis = new FileInputStream(file)) {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .build();
            PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), fis, S3Util.toObjectMetaData(MetaData.buildHours(hours)));
            s3Client.putObject(request);
            LOG.info("succssfully uploaded {}", file.getName());
        } catch (AmazonServiceException e) {
            LOG.error("couldnt write to s3", e);
        } catch (IOException e) {
            LOG.error("couldnt read file", e);
        }
    }

}
