package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.FileWithMetaData;
import com.bockig.crazybackyard.common.ImageProvider;
import com.bockig.crazybackyard.common.SimpleFile;
import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EmailParser implements ImageProvider {

    private static final Logger LOG = LogManager.getLogger(EmailParser.class);

    private static final Pattern TIME_PATTERN = Pattern.compile("[^-]+-[^-]+-(\\d+)/(\\d+) (\\d+):(\\d+)-[^-]+");
    private static final String MULTIPART_MIXED = "multipart/mixed";
    private static final String MULTIPART_ALTERNATIVE = "multipart/alternative";

    private MimeMessageParser message;

    EmailParser(MimeMessageParser message) {
        this.message = message;
    }

    String sender() {
        try {
            return message.getFrom();
        } catch (Exception e) {
            LOG.error(e);
            return "";
        }
    }

    String subject() {
        try {
            return message.getSubject();
        } catch (Exception e) {
            LOG.error(e);
            return "";
        }
    }

    public List<FileWithMetaData> images() {
        if (isMultipartContent()) {
            return extractImages();
        }
        return Collections.emptyList();
    }

    private boolean isMultipartContent() {
        try {
            String contentType = message.getMimeMessage().getContentType();
            return contentType.startsWith(MULTIPART_MIXED);
        } catch (MessagingException e) {
            LOG.error("cannot get content type", e);
        }
        return false;
    }

    private List<FileWithMetaData> extractImages() {
        try {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getMimeMessage().getContent();
            return IntStream.range(0, mimeMultipart.getCount())
                    .mapToObj(i -> bodyPart(i, mimeMultipart))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Image::extractFromBodyPart)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(this::withMetaData)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("cannot get multiparts", e);
        }
        return Collections.emptyList();
    }

    private FileWithMetaData withMetaData(SimpleFile simpleFile) {
        return new FileWithMetaData(simpleFile, metaData());
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
                        .map(EmailText::extractFromBodyPart)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (MessagingException | IOException e) {
            LOG.error("cant createFromMime texts", e);
            return Collections.emptyList();
        }
    }

    private Optional<BodyPart> bodyPart(Integer i, MimeMultipart mimeMultipart) {
        try {
            return Optional.of(mimeMultipart.getBodyPart(i));
        } catch (MessagingException e) {
            LOG.error("cant createFromMime bodypart", e);
            return Optional.empty();
        }
    }

    Optional<ZonedDateTime> timestamp() {
        return Stream.of(timestampFromText(), fromEmailHeader(), fromEmailSubject())
                .filter(Optional::isPresent)
                .findAny()
                .orElse(Optional.empty());
    }

    private Optional<ZonedDateTime> fromEmailSubject() {
        Matcher matcher = TIME_PATTERN.matcher(subject());
        if (matcher.matches()) {
            Integer month = Integer.valueOf(matcher.group(1));
            Integer day = Integer.valueOf(matcher.group(2));
            Integer hour = Integer.valueOf(matcher.group(3));
            Integer minute = Integer.valueOf(matcher.group(4));
            return Optional.of(toTimestamp(month, day, hour, minute));
        }
        return Optional.empty();
    }

    private ZonedDateTime toTimestamp(Integer month, Integer day, Integer hour, Integer minute) {
        int year = ZonedDateTime.now().getYear();
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, Hours.DEFAULT_ZONE);
    }

    private Optional<ZonedDateTime> fromEmailHeader() {
        try {
            String[] date = message.getMimeMessage().getHeader("Date");
            ZonedDateTime timestamp = null;
            if (date != null && date.length > 0) {
                timestamp = ZonedDateTime.parse(date[0].replaceAll(" {2}", ""), DateTimeFormatter.RFC_1123_DATE_TIME);
            }
            return Optional.ofNullable(timestamp);
        } catch (MessagingException| DateTimeParseException e) {
            LOG.error("cant parse timestamp", e);
            return Optional.empty();
        }
    }

    private Optional<ZonedDateTime> timestampFromText() {
        return texts().stream()
                .map(EmailText::getText)
                .map(TimestampExtractor::extract)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    private Map<String, String> metaData() {
        Map<String, String> meta = new HashMap<>();
        meta.put(MetaData.UTC, String.valueOf(timestamp().flatMap(t -> Optional.of(t.toInstant().toEpochMilli())).orElse(0L)));
        meta.put(MetaData.FROM, sender());
        meta.put(MetaData.SUBJECT, subject());
        return meta;
    }
}
