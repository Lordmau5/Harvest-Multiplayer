package com.lordmau5.harvest.server;

//import Configuration;

import com.lordmau5.harvest.server.network.ClientConnection;
import com.lordmau5.harvest.server.network.NetworkServer;
import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.floor.Farmland;

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

        for(int x=0; x<world.farmWidth; x++) {
            for(int y=0; y<world.farmHeight; y++) {
                world.getFarmland().put(new Tile(x, y), new Farmland(x, y));
            }
        }

        world.addEntity("grass", 8, 8);
        world.addEntity("grass", 8, 9);
        world.addEntity("grass", 16, 8);
        world.addEntity("grass", 18, 8);
        world.addEntity("grass", 14, 6);

        world.addEntity("bigStone", 4, 4);
        world.addEntity("bigStone", 16, 16);

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
