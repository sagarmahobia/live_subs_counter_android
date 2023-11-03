package com.sagar.livesubscounter.activities.search;

import com.sagar.livesubscounter.network.interactor.YoutubeService;
import com.sagar.livesubscounter.network.models.channelsearch.Item;
import com.sagar.livesubscounter.viewmodel.listresponse.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by SAGAR MAHOBIA on 16-Jan-19. at 20:03
 */
public class SearchActivityViewModel extends ViewModel {

    private YoutubeService youtubeService;

    private CompositeDisposable viewModelDisposable;

    private List<Item> searchResultList;
    private MutableLiveData<Response> searchResultResponse;
    private Subject<String> throttle;

    private String forQuery = "";
    private String nextPageToken;
    private boolean succeeded;

    SearchActivityViewModel(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;

        searchResultList = new ArrayList<>();
        searchResultResponse = new MutableLiveData<>();
        viewModelDisposable = new CompositeDisposable();
        throttle = PublishSubject.create();

        init();
    }

    private void init() {
        viewModelDisposable.add(throttle.filter(q -> q.length() > 0)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loadList)
        );
    }

    private void loadList(String q) {
        searchResultResponse.setValue(Response.loading());
        viewModelDisposable.add(
                youtubeService.getChannelSearchObservable(q).
                        subscribe(searchResult -> {
                                    succeeded = true;
                                    this.searchResultList.clear();
                                    this.searchResultList.addAll(searchResult.getItems());
                                    searchResultResponse.setValue(Response.newList(searchResultList));
                                    nextPageToken = searchResult.getNextPageToken();
                                },
                                error -> {
                                    succeeded = false;
                                    searchResultResponse.setValue(Response.error(error));
                                })
        );
    }


    void query(String query) {
        if (forQuery.equalsIgnoreCase(query) && succeeded) {
            return;
        }
        succeeded = false;
        forQuery = query;
        throttle.onNext(query);
    }


    void onLoadMore() {
        String cQuery = forQuery;
        if (nextPageToken == null) {
            return;
        }
        searchResultResponse.setValue(Response.loading());
        viewModelDisposable.add(youtubeService.getChannelSearchObservable(cQuery, nextPageToken).
                subscribe(channelSearch -> {
                            nextPageToken = channelSearch.getNextPageToken();
                            if (!forQuery.equalsIgnoreCase(cQuery)) {
                                return;
                            }
                    this.searchResultList.addAll(channelSearch.getItems());
                    this.searchResultResponse.setValue(Response.update(this.searchResultList));
                        }
                )
        );
    }

    MutableLiveData<Response> getSearchResultResponse() {
        return searchResultResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        viewModelDisposable.dispose();
    }

}
