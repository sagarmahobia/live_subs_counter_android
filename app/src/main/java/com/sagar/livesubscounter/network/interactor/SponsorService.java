package com.sagar.livesubscounter.network.interactor;

import com.sagar.livesubscounter.network.models.sponsor.SpareKeys;
import com.sagar.livesubscounter.network.repo.SponsorRepo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SAGAR MAHOBIA on 12-Nov-18. at 00:16
 */

public class SponsorService implements SponsorRepo {

    private final SponsorRepo repo;

    @Inject
    SponsorService(SponsorRepo repo) {
        this.repo = repo;
    }

    @Override
    public Observable<SpareKeys> getSpareKeysObservable() {
        return repo.getSpareKeysObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
