package de.jablab.sebschlicht.series.activities;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.jablab.sebschlicht.series.R;
import de.jablab.sebschlicht.series.SeriesSession;
import de.jablab.sebschlicht.series.model.Series;
import de.jablab.sebschlicht.series.resources.InternalStorageProvider;
import de.jablab.sebschlicht.series.tasks.FindServerTask;
import de.jablab.sebschlicht.series.tasks.TaskCallback;

/**
 * Application startup activity that allows users to start the series guessing
 * game.
 *
 * @author sebschlicht
 *
 */
public class ChooseSeries extends KitsActivity implements TaskCallback<String> {

    /**
     * parameter for the application exiting flag
     */
    public static final String PARAM_EXIT = "exit";

    /**
     * application exiting flag
     */
    protected boolean isExiting;

    /**
     * KiTS server connection state view
     */
    private TextView lampServerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if exiting application
        if (this.isExiting) {
            this.finish();
        }

        // initialize the session if necessary
        if (SESSION == null) {
            final InputStream csvStream =
                    this.getResources().openRawResource(R.raw.series);
            try {
                SESSION =
                        new SeriesSession(InternalStorageProvider.create(this
                                .getApplicationContext()), csvStream);
            } catch (IOException e) {
                Log.e("ChooseSeries", "Failed to load series!", e);
            }
            new FindServerTask(this, KITS_SERVER_PORT).execute();
        }
    }

    @Override
    protected void setContentView() {
        this.setContentView(R.layout.activity_choose_series);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.choose_series_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // disable the server search if connected
        final MenuItem miSearchServer = menu.findItem(R.id.action_search_server);
        miSearchServer.setEnabled(!SESSION.isConnected());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void loadParameters(Bundle params) {
        super.loadParameters(params);
        this.isExiting = params.getBoolean(PARAM_EXIT, false);
    }

    @Override
    protected void connectToUi() {
        super.connectToUi();
        // server connection state
        this.lampServerState = (TextView) this.findViewById(R.id.lampServerState);

        // start game
        final Button btnRandom = (Button) this.findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChooseSeries.this.startGuessingGame();
            }
        });
    }

    /**
     * Starts the guessing game with a random, unwatched series if any is left.
     */
    private void startGuessingGame() {
        final Series series = SESSION.getRandomSeries();
        if (series != null) {
            this.startGuessingGame(series);
        }
    }

    /**
     * Starts the guessing game with a series.
     *
     * @param series
     *            series to start the guessing game with
     */
    private void startGuessingGame(final Series series) {
        final Intent iPlaySeries = new Intent(this, PlayAudio.class);
        final Bundle params = new Bundle();
        params.putSerializable(PlayAudio.PARAM_SERIES, series);
        params.putSerializable(PlayAudio.PARAM_STARTUP, true);
        iPlaySeries.putExtras(params);
        this.startActivity(iPlaySeries);
    }

    @Override
    public void
    handleResult(String result, Class<? extends AsyncTask<?, ?, ?>> taskClass) {
        if (result == null) {
            Toast.makeText(this.getApplicationContext(), "no KiTS server available",
                    Toast.LENGTH_LONG).show();
        } else {
            SESSION.setServerAddress(result);
            this.updateRemotePlaybackVolume(this.getPlaybackVolume());
            this.lampServerState.setText(result);
        }
    }
}
