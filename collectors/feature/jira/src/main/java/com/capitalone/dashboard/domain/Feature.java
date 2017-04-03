package com.capitalone.dashboard.domain;

import com.google.gson.annotations.SerializedName;

public class Feature extends RallyObject {
    @SerializedName("FormattedID")
    String formattedId;

    @SerializedName("DirectChildrenCount")
    int countOfChildren;

    @SerializedName("Name")
    String name;
}
