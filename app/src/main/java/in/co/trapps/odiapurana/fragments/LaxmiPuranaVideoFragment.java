package in.co.trapps.odiapurana.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.ButterKnife;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * Fragment which plays youtube video of Laxmi Purana.
 */
public class LaxmiPuranaVideoFragment extends Fragment {

    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.LaxmiPuranaVideoFragment;

    // Constant
    private final String PURANA_VIDEO_LINK = "Pzz3vNNFM5I";

    private YouTubePlayerSupportFragment youTubeFragment;

    private YouTubePlayer yPlayer;

    public LaxmiPuranaVideoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_laxmi_purana_video, container, false);
        ButterKnife.bind(this, view);

        youTubeFragment = (YouTubePlayerSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.youtube_fragment);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onViewCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.logD(Config.TAG, CLASS_NAME, " >> setUserVisibleHint >> Flag: " + isVisibleToUser);

        // Check and Load
        checkAndLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onResume");

        // Check and Load
        checkAndLoad();
    }

    private void checkAndLoad() {
        // Load Video when the page is visible
        if (getUserVisibleHint() && isResumed()) {
            loadYoutubeLink();
        }
    }

    private void loadYoutubeLink() {
        youTubeFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    yPlayer = youTubePlayer;
                    // TODO: To implement fullscreen mode
//                    yPlayer.setFullscreen(true);
//                    yPlayer.loadVideo(PURANA_VIDEO_LINK);
                    // Doesn't auto play the video.
                    yPlayer.cueVideo(PURANA_VIDEO_LINK);
                    yPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult errorReason) {
                if (errorReason.isUserRecoverableError()) {
                    errorReason.getErrorDialog(getActivity(), 1).show();
                } else {
                    String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onStart");
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

        // To avoid duplicate Id issue, when we navigate to this fragment again.
        if (null != youTubeFragment) {
            getChildFragmentManager().beginTransaction().remove(youTubeFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onDestroy");
    }
}
