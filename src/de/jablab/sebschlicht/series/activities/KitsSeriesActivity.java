package de.jablab.sebschlicht.series.activities;

import android.os.Bundle;
import de.jablab.sebschlicht.series.activities.preferences.PlaybackDevice;
import de.jablab.sebschlicht.series.model.Series;

/**
 * Base activity for the series guessing process.<br>
 * The activity is aware of the current series stored in its parameters.<br>
 * It can store the playback device and state. Halts the playback if the
 * activity pauses in {@link #onPause()}.
 *
 * @author sebschlicht
 *
 */
abstract public class KitsSeriesActivity extends KitsActivity {

    /**
     * parameter key for the current series
     */
    public static final String PARAM_SERIES = "series";

    /**
     * parameter key for the playback count reset flag
     */
    public static final String PARAM_STARTUP = "reset";

    /**
     * current series being guessed
     */
    protected Series series;

    /**
     * activity startup flag
     */
    protected boolean isStartup;

    /**
     * playback device preference
     */
    protected PlaybackDevice playbackDevice;

    /**
     * playback running flag
     */
    protected boolean isPlaying;

    @Override
    protected void onPause() {
        super.onPause();
        this.setPlaying(false);
    }

    @Override
    protected void loadParameters(final Bundle params) {
        super.loadParameters(params);
        this.series = (Series) params.getSerializable(PARAM_SERIES);
        this.isStartup = params.getBoolean(PARAM_STARTUP, false);
    }

    /**
     * @return playback running flag
     */
    protected boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * Sets the playback running flag.
     *
     * @param isPlaying
     *            playback running flag
     */
    protected void setPlaying(boolean isPlaying) {
        if (this.isPlaying == isPlaying) {
            return;
        }
        this.isPlaying = isPlaying;
        if (isPlaying) {
            this.startPlaying();
        } else {
            this.stopPlaying();
        }
    }

    /**
     * Starts the playback.
     */
    abstract protected void startPlaying();

    /**
     * Stops the playback.
     */
    abstract protected void stopPlaying();
}
