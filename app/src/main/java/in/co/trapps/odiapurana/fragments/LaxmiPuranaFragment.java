package in.co.trapps.odiapurana.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaxmiPuranaFragment extends Fragment {

    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.LaxmiPuranaFragment;

    @BindView(R.id.tv_lyrics)
    public TextView tvLyrics;

    public LaxmiPuranaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_laxmi_purana, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onViewCreated");

        // Custom Font for Lyrics
        Typeface droidFont = Typeface.createFromAsset(getActivity().getAssets(), Config.FONT_REGIONAL);
        tvLyrics.setTypeface(droidFont);
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onDestroy");
    }
}
