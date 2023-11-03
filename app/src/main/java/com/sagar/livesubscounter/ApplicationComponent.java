package com.sagar.livesubscounter;

import android.app.Application;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sagar.livesubscounter.daggermodules.ApplicationModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by SAGAR MAHOBIA on 14-Sep-18. at 01:37
 */
@ApplicationScope
@Component(modules = {AndroidInjectionModule.class,
        ApplicationModule.class,
        ActivityProvider.class})
public interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }

    void inject(LiveSubsCounterApplication application);

    AdRequest adRequest();

    InterstitialAd interstitialAd();

}
