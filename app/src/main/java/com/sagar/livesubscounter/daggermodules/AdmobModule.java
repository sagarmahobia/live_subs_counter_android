package com.sagar.livesubscounter.daggermodules;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.R;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SAGAR MAHOBIA on 07-Aug-18. at 16:37
 */
@Module(includes = ApplicationModule.class)
public class AdmobModule {

    @Provides
    AdRequest request() {
        return new AdRequest.Builder().addTestDevice("##Removed").build();
    }

    @Provides
    @ApplicationScope
    InterstitialAd interstitialAd(Context context, AdRequest adRequest) {
        InterstitialAd ad = new InterstitialAd(context);
        ad.setAdUnitId(context.getResources().getString(R.string.admob_full_screen_adunit));
        ad.loadAd(adRequest);
        return ad;
    }
}
