package com.bockig.crazybackyard;

import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Path;
import java.util.function.Consumer;

public class NewFilesWatcherTest {

    @Test
    @Ignore
    public void nameTest() throws Exception {
        Consumer<Path> x = System.out::println;
        NewFilesWatcher watcher = new NewFilesWatcher("D:\\tmp\\crazy", x);
        watcher.startWatching();
    }
}
