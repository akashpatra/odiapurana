package in.co.trapps.odiapurana;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaFragment;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaVideoFragment;
import in.co.trapps.odiapurana.fragments.MusicServiceFragment;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;
import in.co.trapps.odiapurana.utils.MySharedPrefs;

public class MainActivity extends AppCompatActivity {

    private static final int LAXMI_PURANA_COUNT = 3;
    private static final int BACK_PRESS_TIME_INTERVAL = 5000; // # milliseconds, desired time passed between two back presses.
    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MainAct;
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.tabL_laxmi_purana)
    public TabLayout tabLayoutLaxmiPurana;
    @BindView(R.id.vp_laxmi_purana)
    public ViewPager vpLaxmiPurana;
    //    @BindView(R.id.adView_main_banner)
//    public AdView adView;
    @BindView(R.id.rl_ad_view)
    public View rlAdView;
    private AdView adView;
    private long mLastBackPress;
    private Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        setContentView(R.layout.activity_main);

        MySharedPrefs.init(getApplicationContext(), MySharedPrefs.PURANA_PREFS, Activity.MODE_PRIVATE);

        // Bind View
        ButterKnife.bind(this);

        // Initialize the AdMob
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        // Initialize Views
        initializeViews();
    }

    private void initializeViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        vpLaxmiPurana.setAdapter(new LaxmiPuranaAdapter(getSupportFragmentManager()));
        tabLayoutLaxmiPurana.setupWithViewPager(vpLaxmiPurana);

        if (!isAdShowDisabled()) {
            checkAndResetCount();
            loadAdView();
        }
    }

    private boolean isAdShowDisabled() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> isAdShowDisabled");

        long blockedLastTime = MySharedPrefs.with().getLong(MySharedPrefs.AD_BLOCKED_LAST_TIME);
        long diff = System.currentTimeMillis() - blockedLastTime;

        if (diff / (60 * 1000) >= Long.parseLong(Config.AD_BLOCK_TIME)) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> isAdShowDisabled >> Block Time Over");
            return false;
        }

        return true;
    }

    private boolean isAdCountBreached() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> isAdCountBreached");
        int count = MySharedPrefs.with().getInteger(MySharedPrefs.AD_COUNT);
        return count >= Integer.parseInt(Config.AD_COUNT);
    }

    private void checkAndResetCount() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> checkAndResetCount");
        long countInitialTime = MySharedPrefs.with().getLong(MySharedPrefs.AD_COUNT_INITIAL_TIME);
        long diff = System.currentTimeMillis() - countInitialTime;

        if (diff / (60 * 1000) >= Long.parseLong(Config.AD_COUNT_TIME)) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> checkAndResetCount >> Time Crossed");
            MySharedPrefs.with().putInteger(MySharedPrefs.AD_COUNT, 0);
        }
    }

    private void loadAdView() {
        // Set Test Device for Debug Mode.
        AdRequest adRequest;
        if (BuildConfig.DEBUG) {
            adRequest = new AdRequest.Builder()
                    .addTestDevice("D1C88D04D3D8E0ADDA872E4EB3602892") //samsung tab S
//                .addTestDevice("7D43CE2D5C109527E736A2407009450B") //oneplus
                    .build();
        } else {
            adRequest = new AdRequest.Builder().build();
        }

        adView = new AdView(getApplicationContext());
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Logger.logD(Config.TAG, CLASS_NAME, " >> onAdClosed");
                if (isAdCountBreached()) {
                    Logger.logD(Config.TAG, CLASS_NAME, " >> onAdClosed >> Account Breached (DisableView)");
                    disableAdView();
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Logger.logD(Config.TAG, CLASS_NAME, " >> onAdOpened");

                int count = MySharedPrefs.with().getInteger(MySharedPrefs.AD_COUNT);
                if (count == 0) {
                    Logger.logD(Config.TAG, CLASS_NAME, " >> onAdOpened >> Count is 0");
                    MySharedPrefs.with().putInteger(MySharedPrefs.AD_COUNT, ++count);
                    MySharedPrefs.with().putLong(MySharedPrefs.AD_COUNT_INITIAL_TIME, System.currentTimeMillis());
                } else {
                    Logger.logD(Config.TAG, CLASS_NAME, " >> onAdOpened");
                    MySharedPrefs.with().putInteger(MySharedPrefs.AD_COUNT, ++count);
                    checkAndResetCount();
                    if (isAdCountBreached()) {
                        Logger.logD(Config.TAG, CLASS_NAME, " >> onAdOpened >> Account Breached (Setting Start BlockTime)");
                        MySharedPrefs.with().putLong(MySharedPrefs.AD_BLOCKED_LAST_TIME, System.currentTimeMillis());
                    }
                }
            }
        });

        // Set Test Ad Id for Debug Mode.
        if (BuildConfig.DEBUG) {
            adView.setAdUnitId(getString(R.string.test_banner_id));
        } else {
            adView.setAdUnitId(getString(R.string.main_banner_id));
        }
        ((RelativeLayout) rlAdView).addView(adView);
        adView.loadAd(adRequest);
    }

    private void disableAdView() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> disableAdView");

        if (null != adView) {
            adView.destroy();
            adView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onBackPressed");

        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBackPress > BACK_PRESS_TIME_INTERVAL) {
            mSnackBar = Snackbar
                    .make(coordinatorLayout, "Press back again to exit", Snackbar.LENGTH_LONG);
            mSnackBar.show();
            mLastBackPress = currentTime;
        } else {
            if (mSnackBar != null) {
                mSnackBar.dismiss();
            }
            super.onBackPressed();
        }
    }

    private class LaxmiPuranaAdapter extends FragmentPagerAdapter {

        public LaxmiPuranaAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
//                    fragment = new MusicFragment();
                    fragment = new MusicServiceFragment();
                    break;
                case 1:
                    fragment = new LaxmiPuranaFragment();
                    break;
                case 2:
                    fragment = new LaxmiPuranaVideoFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return LAXMI_PURANA_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position) {
                case 0:
                    title = "MUSIC";
                    break;
                case 1:
                    title = "PURANA";
                    break;
                case 2:
                    title = "VIDEO";
                    break;
            }
            return title;
        }
    }
}
