package in.co.trapps.odiapurana.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;

import in.co.trapps.odiapurana.MainActivity;
import in.co.trapps.odiapurana.R;
import in.co.trapps.odiapurana.constants.Config;
import in.co.trapps.odiapurana.logger.Logger;
import in.co.trapps.odiapurana.logger.LoggerEnable;

/**
 * @author Akash Patra
 */
public class MusicService extends Service implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_STOP = "action_stop";
    private static String mSongUrl;
    private static String mSongTitle;
    private static MusicService mInstance = null;
    // For Logging
    private final LoggerEnable CLASS_NAME = LoggerEnable.MusicService;
    private final int NOTIFICATION_ID = 1;
    private MediaPlayer mMediaPlayer = null;
    private State mState = State.Initial;
    // Regarding Notification
    private NotificationManager mNotificationManager;
    private Notification mNotification = null;

    public static MusicService getInstance() {
        return mInstance;
    }

    public static void setSong(String songUrl, String songTitle) {
        mSongUrl = songUrl;
        mSongTitle = songTitle;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onCreate");

        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onStartCommand");

        if (ACTION_PLAY.equalsIgnoreCase(intent.getAction())) {
            if (null == mMediaPlayer) {
                Logger.logD(Config.TAG, CLASS_NAME, " >> ACTION_PLAY");

                mMediaPlayer = new MediaPlayer(); // initialize it here
                mMediaPlayer.setOnPreparedListener(this);
//            mMediaPlayer.setOnErrorListener(this);
                mMediaPlayer.setOnBufferingUpdateListener(this);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                initMediaPlayer();
            } else {
                startMusic();
            }
        } else if (ACTION_PAUSE.equalsIgnoreCase(intent.getAction())) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> ACTION_PAUSE");
            pauseMusic();
        } else if (ACTION_STOP.equalsIgnoreCase(intent.getAction())) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> ACTION_STOP");

            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void initMediaPlayer() {
        try {
            mMediaPlayer.setDataSource(mSongUrl);
        } catch (IllegalArgumentException e) {
            // ...
        } catch (IllegalStateException e) {
            // ...
        } catch (IOException e) {
            // ...
        }

        try {
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IllegalStateException e) {
            // ...
        }
        mState = State.Preparing;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
//        mediaPlayer.start();
        Logger.logD(Config.TAG, CLASS_NAME, " >> onPrepared");

        mState = State.Paused;
    }

    @Override
    public void onDestroy() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> onDestroy");

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mState = State.Initial;
    }

    public void startMusic() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> startMusic");

        if (isPlayerReady() && !isPlaying()) {
            Logger.logD(Config.TAG, CLASS_NAME, " >> startingMusic");

            mMediaPlayer.start();
            mState = State.Playing;
            // Update Notification
            updateNotification(true);
        }
    }

    public void pauseMusic() {
        Logger.logD(Config.TAG, CLASS_NAME, " >> pauseMusic");

        // Pause Playing
        if (mState.equals(State.Playing)) {
            mMediaPlayer.pause();
            mState = State.Paused;
            // Update Notification
            updateNotification(false);
        }
    }

    public boolean isPlayerReady() {
        if (!mState.equals(State.Preparing) && !mState.equals(State.Initial)) {
            return true;
        }
        return false;
    }

    public boolean isPlaying() {
        if (mState.equals(State.Playing)) {
            return true;
        }
        return false;
    }

    private void updateNotification(boolean isPlaying) {
        setUpAsForeground(isPlaying);
    }

    private void setUpAsForeground(boolean isPlaying) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        if (isPlaying) {
            Intent pauseIntent = new Intent(this, MusicService.class);
            pauseIntent.setAction(ACTION_PAUSE);
            PendingIntent pPauseIntent = PendingIntent.getService(this, 0,
                    pauseIntent, 0);

            mNotification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(mSongTitle + " is playing")
                    .setTicker("Odia Purana")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pi)
                    .addAction(android.R.drawable.ic_media_pause, "Pause",
                            pPauseIntent)
                    .build();
        } else {
            Intent playIntent = new Intent(this, MusicService.class);
            playIntent.setAction(ACTION_PLAY);
            PendingIntent pPlayIntent = PendingIntent.getService(this, 0,
                    playIntent, 0);

            mNotification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(mSongTitle + " is paused")
                    .setTicker("Odia Purana")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pi)
                    .addAction(android.R.drawable.ic_media_play, "Play",
                            pPlayIntent)
                    .build();
        }
        startForeground(NOTIFICATION_ID, mNotification);
    }

    // indicates the state our service:
    enum State {
        Initial, // initial state
        Preparing, // media player is preparing...
        Playing, // playback active (media player ready!). (but the media player may actually be
        // paused in this state if we don't have audio focus. But we stay in this state
        // so that we know we have to resume playback once we get focus back)
        Paused, // playback paused (media player ready!)
        Stopped // media player is stopped and not prepared to play
    }
}
