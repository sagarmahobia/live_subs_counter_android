package com.sagar.livesubscounter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.sagar.livesubscounter.utilityservices.SharedPreferenceService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import static com.android.billingclient.api.BillingClient.BillingResponse.ITEM_ALREADY_OWNED;
import static com.android.billingclient.api.BillingClient.BillingResponse.OK;
import static com.android.billingclient.api.BillingClient.BillingResponse.USER_CANCELED;


@ApplicationScope
public class BillingManager implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private Context context;
    private SharedPreferenceService sharedPreferenceService;

    private static final String skuRemoveAds = "remove_ads";

    private boolean failed;
    private boolean ready;

    private SkuDetails skuDetails;

    private BillingListener billingListener;


    @Inject
    BillingManager(Context context, SharedPreferenceService sharedPreferenceService) {
        this.context = context;
        this.sharedPreferenceService = sharedPreferenceService;
        setup();
    }

    private void setup() {

        billingClient = BillingClient.newBuilder(context).setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {

                if (billingResponseCode == OK) {

                    List<String> skuList = new ArrayList<>();
                    skuList.add(skuRemoveAds);
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

                    billingClient.querySkuDetailsAsync(params.build(), (responseCode, skuDetailsList) -> {

                        if (responseCode == OK && skuDetailsList.size() > 0) {
                            skuDetails = skuDetailsList.get(0);
                        }

                    });

                    billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, (responseCode, purchasesList) -> {

                        if (responseCode == BillingClient.BillingResponse.OK && purchasesList != null) {

                            if (purchasesList.size() == 0) {
                                sharedPreferenceService.setHasAds();
                            }

                            for (Purchase purchase : purchasesList) {

                                if (purchase.getSku().equalsIgnoreCase(skuRemoveAds)) {
                                    sharedPreferenceService.setNoAds();
                                    break;
                                }
                            }
                        }
                    });

                } else {
                    failed = true;
                }
                ready = true;
            }

            @Override
            public void onBillingServiceDisconnected() {
                failed = true;
            }
        });

    }

    public void onClickRemoveAds(Activity activity) {
        if (failed) {
            Toast.makeText(context, "Something isn't right. Please restart the app.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ready) {
            Toast.makeText(context, "Please wait.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (billingClient.isReady()) {

            if (skuDetails.getSku().equalsIgnoreCase(skuRemoveAds)) {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build();
                billingClient.launchBillingFlow(activity, flowParams);
            }

        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equalsIgnoreCase(skuRemoveAds)) {
                    sharedPreferenceService.setNoAds();
                    if (billingListener != null) {
                        billingListener.onSuccess();
                    }
                    Toast.makeText(context, "Ads Removed.", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (responseCode == ITEM_ALREADY_OWNED) {
            sharedPreferenceService.setNoAds();
            if (billingListener != null) {
                billingListener.onSuccess();
            }
            Toast.makeText(context, "Ads Removed.", Toast.LENGTH_SHORT).show();

        } else if (responseCode == USER_CANCELED) {

            Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(context, "Something went wrong. code = " + responseCode, Toast.LENGTH_LONG).show();

        }
    }

    public void setBillingListener(BillingListener billingListener) {
        this.billingListener = billingListener;
    }

    public interface BillingListener {

        void onSuccess();

    }
}
