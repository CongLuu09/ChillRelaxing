package com.example.chillrelaxing.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {
    private static final String TAG = "AdManager";

    private RewardedAd rewardedAd;
    private final String adUnitId = "ca-app-pub-3940256099942544/5224354917"; // ⚠️ Test ID - hãy thay bằng ID thật

    public void init(Context context) {
        MobileAds.initialize(context);
        loadAd(context);
    }

    public void loadAd(Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(context, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                Log.d(TAG, "✅ Rewarded Ad loaded.");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                rewardedAd = null;
                Log.e(TAG, "❌ Failed to load rewarded ad: " + adError.getMessage());
            }
        });
    }

    public void showAd(Activity activity, Runnable onAdCompleted) {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    loadAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                    Log.e(TAG, "❌ Failed to show ad: " + adError.getMessage());
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    rewardedAd = null;
                }
            });

            rewardedAd.show(activity, rewardItem -> {
                Log.d(TAG, "🎁 User earned reward: " + rewardItem.getAmount() + " " + rewardItem.getType());
                onAdCompleted.run();
            });

        } else {
            Log.w(TAG, "⚠️ Ad not ready yet.");
        }
    }
}
