package com.sagar.livesubscounter.activities.main.favoritechannels;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.sagar.livesubscounter.ApplicationComponent;
import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.activities.main.favoritechannels.adapter.FavoriteChannelsAdapter;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.greendao.entities.FavoriteChannel;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;
import com.sagar.livesubscounter.viewmodel.Response;
import com.sagar.livesubscounter.viewmodel.Status;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteChannelsFragment extends Fragment {

    @Inject
    FavoriteChannelsViewModelFactory viewModelFactory;

    @Inject
    FavoriteChannelsAdapter adapter;

    @Inject
    FirebaseService firebaseService;

    @Inject
    ApplicationComponent component;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    @BindView(R.id.favorite_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    TextView messageTextView;

    private FavoriteChannelsViewModel favoriteChannelsViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteChannelsViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteChannelsViewModel.class);
        getLifecycle().addObserver(favoriteChannelsViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_favorite_channels, container, false);
        ButterKnife.bind(this, inflate);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener((v, favoriteChannel) -> {
            String channelId = favoriteChannel.getChannelId();
            showChannelFragment(v, channelId);
            firebaseService.accessedFavChannel(channelId);

        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoriteChannelsViewModel.getListLiveData().observe(this, this::onListResponse);
    }


    private void onListResponse(Response<List<FavoriteChannel>> response) {
        if (response.getStatus() == Status.SUCCESS) {
            List<FavoriteChannel> data = response.getData();
            if (data.size() <= 0) {
                showMessage();
            } else {
                showList();
                adapter.setData(data);
            }
        } else if (response.getStatus() == Status.ERROR) {
            showToast("Something went wrong");
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void showToast(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    private void showList() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        recyclerView.setAnimation(alphaAnimation);
        recyclerView.setVisibility(View.VISIBLE);
        messageTextView.setVisibility(View.GONE);
    }

    private void showMessage() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(500);
        messageTextView.setAnimation(alphaAnimation);
        messageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showChannelFragment(View v, String channelId) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.CHANNEL_ID, channelId);

        if (sharedPreferenceService.noAds()) {
            showChannelFragment(v, bundle);
            return;
        }

        InterstitialAd interstitialAd = component.interstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                showChannelFragment(v, bundle);
                interstitialAd.loadAd(component.adRequest());
            }
        });

        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            showChannelFragment(v, bundle);
        }
    }

    private void showChannelFragment(View v, Bundle bundle) {
        Navigation.findNavController(v).navigate(R.id.action_favoriteChannelsFragment_to_channelFragment, bundle);

    }
}
