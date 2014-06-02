package com.github.lordmau5.harvest.server;

//import com.github.lordmau5.harvest.server.config.Configuration;

import com.github.lordmau5.harvest.server.config.Configuration;
import com.github.lordmau5.harvest.server.network.ClientConnection;
import com.github.lordmau5.harvest.server.network.NetworkServer;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Server {
    public static Configuration config;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Shutdown());
        if (config == null) {
            config = new Configuration();
        }
        //The network server blocks the main thread until it gets closed (never)
        //That means that code after this line will never be executed
        //If you want to change this, remove the .syncUninterruptibly() from the method below
        NetworkServer.start(config.serverHost, config.serverPort);
    }

    private static class Shutdown extends Thread {
        private Shutdown() {
            this.setName("Shutdown thread");
            this.setDaemon(false);
        }

        @Override
        public void run() {
            for (ClientConnection connection : NetworkServer.connections) {
                connection.kick("Server shutting down");
            }
        }
    }
}
