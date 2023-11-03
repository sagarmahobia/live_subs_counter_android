
package com.sagar.livesubscounter.network.models.channelinfo;

public class Item {

    private String kind;

    private String etag;

    private String id;

    private Statistics statistics;

    private Snippet snippet;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getId() {
        return id;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public Snippet getSnippet() {
        return snippet;
    }
}
