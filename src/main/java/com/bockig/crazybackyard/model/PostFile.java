package com.bockig.crazybackyard.model;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

public class PostFile implements Consumer<Path> {

    private static final Logger LOG = LogManager.getLogger(PostFile.class);

    @Override
    public void accept(Path path) {
        String clientRegion = "eu-central-1";
        String bucketName = "axl-sml";
//        String fileObjKeyName = "SYEW0038.JPG";
//        String fileName = "D:\\downloads\\SYEW0038.JPG";

        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new EnvironmentVariableCredentialsProvider())
                    .build();

            File file = path.toFile();
            PutObjectRequest request = new PutObjectRequest(bucketName, file.getName(), file);
            ObjectMetadata metadata = MetaData.create(metaData(file));
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            LOG.error("couldnt write to s3", e);
        }
    }

    public Map<String, String> metaData(File file) {
//        Files.getLastModifiedTime()
//        Map<String, String> meta = new HashMap<>();
//        meta.put(MetaData.UTC, String.valueOf(timestamp().flatMap(t -> Optional.of(t.toEpochSecond())).orElse(0L)));
//        meta.put(MetaData.FROM, sender());
//        meta.put(MetaData.SUBJECT, subject());
//        return meta;
        return null;
    }
}
