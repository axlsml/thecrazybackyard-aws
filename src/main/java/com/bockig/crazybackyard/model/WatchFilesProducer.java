package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.BlockingQueue;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatchFilesProducer implements Runnable {

    private static final Logger LOG = LogManager.getLogger(WatchFilesProducer.class);

    private BlockingQueue<Path> queue;
    private String directory;

    public WatchFilesProducer(BlockingQueue<Path> queue, String directory) {
        this.queue = queue;
        this.directory = directory;
    }

    public void run() {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(directory);
            dir.register(watcher, ENTRY_CREATE);

            WatchKey key;
            while ((key = watcher.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path path = (Path) event.context();
                    LOG.info("new file {} - adding to queue", path);
                    queue.add(dir.resolve(path));
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("error watching files?!", e);
        }
    }

}
