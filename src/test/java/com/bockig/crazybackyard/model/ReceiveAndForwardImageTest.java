package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.CanBeEnabled;
import com.bockig.crazybackyard.common.Forward;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ReceiveAndForwardImageTest {

    private Map<String, String> emptyMetaData = new HashMap<>();

    private CanBeEnabled disabled = () -> false;
    private CanBeEnabled enabled = () -> true;

    private boolean forwarded = false;

    private Forward forward = () -> forwarded = true;

    @Before
    public void setUp() {
        forwarded = false;
    }

    @Test
    public void testForwardDisabled() {
        assert ReceiveAndForwardImage.postDisabledCurrently(disabled, null);
        assert !ReceiveAndForwardImage.postDisabledCurrently(enabled, createMetaActive());
        assert ReceiveAndForwardImage.postDisabledCurrently(enabled, createMetaOutsideHours());
    }

    @Test
    public void testCheckPreconditionsAndSendDisabled() {
        new ReceiveAndForwardImage(disabled, createMetaActive(), forward).checkPreconditionsAndSend();
        assert !forwarded;
        new ReceiveAndForwardImage(enabled, createMetaOutsideHours(), forward).checkPreconditionsAndSend();
        assert !forwarded;
        new ReceiveAndForwardImage(enabled, emptyMetaData, forward).checkPreconditionsAndSend();
        assert !forwarded;
        new ReceiveAndForwardImage(enabled, null, forward).checkPreconditionsAndSend();
        assert !forwarded;
    }

    @Test
    public void testCheckPreconditionsAndSendEnabled() {
        new ReceiveAndForwardImage(enabled, createMetaActive(), forward).checkPreconditionsAndSend();
        assert forwarded;
    }

    private Map<String, String> createMetaOutsideHours() {
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(250);
        ZonedDateTime end = ZonedDateTime.now().minusMinutes(50);
        Map<String, String> map = new HashMap<>();
        map.put("active-hours", format(start, end));
        return map;
    }

    private Map<String, String> createMetaActive() {
        ZonedDateTime start = ZonedDateTime.now().minusMinutes(50);
        ZonedDateTime end = ZonedDateTime.now().plusMinutes(50);
        Map<String, String> map = new HashMap<>();
        map.put("active-hours", format(start, end));
        return map;
    }

    private String format(ZonedDateTime start, ZonedDateTime end) {
        String from = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        String to = end.format(DateTimeFormatter.ofPattern("HH:mm"));
        return from + "-" + to;
    }
}
