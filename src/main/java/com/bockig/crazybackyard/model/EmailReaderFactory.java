package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.HasInputStream;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailReaderFactory {

    private EmailReaderFactory() {

    }

    public static EmailParser createFromMime(HasInputStream source) throws IOException, MessagingException {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        try (InputStream is = source.inputStream()) {
            MimeMessage message = new MimeMessage(mailSession, is);
            return new EmailParser(new MimeMessageParser(message));
        }
    }

}
