package com.sagar.livesubscounter.activities.main.channel;

import com.crashlytics.android.Crashlytics;
import com.sagar.livesubscounter.greendao.FavoriteChannelService;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannel;
import com.sagar.livesubscounter.network.interactor.YoutubeService;
import com.sagar.livesubscounter.network.models.channelinfo.Item;
import com.sagar.livesubscounter.network.models.channelinfo.Snippet;
import com.sagar.livesubscounter.network.models.channelinfo.Statistics;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;
import com.sagar.livesubscounter.viewmodel.Response;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by SAGAR MAHOBIA on 14-Jan-19. at 00:10
 */
public class ChannelViewModel extends ViewModel implements LifecycleObserver {

    private final FavoriteChannelService favoriteChannelService;
    private final YoutubeService youtubeService;
    private final FirebaseService firebaseService;
    private final SharedPreferenceService sharedPreferenceService;

    private CompositeDisposable viewModelDisposable;
    private CompositeDisposable disposable;

    private MutableLiveData<Response> counterSnippetResponse;
    private MutableLiveData<Response> counterStatisticsResponse;
    private MutableLiveData<Response> actionFavoriteResponse;
    private MutableLiveData<Response> prepareMenuResponse;
    private MutableLiveData<Response> titleResponse;

    private String title;
    private String channelId;

    private boolean error;

    ChannelViewModel(FavoriteChannelService favoriteChannelService,
                     YoutubeService youtubeService,
                     FirebaseService firebaseService, SharedPreferenceService sharedPreferenceService, String channelId) {

        this.favoriteChannelService = favoriteChannelService;
        this.youtubeService = youtubeService;
        this.firebaseService = firebaseService;
        this.sharedPreferenceService = sharedPreferenceService;
        this.channelId = channelId;

        counterSnippetResponse = new MutableLiveData<>();
        counterStatisticsResponse = new MutableLiveData<>();
        actionFavoriteResponse = new MutableLiveData<>();
        prepareMenuResponse = new MutableLiveData<>();
        titleResponse = new MutableLiveData<>();

        viewModelDisposable = new CompositeDisposable();
        loadStates();
    }

    LiveData<Response> getCounterSnippetResponse() {
        return counterSnippetResponse;
    }

    LiveData<Response> getCounterStatisticsResponse() {
        return counterStatisticsResponse;
    }

    LiveData<Response> getActionFavoriteResponse() {
        return actionFavoriteResponse;
    }

    LiveData<Response> getPrepareMenuResponse() {
        return prepareMenuResponse;
    }

    LiveData<Response> getTitleResponse() {
        return titleResponse;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        disposable = new CompositeDisposable();
        prepareMenu();
        loadOrRefresh();
        if (title != null) {
            titleResponse.setValue(Response.success(title));
        }
    }

    void loadOrRefresh() {
        if (error) {
            loadStates();
        }
        counterStatisticsResponse.setValue(Response.loading());
        updateState(0);
    }

    private void loadStates() {
        viewModelDisposable.add(youtubeService.
                getChannelSnippet(channelId).
                subscribe(channelInfo -> {

                            Item item = channelInfo.getItems().get(0);

                            Snippet snippet = item.getSnippet();
                            String channelTitle = snippet.getTitle();
                            String channelThumbnail = snippet.getThumbnails().getMedium().getUrl();

                            ChannelModel channelModel = new ChannelModel();
                            channelModel.setChannelName(channelTitle);
                            channelModel.setUrl(channelThumbnail);

                            error = false;
                            title = channelTitle;
                            titleResponse.setValue(Response.success(title));
                            counterSnippetResponse.setValue(Response.success(channelModel));
                        },
                        throwable -> {
                            error = true;
                            counterSnippetResponse.setValue(Response.error(throwable));
                            Crashlytics.logException(throwable);
                        }
                )
        );
    }

    private void updateState(int delay) {
        disposable.add(youtubeService.
                getChannelStatistics(channelId).
                delay(delay, TimeUnit.SECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(channelInfo -> {

                            Item item = channelInfo.getItems().get(0);

                            Statistics statistics = item.
                                    getStatistics();

                            String subCount = statistics.
                                    getSubscriberCount();
                            String viewCount = statistics.
                                    getViewCount();
                            String videoCount = statistics.
                                    getVideoCount();


                            ChannelModel channelModel = new ChannelModel();
                            channelModel.setSubscriberCount(subCount);
                            channelModel.setViewCount(viewCount);
                            channelModel.setVideoCount(videoCount);

                            sharedPreferenceService.saveCount(item.getId(), subCount);

                            if (!error) {
                                counterStatisticsResponse.setValue(Response.success(channelModel));
                                updateState(5);
                            }
                        },
                        throwable -> {
                            counterStatisticsResponse.setValue(Response.error(throwable));
                            Crashlytics.logException(throwable);
                        }
                )
        );
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onCleared() {
        if (viewModelDisposable != null) {
            viewModelDisposable.dispose();
        }
    }

    private void prepareMenu() {
        viewModelDisposable.add(favoriteChannelService.
                checkFavorite(channelId).
                subscribe(bool -> prepareMenuResponse.setValue(Response.success(bool)), Crashlytics::logException));
    }


    void onClickActionFavorite() {
        disposable.add(favoriteChannelService.checkFavorite(channelId)
                .subscribe(bool -> {
                    if (bool) {
                        disposable.add(favoriteChannelService.removeFavorite(channelId).
                                subscribe(() -> actionFavoriteResponse.setValue(Response.success(false)), error -> {
                                    actionFavoriteResponse.setValue(Response.error(error));
                                    Crashlytics.logException(error);
                                }));
                    } else {
                        disposable.add(youtubeService.
                                getChannelSnippet(channelId).
                                subscribe((channelInfo) -> {
                                    Item item = channelInfo.getItems().get(0);

                                    Snippet snippet = item.getSnippet();
                                    String channelTitle = snippet.getTitle();
                                    String channelThumbnail = snippet.getThumbnails().getMedium().getUrl();

                                    FavoriteChannel favorite = new FavoriteChannel(null, System.currentTimeMillis(), channelId, channelTitle, channelThumbnail);
                                    firebaseService.addedToFavorite(channelId);
                                    disposable.add(favoriteChannelService.insertFavorite(favorite).subscribe(
                                            key -> actionFavoriteResponse.setValue(Response.success(true)), error -> {

                                        actionFavoriteResponse.setValue(Response.error(error));
                                        Crashlytics.logException(error);
                                    }));
                                }, error -> {
                                    actionFavoriteResponse.setValue(Response.error(error));
                                    Crashlytics.logException(error);
                                }));
                    }
                }, Crashlytics::logException));
    }

}
