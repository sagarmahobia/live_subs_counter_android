package com.sagar.livesubscounter.activities.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.sagar.livesubscounter.ApplicationComponent;
import com.sagar.livesubscounter.BillingManager;
import com.sagar.livesubscounter.R;
import com.sagar.livesubscounter.constants.ConstantKey;
import com.sagar.livesubscounter.utilityservices.FirebaseService;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.navigateUp;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;
import static androidx.navigation.ui.NavigationUI.setupWithNavController;

/**
 * Created by SAGAR MAHOBIA on 19-Jan-19. at 19:35
 */
public class HostActivity extends AppCompatActivity
        implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    FirebaseService firebaseService;

    @Inject
    ApplicationComponent component;

    @Inject
    InterstitialAd interstitialAd;// Forces dagger to init ad.

    @Inject
    BillingManager billingManager;

    @Inject
    SharedPreferenceService sharedPreferenceService;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.adGroup)
    Group adGroup;

    private ActionBar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        supportActionBar = getSupportActionBar();

        Bundle bundle = new Bundle();
        bundle.putString(ConstantKey.FIRST_ID, sharedPreferenceService.getFirstChannelId());
        bundle.putString(ConstantKey.SECOND_ID, sharedPreferenceService.getSecondChannelId());
        bundle.putBoolean(ConstantKey.IS_HOME, true);

        NavHostFragment finalHost = NavHostFragment.create(R.navigation.mobile_navigation, bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, finalHost)
                .setPrimaryNavigationFragment(finalHost) // this is the equivalent to app:defaultNavHost="true"
                .commitNow();

        setUp(finalHost);
        setUpAds();

        billingManager.setBillingListener(() -> adGroup.setVisibility(View.GONE));
    }

    private void setUp(NavHostFragment finalHost) {


        NavController navController = finalHost.getNavController();
        setupActionBarWithNavController(this, navController, drawer);
        setupWithNavController(navigationView, navController);
    }

    private void setUpAds() {
        if (sharedPreferenceService.noAds()) {
            adGroup.setVisibility(View.GONE);
        } else {
            adView.loadAd(component.adRequest());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    adGroup.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adGroup.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public boolean onSupportNavigateUp() {
        return navigateUp(findNavController(this, R.id.fragment_view), drawer);
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.cross_button_ctr)
    public void onClickClear(View view) {
        billingManager.onClickRemoveAds(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            String shareBody = "Get the live subscriber count of PewDiePie and T-Series" +
                    " right on your android device. " +
                    "https://play.google.com/store/apps/details?" +
                    "id=com.sagar.livesubscounter";

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            this.startActivity(Intent.createChooser(sharingIntent, "Share via"));

            firebaseService.appShared();

        } else if (id == R.id.rate_this_app) {
            openStore();
            firebaseService.openStoreFromDrawer();
        } else if (id == R.id.join_on_discord) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://discord.gg/hB8nrxy")
            );
            startActivity(browserIntent);
        } else if (id == R.id.remove_ads) {

            if (sharedPreferenceService.noAds()) {
                Toast.makeText(this, "Already removed.", Toast.LENGTH_SHORT).show();
            } else {
                billingManager.onClickRemoveAds(this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void openStore() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())
        );
        startActivity(browserIntent);
    }

    public void setTitle(String title) {
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
