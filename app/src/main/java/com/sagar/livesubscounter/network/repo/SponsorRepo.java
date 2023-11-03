package com.sagar.livesubscounter.network.repo;

import com.sagar.livesubscounter.network.models.sponsor.SpareKeys;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by SAGAR MAHOBIA on 12-Nov-18. at 00:12
 */
public interface SponsorRepo {

    @GET("livesubscounter/extra_keys/keys.json")
    Observable<SpareKeys> getSpareKeysObservable();

}
