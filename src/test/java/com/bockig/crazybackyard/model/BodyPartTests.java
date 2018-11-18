package com.bockig.crazybackyard.model;

import javax.mail.BodyPart;

abstract class BodyPartTests {

    BodyPart fileBodyPart(String filename, String content) {
        return BodyPartMock.createFile(filename, content);
    }

    BodyPart fileBodyPart(String filename) {
        return fileBodyPart(filename, "");
    }

    BodyPart plainBodyPart(String contentType) {
        return BodyPartMock.createPlain(contentType);
    }

    BodyPart plainBodyPart(String contentType, String content) {
        return BodyPartMock.createPlain(contentType, content);
    }
}
