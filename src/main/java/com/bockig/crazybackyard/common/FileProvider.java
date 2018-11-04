package com.bockig.crazybackyard.common;

import com.bockig.crazybackyard.model.FileWithMetaData;

import java.util.List;

public interface FileProvider {
    List<FileWithMetaData> images();
}
