package com.bockig.crazybackyard.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SimpleFile implements FileContent {

    private final String filename;
    private final byte[] bytes;

    public SimpleFile(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    public String getFilename() {
        return filename;
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public String toString() {
        return "SimpleFile{" +
                "filename='" + filename + '\'' +
                ", bytes=" + bytes.length +
                '}';
    }
}
