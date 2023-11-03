package com.sagar.livesubscounter.activities.main.favoritechannels.adapter;

import android.view.View;

import com.sagar.livesubscounter.activities.main.favoritechannels.FavoriteChannelsFragmentScope;
import com.sagar.livesubscounter.adapter.BaseAdapter;
import com.sagar.livesubscounter.adapter.SingleItemViewHolder;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannel;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Created by SAGAR MAHOBIA on 15-Jan-19. at 23:22
 */

@FavoriteChannelsFragmentScope
public class FavoriteChannelsAdapter extends BaseAdapter {

    private List<FavoriteChannel> favoriteChannels;
    private OnItemClickListener itemClickListener;

    @Inject
    FavoriteChannelsAdapter(Picasso picasso) {
        super(picasso);
    }

    public void setData(List<FavoriteChannel> favoriteChannels) {
        this.favoriteChannels = favoriteChannels;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onBindViewHolder(@NonNull SingleItemViewHolder singleItemViewHolder, int position) {
        FavoriteChannel favoriteChannel = favoriteChannels.get(position);

        singleItemViewHolder.setName(favoriteChannel.getTitle());
        singleItemViewHolder.setThumbnail(favoriteChannel.getThumbnailUrl());
        singleItemViewHolder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onClick(v, favoriteChannel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteChannels != null ? favoriteChannels.size() : 0;
    }

    public interface OnItemClickListener {
        void onClick(View v, FavoriteChannel favoriteChannel);
    }
}
