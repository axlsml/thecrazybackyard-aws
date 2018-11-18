package com.bockig.crazybackyard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewFilesWatcherConfigTest {

    private static final Path VALID_PROPERTIES = new File("src/test/resources/watch_ok.properties").toPath();
    private static final Path INVALID_PROPERTIES = new File("src/test/resources/watch_incomplete.properties").toPath();
    private static final String TARGET_FOLDER = "target";
    private static final Path TARGET = new File("target/watch.properties").toPath();

    @Before
    public void setUp() throws Exception {
        Files.deleteIfExists(TARGET);
    }

    @Test
    public void testLoadProperties() throws IOException {
        Files.copy(VALID_PROPERTIES, TARGET);
        NewFilesWatcherConfig config = NewFilesWatcherConfig.load(TARGET_FOLDER);
        assert config.getHours().equals("closed");
        assert config.getTargetBucket().equals("thecrazybackyard-extracted-pictures");
        assert config.watchDirectory().equals("/home/bockig/homes/trailcam/");
    }

    @Test(expected = RuntimeException.class)
    public void testLoadPropertiesMissing() {
        NewFilesWatcherConfig.load(TARGET_FOLDER);
    }

    @Test(expected = RuntimeException.class)
    public void testLoadPropertiesIncomplete() throws IOException {
        Files.copy(INVALID_PROPERTIES, TARGET);
        NewFilesWatcherConfig.load(TARGET_FOLDER);
    }
}
