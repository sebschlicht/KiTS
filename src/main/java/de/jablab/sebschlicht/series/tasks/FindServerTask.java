package de.jablab.sebschlicht.series.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import android.os.AsyncTask;
import android.util.Log;
import de.jablab.sebschlicht.android.UDPServer;
import de.jablab.sebschlicht.android.kits.commands.Command;
import de.jablab.sebschlicht.android.kits.commands.RegisterCommand;

/**
 * Asynchronous task to find the KiTS server in any network the device is
 * connected to. Most likely the device and the KiTS server are connected to the
 * same WLAN.
 *
 * @author sebschlicht
 *
 */
public class FindServerTask extends AsyncTask<Void, Void, String> {

    /**
     * default response timeout
     */
    private static final int TIMEOUT = 5000;

    /**
     * completion callback
     */
    private TaskCallback<String> callback;

    /**
     * server port
     */
    private int serverPort;

    /**
     * Creates a server search task.
     *
     * @param callback
     *            completion callback (the server address will be passed, may be
     *            <code>null</code> if no server has been found)
     * @param serverPort
     *            server port
     */
    public FindServerTask(
            final TaskCallback<String> callback,
            final int serverPort) {
        this.callback = callback;
        this.serverPort = serverPort;
    }

    @Override
    protected String doInBackground(final Void... params) {
        // broadcast the register command (JSON) and wait for a response
        String message = new RegisterCommand().toJson();
        byte[] baRegisterCommand = null;
        try {
            baRegisterCommand = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d("FindServer", "Failed to encode registration command!");
            e.printStackTrace();
            return null;
        }
        Log.d("FindServer - message", message);

        UDPServer server = null;
        try {
            server = new UDPServer();
            server.broadcast(this.serverPort, baRegisterCommand);

            Log.d("FindServer", "waiting up to " + TIMEOUT + " seconds for response...");
            DatagramPacket responsePacket = server.receive(TIMEOUT);

            String response =
                    new String(responsePacket.getData(), 0, responsePacket.getLength(),
                            "UTF-8");
            Log.d("FindServer - response", response);

            if (response.equals(Command.SERVER_SEARCH_RESPONSE_STRING)) {
                return responsePacket.getAddress().getHostAddress();
            } else {
                // unknown response
                return null;
            }

        } catch (IOException e) {
            Log.d("FindServer", "Failed to find server!");
            e.printStackTrace();
            return null;
        } finally {
            if (server != null) {
                server.close();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        this.callback.handleResult(result, this.getClass());
    }
}
