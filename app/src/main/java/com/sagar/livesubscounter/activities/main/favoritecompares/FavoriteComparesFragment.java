package com.sagar.livesubscounter.activities.main.favoritecompares;

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
import com.sagar.livesubscounter.activities.main.favoritecompares.adapter.FavoriteComparesAdapter;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.greendao.entities.FavoriteCompare;
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


public class FavoriteComparesFragment extends Fragment {


    @Inject
    FavoriteComparesViewModelFactory viewModelFactory;

    @Inject
    FavoriteComparesAdapter adapter;

    @Inject
    FirebaseService firebaseService;

    @Inject
    ApplicationComponent component;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    @BindView(R.id.favorite_comparisons_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    TextView messageTextView;

    private FavoriteComparesViewModel favoriteComparesViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteComparesViewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteComparesViewModel.class);
        getLifecycle().addObserver(favoriteComparesViewModel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_favorite_compares, container, false);

        ButterKnife.bind(this, inflate);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener((v, favoriteCompare) -> {
            String firstChannelId = favoriteCompare.getFirstChannelId();
            String secondChannelId = favoriteCompare.getSecondChannelId();
            showCompareFragment(v, firstChannelId, secondChannelId);
            firebaseService.accessedFavCompare(firstChannelId, secondChannelId);
        });

        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        favoriteComparesViewModel.getListLiveData().observe(this, this::onListResponse);

    }

    private void onListResponse(Response<List<FavoriteCompare>> response) {
        if (response.getStatus() == Status.SUCCESS) {
            List<FavoriteCompare> data = response.getData();
            if (data.size() <= 0) {
                showMessage();
            } else {
                showList();
                adapter.setData(data);
            }
        } else if (response.getStatus() == Status.ERROR) {
            showToast(R.id.error_message_text);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void showToast(int id) {
        Toast.makeText(this.getContext(), id, Toast.LENGTH_SHORT).show();
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

    private void showCompareFragment(View v, String firstChannel, String secondChannel) {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.FIRST_ID, firstChannel);
        bundle.putString(ConstantKey.SECOND_ID, secondChannel);

        if (sharedPreferenceService.noAds()) {
            showCompareFragment(v, bundle);
            return;
        }
        InterstitialAd interstitialAd = component.interstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                showCompareFragment(v, bundle);
                interstitialAd.loadAd(component.adRequest());
            }
        });

        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            showCompareFragment(v, bundle);
        }
    }

    private void showCompareFragment(View v, Bundle bundle) {

        Navigation.findNavController(v).navigate(R.id.action_favoriteComparesFragment_to_compareFragment, bundle);

    }
}
