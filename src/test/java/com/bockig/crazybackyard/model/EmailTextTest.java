package com.bockig.crazybackyard.model;

import org.junit.Test;

import java.util.Optional;

public class EmailTextTest extends BodyPartTests {

    @Test
    public void testIsText() {
        assert EmailText.isText(plainBodyPart("text/plain"));
        assert !EmailText.isText(plainBodyPart("application/json"));
    }

    @Test
    public void testExtractFromBodyPartInvalidType() {
        Optional<EmailText> result = EmailText.extractFromBodyPart(plainBodyPart("text/anything", "any text"));
        assert !result.isPresent();
    }

    @Test
    public void testExtractFromBodyPart() {
        Optional<EmailText> result = EmailText.extractFromBodyPart(plainBodyPart("text/plain", "any text"));
        assert result.isPresent();
        assert result.get().getText().equals("any text");
    }

}
