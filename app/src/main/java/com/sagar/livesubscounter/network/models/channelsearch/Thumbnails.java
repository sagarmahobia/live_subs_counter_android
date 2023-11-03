
package com.sagar.livesubscounter.network.models.channelsearch;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("default")
    private Default _default;

    private Medium medium;

    private High high;

    public Default getDefault() {
        return _default;
    }

    public Medium getMedium() {
        return medium;
    }

    public High getHigh() {
        return high;
    }
}
