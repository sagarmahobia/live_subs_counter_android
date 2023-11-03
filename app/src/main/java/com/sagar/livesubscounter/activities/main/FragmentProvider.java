package com.sagar.livesubscounter.activities.main;

import com.sagar.livesubscounter.activities.main.channel.ChannelFragment;
import com.sagar.livesubscounter.activities.main.channel.ChannelFragmentModule;
import com.sagar.livesubscounter.activities.main.channel.ChannelFragmentScope;
import com.sagar.livesubscounter.activities.main.compare.CompareFragment;
import com.sagar.livesubscounter.activities.main.compare.CompareFragmentModule;
import com.sagar.livesubscounter.activities.main.compare.CompareFragmentScope;
import com.sagar.livesubscounter.activities.main.favoritechannels.FavoriteChannelsFragment;
import com.sagar.livesubscounter.activities.main.favoritechannels.FavoriteChannelsFragmentModule;
import com.sagar.livesubscounter.activities.main.favoritechannels.FavoriteChannelsFragmentScope;
import com.sagar.livesubscounter.activities.main.favoritecompares.FavoriteComparesFragment;
import com.sagar.livesubscounter.activities.main.favoritecompares.FavoriteComparesFragmentModule;
import com.sagar.livesubscounter.activities.main.favoritecompares.FavoriteComparesFragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mertsimsek on 02/06/2017.
 */
@SuppressWarnings("unused")
@Module
public abstract class FragmentProvider {

    //methods are not used but @ are read to generate dependency map

    @ChannelFragmentScope
    @ContributesAndroidInjector(modules = ChannelFragmentModule.class)
    abstract ChannelFragment provideChannelFragment();

    @CompareFragmentScope
    @ContributesAndroidInjector(modules = CompareFragmentModule.class)
    abstract CompareFragment provideCompareFragment();

    @FavoriteChannelsFragmentScope
    @ContributesAndroidInjector(modules = FavoriteChannelsFragmentModule.class)
    abstract FavoriteChannelsFragment provideFavoriteChannelsFragment();

    @FavoriteComparesFragmentScope
    @ContributesAndroidInjector(modules = FavoriteComparesFragmentModule.class)
    abstract FavoriteComparesFragment provideFavoriteComparesFragment();

}
