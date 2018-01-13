package in.co.trapps.odiapurana;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MainAct;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.tabL_laxmi_purana)
    public TabLayout tabLayoutLaxmiPurana;

    @BindView(R.id.vp_laxmi_purana)
    public ViewPager vpLaxmiPurana;

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
