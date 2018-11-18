package com.bockig.crazybackyard.model;

import com.bockig.crazybackyard.common.CanBeEnabled;
import com.bockig.crazybackyard.common.Forward;
import com.bockig.crazybackyard.common.ForwardFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ReceiveAndForwardImage {

    private static final Logger LOG = LogManager.getLogger(ReceiveAndForwardImage.class);

    private final CanBeEnabled enabled;
    private final Map<String, String> metaData;
    private final Forward forward;

    public ReceiveAndForwardImage(CanBeEnabled enabled, Map<String, String> metaData, Forward forward) {
        this.enabled = enabled;
        this.metaData = metaData;
        this.forward = forward;
    }

    public void checkPreconditionsAndSend() {
        if (postDisabledCurrently(enabled, metaData)) {
            return;
        }
        try {
            forward.forward();
        } catch (ForwardFailedException e) {
            LOG.error("an error occurred during forwarding", e);
        }
    }

    static boolean postDisabledCurrently(CanBeEnabled global, Map<String, String> userMetadata) {
        if (!global.isEnabled()) {
            LOG.info("disabled now (global config) - skipping");
            return true;
        }
        if (userMetadata == null) {
            LOG.info("user meta data null??");
            return true;
        }
        if (!MetaData.isNowActive(userMetadata)) {
            LOG.info("disabled now (sender-specific config) - skipping");
            return true;
        }
        return false;
    }

}
