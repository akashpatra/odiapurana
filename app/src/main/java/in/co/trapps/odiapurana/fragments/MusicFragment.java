package in.co.trapps.odiapurana.fragments;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements View.OnTouchListener, MediaPlayer.OnBufferingUpdateListener,
        SeekBar.OnSeekBarChangeListener {

    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MusicFragment;

    // Music Url
    private final String MUSIC_URL = "http://www.kunuweb.in/siteuploads/files/sfd19/9263" +
            "/Laxmi%20Purana%20-%20Namita%20Agrawal%20-%20Geeta%20Dash%20-%20Full%20Song(kunuweb.In).mp3";
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

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        progressDialog = new ProgressDialog(getActivity());
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
        musicSeekBar.setOnTouchListener(this);
    }

    @OnClick(R.id.audio_button)
    public void audioButtonClick(View view) {
        if (!isPlaying) {
            // Start Playing
            audioButton.setText("Pause Streaming");

            if (initialStage) {
                try {
                    mediaPlayer.setDataSource(MUSIC_URL);
                    mediaPlayer.prepareAsync();

                    // Show Progress Dialog
                    progressDialog.setMessage("Buffering...");
                    progressDialog.show();

                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();

                            // Hide Progress Dialog
                            if (progressDialog.isShowing()) {
                                progressDialog.cancel();
                            }

                            mediaPlayer.start();
                            initialStage = false;

                            primarySeekBarProgressUpdate();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    pauseMusic();
                }
            } else {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }

                primarySeekBarProgressUpdate();
            }

            isPlaying = true;
        } else {
            pauseMusic();
        }
    }

    private void pauseMusic() {
        // Pause Playing
        audioButton.setText("Launch Streaming");

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        isPlaying = false;
    }

    private void primarySeekBarProgressUpdate() {
        handler.postDelayed(updateProgressNotification, 100);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (R.id.seekBar == view.getId()) {
            if (mediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) view;
                int playPositionInMilliSeconds = (mediaPlayer.getDuration() / 100)
                        * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMilliSeconds);
            }
        }
        return false;
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

        initResetMediaPlayer(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onPause");

        // Remove message Handler from updating progress bar
        handler.removeCallbacks(updateProgressNotification);
        // Pause Music
        pauseMusic();
        // Release media player
        initResetMediaPlayer(false);
    }

    private void initResetMediaPlayer(boolean initializeMediaPlayer) {
        if (initializeMediaPlayer) {
            if (null == mediaPlayer) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
        } else {
            initialStage = true;

            if (null != mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
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
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        handler.removeCallbacks(updateProgressNotification);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(updateProgressNotification);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        primarySeekBarProgressUpdate();
    }
}
