package com.capitalone.dashboard.client;

import org.apache.commons.lang3.StringUtils;

public class RallyServiceException extends RuntimeException {
    public RallyServiceException(String[] errors) {
        super(StringUtils.join(errors));
    }

    public RallyServiceException(Throwable e) {
        super(e);
    }
}
