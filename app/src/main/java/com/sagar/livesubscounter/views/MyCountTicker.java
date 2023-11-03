package com.sagar.livesubscounter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import com.daasuu.cat.CountAnimationTextView;
import com.sagar.livesubscounter.R;

import java.text.DecimalFormat;

/**
 * Created by SAGAR MAHOBIA on 12-Feb-19. at 14:04
 */
public class MyCountTicker extends CountAnimationTextView {
    public MyCountTicker(Context context) {
        super(context);
        this.init();
    }

    public MyCountTicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public MyCountTicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        super.setDecimalFormat(new DecimalFormat("###,###,###"))
                .setInterpolator(new DecelerateInterpolator())
                .setAnimationDuration(3000);
    }


    @Override
    public void countAnimation(int fromValue, int toValue) {
        int color;
        if (toValue >= fromValue) {
            color = R.color.colorTextSuccess;
        } else {
            color = R.color.colorTextFailure;
        }
        setTextColor(getResources().getColor(color));
        super.countAnimation(fromValue, toValue);
    }

    public void countAnimation(int fromValue, int toValue, boolean noColor) {

        if (noColor) {
            super.countAnimation(fromValue, toValue);
        } else {
            this.countAnimation(fromValue, toValue);
        }

    }
}
