package com.bockig.crazybackyard.model;

import java.util.function.Consumer;

public class ImageSender {

    private ImageProvider imageProvider;
    private Consumer<Image> sender;

    public ImageSender(ImageProvider imageProvider, Consumer<Image> sender) {
        this.imageProvider = imageProvider;
        this.sender = sender;
    }

    public void pushAll() {
        imageProvider.images().forEach(sender::accept);
    }
}
