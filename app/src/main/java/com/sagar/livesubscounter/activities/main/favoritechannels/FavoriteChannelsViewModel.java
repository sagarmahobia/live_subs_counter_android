package com.sagar.livesubscounter.activities.main.favoritechannels;

import com.crashlytics.android.Crashlytics;
import com.sagar.livesubscounter.greendao.FavoriteChannelService;
import com.sagar.livesubscounter.viewmodel.Response;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by SAGAR MAHOBIA on 15-Jan-19. at 23:39
 */
public class FavoriteChannelsViewModel extends ViewModel implements LifecycleObserver {

    private final FavoriteChannelService favoritesService;

    private CompositeDisposable disposable;
    private MutableLiveData<Response> listMutableLiveData;

    FavoriteChannelsViewModel(FavoriteChannelService favoritesService) {
        this.favoritesService = favoritesService;
        listMutableLiveData = new MutableLiveData<>();

        disposable = new CompositeDisposable();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        disposable.add(favoritesService.
                getFavoriteChannelsSingle().
                subscribe(favorites -> this.listMutableLiveData.setValue(Response.success(favorites)),
                        Crashlytics::logException));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    LiveData<Response> getListLiveData() {
        return listMutableLiveData;
    }
}
