package in.co.trapps.odiapurana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaFragment;
import in.co.trapps.odiapurana.fragments.LaxmiPuranaVideoFragment;
import in.co.trapps.odiapurana.fragments.MusicFragment;
import in.co.trapps.odiapurana.logger.LoggerEnable;

public class MainActivity extends AppCompatActivity {

    private static final int LAXMI_PURANA_COUNT = 3;
    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MainAct;
    @BindView(R.id.vp_laxmi_purana)
    public ViewPager vpLaxmiPurana;

//    @BindView(R.id.pts_laxmi_purana)
//    public PagerTabStrip ptsLaxmiPurana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Initialize Views
        initializeViews();
    }

    private void initializeViews() {
        vpLaxmiPurana.setAdapter(new LaxmiPuranaAdapter(getSupportFragmentManager()));
//        ptsLaxmiPurana.setTabIndicatorColor(ContextCompat.getColor(this, android.R.color.black));
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
                    fragment = new MusicFragment();
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
