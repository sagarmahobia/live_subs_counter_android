package com.sagar.livesubscounter.greendao.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by SAGAR MAHOBIA on 18-Sep-18. at 23:05
 */

@Entity(nameInDb = "favorite_channels")
public class FavoriteChannel {

    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @Property(nameInDb = "timestamp")
    private long timestamp;

    @Property(nameInDb = "channel_id")
    private String channelId;

    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "thumbnail_url")
    private String thumbnailUrl;


    @Generated(hash = 149796794)
    public FavoriteChannel(Long id, long timestamp, String channelId, String title,
                           String thumbnailUrl) {
        this.id = id;
        this.timestamp = timestamp;
        this.channelId = channelId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Generated(hash = 2026418151)
    public FavoriteChannel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


}
