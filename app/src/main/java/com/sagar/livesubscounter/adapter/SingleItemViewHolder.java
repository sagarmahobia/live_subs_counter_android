package com.sagar.livesubscounter.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagar.livesubscounter.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SAGAR MAHOBIA on 16-Sep-18. at 20:14
 */
public class SingleItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.thumbnail_card_image)
    ImageView thumbnail;

    @BindView(R.id.search_item_text)
    TextView searchItemText;

    private final Picasso picasso;

    SingleItemViewHolder(View view, Picasso picasso) {
        super(view);
        ButterKnife.bind(this, view);
        this.picasso = picasso;
    }


    public void setThumbnail(String url) {
        picasso.load(url).into(thumbnail);
    }

    public void setName(String title) {
        searchItemText.setText(title);
    }
}
