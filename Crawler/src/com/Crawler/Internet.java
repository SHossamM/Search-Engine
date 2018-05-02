package com.Crawler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class Internet {
    public static boolean isInternetAvailable() throws IOException  //this checks internet connection
    {
        return isHostAvailable("google.com") || isHostAvailable("amazon.com")
                || isHostAvailable("facebook.com") || isHostAvailable("apple.com");
    }

    private static boolean isHostAvailable(String hostName) throws IOException {
        try (Socket socket = new Socket()) {
            int port = 80;
            InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
            socket.connect(socketAddress, 3000);

            return true;
        } catch (UnknownHostException | SocketTimeoutException unknownHost) {
            return false;
        }
    }
}
