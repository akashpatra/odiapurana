package in.co.trapps.odiapurana.fragments;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MusicFragment;

    private final String MUSIC_URL = "http://www.kunuweb.in/siteuploads/files/sfd19/9263" +
            "/Laxmi%20Purana%20-%20Namita%20Agrawal%20-%20Geeta%20Dash%20-%20Full%20Song(kunuweb.In).mp3";
    @BindView(R.id.audio_button)
    public Button audioButton;
    public ProgressDialog progressDialog;
    private MediaPlayer mediaPlayer;
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

        // Set Listeners
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    // Start Playing
                    audioButton.setText("Pause Streaming");

                    if (initialStage) {
//                        new Player().execute(MUSIC_URL);
                        try {
                            mediaPlayer.setDataSource(MUSIC_URL);
                            mediaPlayer.prepareAsync();
                            progressDialog.setMessage("Buffering...");
                            progressDialog.show();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    if (progressDialog.isShowing()) {
                                        progressDialog.cancel();
                                    }

                                    mediaPlayer.start();
                                    initialStage = false;
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    }

                    isPlaying = true;
                } else {
                    pauseMusic();
                }
            }
        });
    }

    private void pauseMusic() {
        // Pause Playing
        audioButton.setText("Launch Streaming");

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        isPlaying = false;
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

        pauseMusic();
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

    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        isPlaying = false;
                        audioButton.setText("Launch Streaming");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                prepared = false;
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }
    }
}
