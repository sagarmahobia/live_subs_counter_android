package com.sagar.livesubscounter.network.interactor;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.network.models.channelinfo.ChannelInfo;
import com.sagar.livesubscounter.network.models.channelsearch.ChannelSearch;
import com.sagar.livesubscounter.network.repo.YoutubeRepo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SAGAR MAHOBIA on 15-Sep-18. at 12:55
 */

@ApplicationScope
public class YoutubeService implements YoutubeRepo {

    private YoutubeRepo youtubeRepo;

    @Inject
    public YoutubeService(YoutubeRepo youtubeRepo) {
        this.youtubeRepo = youtubeRepo;
    }


    @Override
    public Observable<ChannelInfo> getChannelStatisticsAndSnippet(String id) {
        return youtubeRepo.
                getChannelStatisticsAndSnippet(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ChannelInfo> getChannelStatistics(String id) {
        return youtubeRepo.
                getChannelStatistics(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ChannelInfo> getChannelSnippet(String id) {
        return youtubeRepo.
                getChannelSnippet(id).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ChannelSearch> getChannelSearchObservable(String q) {
        return youtubeRepo.
                getChannelSearchObservable(q).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<ChannelSearch> getChannelSearchObservable(String q, String nextPageToken) {
        return youtubeRepo.
                getChannelSearchObservable(q, nextPageToken).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }
}
