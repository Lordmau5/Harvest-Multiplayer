package com.lordmau5.harvest.server;

//import Configuration;

import com.lordmau5.harvest.server.network.ClientConnection;
import com.lordmau5.harvest.server.network.NetworkServer;
import com.lordmau5.harvest.shared.World;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Server {

    public static World world;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Shutdown());

        world = new World();

        NetworkServer.start("localhost", 8075);
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
