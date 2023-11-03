
package com.sagar.livesubscounter.network.models.channelinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("customUrl")
    @Expose
    private String customUrl;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

}
