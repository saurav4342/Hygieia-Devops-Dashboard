package com.capitalone.dashboard.domain;

import com.google.gson.annotations.SerializedName;

public abstract class RallyObject {
    @SerializedName("_refObjectUUID")
    String uuid;
    @SerializedName("_ref")
    String referenceUri;

    public String getUuid() {
        return uuid;
    }
}
