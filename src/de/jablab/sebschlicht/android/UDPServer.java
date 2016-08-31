package de.jablab.sebschlicht.android;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

public class UDPServer {

    private DatagramSocket socket;

    public UDPServer() throws SocketException {
        this.socket = new DatagramSocket();
    }

    protected InetAddress getBroadcastAddress() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces =
                NetworkInterface.getNetworkInterfaces();

        NetworkInterface networkInterface;
        InetAddress broadcastAddress = null;
        while (networkInterfaces.hasMoreElements()) {
            networkInterface = networkInterfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                broadcastAddress = address.getBroadcast();
                if (broadcastAddress == null) {
                    continue;
                }
            }
        }

        return broadcastAddress;
    }

    public void send(InetAddress address, int port, byte[]... dataPackets)
            throws IOException {
        for (byte[] data : dataPackets) {
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            Log.d("UDP server",
                    "sending UDP message with length " + data.length + " to "
                            + address.getHostAddress() + ":" + port + " (\""
                            + address.getCanonicalHostName() + "\")...");
            this.socket.send(packet);
        }
    }

    public DatagramPacket receive(int timeout) throws IOException {
        byte[] receiveData = new byte[4096];
        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
        this.socket.setSoTimeout(timeout);
        this.socket.receive(packet);
        return packet;
    }

    public void broadcast(int port, byte[]... dataPackets) throws IOException {
        this.socket.setBroadcast(true);
        InetAddress broadcastAddress = this.getBroadcastAddress();
        Log.d("UDP server", "broadcasting via " + broadcastAddress.getHostAddress());
        try {
            this.send(broadcastAddress, port, dataPackets);
        } finally {
            this.socket.setBroadcast(false);
        }
    }

    public boolean sendSilent(InetAddress address, int port, byte[]... dataPackets) {
        try {
            this.send(address, port, dataPackets);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        if (this.socket != null) {
            this.socket.close();
        }
    }
}
