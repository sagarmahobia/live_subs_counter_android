package com.sagar.livesubscounter.activities.search.adapter;

import com.sagar.livesubscounter.activities.search.SearchActivityScope;
import com.sagar.livesubscounter.adapter.BaseAdapter;
import com.sagar.livesubscounter.adapter.SingleItemViewHolder;
import com.sagar.livesubscounter.network.models.channelsearch.Item;
import com.sagar.livesubscounter.network.models.channelsearch.Snippet;
import com.sagar.livesubscounter.network.models.channelsearch.Thumbnails;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * Created by SAGAR MAHOBIA on 16-Sep-18. at 20:15
 */

@SearchActivityScope
public class SearchItemsAdapter extends BaseAdapter {

    private List<Item> channels;
    private OnItemClickListener itemClickListener;

    @Inject
    SearchItemsAdapter(Picasso picasso) {
        super(picasso);
    }

    public void setData(List<Item> channelSearches) {
        this.channels = channelSearches;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemViewHolder holder, int position) {
        Item item = channels.get(position);

        Snippet snippet = item.getSnippet();
        holder.setName(snippet.getTitle());
        Thumbnails thumbnails = snippet.getThumbnails();
        if (thumbnails != null) {
            holder.setThumbnail(thumbnails.getMedium().getUrl());
        } else {
            holder.setThumbnail(null);
        }

        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels != null ? channels.size() : 0;
    }

    public interface OnItemClickListener {
        void onClick(Item item);
    }
}
