package com.sagar.livesubscounter.activities.main.favoritecompares;

import com.sagar.livesubscounter.greendao.FavoriteCompareService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by SAGAR MAHOBIA on 16-Jan-19. at 19:13
 */

@FavoriteComparesFragmentScope
public class FavoriteComparesViewModelFactory implements ViewModelProvider.Factory {

    private final FavoriteCompareService favoritesService;

    @Inject
    FavoriteComparesViewModelFactory(FavoriteCompareService favoritesService) {
        this.favoritesService = favoritesService;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FavoriteComparesViewModel.class)) {
            return (T) new FavoriteComparesViewModel(favoritesService);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
