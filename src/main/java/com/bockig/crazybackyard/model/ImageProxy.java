package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.common.ImageProvider;

import java.util.function.Consumer;

public class ImageProxy {

    private ImageProvider imageProvider;
    private Consumer<FileWithMetaData> upload;

    public ImageProxy(ImageProvider imageProvider, Consumer<FileWithMetaData> upload) {
        this.imageProvider = imageProvider;
        this.upload = upload;
    }

    public void upload() {
        imageProvider.images().forEach(upload);
    }
}
