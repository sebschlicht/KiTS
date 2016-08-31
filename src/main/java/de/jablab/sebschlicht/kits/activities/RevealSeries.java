package de.jablab.sebschlicht.kits.activities;

import java.util.Timer;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.jablab.sebschlicht.android.kits.commands.IntroType;
import de.jablab.sebschlicht.android.kits.commands.PlayCommand;
import de.jablab.sebschlicht.android.kits.commands.StopCommand;
import de.jablab.sebschlicht.kits.R;
import de.jablab.sebschlicht.kits.activities.preferences.PlaybackDevice;
import de.jablab.sebschlicht.kits.resources.ResourceProvider;
import de.jablab.sebschlicht.kits.tasks.SendCommandTask;

/**
 * Activity to reveal the series.<br>
 * The activity serves as controller for remote playback or allows to start
 * local playback in a separate activity.
 *
 * @author sebschlicht
 *
 */
public class RevealSeries extends KitsSeriesActivity {

    protected Timer _timer = new Timer();

    protected View _vActions;

    protected ImageView _ivFigure;

    @Override
    protected void loadPreferences() {
        super.loadPreferences();
        // video playback device (default: auto)
        String videoPlaybackDeviceIdentifier =
                this.preferences.getString(PlaybackDevice.KEY_VIDEO,
                        PlaybackDevice.AUTO.getIdentifier());
        this.playbackDevice = PlaybackDevice.parseString(videoPlaybackDeviceIdentifier);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to UI
        this._vActions = this.findViewById(R.id.rlActions);
        this._ivFigure = (ImageView) this.findViewById(R.id.ivFigure);
        TextView tvSolution = (TextView) this.findViewById(R.id.tvSolution);
        Button btnContinue = (Button) this.findViewById(R.id.btnContinue);

        Typeface titleFont =
                Typeface.createFromAsset(this.getAssets(), "fonts/surfer.ttf");
        tvSolution.setTypeface(titleFont);
        tvSolution.setTextSize(28);
        tvSolution.setText(this.series.getName());

        Button btnPlayVideo = (Button) this.findViewById(R.id.btnPlayVideo);
        btnPlayVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RevealSeries.this.playVideo();
            }
        });
        //TimerTask taskPlayVideo = new TimerTaskPlayVideo(this);
        // _timer.schedule(taskPlayVideo, 5 * 1000);

        btnContinue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RevealSeries.this.continueGame();
            }
        });

        this._ivFigure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RevealSeries.this.switchToVideo();
            }
        });

        // show series image
        final ResourceProvider resourceProvider = SESSION.getResourceProvider();
        if (!resourceProvider.hasImageFile(this.series)) {
            return;
        }
        final Uri imageFileUri = resourceProvider.getImageFileUri(this.series);
        this._ivFigure.setImageURI(imageFileUri);
        //        final File imageFile = resourceProvider.getImageFile(this._series);
        //        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        //        this._ivFigure.setImageBitmap(bitmap);
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_reveal_series);
    }

    public void switchToVideo() {
        this._timer.cancel();
        AlphaAnimation anmFadeOutFigure = new AlphaAnimation(1f, 0f);
        anmFadeOutFigure.setDuration(500);
        anmFadeOutFigure.setFillAfter(true);
        this._ivFigure.startAnimation(anmFadeOutFigure);
        this._vActions.setVisibility(View.VISIBLE);
        AlphaAnimation anmFadeActions = new AlphaAnimation(0f, 1f);
        anmFadeActions.setDuration(500);
        anmFadeActions.setFillAfter(true);
        this._vActions.startAnimation(anmFadeActions);

        // video controller UI
        if (SESSION.isConnected() && this.playbackDevice != PlaybackDevice.DEVICE) {
            // play video on server
            this.setPlaying(true);
            // hide button if remote playback
            // btnPlayVideo.setVisibility(View.INVISIBLE);
        } else if (!SESSION.getResourceProvider().hasVideoFile(this.series)) {
            // hide button if no video available
            // btnPlayVideo.setVisibility(View.INVISIBLE);
        } else {
            // play video on local device
            this.playVideo();
        }
    }

    @Override
    protected void startPlaying() {
        if (SESSION.isConnected() && this.playbackDevice != PlaybackDevice.DEVICE) {
            // start remote playback,
            new SendCommandTask(SESSION.getServerAddress(), KITS_SERVER_PORT)
            .execute(new PlayCommand(this.series.getName(), IntroType.FULL));

            // TODO stop remote playback if switching to local playback in
            // preferences
        }
    }

    @Override
    protected void stopPlaying() {
        if (SESSION.isConnected() && this.playbackDevice != PlaybackDevice.DEVICE) {
            // stop playback on server
            new SendCommandTask(SESSION.getServerAddress(), KITS_SERVER_PORT)
            .execute(new StopCommand());
        }
    }

    private void playVideo() {
        Intent iPlayVideo = new Intent(this, PlayVideo.class);
        Bundle params = new Bundle();
        params.putSerializable(PlayVideo.PARAM_SERIES, this.series);
        iPlayVideo.putExtras(params);
        this.startActivity(iPlayVideo);
    }

    private void continueGame() {
        Intent iChooseSeries = new Intent(this, ChooseSeries.class);
        iChooseSeries.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(iChooseSeries);
    }
}
