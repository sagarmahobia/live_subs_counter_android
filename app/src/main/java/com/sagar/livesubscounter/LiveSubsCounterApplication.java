package com.sagar.livesubscounter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.sagar.livesubscounter.constants.KeyStore;
import com.sagar.livesubscounter.network.interactor.SponsorService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by SAGAR MAHOBIA on 14-Sep-18. at 01:36
 */
public class LiveSubsCounterApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    @Inject
    SponsorService sponsorService;

    @Inject
    KeyStore keyStore;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.admob_app_pub_id));

        DaggerApplicationComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        sharedPreferenceService.incrementLaunchCount();
        sponsorService.getSpareKeysObservable().subscribe((keys) -> {
            keyStore.addAll(keys);
        }, Crashlytics::logException);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }


}
