package com.bockig.crazybackyard.model;

import java.util.function.Consumer;

public class ImageUploader {

    private ImageProvider imageProvider;
    private Consumer<Image> upload;

    public ImageUploader(ImageProvider imageProvider, Consumer<Image> upload) {
        this.imageProvider = imageProvider;
        this.upload = upload;
    }

    public void upload() {
        imageProvider.images().forEach(upload);
    }
}
