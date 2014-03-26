package com.github.lordmau5.harvest.server.connection;

import com.github.lordmau5.harvest.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 19:49
 */
public class ConnectionHandler implements Runnable {

    private static Socket clientSocket;
    private static Thread thread;

    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    public ConnectionHandler(Socket connection) {
        clientSocket = connection;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {

        try {
            if(clientSocket != null && !clientSocket.isClosed() && clientSocket.isConnected() && !clientSocket.isInputShutdown() && !clientSocket.isOutputShutdown())
                setupStreams();
            else
                closeCon();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(clientSocket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Streams are set-up!");

        syncConnection();
    }

    private void syncConnection() throws IOException {
        Object msg = "Whatever";
        do {
            if(clientSocket == null) {
                return;
            }
            try {
                msg = input.readObject();
                if(msg instanceof String[]) {
                    String[] str = (String[]) msg;
                    if(str[0].equals("username")) {
                        if(Server.players.size() >= Server.config.maxPlayers) {
                            sendToClient("SERVER - FULL");
                            closeCon();
                            return;
                        }
                        if(Server.players.containsValue(str[1])) {
                            sendToClient("NameInUse");
                            closeCon();
                            return;
                        }
                        Server.addPlayer(str[1], clientSocket);
                        System.out.println(str[1] + " joined! - " + Server.players.size() + " players connected!");
                    }
                }
                if(msg instanceof String) {
                    String str = (String) msg;
                    if(str.equals("CLIENT - CONTEST")) {
                        sendToClient("SERVER - CONTEST_SUCCESSFUL");
                    }
                }
            }
            catch (StreamCorruptedException e) {
                closeCon();
            }
            catch (SocketException e) {
                closeCon();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        while(!msg.equals("CLIENT - LEFT"));
    }

    public boolean sendToClient(Object object) {
        if(output == null || clientSocket == null)
            return false;

        try {
            output.writeObject(object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeCon() {
        try {
            output.writeObject("SERVER - STOP");

            Server.players.remove(clientSocket);
            System.out.println("Player left D= | " + Server.players.size() + " left!");

            if(output != null)
                output.close();
            output = null;
            if(input != null)
                input.close();
            input = null;
            if(clientSocket != null)
                clientSocket.close();
            clientSocket = null;

            thread.interrupt();

            Server.removeConHandler(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}