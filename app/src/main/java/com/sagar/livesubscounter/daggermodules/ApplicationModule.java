package com.sagar.livesubscounter.daggermodules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sagar.livesubscounter.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by SAGAR MAHOBIA on 14-Sep-18. at 01:37
 */

@Module(includes = {NetworkModule.class, GreenDaoModule.class, FirebaseModule.class, AdmobModule.class})
public class ApplicationModule {


    @Provides
    @ApplicationScope
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
