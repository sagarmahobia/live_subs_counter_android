package com.sagar.livesubscounter.activities.main.favoritecompares.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.activities.main.favoritecompares.FavoriteComparesFragmentScope;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompare;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by SAGAR MAHOBIA on 19-Sep-18. at 15:07
 */


@FavoriteComparesFragmentScope
public class FavoriteComparesAdapter extends RecyclerView.Adapter<FavoriteCompareViewHolder> {


    private List<FavoriteCompare> favoriteCompares;
    private OnItemClickListener itemClickListener;

    private Picasso picasso;

    @Inject
    FavoriteComparesAdapter(Picasso picasso) {
        this.picasso = picasso;

    }

    public void setData(List<FavoriteCompare> favoriteCompares) {
        this.favoriteCompares = favoriteCompares;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public FavoriteCompareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.compare_list_item, parent, false);
        return new FavoriteCompareViewHolder(view, picasso);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteCompareViewHolder holder, int position) {
        FavoriteCompare favoriteCompare = favoriteCompares.get(position);

        holder.setFirstChannelName(favoriteCompare.getFirstChannelTitle());
        holder.setSecondChannelName(favoriteCompare.getSecondChannelTitle());

        holder.setFirstChannelThumbnail(favoriteCompare.getFirstChannelThumbnailUrl());
        holder.setSecondChannelThumbnail(favoriteCompare.getSecondChannelThumbnailUrl());

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onClick(v, favoriteCompare);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteCompares != null ? favoriteCompares.size() : 0;
    }

    public interface OnItemClickListener {
        void onClick(View v, FavoriteCompare favoriteCompare);
    }
}
