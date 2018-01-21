package in.co.trapps.odiapurana;

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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaFragment;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaVideoFragment;
import in.co.trapps.odiapurana.fragments.MusicServiceFragment;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

public class MainActivity extends AppCompatActivity {

    private static final int LAXMI_PURANA_COUNT = 3;
    private static final int TIME_INTERVAL = 5000; // # milliseconds, desired time passed between two back presses.
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
    private long mLastBackPress;
    private Toast mBackPressToast;
    private Snackbar mSnackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialize Views
        initializeViews();
    }

    private void initializeViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        vpLaxmiPurana.setAdapter(new LaxmiPuranaAdapter(getSupportFragmentManager()));
        tabLayoutLaxmiPurana.setupWithViewPager(vpLaxmiPurana);
    }

    @Override
    public void onBackPressed() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onBackPressed");

        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBackPress > TIME_INTERVAL) {
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
