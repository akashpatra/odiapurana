package in.co.trapps.odiapurana.fragments;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;
import in.co.trapps.odiapurana.service.MusicService;

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

    // Set states
//    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    @BindView(R.id.audio_button)
    public Button audioButton;
    @BindView(R.id.seekBar)
    public SeekBar musicSeekBar;
    public ProgressDialog progressDialog;
    private MediaPlayer mediaPlayer;
    Runnable updateProgressNotification = new Runnable() {
        @Override
        public void run() {
            int progress = getProgressPercentage(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
            musicSeekBar.setProgress(progress);

            handler.postDelayed(this, 100);
        }
    };
    private boolean isPlaying;
    private boolean initialStage = true;
    private MusicService mInstance;

    public MusicServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        progressDialog = new ProgressDialog(getActivity());

        // Will work, if coming from Notification
        if (isServiceRunning(MusicService.class)) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate >> Service is running.");
            mInstance = MusicService.getInstance();
        }

        MusicService.setSong(MUSIC_URL, "Odiaaa");
        Intent musicIntent = new Intent(getActivity(), MusicService.class);
        musicIntent.setAction(MusicService.ACTION_PLAY);
        getActivity().startService(musicIntent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

        setDefaultViews();
    }

    @OnClick(R.id.audio_button)
    public void audioButtonClick(View view) {
        Logger.logD(Config.TAG, CLASS_NAME, " >> audioButtonClick");

        if (mInstance == null) {
            mInstance = MusicService.getInstance();
        }

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
            // Change the views of Buttons
            if (mInstance.isPlaying()) {
                // Pause Music
                audioButton.setText("Pause Music");
            } else {
                // Play Music
                audioButton.setText("Play Music");
            }

            // Change the views of SeekBar

        }
    }

    private void primarySeekBarProgressUpdate() {

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


    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onPause");

    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     *
     * @param progress      -
     * @param totalDuration returns current duration in milliseconds
     */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
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

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
