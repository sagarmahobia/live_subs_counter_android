package com.sagar.livesubscounter.activities.search;

import com.sagar.livesubscounter.network.interactor.YoutubeService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by SAGAR MAHOBIA on 16-Jan-19. at 20:08
 */

@SearchActivityScope
public class SearchActivityViewModelFactory implements ViewModelProvider.Factory {

    private final YoutubeService youtubeService;

    @Inject
    SearchActivityViewModelFactory(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchActivityViewModel.class)) {
            return (T) new SearchActivityViewModel(youtubeService);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
