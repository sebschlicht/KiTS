package de.jablab.sebschlicht.kits.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import de.jablab.sebschlicht.android.kits.commands.Command;
import de.jablab.sebschlicht.android.kits.commands.SetVolumeCommand;
import de.jablab.sebschlicht.kits.R;
import de.jablab.sebschlicht.kits.SeriesSession;
import de.jablab.sebschlicht.kits.activities.preferences.PlaySeriesPreferences;
import de.jablab.sebschlicht.kits.tasks.SendCommandTask;

/**
 * Basic class for activities in KiTS.
 *
 * @author sebschlicht
 *
 */
abstract public class KitsActivity extends Activity {

    /**
     * default KiTS server port
     */
    public static final int KITS_SERVER_PORT = 61010;

    /**
     * shared KiTS session
     */
    protected static SeriesSession SESSION = null;

    /**
     * application preferences
     */
    protected SharedPreferences preferences;

    /**
     * audio manager to work with device volume
     */
    protected AudioManager audioManager;

    /**
     * global remote playback volume
     */
    protected int playbackVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView();

        // application activities are fullscreen
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // replace action bar with KiTS action bar if present
        final ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.action_bar);
        }

        // load device volume
        this.audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        this.playbackVolume = this.calcPlaybackVolume();

        // load preferences
        this.loadPreferences();

        // load parameters
        final Bundle params = this.getIntent().getExtras();
        if (params != null) {
            this.loadParameters(params);
        }

        // connect to UI
        this.connectToUi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.startActivity(new Intent(this.getApplicationContext(),
                        PlaySeriesPreferences.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(R.string.alert_exit_title);
        builder.setMessage(R.string.alert_exit_message);
        builder.setNegativeButton(R.string.no, null);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(KitsActivity.this, ChooseSeries.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(ChooseSeries.PARAM_EXIT, true);
                KitsActivity.this.startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // check if volume has been changed
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // adjust local media volume
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            } else {
                this.audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }

            // update remote playback volume
            this.setPlaybackVolume(this.calcPlaybackVolume());

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @return global remote playback volume
     */
    protected int getPlaybackVolume() {
        return this.playbackVolume;
    }

    /**
     * Updates the global playback volume of the KiTS server, if connected.
     *
     * @param playbackVolume
     *            global remote playback volume
     */
    public void setPlaybackVolume(final int playbackVolume) {
        if (this.playbackVolume == playbackVolume) {
            return;
        }
        this.playbackVolume = playbackVolume;
        if (SESSION.isConnected()) {
            this.updateRemotePlaybackVolume(playbackVolume);
        }
    }

    /**
     * Sets the content view of this activity.
     */
    protected abstract void setContentView();

    /**
     * Loads the application preferences.
     */
    protected void loadPreferences() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Loads the parameters needed by this activity.
     *
     * @param params
     *            activity parameter bundle
     */
    protected void loadParameters(final Bundle params) {
        // to be overridden
    }

    /**
     * Connects this activity to its UI elements.
     */
    protected void connectToUi() {
        // to be overridden
    }

    /**
     * Calculates the current media playback volume set in the device.
     *
     * @return current media playback volume of the device
     */
    protected int calcPlaybackVolume() {
        float maxMediaVolume =
                this.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float mediaVolumePercentage =
                this.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                        / maxMediaVolume;
        return (int) (mediaVolumePercentage * 100);
    }

    /**
     * Sends the global playback volume to the KiTS server.
     *
     * @param playbackVolume
     *            global remote playback volume
     */
    protected void updateRemotePlaybackVolume(final int playbackVolume) {
        Log.d("Server", "volume: " + playbackVolume);
        final Command command = new SetVolumeCommand(playbackVolume);
        new SendCommandTask(SESSION.getServerAddress(), KitsActivity.KITS_SERVER_PORT)
        .execute(command);
    }
}
