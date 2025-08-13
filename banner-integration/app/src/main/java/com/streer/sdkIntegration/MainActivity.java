package com.streer.sdkIntegration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.sourcepoint.cmplibrary.model.MessageLanguage;
import com.stroeer.plugins.backfill.IInitializationCallback;
import com.stroeer.plugins.backfill.gravite.GraviteLoader;
import com.stroeer.plugins.monitoring.IAdMonitorCallback;
import com.stroeer.plugins.monitoring.confiant.ConfiantLoader;
import com.yieldlove.adIntegration.AdFormats.YieldloveBannerAd;
import com.yieldlove.adIntegration.AdFormats.YieldloveBannerAdListener;
import com.yieldlove.adIntegration.AdFormats.YieldloveBannerAdView;
import com.yieldlove.adIntegration.AdFormats.YieldloveInterstitialAd;
import com.yieldlove.adIntegration.AdFormats.YieldloveInterstitialAdListener;
import com.yieldlove.adIntegration.AdFormats.YieldloveInterstitialAdView;
import com.yieldlove.adIntegration.Yieldlove;
import com.yieldlove.adIntegration.YieldloveConsent;
import com.yieldlove.adIntegration.exceptions.YieldloveException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    YieldloveConsent consent;
    YieldloveBannerAd bannerAd;
    YieldloveInterstitialAd interstitialAd;

    boolean isLoading = false; // As the reload button is used, we need to track if the ad is already loading to prevent multiple loads with a single instance. if you don't have reload button, you don't need this variable.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Yieldlove SDK
        Yieldlove.setApplicationName(getApplicationContext(),"appDfpTestID5");
        // Enable debug mode for Yieldlove SDK
        // This will provide additional logs for debugging purposes.
        Yieldlove.enableDebugMode();

        boolean useConfiant = false;
        boolean useGravite = false;

        if(useConfiant) {
            // Please inquire to use this Confiant.
            // This is not available for all publishers.
            ConfiantLoader.getInstance().enableTestMode();      // Enable Test Mode, This function blocks all banners and interstitials.
            ConfiantLoader.getInstance().initialize("", true, new IAdMonitorCallback() { // Please inquire to get the accountId and set up the bundle id in Confiant.
                @Override
                public void onInitialized(boolean success) {

                }
            });
        }

        // enable Gravite
        if(useGravite) {
            // Please inquire to use this Gravite.
            // This is not available for all publishers.
            GraviteLoader.getInstance().enableDebugMode(); // Enable debug mode
            // GraviteLoader.getInstance().enableTestMode(null, null, true); // Enable test mode, Please inquire to get accountId and set up the bundle id in Gravite.

            //GraviteLoader.getInstance().enableDirectGraviteCall(); // Enable direct Gravite call. This will bypass Stroeer SDK and call Gravite directly. Please discuss with a Stroeer dealer to use this.
            GraviteLoader.getInstance().setCacheSize(3);           // Set the cache size to 3. This is only for direct Gravite call.

            // Initialize Gravite
            GraviteLoader.getInstance().initialize(getApplication(), new IInitializationCallback() {
                @Override
                public void onInitialized(boolean success) {

                }
            });
        }

        // Create a map to define custom targeting parameters
        Map<String, List<String>> map = new HashMap<>();
        // Add user-specific data to the targeting map
        map.put("userAge", List.of("25")); // Example: User's age
        map.put("userInterest", List.of("sports", "technology", "music")); // Example: User's interests
        map.put("userLocation", List.of("New York")); // Example: User's location
        // Apply the custom targeting globally for Gravite
        Yieldlove.setCustomTargeting(map);

        // Initialize YieldloveConsent
        // If you have your own consent management system, you can skip this step.
        this.consent = new YieldloveConsent(
                this,
                R.id.main_activity_layout);

        this.consent.collect(MessageLanguage.ENGLISH);

        createBanner();
    }

    private void createBanner(){
        final ViewGroup adContainer = findViewById(R.id.generalAdContainer);

        try {
            bannerAd = new YieldloveBannerAd(this);

            bannerAd.load("b1", new YieldloveBannerAdListener() {

                @Override
                public AdManagerAdRequest.Builder onAdRequestBuild() {
                    return null;
                }

                @Override
                public void onAdLoaded(YieldloveBannerAdView banner) {
                    adContainer.removeAllViews();
                    adContainer.addView(banner.getAdView());
                    Toast.makeText(getApplicationContext(), "Ad loaded", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                }

                @Override
                public void onAdFailedToLoad(YieldloveBannerAdView yieldloveBannerAdView, YieldloveException e) {
                    Toast.makeText(getApplicationContext(), "Ad load failed", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                }

                @Override
                public void onAdOpened(YieldloveBannerAdView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad opened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed(YieldloveBannerAdView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad closed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClicked(YieldloveBannerAdView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad clicked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdImpression(YieldloveBannerAdView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad impression", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (YieldloveException e) {
            e.printStackTrace();
        }
    }

    public void btnPrivacyClick(View view) {
        this.consent.showPrivacyManager(MessageLanguage.JAPANESE);
    }

    public void btnReloadClick(View view){
        // You may not need to use this reloading logic because you don't have a reload button.
        // This is just to prevent multiple loads with a single instance.
        if(isLoading == true){
            Toast.makeText(getApplicationContext(), "Ad is already loading", Toast.LENGTH_SHORT).show();
            return;
        }

        isLoading = true;
        // Release the previous ad instance if it exists
        destroyAd();
        createBanner();
    }

    public void btnConsentClick(View view) {
        this.consent.collect(MessageLanguage.ENGLISH);
    }

    public void btnRemoveConsentClick(View view) {
        this.consent.clearConsent();
        Toast.makeText(this, "Consent reset", Toast.LENGTH_SHORT).show();
    }

    public void btnInterstitialClick(View view) {
        try {
            interstitialAd = new YieldloveInterstitialAd(this);
            interstitialAd.load("int", new YieldloveInterstitialAdListener(){  // <-- put here your adslot name

                @Override
                public AdManagerAdRequest.Builder onAdRequestBuild() {
                    return null;
                }

                @Override
                public void onAdLoaded(YieldloveInterstitialAdView interstitial) {
                    interstitial.show();
                }

                @Override
                public void onAdFailedToLoad(YieldloveInterstitialAdView interstitial, YieldloveException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ad load failed", Toast.LENGTH_LONG).show();
                }
            });
        }catch (YieldloveException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        GraviteLoader.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // if you use Gravite, you should call onPause to pause the ads.
        GraviteLoader.getInstance().onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyAd();
    }

    private void destroyAd(){
        if(bannerAd != null) {
            bannerAd.destroy();
        }
    }
}