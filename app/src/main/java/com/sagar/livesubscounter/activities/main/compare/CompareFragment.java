package com.sagar.livesubscounter.activities.main.compare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sagar.livesubscounter.ApplicationComponent;
import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.activities.main.HostActivity;
import com.sagar.livesubscounter.activities.main.compare.channelcard.ChannelCardModel;
import com.sagar.livesubscounter.activities.search.SearchActivity;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.constants.RequestCodes;
import com.sagar.livesubscounter.databinding.FragmentCompareBinding;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;
import com.sagar.livesubscounter.viewmodel.Response;
import com.sagar.livesubscounter.viewmodel.Status;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompareFragment extends Fragment {

    private static final int EDIT_FIRST_CHANNEL_REQUEST = 2001;
    private static final int EDIT_SECOND_CHANNEL_REQUEST = 2002;

    @Inject
    CompareViewModelFactory compareViewModelFactory;

    @Inject
    Picasso picasso;

    @Inject
    FirebaseService firebaseService;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    @Inject
    ApplicationComponent component;

    private FragmentCompareBinding binding;


    private boolean isHome;
    private String firstId;
    private String secondId;

    private int firstSubCount = 0;
    private int secondSubCount = 0;
    private int difference = 0;

    private HostActivity activity;

    private CompareViewModel compareViewModel;
    private int count;

    private ChannelCardModel firstChannelCardModel;
    private ChannelCardModel secondChannelCardModel;


    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            firstId = arguments.getString(ConstantKey.FIRST_ID);
            secondId = arguments.getString(ConstantKey.SECOND_ID);
            isHome = arguments.getBoolean(ConstantKey.IS_HOME);
        }

        if (firstId != null && secondId != null) {
            compareViewModelFactory.setChannelIds(firstId, secondId);

            firstSubCount = Integer.parseInt(sharedPreferenceService.getCount(firstId));
            secondSubCount = Integer.parseInt(sharedPreferenceService.getCount(secondId));
            difference = Math.abs(firstSubCount - secondSubCount);

        } else {
            throw new IllegalStateException("firstId and secondId should be passed");
        }

        compareViewModel = ViewModelProviders.of(this, compareViewModelFactory).get(CompareViewModel.class);
        firstChannelCardModel = compareViewModel.getFirstChannelCardModel();
        secondChannelCardModel = compareViewModel.getSecondChannelCardModel();

        firstChannelCardModel.setHome(isHome);
        secondChannelCardModel.setHome(isHome);

        getLifecycle().addObserver(compareViewModel);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare, container, false);

        binding.firstCard.getRoot().setOnClickListener(this::onClickFirstChannel);
        binding.secondCard.getRoot().setOnClickListener(this::onClickSecondChannel);

        binding.firstCard.setHandler(() -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivityForResult(intent, RequestCodes.FOR_EDIT_FIRST_CHANNEL);

        });

        binding.secondCard.setHandler(() -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivityForResult(intent, RequestCodes.FOR_EDIT_SECOND_CHANNEL);
        });


        binding.firstCard.setModel(firstChannelCardModel);
        binding.secondCard.setModel(secondChannelCardModel);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (HostActivity) getActivity();
        setUpObservers();
    }

    private void setUpObservers() {
        compareViewModel.getCounterSnippetResponse().observe(this, this::onCounterSnippetResponse);
        compareViewModel.getCounterStatisticsResponse().observe(this, this::onCounterStatisticsResponse);
        compareViewModel.getActionFavoriteResponse().observe(this, this::onActionFavoriteResponse);
        compareViewModel.getTitleResponse().observe(this, this::setTitle);
    }

    private void onCounterSnippetResponse(Response<CompareModel> response) {
        Status status = response.getStatus();
        drawScreenState(status);
        if (status == Status.SUCCESS) {
            CompareModel compareModel = response.getData();
            prepareInfo(compareModel);
        }
    }

    private void onCounterStatisticsResponse(Response<CompareModel> response) {
        Status status = response.getStatus();
        drawScreenState(status);
        if (status == Status.SUCCESS) {
            CompareModel channelModel = response.getData();
            updateStats(channelModel);
            notifyAdTimer();
        }
    }

    private void notifyAdTimer() {

        if (sharedPreferenceService.noAds()) {
            return;
        }

        if (count == 2 && isHome) {
            boolean openedEarlier = sharedPreferenceService.checkRated();
            long launchCount = sharedPreferenceService.getLaunchCount();
            if (launchCount % 5 == 0 && !openedEarlier) {
                showRateUsDialog();
            } else {

                InterstitialAd interstitialAd = component.interstitialAd();
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(component.adRequest());
                    }
                });
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        }
        count++;
    }

    private void showRateUsDialog() {

        Context context = this.getContext();
        if (context == null) {
            return;
        } else {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("⭐⭐⭐⭐⭐")
                    .setMessage("If you enjoy using this app, please take a moment to rate it.")
                    .setPositiveButton("Rate", (dialog, which) -> {
                        dialog.dismiss();
                        activity.openStore();
                        sharedPreferenceService.setRated();
                        firebaseService.onRateRequestAccepted();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                        firebaseService.onRateRequestRejected();
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
        firebaseService.askForRate();

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


    private void prepareInfo(CompareModel compareModel) {

        String firstChannelName = compareModel.getFirstChannelName();
        String secondChannelName = compareModel.getSecondChannelName();

        binding.firstCard.channelName.setText(firstChannelName);
        binding.secondCard.channelName.setText(secondChannelName);

        picasso.load(compareModel.getFirstUrl()).into(binding.firstCard.channelImage);
        picasso.load(compareModel.getSecondUrl()).into(binding.secondCard.channelImage);


    }

    private void updateStats(CompareModel channelModel) {
        String firstSubscriberCount = channelModel.getFirstSubscriberCount();
        String secondSubscriberCount = channelModel.getSecondSubscriberCount();

        updateFirstChannel(firstSubscriberCount);
        updateSecondChannel(secondSubscriberCount);

        updateDifferenceTicker(firstSubscriberCount, secondSubscriberCount);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (isHome) {
            inflater.inflate(R.menu.search, menu);
        } else {
            inflater.inflate(R.menu.favorite, menu);
            compareViewModel.getPrepareMenuResponse().observe(this, this::onPrepareMenuResponse);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            compareViewModel.onClickActionFavorite();
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this.getContext(), SearchActivity.class);
            startActivityForResult(intent, RequestCodes.FOR_SEARCH);
            firebaseService.searchingForChannel();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        if (requestCode == RequestCodes.FOR_SEARCH) {
            String channelId = data.getStringExtra(ConstantKey.CHANNEL_ID);
            Bundle bundle = new Bundle();
            bundle.putString(ConstantKey.CHANNEL_ID, channelId);
            Navigation.findNavController(binding.firstCard.channelName).navigate(R.id.action_homeFragment_to_channelFragment, bundle);
        } else if (requestCode == RequestCodes.FOR_EDIT_FIRST_CHANNEL) {

            String channelId = data.getStringExtra(ConstantKey.CHANNEL_ID);

            if (sharedPreferenceService.getSecondChannelId().equals(channelId)) {
                Toast.makeText(getContext(), "Channel already compared.", Toast.LENGTH_SHORT).show();
                return;
            }

            sharedPreferenceService.setFirstChannelId(channelId);

            firebaseService.subscribeToEditedUsers();

            activity.finish();
            startActivity(new Intent(getContext(), HostActivity.class));

        } else if (requestCode == RequestCodes.FOR_EDIT_SECOND_CHANNEL) {
            String channelId = data.getStringExtra(ConstantKey.CHANNEL_ID);

            if (sharedPreferenceService.getFirstChannelId().equals(channelId)) {
                Toast.makeText(getContext(), "Channel already compared.", Toast.LENGTH_SHORT).show();
                return;
            }

            sharedPreferenceService.setSecondChannelId(channelId);

            firebaseService.subscribeToEditedUsers();

            activity.finish();
            startActivity(new Intent(getContext(), HostActivity.class));

        }

    }

    private void updateFirstChannel(String subscriberCount) {
        int subCount = Integer.parseInt(subscriberCount);
        binding.firstCard.channelTicker.countAnimation(firstSubCount, subCount);
        firstSubCount = subCount;
    }

    private void updateSecondChannel(String subscriberCount) {
        int subCount = Integer.parseInt(subscriberCount);
        binding.secondCard.channelTicker.countAnimation(secondSubCount, subCount);
        secondSubCount = subCount;
    }

    private void updateDifferenceTicker(String firstSubCount, String secondSubsCount) {
        int first = Integer.parseInt(firstSubCount);
        int second = Integer.parseInt(secondSubsCount);

        if (first > second) {
            binding.firstCard.crownImage.setVisibility(View.VISIBLE);
            binding.secondCard.crownImage.setVisibility(View.INVISIBLE);
        } else if (second > first) {
            binding.firstCard.crownImage.setVisibility(View.INVISIBLE);
            binding.secondCard.crownImage.setVisibility(View.VISIBLE);
        } else {
            binding.firstCard.crownImage.setVisibility(View.INVISIBLE);
            binding.secondCard.crownImage.setVisibility(View.INVISIBLE);
        }

        int diff = Math.abs(first - second);
        binding.differenceTicker.countAnimation(difference, diff, true);
        difference = diff;
    }


    private void showProgress() {
        binding.swipeRefreshLayout.setRefreshing(true);
    }

    private void hideProgress() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    private void hideErrorMessage() {
        binding.liveCounterGroup.setVisibility(View.VISIBLE);
        binding.errorMessageTextAndImage.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            //do nothing
        });
    }

    private void showErrorMessage() {
        binding.liveCounterGroup.setVisibility(View.GONE);
        binding.errorIconImageView.setImageResource(R.drawable.ic_error_outline);
        binding.errorMessageText.setText(R.string.error_message);
        binding.errorMessageTextAndImage.setVisibility(View.VISIBLE);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> compareViewModel.loadOrRefresh());
    }


    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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


    private void onClickFirstChannel(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.CHANNEL_ID, firstId);
        if (isHome) {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_channelFragment, bundle);
        } else {
            Navigation.findNavController(v).navigate(R.id.action_compareFragment_to_channelFragment, bundle);
        }
    }

    private void onClickSecondChannel(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.CHANNEL_ID, secondId);
        if (isHome) {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_channelFragment, bundle);
        } else {
            Navigation.findNavController(v).navigate(R.id.action_compareFragment_to_channelFragment, bundle);
        }
    }
}
