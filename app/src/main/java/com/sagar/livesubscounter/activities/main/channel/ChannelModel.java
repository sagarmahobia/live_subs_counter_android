package com.sagar.livesubscounter.activities.main.channel;

/**
 * Created by SAGAR MAHOBIA on 14-Jan-19. at 21:20
 */


class ChannelModel {

    private String channelName;
    private String url;

    private String subscriberCount;
    private String viewCount;
    private String videoCount;


    String getChannelName() {
        return channelName;
    }

    void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    String getSubscriberCount() {
        return subscriberCount;
    }

    void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    String getViewCount() {
        return viewCount;
    }

    void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    String getVideoCount() {
        return videoCount;
    }

    void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }
}
