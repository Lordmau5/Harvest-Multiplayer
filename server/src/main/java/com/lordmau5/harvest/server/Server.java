package com.lordmau5.harvest.server;

import com.lordmau5.harvest.server.net.HarvestServer;

public class Server {
    public static void main(String[] args) throws Exception {
        HarvestServer server = new HarvestServer(1234);
        server.run();
    }
}
