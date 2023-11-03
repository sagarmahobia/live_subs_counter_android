package com.sagar.livesubscounter.activities.main.compare;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.crashlytics.android.Crashlytics;
import com.sagar.livesubscounter.activities.main.compare.channelcard.ChannelCardModel;
import com.sagar.livesubscounter.greendao.FavoriteCompareService;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompare;
import com.sagar.livesubscounter.network.interactor.YoutubeService;
import com.sagar.livesubscounter.network.models.channelinfo.Item;
import com.sagar.livesubscounter.network.models.channelinfo.Snippet;
import com.sagar.livesubscounter.network.models.channelinfo.Statistics;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;
import com.sagar.livesubscounter.viewmodel.Response;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by SAGAR MAHOBIA on 15-Jan-19. at 21:45
 */
public class CompareViewModel extends ViewModel implements LifecycleObserver {


    private final YoutubeService youtubeService;
    private final FavoriteCompareService favoriteCompareService;
    private final FirebaseService firebaseService;

    private CompositeDisposable viewModelDisposable;
    private CompositeDisposable disposable;

    private MutableLiveData<Response> counterSnippetResponse;
    private MutableLiveData<Response> counterStatisticsResponse;
    private MutableLiveData<Response> actionFavoriteResponse;
    private MutableLiveData<Response> prepareMenuResponse;
    private MutableLiveData<Response> titleResponse;

    private final String firstId;
    private final String secondId;

    private boolean error;
    private String title;
    private SharedPreferenceService sharedPreferenceService;

    private ChannelCardModel firstChannelCardModel;
    private ChannelCardModel secondChannelCardModel;

    CompareViewModel(YoutubeService youtubeService,
                     FavoriteCompareService favoriteCompareService,
                     FirebaseService firebaseService, SharedPreferenceService sharedPreferenceService, String firstId, String secondId) {

        this.youtubeService = youtubeService;
        this.favoriteCompareService = favoriteCompareService;
        this.firebaseService = firebaseService;
        this.firstId = firstId;
        this.secondId = secondId;
        this.sharedPreferenceService = sharedPreferenceService;

        counterSnippetResponse = new MutableLiveData<>();
        counterStatisticsResponse = new MutableLiveData<>();
        actionFavoriteResponse = new MutableLiveData<>();
        prepareMenuResponse = new MutableLiveData<>();
        titleResponse = new MutableLiveData<>();

        viewModelDisposable = new CompositeDisposable();

        firstChannelCardModel = new ChannelCardModel();
        secondChannelCardModel = new ChannelCardModel();

        loadStats();
    }

    ChannelCardModel getFirstChannelCardModel() {
        return firstChannelCardModel;
    }

    public ChannelCardModel getSecondChannelCardModel() {
        return secondChannelCardModel;
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

    MutableLiveData<Response> getPrepareMenuResponse() {
        return prepareMenuResponse;
    }

    MutableLiveData<Response> getTitleResponse() {
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
            loadStats();
        }
        error = false;
        counterStatisticsResponse.setValue(Response.loading());
        updateStats(0);
    }

    private void loadStats() {
        viewModelDisposable.add(youtubeService.
                getChannelSnippet(firstId + "," + secondId).
                subscribe(channelInfo -> {

                            Item firstItem = channelInfo.getItems().get(0);
                            Item secondItem = channelInfo.getItems().get(1);

                            Snippet firstItemSnippet = firstItem.getSnippet();
                            String firstChannelTitle = firstItemSnippet.getTitle();
                            String firstChannelThumbnail = firstItemSnippet.getThumbnails().getMedium().getUrl();

                            Snippet secondItemSnippet = secondItem.getSnippet();
                            String secondChannelTitle = secondItemSnippet.getTitle();
                            String secondChannelThumbnail = secondItemSnippet.getThumbnails().getMedium().getUrl();

                            CompareModel compareModel = new CompareModel();

                            if (firstItem.getId().equalsIgnoreCase(firstId)) {

                                compareModel.setFirstChannelName(firstChannelTitle);
                                compareModel.setSecondChannelName(secondChannelTitle);

                                compareModel.setFirstUrl(firstChannelThumbnail);
                                compareModel.setSecondUrl(secondChannelThumbnail);

                            } else {
                                compareModel.setFirstChannelName(secondChannelTitle);
                                compareModel.setSecondChannelName(firstChannelTitle);

                                compareModel.setFirstUrl(secondChannelThumbnail);
                                compareModel.setSecondUrl(firstChannelThumbnail);
                            }
                            error = false;
                            title = compareModel.getFirstChannelName() + " VS " + compareModel.getSecondChannelName();
                            titleResponse.setValue(Response.success(title));
                            counterSnippetResponse.setValue(Response.success(compareModel));

                        },
                        throwable -> {
                            error = true;
                            counterSnippetResponse.setValue(Response.error(throwable));
                            Crashlytics.logException(throwable);
                        }
                )
        );
    }

    private void updateStats(int delay) {
        disposable.add(youtubeService.
                getChannelStatistics(firstId + "," + secondId).
                delay(delay, TimeUnit.SECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(channelInfo -> {

                            Item firstItem = channelInfo.getItems().get(0);
                            Item secondItem = channelInfo.getItems().get(1);

                            Statistics firstItemStatistics = firstItem.
                                    getStatistics();
                            Statistics secondItemStatistics = secondItem.
                                    getStatistics();

                            String firstSubCount = firstItemStatistics.
                                    getSubscriberCount();
                            String secondSubsCount = secondItemStatistics.
                                    getSubscriberCount();

                            CompareModel compareModel = new CompareModel();

                            String firstItemId = firstItem.getId();
                            String secondItemId = secondItem.getId();

                            if (firstItemId.equalsIgnoreCase(firstId)) {

                                compareModel.setFirstSubscriberCount(firstSubCount);
                                compareModel.setSecondSubscriberCount(secondSubsCount);

                            } else {

                                compareModel.setFirstSubscriberCount(secondSubsCount);
                                compareModel.setSecondSubscriberCount(firstSubCount);

                            }

                            sharedPreferenceService.saveCount(firstItemId, firstSubCount);
                            sharedPreferenceService.saveCount(secondItemId, secondSubsCount);

                            if (!error) {
                                counterStatisticsResponse.setValue(Response.success(compareModel));
                                updateStats(5);
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
        disposable.add(favoriteCompareService.
                checkFavorite(firstId, secondId).
                subscribe(bool -> prepareMenuResponse.setValue(Response.success(bool)), Crashlytics::logException));
    }

    void onClickActionFavorite() {

        disposable.add(favoriteCompareService.checkFavorite(firstId, secondId)
                .subscribe(bool -> {
                    if (bool) {
                        disposable.add(favoriteCompareService.removeFavorite(firstId, secondId).
                                subscribe(() -> actionFavoriteResponse.setValue(Response.success(false)),
                                        error -> {
                                            actionFavoriteResponse.setValue(Response.error(error));
                                            Crashlytics.logException(error);
                                        }));
                    } else {
                        disposable.add(youtubeService.
                                getChannelSnippet(firstId + "," + secondId).
                                subscribe((channelInfo) -> {
                                    Item firstItem = channelInfo.getItems().get(0);

                                    Snippet firstItemSnippet = firstItem.getSnippet();
                                    String firstChannelTitle = firstItemSnippet.getTitle();
                                    String firstChannelThumbnail = firstItemSnippet.getThumbnails().getMedium().getUrl();
                                    String firstChannelId = firstItem.getId();

                                    Item secondItem = channelInfo.getItems().get(1);

                                    Snippet secondItemSnippet = secondItem.getSnippet();
                                    String secondChannelTitle = secondItemSnippet.getTitle();
                                    String secondChannelThumbnail = secondItemSnippet.getThumbnails().getMedium().getUrl();
                                    String secondChannelId = secondItem.getId();

                                    FavoriteCompare favorite = new FavoriteCompare();
                                    favorite.setTimestamp(System.currentTimeMillis());

                                    favorite.setFirstChannelId(firstChannelId);
                                    favorite.setFirstChannelTitle(firstChannelTitle);
                                    favorite.setFirstChannelThumbnailUrl(firstChannelThumbnail);

                                    favorite.setSecondChannelId(secondChannelId);
                                    favorite.setSecondChannelTitle(secondChannelTitle);
                                    favorite.setSecondChannelThumbnailUrl(secondChannelThumbnail);
                                    firebaseService.addedToFavCompare(firstChannelId, secondChannelId);
                                    disposable.add(favoriteCompareService.insertFavorite(favorite).
                                            subscribe(key -> actionFavoriteResponse.setValue(Response.success(true)),
                                                    error -> {
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
