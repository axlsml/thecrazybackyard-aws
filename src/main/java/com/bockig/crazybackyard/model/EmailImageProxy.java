package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.FileProvider;

import java.util.function.Consumer;

public class EmailImageProxy {

    private FileProvider fileProvider;
    private Consumer<FileWithMetaData> upload;

    public EmailImageProxy(FileProvider fileProvider, Consumer<FileWithMetaData> upload) {
        this.fileProvider = fileProvider;
        this.upload = upload;
    }

    public void upload() {
        fileProvider.images().forEach(upload);
    }
}
