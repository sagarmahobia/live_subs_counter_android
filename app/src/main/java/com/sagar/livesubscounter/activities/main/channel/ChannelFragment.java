package com.sagar.livesubscounter.activities.main.channel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.activities.main.HostActivity;
import com.sagar.livesubscounter.activities.search.SearchActivity;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.constants.RequestCodes;
import com.sagar.livesubscounter.databinding.FragmentChannelBinding;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;
import com.sagar.livesubscounter.viewmodel.Response;
import com.sagar.livesubscounter.viewmodel.Status;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;


public class ChannelFragment extends Fragment {

    @Inject
    ChannelViewModelFactory channelViewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    FirebaseService firebaseService;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    private String channelId;

    private int prevSubCount;
    private HostActivity activity;
    private ChannelViewModel channelViewModel;

    private FragmentChannelBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        channelId = arguments != null ? arguments.getString(ConstantKey.CHANNEL_ID) : null;

        if (channelId != null) {
            channelViewModelFactory.setChannelId(channelId);
            prevSubCount = Integer.parseInt(sharedPreferenceService.getCount(channelId));
        } else {
            throw new IllegalStateException("Channel Id should be passed");
        }

        channelViewModel = ViewModelProviders.of(this, channelViewModelFactory).get(ChannelViewModel.class);
        getLifecycle().addObserver(channelViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_channel, container, false);

        binding.openYoutube.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/" + channelId)
            );
            startActivity(browserIntent);
        });

        binding.compareButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivityForResult(intent, RequestCodes.FOR_COMPARE);
                    firebaseService.comparingChannel();
                }
        );
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (HostActivity) getActivity();
        setUpObservers();
    }


    private void setUpObservers() {
        channelViewModel.getCounterSnippetResponse().observe(this, this::onCounterSnippetResponse);
        channelViewModel.getCounterStatisticsResponse().observe(this, this::onCounterStatisticsResponse);
        channelViewModel.getActionFavoriteResponse().observe(this, this::onActionFavoriteResponse);
        channelViewModel.getTitleResponse().observe(this, this::setTitle);

    }

    private void onCounterSnippetResponse(Response<ChannelModel> response) {
        Status status = response.getStatus();
        drawScreenState(status);
        if (status == Status.SUCCESS) {
            ChannelModel channelModel = response.getData();
            prepareInfo(channelModel);
        }
    }

    private void onCounterStatisticsResponse(Response<ChannelModel> response) {
        Status status = response.getStatus();
        drawScreenState(status);
        if (status == Status.SUCCESS) {
            ChannelModel channelModel = response.getData();
            updateStats(channelModel);
        }

    }

    private void onPrepareMenuResponse(Response<Boolean> response) {
        if (response.getStatus() == Status.SUCCESS) {
            setFavoriteIcon(response.getData());
        }
    }

    private void onActionFavoriteResponse(Response<Boolean> response) {
        if (response.getStatus() == Status.SUCCESS) {
            Boolean data = response.getData();

            setFavoriteIcon(data);
            if (data) {
                showToast("Added to favorites");
            } else {
                showToast("Removed from favorites");
            }

        } else if (response.getStatus() == Status.ERROR) {
            showToast("There was an error");
        }
    }


    private void setTitle(Response response) {
        if (activity != null) {
            activity.setTitle((String) response.getData());
        }
    }

    private void drawScreenState(Status status) {

        if (status == Status.LOADING) {
            hideErrorMessage();
            showProgress();
        } else if (status == Status.ERROR) {
            hideProgress();
            showErrorMessage();
        } else if (status == Status.SUCCESS) {
            hideProgress();
            hideErrorMessage();

        }
    }

    private void prepareInfo(ChannelModel channelModel) {
        binding.channelName.setText(channelModel.getChannelName());
        picasso.load(channelModel.getUrl()).into(binding.channelImage);
    }

    private void updateStats(ChannelModel channelModel) {
        int subCount = Integer.parseInt(channelModel.getSubscriberCount());
        binding.channelSubscribersTicker.countAnimation(prevSubCount, subCount);

        NumberFormat numberFormat = new DecimalFormat("###,###,###,###");

        binding.channelViewsTicker.setText(numberFormat.format(Long.parseLong(channelModel.getViewCount())));
        binding.channelVideosTicker.setText(numberFormat.format(Integer.parseInt(channelModel.getVideoCount())));
        prevSubCount = subCount;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite, menu);
        channelViewModel.getPrepareMenuResponse().observe(this, this::onPrepareMenuResponse);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            channelViewModel.onClickActionFavorite();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == RequestCodes.FOR_COMPARE) {
            String secondChannel = data.getStringExtra(ConstantKey.CHANNEL_ID);
            if (!channelId.equals(secondChannel)) {
                Bundle bundle = new Bundle();
                bundle.putString(ConstantKey.FIRST_ID, channelId);
                bundle.putString(ConstantKey.SECOND_ID, secondChannel);
                Navigation.findNavController(binding.compareButton).navigate(R.id.action_channelFragment_to_compareFragment, bundle);
            } else {
                showToast("Please select a different channel");
            }
        }
    }


    private void showProgress() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }


    private void hideProgress() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void showToast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }


    private void hideErrorMessage() {
        binding.errorMessageTextAndImage.setVisibility(View.GONE);
        binding.liveCounterGroup.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            //set to nothing
        });
    }

    private void showErrorMessage() {
        binding.liveCounterGroup.setVisibility(View.GONE);
        binding.errorMessageText.setText(R.string.error_message);
        binding.errorIconImageView.setImageResource(R.drawable.ic_error_outline);
        binding.errorMessageTextAndImage.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> channelViewModel.loadOrRefresh());

    }

    @SuppressLint("RestrictedApi")
    private void setFavoriteIcon(boolean bool) {
        ActionMenuItemView menuView = activity.findViewById(R.id.action_favorite);
        if (menuView == null) {
            return;
        }
        Drawable drawable;
        if (bool) {
            drawable = ContextCompat.getDrawable(activity, R.drawable.ic_favorite_white);
        } else {
            drawable = ContextCompat.getDrawable(activity, R.drawable.ic_favorite_border_white);
        }
        menuView.setIcon(drawable);
    }
}
