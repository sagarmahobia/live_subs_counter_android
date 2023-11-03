package com.sagar.livesubscounter.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.activities.search.adapter.SearchItemsAdapter;
import com.sagar.livesubscounter.adapter.EndlessRecyclerViewScrollListener;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.network.models.channelsearch.Id;
import com.sagar.livesubscounter.network.models.channelsearch.Item;
import com.sagar.livesubscounter.viewmodel.listresponse.Response;
import com.sagar.livesubscounter.viewmodel.listresponse.Type;

import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    @Inject
    SearchActivityViewModelFactory factory;

    @Inject
    SearchItemsAdapter adapter;

    @BindView(R.id.searchbar_edittext)
    EditText searchBarEditText;

    @BindView(R.id.clear_icon)
    ImageButton clearButton;

    @BindView(R.id.back_icon)
    ImageButton backButton;

    @BindView(R.id.search_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.error_icon_image_view)
    ImageView errorIcon;

    @BindView(R.id.error_message_text)
    TextView errorMessageTextView;

    @BindView(R.id.error_message_text_and_image)
    Group errorMessage;

    private SearchActivityViewModel viewModel;

    private String query = "";
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, factory).get(SearchActivityViewModel.class);
        viewModel.getSearchResultResponse().observe(this, this::onListResponse);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (query.isEmpty()) {
                hideProgress();
            } else {
                viewModel.query(query);
            }
        });

        searchBarEditText.addTextChangedListener(this);

        clearButton.setOnClickListener(view -> searchBarEditText.setText(""));

        backButton.setOnClickListener((View view) -> this.onBackPressed());

        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(item -> {
            Id id = item.getId();
            String channelId;
            if (id != null) {
                channelId = id.getChannelId();
            } else {
                channelId = item.getSnippet().getChannelId();
            }
            sendResult(channelId);
        });

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int pageToLoad, int totalItemsCount, RecyclerView view) {
                viewModel.onLoadMore();
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }


    private void onListResponse(Response<List<Item>> response) {
        Type type = response.getType();

        if (type == Type.NEW_LIST) {

            hideProgress();
            endlessRecyclerViewScrollListener.resetState();

            if (response.getData().size() < 1) {
                showNoMatchMessage();
            } else {
                adapter.setData(response.getData());
                hideErrorMessage();

            }
        } else if (type == Type.UPDATE_LIST) {

            hideProgress();
            adapter.setData(response.getData());

        } else if (type == Type.ERROR) {

            hideProgress();
            showErrorMessage();


        } else if (type == Type.LOADING) {

            showProgress();

        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String query = editable.toString();
        this.query = query;
        viewModel.query(query);
    }

    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void hideErrorMessage() {
        errorMessage.setVisibility(View.GONE);
    }

    public void showErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        errorIcon.setImageResource(R.drawable.ic_error_outline);
        errorMessageTextView.setText(R.string.error_message);
    }

    public void showNoMatchMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        errorIcon.setImageResource(R.drawable.ic_sad_emoticon);
        errorMessageTextView.setText(R.string.no_match);
    }

    public void sendResult(String channelId) {
        Intent intent = new Intent();
        intent.putExtra(ConstantKey.CHANNEL_ID, channelId);
        setResult(200, intent);
        finish();
    }

}
