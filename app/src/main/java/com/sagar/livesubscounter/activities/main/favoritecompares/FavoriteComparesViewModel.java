package com.sagar.livesubscounter.activities.main.favoritecompares;

import com.crashlytics.android.Crashlytics;
import com.sagar.livesubscounter.greendao.FavoriteCompareService;
import com.sagar.livesubscounter.viewmodel.Response;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by SAGAR MAHOBIA on 16-Jan-19. at 19:13
 */
public class FavoriteComparesViewModel extends ViewModel implements LifecycleObserver {

    private
    FavoriteCompareService favoriteCompareService;


    private CompositeDisposable disposable;
    private MutableLiveData<Response> listMutableLiveData;


    FavoriteComparesViewModel(FavoriteCompareService favoriteCompareService) {
        this.favoriteCompareService = favoriteCompareService;
        listMutableLiveData = new MutableLiveData<>();

        disposable = new CompositeDisposable();

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        disposable.add(favoriteCompareService.
                getFavoriteComparesSingle().
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
