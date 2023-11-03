
package com.sagar.livesubscounter.network.models.channelsearch;

import java.util.List;

public class ChannelSearch {

    private String kind;

    private String etag;

    private String nextPageToken;

    private String regionCode;

    private PageInfo pageInfo;

    private List<Item> items;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }
}
