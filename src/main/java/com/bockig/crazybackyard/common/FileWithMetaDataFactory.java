package com.bockig.crazybackyard.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class FileWithMetaDataFactory {

    public static FileWithMetaData create(Path path) throws IOException {
        SimpleFile file = new SimpleFile(path.getFileName().toString(), Files.readAllBytes(path));
        return new FileWithMetaData(file, new HashMap<>());
    }
}
