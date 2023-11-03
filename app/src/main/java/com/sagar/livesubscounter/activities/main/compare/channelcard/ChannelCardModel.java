package com.sagar.livesubscounter.activities.main.compare.channelcard;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.sagar.livesubscounter.BR;

public class ChannelCardModel extends BaseObservable {

    private boolean home;

    @Bindable
    public int getEditButtonVisibility() {
        return home ? View.VISIBLE : View.GONE;
    }

    public void setHome(boolean home) {
        this.home = home;
        notifyPropertyChanged(BR.editButtonVisibility);
    }

}
