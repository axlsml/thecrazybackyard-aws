package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.aws.AmazonS3Downloader;
import com.bockig.crazybackyard.aws.S3FileReceivedHandler;
import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.common.Forward;
import com.bockig.crazybackyard.model.ReceiveAndForwardImage;
import com.bockig.crazybackyard.model.TheCrazyBackyardTweeter;

public class PhotoReceived implements S3FileReceivedHandler {

    @Override
    public void receiveObject(S3Object object, AmazonS3 s3Client) {
        PhotoReceivedConfig config = PhotoReceivedConfig.load();
        FileWithMetaData fileWithMetaData = AmazonS3Downloader.download(object);
        Forward forward = new TheCrazyBackyardTweeter(config.twitterConfig(), fileWithMetaData);

        new ReceiveAndForwardImage(config, fileWithMetaData.getMeta(), forward).checkPreconditionsAndSend();
    }

}
