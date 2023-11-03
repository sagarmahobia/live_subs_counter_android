package com.sagar.livesubscounter.network.repo;

import com.sagar.livesubscounter.network.models.channelinfo.ChannelInfo;
import com.sagar.livesubscounter.network.models.channelsearch.ChannelSearch;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SAGAR MAHOBIA on 14-Sep-18. at 13:43
 */
public interface YoutubeRepo {

    @GET("channels?part=statistics,snippet")
    Observable<ChannelInfo> getChannelStatisticsAndSnippet(@Query("id") String id);

    @GET("channels?part=statistics")
    Observable<ChannelInfo> getChannelStatistics(@Query("id") String id);

    @GET("channels?part=snippet")
    Observable<ChannelInfo> getChannelSnippet(@Query("id") String id);

    @GET("search?part=snippet&type=channel")
    Observable<ChannelSearch> getChannelSearchObservable(@Query("q") String q);

    @GET("search?part=snippet&type=channel")
    Observable<ChannelSearch> getChannelSearchObservable(@Query("q") String q, @Query("pageToken") String nextPageToken);
}
