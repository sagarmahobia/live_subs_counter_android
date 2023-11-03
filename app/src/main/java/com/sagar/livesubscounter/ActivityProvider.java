package com.sagar.livesubscounter;

import com.sagar.livesubscounter.activities.main.FragmentProvider;
import com.sagar.livesubscounter.activities.main.HostActivity;
import com.sagar.livesubscounter.activities.main.HostActivityModule;
import com.sagar.livesubscounter.activities.main.HostActivityScope;
import com.sagar.livesubscounter.activities.search.SearchActivity;
import com.sagar.livesubscounter.activities.search.SearchActivityModule;
import com.sagar.livesubscounter.activities.search.SearchActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@SuppressWarnings("unused")
@Module
abstract class ActivityProvider {

    @ContributesAndroidInjector(modules = SearchActivityModule.class)
    @SearchActivityScope
    abstract SearchActivity bindSearchActivity();

    @ContributesAndroidInjector(modules = {HostActivityModule.class, FragmentProvider.class})
    @HostActivityScope
    abstract HostActivity hostActivity();

}
