package de.jablab.sebschlicht.series.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;
import de.jablab.sebschlicht.series.R;
import de.jablab.sebschlicht.series.resources.ResourceProvider;

/**
 * Video player activity.<br>
 * This activity starts if the video has to be played on the local device.
 *
 * @author sebschlicht
 *
 */
public class PlayVideo extends KitsSeriesActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make video view fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // connect to UI
        this.videoView = (VideoView) this.findViewById(R.id.vvIntro);

        MediaController mediaController = new MediaController(PlayVideo.this);
        mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(mediaController);

        final ResourceProvider resourceProvider = SESSION.getResourceProvider();
        if (!resourceProvider.hasVideoFile(this.series)) {
            return;
        }
        final Uri videoFileUri = resourceProvider.getVideoFileUri(this.series);
        this.videoView.setVideoURI(videoFileUri);

        //        final File videoFile = resourceProvider.getVideoFile(this._series);
        //        this.videoView.setVideoPath(videoFile.getAbsolutePath());

        this.videoView.requestFocus();
        this.setPlaying(true);
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_play_video);
    }

    @Override
    protected void startPlaying() {
        this.videoView.start();
    }

    @Override
    protected void stopPlaying() {
        this.videoView.stopPlayback();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
