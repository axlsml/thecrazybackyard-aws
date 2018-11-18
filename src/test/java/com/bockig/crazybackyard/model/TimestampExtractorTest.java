package com.bockig.crazybackyard.model;

import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

public class TimestampExtractorTest {

    @Test
    public void testExtract() {
        ZonedDateTime expectedTimestamp = ZonedDateTime.of(2018, 9, 6, 19, 29, 46, 0, Hours.DEFAULT_ZONE);
        Optional<ZonedDateTime> result = TimestampExtractor.extract("Pic was taken on 06/09/2018 19:29:46 Battery:60%~80%");

        assert result.isPresent();
        assert result.get().equals(expectedTimestamp);
    }

    @Test
    public void testExtractFromBodyPartInvalidFormat() {
        Optional<ZonedDateTime> result = TimestampExtractor.extract("any text");
        assert !result.isPresent();
    }
}
