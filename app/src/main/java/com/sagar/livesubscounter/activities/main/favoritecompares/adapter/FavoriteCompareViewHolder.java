package com.sagar.livesubscounter.activities.main.favoritecompares.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagar.livesubscounter.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SAGAR MAHOBIA on 19-Sep-18. at 15:08
 */
class FavoriteCompareViewHolder extends ViewHolder {

    @BindView(R.id.first_channel_name_vs)
    TextView firstChannelName;

    @BindView(R.id.second_channel_name_vs)
    TextView secondChannelName;

    @BindView(R.id.first_channel_image_vs)
    ImageView firstChannelImage;

    @BindView(R.id.second_channel_image_vs)
    ImageView secondChannelImage;

    private final Picasso picasso;

    FavoriteCompareViewHolder(View itemView, Picasso picasso) {
        super(itemView);
        this.picasso = picasso;

        ButterKnife.bind(this, itemView);
    }

    void setFirstChannelName(String name) {
        firstChannelName.setText(name);
    }

    void setFirstChannelThumbnail(String url) {
        picasso.load(url).into(firstChannelImage);
    }

    void setSecondChannelName(String name) {
        secondChannelName.setText(name);
    }

    void setSecondChannelThumbnail(String url) {
        picasso.load(url).into(secondChannelImage);
    }
}
