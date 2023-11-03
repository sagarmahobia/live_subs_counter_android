package com.sagar.livesubscounter.greendao.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by SAGAR MAHOBIA on 20-Sep-18. at 16:57
 */

@Entity(nameInDb = "favorites_compares")
public class FavoriteCompare {

    @Id(autoincrement = true)
    @Property(nameInDb = "id")
    private Long id;

    @Property(nameInDb = "timestamp")
    private long timestamp;

    @Property(nameInDb = "first_channel_id")
    private String firstChannelId;

    @Property(nameInDb = "second_channel_id")
    private String secondChannelId;

    @Property(nameInDb = "first_channel_title")
    private String firstChannelTitle;

    @Property(nameInDb = "second_channel_title")
    private String secondChannelTitle;

    @Property(nameInDb = "first_channel_thumbnail_url")
    private String firstChannelThumbnailUrl;

    @Property(nameInDb = "second_channel_thumbnail_url")
    private String secondChannelThumbnailUrl;

    @Generated(hash = 847172254)
    public FavoriteCompare(Long id, long timestamp, String firstChannelId,
                           String secondChannelId, String firstChannelTitle,
                           String secondChannelTitle, String firstChannelThumbnailUrl,
                           String secondChannelThumbnailUrl) {
        this.id = id;
        this.timestamp = timestamp;
        this.firstChannelId = firstChannelId;
        this.secondChannelId = secondChannelId;
        this.firstChannelTitle = firstChannelTitle;
        this.secondChannelTitle = secondChannelTitle;
        this.firstChannelThumbnailUrl = firstChannelThumbnailUrl;
        this.secondChannelThumbnailUrl = secondChannelThumbnailUrl;
    }

    @Generated(hash = 2025953738)
    public FavoriteCompare() {
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

    public String getFirstChannelId() {
        return this.firstChannelId;
    }

    public void setFirstChannelId(String firstChannelId) {
        this.firstChannelId = firstChannelId;
    }

    public String getSecondChannelId() {
        return this.secondChannelId;
    }

    public void setSecondChannelId(String secondChannelId) {
        this.secondChannelId = secondChannelId;
    }

    public String getFirstChannelTitle() {
        return this.firstChannelTitle;
    }

    public void setFirstChannelTitle(String firstChannelTitle) {
        this.firstChannelTitle = firstChannelTitle;
    }

    public String getSecondChannelTitle() {
        return this.secondChannelTitle;
    }

    public void setSecondChannelTitle(String secondChannelTitle) {
        this.secondChannelTitle = secondChannelTitle;
    }

    public String getFirstChannelThumbnailUrl() {
        return this.firstChannelThumbnailUrl;
    }

    public void setFirstChannelThumbnailUrl(String firstChannelThumbnailUrl) {
        this.firstChannelThumbnailUrl = firstChannelThumbnailUrl;
    }

    public String getSecondChannelThumbnailUrl() {
        return this.secondChannelThumbnailUrl;
    }

    public void setSecondChannelThumbnailUrl(String secondChannelThumbnailUrl) {
        this.secondChannelThumbnailUrl = secondChannelThumbnailUrl;
    }


}


