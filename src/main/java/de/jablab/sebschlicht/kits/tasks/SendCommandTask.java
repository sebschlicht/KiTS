package de.jablab.sebschlicht.kits.tasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import de.jablab.sebschlicht.android.kits.commands.Command;

/**
 * Asynchronous task to send a command to a KiTS server.
 *
 * @author sebschlicht
 *
 */
public class SendCommandTask extends AsyncTask<Command, Void, Boolean> {

    /**
     * server address
     */
    private String serverAddress;

    /**
     * server port
     */
    private int serverPort;

    /**
     * Creates a task, that will send a command to a KiTS server.
     *
     * @param serverAddress
     *            server address
     * @param serverPort
     *            server port
     */
    public SendCommandTask(
            String serverAddress,
            int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    protected Boolean doInBackground(final Command... params) {
        // send exactly one command
        if (params.length != 1) {
            return null;
        }
        // serialize command to JSON
        byte[] data;
        try {
            data = params[0].toJson().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        // send JSON to endpoint specified
        try {
            DatagramSocket socket = new DatagramSocket();

            for (InetAddress address : InetAddress.getAllByName(this.serverAddress)) {
                try {
                    DatagramPacket packet =
                            new DatagramPacket(data, data.length, address,
                                    this.serverPort);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }
}
