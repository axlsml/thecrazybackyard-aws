package com.bockig.crazybackyard.aws;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.Map;
import java.util.function.Function;

public class AmazonS3PushToBucketFactory {

    public static AmazonS3PushToBucket create(String targetBucket, Function<Map<String, String>, Map<String, String>> createMetaData) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .build();
        return new AmazonS3PushToBucket(s3Client, targetBucket, createMetaData);
    }
}
