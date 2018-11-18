package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.SimpleFile;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class ImageTest extends BodyPartTests {

    @Test
    public void testIsImage() {
        assert Image.isImage(fileBodyPart("image.jpg"));
        assert Image.isImage(fileBodyPart("image.JPG"));
        assert !Image.isImage(fileBodyPart("image.png"));
    }

    @Test
    public void testExtractFromBodyPart() throws IOException {
        String content = "dummy content of image.jpg";
        Optional<SimpleFile> result = Image.extractFromBodyPart(fileBodyPart("image.jpg", content));
        assert result.isPresent();
        assert result.get().getFilename().equals("image.jpg");
        byte[] bytes = IOUtils.toByteArray(result.get().inputStream());
        assert Arrays.equals(bytes, content.getBytes());
    }

    @Test
    public void testExtractFromBodyPartNonImage() throws IOException {
        Optional<SimpleFile> result = Image.extractFromBodyPart(fileBodyPart("image.png", "dummy content"));
        assert !result.isPresent();
    }

}
