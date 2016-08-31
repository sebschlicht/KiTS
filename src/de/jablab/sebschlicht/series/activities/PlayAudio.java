package de.jablab.sebschlicht.series.activities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.jablab.sebschlicht.android.kits.commands.IntroType;
import de.jablab.sebschlicht.android.kits.commands.PlayCommand;
import de.jablab.sebschlicht.android.kits.commands.StopCommand;
import de.jablab.sebschlicht.android.ui.ProgressBarTextView;
import de.jablab.sebschlicht.series.R;
import de.jablab.sebschlicht.series.activities.preferences.PlaybackDevice;
import de.jablab.sebschlicht.series.resources.ResourceProvider;
import de.jablab.sebschlicht.series.tasks.SendCommandTask;

public class PlayAudio extends KitsSeriesActivity {

    /**
     * UI update interval in milliseconds
     */
    private static final int UPDATE_INTERVAL_UI = 200;

    /**
     * UI thread scheduler
     */
    private Handler handler = new Handler();

    /**
     * audio player
     */
    private MediaPlayer player;

    /**
     * playback border time labels
     */
    private TextView tvStartTime, tvEndTime;

    /**
     * playback progress bar
     */
    private ProgressBar sbProgress;

    /**
     * playback control button (start/stop)
     */
    private Button btnPlayStop;

    /**
     * playback counter
     */
    private int numPlayed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sbProgress.setEnabled(false);
        if (this.isStartup) {
            this.reset();
        }

        // create audio player
        this.player = new MediaPlayer();
        this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                PlayAudio.this.setPlaying(false);
            }
        });

        // prepare playback
        this.updateLocalPlayerVolume();
        this.initializePlayer();

        // update playback UI
        long endTime = this.player.getDuration();
        this.updateLabel(this.tvEndTime, endTime);
        this.sbProgress.setMax((int) endTime);

        // start audio playback
        this.setPlaying(true);
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_play_audio);
    }

    @Override
    protected void loadPreferences() {
        super.loadPreferences();

        // audio playback device (default: auto)
        final PlaybackDevice defaultPlaybackDevice = PlaybackDevice.AUTO;
        final String defaultIdentifier = defaultPlaybackDevice.getIdentifier();
        final String identifier =
                this.preferences.getString(PlaybackDevice.KEY_AUDIO, defaultIdentifier);
        this.playbackDevice = PlaybackDevice.parseString(identifier);
    }

    @Override
    protected void connectToUi() {
        super.connectToUi();

        // playback control button
        this.btnPlayStop = (Button) this.findViewById(R.id.btnPlayStop);
        this.btnPlayStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                PlayAudio.this.togglePlaying();
            }
        });

        // reveal series
        final Button btnReveal = (Button) this.findViewById(R.id.btnReveal);
        btnReveal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                PlayAudio.this.revealSeries();
            }
        });

        // playback progress bar
        this.sbProgress = (ProgressBar) this.findViewById(R.id.sbProgress);

        // playback border time labels
        this.tvStartTime = (TextView) this.findViewById(R.id.tvStartTime);
        this.tvEndTime = (TextView) this.findViewById(R.id.tvEndTime);
        // connect time labels with progress bar
        final ProgressBarTextView pts = (ProgressBarTextView) this.tvStartTime;
        pts.setProgressBar(this.sbProgress);
        final ProgressBarTextView pte = (ProgressBarTextView) this.tvEndTime;
        pte.setProgressBar(this.sbProgress);
    }

    protected void reset() {
        final Button btnRepeat = (Button) this.findViewById(R.id.btnPlayStop);
        btnRepeat.setEnabled(true);
        Log.d("PlayAudio UI", "UI reset requested.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.updateLocalPlayerVolume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.player.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.play_series_menu, menu);
        return true;
    }

    private void updateLocalPlayerVolume() {
        // TODO remove but update on preference change
        this.loadPreferences();

        // disable local playback volume by default
        float playerVolume = 0;
        if (!SESSION.isConnected() || this.playbackDevice == PlaybackDevice.DEVICE) {
            // enable local playback volume
            playerVolume = 1f;
        }

        if (this.player != null) {
            this.player.setVolume(playerVolume, playerVolume);
        }
    }

    private void initializePlayer() {
        final ResourceProvider resourceProvider = SESSION.getResourceProvider();
        if (!resourceProvider.hasAudioFile(this.series)) {
            Log.d("PlayAudio | initPlayer",
                    "audio file for series "
                            + this.series.getFileName()
                            + " is missing at "
                            + resourceProvider.getAudioFile(this.series)
                            .getAbsolutePath());
            return;
        }

        FileInputStream fi = null;
        try {
            final Uri audioFileUri = resourceProvider.getAudioFileUri(this.series);
            this.player.setDataSource(this.getApplicationContext(), audioFileUri);

            //            final File audioFile = resourceProvider.getAudioFile(this._series);
            //            fi = new FileInputStream(audioFile);
            //            this.player.setDataSource(fi.getFD());

            this.player.prepare();
        } catch (Exception e) {
            Log.e("playback error",
                    "failed to load series audio file \"" + this.series.getFileName()
                    + "\"");
        } finally {
            if (fi != null) {
                try {
                    fi.close();
                } catch (IOException e) {
                    Log.e("playback cleanup exception",
                            "failed to close the audio file!", e);
                }
            }
        }
    }

    private void togglePlaying() {
        this.setPlaying(!this.isPlaying());
    }

    @Override
    protected void startPlaying() {
        // start playback on local device (may be muted)
        this.player.start();
        if (SESSION.isConnected() && this.playbackDevice != PlaybackDevice.DEVICE) {
            // start playback on server
            Log.d("Server", "volume: " + this.getPlaybackVolume());
            new SendCommandTask(SESSION.getServerAddress(), KITS_SERVER_PORT)
            .execute(new PlayCommand(this.series.getName(), IntroType.SHORT));
        }
        // this.btnPlayStop.setText("anhalten");
        this.handler.postDelayed(this.UpdatePlayerUI, UPDATE_INTERVAL_UI);
        this.numPlayed += 1;
        // TODO stop remote playback if switching to local playback in
        // preferences
    }

    @Override
    protected void stopPlaying() {
        // stop playback on local device
        this.player.stop();
        this.player.reset();
        if (SESSION.isConnected() && this.playbackDevice != PlaybackDevice.DEVICE) {
            // stop playback on server
            new SendCommandTask(SESSION.getServerAddress(), KITS_SERVER_PORT)
            .execute(new StopCommand());
        }
        // this.btnPlayStop.setText("Nochmal hören");
        if (this.numPlayed < 3) {
            // prepare player for next repeat
            this.initializePlayer();
        } else {
            // disallow further repeating
            this.btnPlayStop.setEnabled(false);
        }
    }

    private void updateLabel(TextView tvTimeLabel, long position) {
        tvTimeLabel.setText(String.format("%d Sek.",
                TimeUnit.MILLISECONDS.toSeconds(position)));
    }

    private void revealSeries() {
        Intent iRevealSeries = new Intent(this, RevealSeries.class);
        Bundle params = new Bundle();
        params.putSerializable(RevealSeries.PARAM_SERIES, this.series);
        iRevealSeries.putExtras(params);
        this.startActivity(iRevealSeries);
    }

    private Runnable UpdatePlayerUI = new Runnable() {

        @Override
        public void run() {
            if (PlayAudio.this.player.isPlaying()) {
                long currentTime = PlayAudio.this.player.getCurrentPosition();
                PlayAudio.this.updateLabel(PlayAudio.this.tvStartTime, currentTime);
                PlayAudio.this.tvEndTime.postInvalidate();
                PlayAudio.this.sbProgress.setProgress((int) currentTime);
                PlayAudio.this.handler.postDelayed(this, UPDATE_INTERVAL_UI);
            } else {
                int endTime = PlayAudio.this.sbProgress.getMax();
                PlayAudio.this.updateLabel(PlayAudio.this.tvStartTime, endTime);
                PlayAudio.this.tvEndTime.postInvalidate();
                PlayAudio.this.sbProgress.setProgress(endTime);
            }
        }
    };
}
