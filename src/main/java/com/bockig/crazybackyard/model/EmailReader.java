package com.bockig.crazybackyard.model;

import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmailReader {

    private static final Logger LOG = LogManager.getLogger(EmailReader.class);

    private static final String MULTIPART_MIXED = "multipart/mixed";
    private static final String MULTIPART_ALTERNATIVE = "multipart/alternative";

    private MimeMessageParser message;

    private EmailReader(MimeMessageParser message) {
        this.message = message;
    }

    public static Optional<EmailReader> create(InputStream source) {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        try {
            MimeMessage message = new MimeMessage(mailSession, source);
            return Optional.of(new EmailReader(new MimeMessageParser(message)));
        } catch (MessagingException e) {
            LOG.error("cant create reader", e);
        }
        return Optional.empty();
    }

    public String sender() {
        try {
            return message.getFrom();
        } catch (Exception e) {
            LOG.error(e);
            return "";
        }
    }

    public String subject() {
        try {
            return message.getSubject();
        } catch (Exception e) {
            LOG.error(e);
            return "";
        }
    }

    public List<Image> images() throws Exception {
        String contentType = message.getMimeMessage().getContentType();
        if (contentType.startsWith(MULTIPART_MIXED)) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getMimeMessage().getContent();
            return IntStream.range(0, mimeMultipart.getCount())
                    .mapToObj(i -> bodyPart(i, mimeMultipart))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Image::fromBodyPart)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<EmailText> texts() {
        try {
            String contentType = message.getMimeMessage().getContentType();
            if (contentType.startsWith(MULTIPART_MIXED) || contentType.startsWith(MULTIPART_ALTERNATIVE)) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getMimeMessage().getContent();
                return IntStream.range(0, mimeMultipart.getCount())
                        .mapToObj(i -> bodyPart(i, mimeMultipart))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(EmailText::fromBodyPart)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (MessagingException | IOException e) {
            LOG.error("cant create texts", e);
            return Collections.emptyList();
        }
    }

    private Optional<BodyPart> bodyPart(Integer i, MimeMultipart mimeMultipart) {
        try {
            return Optional.of(mimeMultipart.getBodyPart(i));
        } catch (MessagingException e) {
            LOG.error("cant create bodypart", e);
            return Optional.empty();
        }
    }

    Optional<ZonedDateTime> timestamp() {
        Optional<ZonedDateTime> fromText = timestampFromText();
        if (fromText.isPresent()) {
            return fromText;
        }
        return fromEmailHeader();
    }

    private Optional<ZonedDateTime> fromEmailHeader() {
        try {
            String[] date = message.getMimeMessage().getHeader("Date");
            ZonedDateTime timestamp = null;
            if (date.length > 0) {
                timestamp = ZonedDateTime.parse(date[0].replaceAll(" {2}", ""), DateTimeFormatter.RFC_1123_DATE_TIME);
            }
            return Optional.ofNullable(timestamp);
        } catch (MessagingException e) {
            LOG.error("cant parse timestamp", e);
            return Optional.empty();
        }
    }

    private Optional<ZonedDateTime> timestampFromText() {
        return texts().stream()
                .map(EmailText::extractTimestampFromText)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    public Map<String, String> metaData() {
        Map<String, String> meta = new HashMap<>();
        meta.put(MetaData.UTC, String.valueOf(timestamp().flatMap(t -> Optional.of(t.toInstant().toEpochMilli())).orElse(0L)));
        meta.put(MetaData.FROM, sender());
        meta.put(MetaData.SUBJECT, subject());
        return meta;
    }
}
