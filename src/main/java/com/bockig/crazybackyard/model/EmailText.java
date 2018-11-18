package com.bockig.crazybackyard.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

class EmailText {

    private static final Logger LOG = LogManager.getLogger(EmailText.class);
    private static final String TEXT_PLAIN = "text/plain";

    private final String text;

    private EmailText(String text) {
        this.text = text;
    }

    static Optional<EmailText> extractFromBodyPart(BodyPart bodyPart) {
        if (!isText(bodyPart)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new EmailText((String) bodyPart.getContent()));
        } catch (MessagingException | IOException e) {
            LOG.error("cannot createFromMime text from BodyPart", e);
        }
        return Optional.empty();
    }

    static boolean isText(BodyPart bodyPart) {
        try {
            return bodyPart.getContentType().startsWith(TEXT_PLAIN);
        } catch (MessagingException e) {
            LOG.error("check test if text", e);
            return false;
        }
    }

    public String getText() {
        return text;
    }
}
