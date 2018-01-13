package in.co.trapps.odiapurana.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;
import in.co.trapps.odiapurana.service.MusicService;
import in.co.trapps.odiapurana.utils.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicServiceFragment extends Fragment implements MediaPlayer.OnBufferingUpdateListener,
        SeekBar.OnSeekBarChangeListener {

    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MusicServiceFragment;

    // Music Url
//    private final String MUSIC_URL = "http://www.kunuweb.in/siteuploads/files/sfd19/9263" +
//            "/Laxmi%20Purana%20-%20Namita%20Agrawal%20-%20Geeta%20Dash%20-%20Full%20Song(kunuweb.In).mp3";

    private final String MUSIC_URL = "http://media.djmazadownload.com/music/indian_movies/Tumhari" +
            "%20Sulu%20%282017%29/01%20-%20Tumhari%20Sulu%20-%20Ban%20Ja%20Rani%20%5BDJMaza.Info%5D.mp3";
    private final Handler mHandler = new Handler();
    @BindView(R.id.audio_button)
    public Button audioButton;
    @BindView(R.id.seekBar)
    public SeekBar musicSeekBar;
    @BindView(R.id.tv_duration)
    public TextView tvDuration;
    public ProgressDialog progressDialog;
    // Set states
    private MusicService mInstance;
    Runnable seekBarRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.logD(Config.TAG, CLASS_NAME, " >> run");

            if (mInstance == null) {
                mInstance = MusicService.getInstance();
            }

            //
            long currentDuration = mInstance.getCurrentDuration();
            long totalDuration = mInstance.getTotalDuration();
            String duration = CommonUtils.milliSecondsToTimer(currentDuration) + " / "
                    + CommonUtils.milliSecondsToTimer(totalDuration);
            tvDuration.setText(duration);

            int progress = CommonUtils.getProgressPercentage(currentDuration, totalDuration);
            musicSeekBar.setProgress(progress);
            musicSeekBar.setSecondaryProgress(mInstance.getBufferingPosition());

            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        progressDialog = new ProgressDialog(getActivity());

        MusicService.setSong(MUSIC_URL, "Odiaaa");
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        musicIntent.setAction(MusicService.ACTION_PLAY);
        getActivity().startService(musicIntent);

        // Start task to update SeekBar Progress
        mHandler.postDelayed(seekBarRunnable, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreateView");

        View view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onViewCreated");

        // Initialize Views
        musicSeekBar.setMax(99);
        musicSeekBar.setOnSeekBarChangeListener(this);
    }

    @OnClick(R.id.audio_button)
    public void audioButtonClick(View view) {
        Logger.logD(Config.TAG, CLASS_NAME, " >> audioButtonClick");

        if (null != mInstance && mInstance.isPlayerReady()) {
            if (mInstance.isPlaying()) {
                // Pause Music
                mInstance.pauseMusic();
            } else {
                // Play Music
                mInstance.startMusic();
            }
        } else {
            Logger.logD(Config.TAG, CLASS_NAME, " >> Player Not Ready");
        }
        setDefaultViews();
    }

    private void setDefaultViews() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> setDefaultViews");

        if (null != mInstance) {
            // Change the views of Buttons & SeekBar
            if (mInstance.isPlaying()) {
                // Pause Music
                audioButton.setText("Pause Music");
                // Start task to update SeekBar Progress
                mHandler.postDelayed(seekBarRunnable, 100);
            } else {
                // Play Music
                audioButton.setText("Play Music");
                // Stop task to update SeekBar Progress
                mHandler.removeCallbacks(seekBarRunnable);
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        musicSeekBar.setSecondaryProgress(i);
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

        setDefaultViews();

        if (null != mInstance && mInstance.isPlayerReady()) {
            mInstance.updateNotification(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onPause");

        if (null != mInstance && mInstance.isPlayerReady()) {
            mInstance.updateNotification(false);
        }
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

        if (null != mInstance && mInstance.isPlaying()) {
            mInstance.pauseMusic();
            mInstance.updateNotification(true);
            setDefaultViews();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onDestroy");

        if (null != mInstance) {
            Intent musicIntent = new Intent(getActivity(), MusicService.class);
            musicIntent.setAction(MusicService.ACTION_STOP);
            getActivity().startService(musicIntent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Pause Music
        mInstance.pauseMusic();
        setDefaultViews();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Calculate the progress
        int position = CommonUtils.progressToTimer(seekBar.getProgress(), mInstance.getTotalDuration());
        // Set the position in media player
        mInstance.seekTo(position);
        // Play Music
        mInstance.startMusic();
        setDefaultViews();
    }
}
