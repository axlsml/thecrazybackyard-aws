package com.bockig.crazybackyard.model;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.BlockingQueue;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatchFiles {



    private BlockingQueue<Path> queue;
    private String directory;

    public WatchFiles(BlockingQueue<Path> queue, String directory) {
        this.queue = queue;
        this.directory = directory;
    }

    public void startWatching() throws InterruptedException {
        WatchService watcher = null;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(directory);
            dir.register(watcher, ENTRY_CREATE);

            WatchKey key;
            while ((key = watcher.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path path = (Path) event.context();
                    System.out.println("adding " + path + " to queue");
                    queue.add(dir.resolve(path));
                }
                key.reset();
            }
        } catch (IOException e) {
        }
    }

}
