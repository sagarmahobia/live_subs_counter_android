
package com.sagar.livesubscounter.activities.main.compare;

/**
 * Created by SAGAR MAHOBIA on 16-Jan-19. at 12:54
 */
class CompareModel {

    private String firstChannelName;
    private String firstUrl;
    private String firstSubscriberCount;

    private String secondChannelName;
    private String secondUrl;
    private String secondSubscriberCount;

    String getFirstChannelName() {
        return firstChannelName;
    }

    void setFirstChannelName(String firstChannelName) {
        this.firstChannelName = firstChannelName;
    }

    String getFirstUrl() {
        return firstUrl;
    }

    void setFirstUrl(String firstUrl) {
        this.firstUrl = firstUrl;
    }

    String getFirstSubscriberCount() {
        return firstSubscriberCount;
    }

    void setFirstSubscriberCount(String firstSubscriberCount) {
        this.firstSubscriberCount = firstSubscriberCount;
    }

    String getSecondChannelName() {
        return secondChannelName;
    }

    void setSecondChannelName(String secondChannelName) {
        this.secondChannelName = secondChannelName;
    }

    String getSecondUrl() {
        return secondUrl;
    }

    void setSecondUrl(String secondUrl) {
        this.secondUrl = secondUrl;
    }

    String getSecondSubscriberCount() {
        return secondSubscriberCount;
    }

    void setSecondSubscriberCount(String secondSubscriberCount) {
        this.secondSubscriberCount = secondSubscriberCount;
    }
}
