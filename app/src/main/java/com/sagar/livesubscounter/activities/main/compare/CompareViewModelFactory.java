package com.sagar.livesubscounter.activities.main.compare;

import com.sagar.livesubscounter.greendao.FavoriteCompareService;
import com.sagar.livesubscounter.network.interactor.YoutubeService;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by SAGAR MAHOBIA on 15-Jan-19. at 21:43
 */


@CompareFragmentScope
public class CompareViewModelFactory implements ViewModelProvider.Factory {
    private final YoutubeService youtubeService;
    private final FavoriteCompareService favoriteCompareService;
    private final FirebaseService firebaseService;
    private String firstId;
    private String secondId;
    private SharedPreferenceService sharedPreferenceService;

    @Inject
    CompareViewModelFactory(YoutubeService youtubeService, FavoriteCompareService favoriteCompareService, FirebaseService firebaseService, SharedPreferenceService sharedPreferenceService) {
        this.youtubeService = youtubeService;
        this.favoriteCompareService = favoriteCompareService;
        this.firebaseService = firebaseService;
        this.sharedPreferenceService = sharedPreferenceService;
    }

    void setChannelIds(String firstId, String secondId) {
        this.firstId = firstId;
        this.secondId = secondId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (firstId == null || secondId == null) {
            throw new IllegalStateException("setChannelIds(String) should be called");
        }
        if (modelClass.isAssignableFrom(CompareViewModel.class)) {
            return (T) new CompareViewModel(youtubeService, favoriteCompareService, firebaseService, sharedPreferenceService, firstId, secondId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
