package com.bockig.crazybackyard.common;

import java.io.InputStream;
import java.util.Map;

public class FileWithMetaData implements HasInputStream {

    private FileContent fileContent;
    private Map<String, String> meta;

    public FileWithMetaData(FileContent fileContent, Map<String, String> meta) {
        this.fileContent = fileContent;
        this.meta = meta;
    }

    public String getName() {
        return fileContent.getFilename();
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    @Override
    public String toString() {
        return "FileWithMetaData{" +
                "file=" + fileContent +
                ", meta=" + meta +
                '}';
    }

    public InputStream createInputStream() {
        return fileContent.inputStream();
    }

    @Override
    public InputStream inputStream() {
        return fileContent.inputStream();
    }
}
