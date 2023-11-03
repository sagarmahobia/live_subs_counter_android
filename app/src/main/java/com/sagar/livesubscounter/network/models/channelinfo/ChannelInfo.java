
package com.sagar.livesubscounter.network.models.channelinfo;

import java.util.List;

public class ChannelInfo {

    private String kind;

    private String etag;

    private PageInfo pageInfo;

    private List<Item> items = null;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }
}
