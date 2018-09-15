package com.bockig.crazybackyard;


import com.bockig.crazybackyard.model.WatchFiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NewFilesWatcher {

    private static final Logger LOG = LogManager.getLogger(NewFilesWatcher.class);

    private final BlockingQueue<Path> queue = new LinkedBlockingQueue<>();

    public void startWatch() throws IOException, InterruptedException {
        new WatchFiles(queue, "C:\\dev\\ilg\\blub")

    }
}
