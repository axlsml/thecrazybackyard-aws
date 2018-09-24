package com.bockig.crazybackyard.model;

import org.junit.Test;

import java.time.*;

import static com.bockig.crazybackyard.model.Hours.checkActive;

public class HoursTest {

    private static final String morning = "07:00-12:00";
    private static final String atNight = "23:00-07:00";
    private static final String closed = "";
    private static final String invalid = "quatsch";
    private static final String always = "open";
    private static final String always2 = "Open ";

    private static final ZoneId zone = ZoneId.of("Europe/Berlin");

    @Test
    public void testCheckActiveAt12Pm() {
        Clock clock = Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(12, 0), zone).toInstant(), zone);
        assert checkActive(clock, morning);
        assert !checkActive(clock, atNight);
        assert !checkActive(clock, closed);
        assert !checkActive(clock, invalid);
        assert checkActive(clock, always);
        assert checkActive(clock, always2);
    }

    @Test
    public void testCheckActiveAt01Am() {
        Clock clock = Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(1, 0), zone).toInstant(), zone);
        assert !checkActive(clock, morning);
        assert checkActive(clock, atNight);
        assert !checkActive(clock, closed);
        assert !checkActive(clock, invalid);
        assert checkActive(clock, always);
        assert checkActive(clock, always2);
    }

    @Test
    public void testCheckActiveAt10Am() {
        Clock clock = Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(10, 0), zone).toInstant(), zone);
        assert checkActive(clock, morning);
        assert !checkActive(clock, atNight);
        assert !checkActive(clock, closed);
        assert !checkActive(clock, invalid);
        assert checkActive(clock, always);
        assert checkActive(clock, always2);
    }

    @Test
    public void testCheckActiveAt10Pm() {
        Clock clock = Clock.fixed(ZonedDateTime.of(LocalDate.now(), LocalTime.of(22, 0), zone).toInstant(), zone);
        assert !checkActive(clock, morning);
        assert !checkActive(clock, atNight);
        assert !checkActive(clock, closed);
        assert !checkActive(clock, invalid);
        assert checkActive(clock, always);
        assert checkActive(clock, always2);
    }
}
