package com.sagar.livesubscounter.activities.main.favoritechannels;

import com.sagar.livesubscounter.greendao.FavoriteChannelService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by SAGAR MAHOBIA on 15-Jan-19. at 23:39
 */

@FavoriteChannelsFragmentScope
public class FavoriteChannelsViewModelFactory implements ViewModelProvider.Factory {

    private final FavoriteChannelService favoritesService;

    @Inject
    FavoriteChannelsViewModelFactory(FavoriteChannelService favoritesService) {
        this.favoritesService = favoritesService;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteChannelsViewModel.class)) {
            return (T) new FavoriteChannelsViewModel(favoritesService);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
