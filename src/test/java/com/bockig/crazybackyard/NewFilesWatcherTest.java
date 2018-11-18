package com.bockig.crazybackyard;

import com.bockig.crazybackyard.aws.AmazonS3PushToBucket;
import com.bockig.crazybackyard.aws.AmazonS3PushToBucketFactory;
import org.junit.Ignore;
import org.junit.Test;

public class NewFilesWatcherTest {

    @Test
    @Ignore
    public void nameTest() {
        AmazonS3PushToBucket x = AmazonS3PushToBucketFactory.create("axl-sml", a -> a);
        NewFilesWatcher watcher = new NewFilesWatcher("D:\\tmp\\crazy", x);
        watcher.startWatching();
    }
}
