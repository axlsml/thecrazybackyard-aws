package com.bockig.crazybackyard.model;

import com.amazonaws.util.StringInputStream;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

class BodyPartMock extends BodyPart {

    static BodyPart createFile(String filename, String content) {
        return new BodyPartMock(filename, content, "", true);
    }

    static BodyPart createPlain(String contentType, String content) {
        return new BodyPartMock("", content, contentType, false);
    }

    static BodyPart createPlain(String contentType) {
        return createPlain(contentType, "");
    }

    private String filename;
    private String content;
    private String type;
    private Boolean asStream;

    private BodyPartMock(String filename, String content, String type, Boolean asStream) {
        this.filename = filename;
        this.content = content;
        this.type = type;
        this.asStream = asStream;
    }

    @Override
    public int getSize() throws MessagingException {
        return 0;
    }


    @Override
    public Object getContent() throws IOException, MessagingException {
        return asStream ? new StringInputStream(content) : content;
    }

    @Override
    public int getLineCount() throws MessagingException {
        return 0;
    }

    @Override
    public String getContentType() throws MessagingException {
        return type;
    }

    @Override
    public boolean isMimeType(String mimeType) throws MessagingException {
        return false;
    }

    @Override
    public String getDisposition() throws MessagingException {
        return null;
    }

    @Override
    public void setDisposition(String disposition) throws MessagingException {

    }

    @Override
    public String getDescription() throws MessagingException {
        return null;
    }

    @Override
    public void setDescription(String description) throws MessagingException {

    }

    @Override
    public String getFileName() throws MessagingException {
        return filename;
    }

    @Override
    public void setFileName(String filename) throws MessagingException {

    }

    @Override
    public InputStream getInputStream() throws IOException, MessagingException {
        return null;
    }

    @Override
    public DataHandler getDataHandler() throws MessagingException {
        return null;
    }

    @Override
    public void setDataHandler(DataHandler dh) throws MessagingException {

    }

    @Override
    public void setContent(Object obj, String type) throws MessagingException {

    }

    @Override
    public void setText(String text) throws MessagingException {

    }

    @Override
    public void setContent(Multipart mp) throws MessagingException {

    }

    @Override
    public void writeTo(OutputStream os) throws IOException, MessagingException {

    }

    @Override
    public String[] getHeader(String header_name) throws MessagingException {
        return new String[0];
    }

    @Override
    public void setHeader(String header_name, String header_value) throws MessagingException {

    }

    @Override
    public void addHeader(String header_name, String header_value) throws MessagingException {

    }

    @Override
    public void removeHeader(String header_name) throws MessagingException {

    }

    @Override
    public Enumeration getAllHeaders() throws MessagingException {
        return null;
    }

    @Override
    public Enumeration getMatchingHeaders(String[] header_names) throws MessagingException {
        return null;
    }

    @Override
    public Enumeration getNonMatchingHeaders(String[] header_names) throws MessagingException {
        return null;
    }
}
