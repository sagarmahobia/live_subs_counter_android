package com.sagar.livesubscounter.utilityservices;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sagar.livesubscounter.ApplicationScope;

import javax.inject.Inject;

/**
 * Created by SAGAR MAHOBIA on 15-Nov-18. at 23:09
 */

@ApplicationScope
public class FirebaseService {

    private FirebaseAnalytics firebaseAnalytics;
    private SharedPreferenceService sharedPreferenceService;

    interface FirebaseLogTags {

        String RATE_REQUEST_ACCEPTED = "rate_request_accepted";
        String ASKED_FOR_RATE = "ask_for_rate";
        String RATE_REQUEST_REJECTED = "rate_dialog_dismissed";

    }

    @Inject
    FirebaseService(FirebaseAnalytics firebaseAnalytics, SharedPreferenceService sharedPreferenceService) {
        this.firebaseAnalytics = firebaseAnalytics;
        this.sharedPreferenceService = sharedPreferenceService;
    }

    public void onRateRequestAccepted() {
        firebaseAnalytics.logEvent(FirebaseLogTags.RATE_REQUEST_ACCEPTED, null);
    }

    public void onRateRequestRejected() {
        firebaseAnalytics.logEvent(FirebaseLogTags.RATE_REQUEST_REJECTED, null);
    }

    public void askForRate() {
        firebaseAnalytics.logEvent(FirebaseLogTags.ASKED_FOR_RATE, null);
    }

    public void searchingForChannel() {
        firebaseAnalytics.logEvent("searching_for_channel", null);
    }

    public void appShared() {
        firebaseAnalytics.logEvent("app_shared", null);
    }

    public void openStoreFromDrawer() {
        firebaseAnalytics.logEvent("open_store_from_drawer", null);
    }

    public void comparingChannel() {
        firebaseAnalytics.logEvent("comparing_channel", null);
    }

    public void addedToFavorite(String channelId) {
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channelId);
        firebaseAnalytics.logEvent("fav_channel", bundle);
    }

    public void addedToFavCompare(String firstChannelId, String secondChannelId) {
        Bundle bundle = new Bundle();
        bundle.putString("channels", firstChannelId + "@VS@" + secondChannelId);
        firebaseAnalytics.logEvent("fav_compare", bundle);
    }

    public void accessedFavChannel(String channelId) {
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channelId);
        firebaseAnalytics.logEvent("accessed_fav_channel", bundle);
    }

    public void accessedFavCompare(String firstChannelId, String secondChannelId) {
        Bundle bundle = new Bundle();
        bundle.putString("channels", firstChannelId + "@VS@" + secondChannelId);
        firebaseAnalytics.logEvent("accessed_fav_compare", bundle);
    }

    public void subscribeToEditedUsers() {
        if (!sharedPreferenceService.isSubscribedToEditedUser()) {
            FirebaseMessaging.getInstance().subscribeToTopic("edited_user");
            sharedPreferenceService.subscribeToEditedUser();
        }
    }

}

