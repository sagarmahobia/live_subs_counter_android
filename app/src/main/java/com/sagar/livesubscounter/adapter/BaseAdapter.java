package com.sagar.livesubscounter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagar.livesubscounter.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by SAGAR MAHOBIA on 19-Sep-18. at 00:07
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<SingleItemViewHolder> {

    private Picasso picasso;

    public BaseAdapter(Picasso picasso) {

        this.picasso = picasso;
    }

    @NonNull
    @Override
    public SingleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.single_list_item, parent, false);
        return new SingleItemViewHolder(view, picasso);
    }
}
