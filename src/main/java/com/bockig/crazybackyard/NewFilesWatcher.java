package com.bockig.crazybackyard;


import com.bockig.crazybackyard.aws.AmazonS3PushToBucket;
import com.bockig.crazybackyard.aws.AmazonS3PushToBucketFactory;
import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.common.FileWithMetaDataFactory;
import com.bockig.crazybackyard.model.MetaData;
import com.bockig.crazybackyard.watcher.WatchFilesProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class NewFilesWatcher {

    private static final Logger LOG = LogManager.getLogger(NewFilesWatcher.class);
    private static final int MAX_MINUTES = 2;

    private final BlockingQueue<Path> queue = new LinkedBlockingQueue<>();
    private String directoy;
    private Consumer<FileWithMetaData> fileConsumer;

    NewFilesWatcher(String directoy, Consumer<FileWithMetaData> fileConsumer) {
        this.directoy = directoy;
        this.fileConsumer = fileConsumer;
    }

    public static void main(String[] args) {
        LOG.info("start watching for files ...");
        NewFilesWatcherConfig config = NewFilesWatcherConfig.load();
        AmazonS3PushToBucket pushToBucket = AmazonS3PushToBucketFactory.create(config.getTargetBucket(), meta -> MetaData.appendHours(meta, config.getHours()));
        NewFilesWatcher watcher = new NewFilesWatcher(config.watchDirectory(), pushToBucket);
        watcher.startWatching();
    }

    void startWatching() {
        CompletableFuture.supplyAsync(new WatchFilesProducer(queue, directoy));

        try {
            receiveNewFiles();
        } catch (InterruptedException e) {
            LOG.info("exiting", e);
        }
    }

    private void receiveNewFiles() throws InterruptedException {
        while (true) {
            try {
                Path path = queue.take();
                LOG.info("found new file {}", path);
                boolean writtenSuccessfully = waitUntilFileIsWritten(path);
                if (!writtenSuccessfully) {
                    LOG.error("file write timeout - skipping this file");
                } else {
                    fileConsumer.accept(createFile(path));
                }
            } catch (IOException e) {
                LOG.error("error consuming new file", e);
            }
        }
    }

    private FileWithMetaData createFile(Path path) throws IOException {
        return FileWithMetaDataFactory.create(path);
    }

    private boolean waitUntilFileIsWritten(Path path) throws IOException, InterruptedException {
        ZonedDateTime abortTimestamp = ZonedDateTime.now().plusMinutes(MAX_MINUTES);
        long size1, size2;
        do {
            size1 = Files.size(path);
            Thread.sleep(1000);
            size2 = Files.size(path);
        } while (size2 != size1 && ZonedDateTime.now().isBefore(abortTimestamp));
        return size2 == size1;
    }


}
