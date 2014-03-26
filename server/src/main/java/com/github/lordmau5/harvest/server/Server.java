package com.github.lordmau5.harvest.server;

//import com.github.lordmau5.harvest.server.config.Configuration;

import com.github.lordmau5.harvest.server.config.Configuration;
import com.github.lordmau5.harvest.server.connection.ConnectionHandler;
import com.github.lordmau5.harvest.server.util.helper.PortUsage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Server implements Runnable {

    private static ServerSocket serverSocket;
    public static Configuration config;
    private static List<ConnectionHandler> conHandlers = new ArrayList<ConnectionHandler>();
    public static Map<Socket, String> players = new HashMap<Socket, String>();

    //1951 as port == Skype (in usage)

    private static class Shutdown extends Thread {
        public void run() {
            for(ConnectionHandler con : conHandlers) {
                con.closeCon();
            }
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Shutdown());
        new Server().run();
    }

    public static void removeConHandler(ConnectionHandler conHandler) {
        conHandlers.remove(conHandler);
    }

    private void waitForConnection() throws IOException {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connected to: " + clientSocket.getInetAddress().getHostAddress());
        conHandlers.add(new ConnectionHandler(clientSocket));
    }

    public static void addPlayer(String username, Socket connection) throws IOException {
        Server.players.put(connection, username);
    }

    @Override
    public void run() {
        if(config == null)
            config = new Configuration();

        if(!PortUsage.available(config.serverPort)) {
            System.out.println("The port \"" + config.serverPort + "\" is already in usage. Try a different one.");
            System.out.println("Press any key to continue...");
            try {
                System.in.read();
                System.exit(1);
            }
            catch (IOException e) {}
        }

        try {
            serverSocket = new ServerSocket(config.serverPort, config.maxPlayers);
            while(true) {
                waitForConnection();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}