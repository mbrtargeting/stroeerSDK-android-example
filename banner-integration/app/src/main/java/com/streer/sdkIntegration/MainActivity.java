package com.streer.sdkIntegration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.stroeer.ads.StroeerSDK;
import com.stroeer.ads.exceptions.StroeerException;
import com.stroeer.ads.formats.banner.StroeerBannerListener;
import com.stroeer.ads.formats.banner.StroeerBannerView;
import com.stroeer.ads.formats.interstitial.StroeerInterstitialListener;
import com.stroeer.ads.formats.interstitial.StroeerInterstitialView;
import com.stroeer.cmp.StroeerConsent;
import com.stroeer.plugins.backfill.IInitializationCallback;
import com.stroeer.plugins.backfill.gravite.GraviteLoader;
import com.stroeer.plugins.monitoring.IAdMonitorCallback;
import com.stroeer.plugins.monitoring.confiant.ConfiantLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    StroeerConsent consent;
    StroeerBannerView bannerAd;
    StroeerInterstitialView interstitialAd;

    boolean isLoading = false; // As the reload button is used, we need to track if the ad is already loading to prevent multiple loads with a single instance. if you don't have reload button, you don't need this variable.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Yieldlove SDK
        StroeerSDK.setApplicationName(getApplicationContext(),"appDfpTest");
        // Enable debug mode for Yieldlove SDK
        // This will provide additional logs for debugging purposes.
        StroeerSDK.enableDebugMode();

        // Enable Debug Panel
        StroeerSDK.enableInspectionMode();

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
        StroeerSDK.setCustomTargeting(map);

        // Initialize YieldloveConsent
        // If you have your own consent management system, you can skip this step.
        this.consent = new StroeerConsent(
                this,
                R.id.main_activity_layout);

        this.consent.collect();

        createBanner();
    }

    private void createBanner(){
        final ViewGroup adContainer = findViewById(R.id.generalAdContainer);

        try {
            bannerAd = new StroeerBannerView(this);

            //banner, banner2, banner3 can be used in the publisherSlotName
            bannerAd.load("banner2", new StroeerBannerListener() {
                @Override
                public void onAdLoaded(StroeerBannerView banner) {
                    Toast.makeText(getApplicationContext(), "Ad loaded", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                }

                @Override
                public void onAdFailedToLoad(StroeerBannerView yieldloveBannerAdView, StroeerException e) {
                    Toast.makeText(getApplicationContext(), "Ad load failed", Toast.LENGTH_SHORT).show();
                    isLoading = false;
                }

                @Override
                public void onAdOpened(StroeerBannerView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad opened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed(StroeerBannerView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad closed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClicked(StroeerBannerView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad clicked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdImpression(StroeerBannerView yieldloveBannerAdView) {
                    Toast.makeText(getApplicationContext(), "Ad impression", Toast.LENGTH_SHORT).show();
                }
            });

            // this should be added
            adContainer.addView(bannerAd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnPrivacyClick(View view) {
        this.consent.showPrivacyManager();
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
        this.consent.collect();
    }

    public void btnRemoveConsentClick(View view) {
        this.consent.clearConsent();
        Toast.makeText(this, "Consent reset", Toast.LENGTH_SHORT).show();
    }

    public void btnInterstitialClick(View view) {
        try {
            interstitialAd = new StroeerInterstitialView(this);
            interstitialAd.load("interstitial", new StroeerInterstitialListener(){  // <-- put here your adslot name
                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdFailedToLoad(StroeerException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ad load failed", Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e) {
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