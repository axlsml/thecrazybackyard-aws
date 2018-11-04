package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.HasInputStream;
import org.junit.Assert;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class EmailReaderTest {

    @Test
    public void test() throws Exception {
        EmailReader readerObj = create("src/test/resources/mime/email-without-attachment");
        ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 4, 23, 1, 0, 0, ZoneId.of("Europe/Berlin"));

        Assert.assertEquals("axel@fake.com", readerObj.sender());
        Assert.assertEquals("test e-mail", readerObj.subject());
        Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
        Assert.assertEquals(0, readerObj.images().size());
    }

    @Test
    public void testAttachment() throws Exception {
        EmailReader readerObj = create("src/test/resources/mime/email-with-attachment");
        ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 2, 14, 8, 18, 0, ZoneId.of("Europe/Berlin"));

        Assert.assertEquals("lappen@fake.com", readerObj.sender());
        Assert.assertEquals("Fwd: 4849-SYEW0124.JPG", readerObj.subject());
        Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
        List<FileWithMetaData> images = readerObj.images();
        Assert.assertEquals(1, images.size());
        Assert.assertEquals("SYEW0124.JPG", images.get(0).getName());
    }

    @Test
    public void testAttachmentReal() throws Exception {
        EmailReader readerObj = create("src/test/resources/mime/q6k3f98015ka9d24kalp1ffi8mifrnfr4d2tfq81");
        ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 6, 19, 29, 46, 0, ZoneId.of("Europe/Berlin"));

        Assert.assertEquals("sender@wildcam.com", readerObj.sender());
        Assert.assertEquals("4849-SYEW0144.JPG", readerObj.subject());
        Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
        List<FileWithMetaData> images = readerObj.images();
        Assert.assertEquals(1, images.size());
        Assert.assertEquals("SYEW0144.JPG", images.get(0).getName());
    }

    private EmailReader create(String filePath) throws IOException, MessagingException {
        HasInputStream x = () -> {
            try {
                return new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        };
        return EmailReaderFactory.createFromMime(x);
    }

}
