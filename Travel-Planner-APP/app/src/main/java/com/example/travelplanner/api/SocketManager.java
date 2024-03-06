package com.example.travelplanner.api;

import android.content.Context;
import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketManager {
    private static final String SERVER_URL = RetrofitInstance.IPAddress;
    private static Socket mSocket;

    private SocketManager() {
        try {
            mSocket = IO.socket(SERVER_URL);
            Log.e("socket","connected");
        } catch (URISyntaxException e) {
            Log.e("socket",e.toString());
            e.printStackTrace();
        }
    }

    public static Socket getInstance() {
        if (mSocket == null) {
            new SocketManager();
        }
        return mSocket;
    }
}

