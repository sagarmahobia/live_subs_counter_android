package com.sagar.livesubscounter.utilityservices;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.sagar.livesubscounter.ApplicationScope;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.constants.HomeChannelIds;

import javax.inject.Inject;

/**
 * Created by SAGAR MAHOBIA on 15-Nov-18. at 23:27
 */

@ApplicationScope
public class SharedPreferenceService {

    private SharedPreferences sharedPreferences;

    public void setRated() {
        sharedPreferences.edit().putBoolean(GlobalConstants.OPENED_RATE_APP, true).apply();
    }

    interface GlobalConstants {

        String LAUNCH_COUNT = "launch_count";

        String OPENED_RATE_APP = "open_rate_app";
        String PREFIX_CHANNEL_ID = "CH_";
        String NO_ADS = "no_ads";
    }

    @Inject
    SharedPreferenceService(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void incrementLaunchCount() {
        long launchCount = sharedPreferences.getLong(GlobalConstants.LAUNCH_COUNT, 0);
        sharedPreferences.edit().putLong(GlobalConstants.LAUNCH_COUNT, launchCount + 1).apply();
    }

    public long getLaunchCount() {
        return sharedPreferences.getLong(GlobalConstants.LAUNCH_COUNT, 1);
    }

    public boolean checkRated() {
        return sharedPreferences.getBoolean(GlobalConstants.OPENED_RATE_APP, false);
    }

    @SuppressLint("ApplySharedPref")
    public void saveCount(String channelId, String value) {
        sharedPreferences.edit().putString(GlobalConstants.PREFIX_CHANNEL_ID + channelId, value).commit();
    }

    public String getCount(String channelId) {
        return sharedPreferences.getString(GlobalConstants.PREFIX_CHANNEL_ID + channelId, "0");
    }

    public void setNoAds() {
        sharedPreferences.edit().putBoolean(GlobalConstants.NO_ADS, true).apply();
    }

    public void setHasAds() {
        sharedPreferences.edit().putBoolean(GlobalConstants.NO_ADS, false).apply();
    }

    public boolean noAds() {
        return sharedPreferences.getBoolean(GlobalConstants.NO_ADS, false);
    }

    public String getFirstChannelId() {
        return sharedPreferences.getString(ConstantKey.FIRST_ID, HomeChannelIds.FIRST_CHANNEL_ID);
    }

    public String getSecondChannelId() {
        return sharedPreferences.getString(ConstantKey.SECOND_ID, HomeChannelIds.SECOND_CHANNEL_ID);
    }

    public void setFirstChannelId(String firstChannelId) {
        sharedPreferences.edit().putString(ConstantKey.FIRST_ID, firstChannelId).apply();
    }

    public void setSecondChannelId(String secondChannelId) {
        sharedPreferences.edit().putString(ConstantKey.SECOND_ID, secondChannelId).apply();
    }

    void subscribeToEditedUser() {
        sharedPreferences.edit().putBoolean(ConstantKey.EDITED_USER, true).apply();
    }

    boolean isSubscribedToEditedUser() {
        return sharedPreferences.getBoolean(ConstantKey.EDITED_USER, false);
    }

}
