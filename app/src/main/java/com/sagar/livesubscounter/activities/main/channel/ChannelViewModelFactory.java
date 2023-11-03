package com.sagar.livesubscounter.activities.main.channel;

import com.sagar.livesubscounter.greendao.FavoriteChannelService;
import com.sagar.livesubscounter.network.interactor.YoutubeService;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by SAGAR MAHOBIA on 14-Jan-19. at 00:11
 */

@ChannelFragmentScope
public class ChannelViewModelFactory implements ViewModelProvider.Factory {


    private final FavoriteChannelService favoriteChannelService;
    private final YoutubeService youtubeService;
    private final FirebaseService firebaseService;
    private String channelId;
    private SharedPreferenceService sharedPreferenceService;

    @Inject
    ChannelViewModelFactory(FavoriteChannelService favoriteChannelService,
                            YoutubeService youtubeService,
                            FirebaseService firebaseService, SharedPreferenceService sharedPreferenceService) {
        this.favoriteChannelService = favoriteChannelService;
        this.youtubeService = youtubeService;
        this.firebaseService = firebaseService;
        this.sharedPreferenceService = sharedPreferenceService;
    }

    void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (channelId == null) {
            throw new IllegalStateException("setChannelId(String) should be called");
        }
        if (modelClass.isAssignableFrom(ChannelViewModel.class)) {
            return (T) new ChannelViewModel(favoriteChannelService, youtubeService, firebaseService, sharedPreferenceService, channelId);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }


}
