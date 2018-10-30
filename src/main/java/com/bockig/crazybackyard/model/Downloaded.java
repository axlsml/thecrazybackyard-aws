package com.bockig.crazybackyard.model;

import java.util.Arrays;
import java.util.Map;

public class Downloaded {

    private String name;
    private byte[] bytes;
    private Map<String, String> meta;

    Downloaded(String name, byte[] bytes, Map<String, String> meta) {
        this.name = name;
        this.bytes = bytes;
        this.meta = meta;
    }

    String getName() {
        return name;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return "Downloaded{" +
                "name='" + name + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", meta=" + meta +
                '}';
    }
}
