package client.connection;

import client.Client;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Author: Lordmau5
 * Date: 26.03.14
 * Time: 15:56
 */
public class ConnectionHandler implements Runnable {

    public Socket connection;
    private Thread thread;

    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ConnectionHandler(String adress, int port) throws IOException {
        connection = new Socket(adress, port);
        thread = new Thread(this);
        thread.start();
    }

    public void run() {

        try {
            if(connection != null && !connection.isClosed() && connection.isConnected() && !connection.isInputShutdown() && !connection.isOutputShutdown())
                setupStreams();
            else
                closeCon(true);
        }
        catch (EOFException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        Client.setConnectState(false);

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Streams are set-up!");
        output.writeObject(new String[]{"username", Client.playerName});

        syncConnection();
    }

    private void syncConnection() throws IOException {
        Object msg = "Whatever";
        do {
            if(connection == null) {
                return;
            }
            try {
                msg = input.readObject();
                if(msg instanceof String) {
                    String str = (String) msg;
                    if(str.equals("NameInUse")) {
                        closeCon(false);
                        JOptionPane.showMessageDialog(null, "The name is already in usage. Try a different one!", "ERROR: Name already in use!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(str.equals("SERVER - FULL")) {
                        JOptionPane.showMessageDialog(null, "Server is full!", "ERROR: Server full!", JOptionPane.ERROR_MESSAGE);
                        closeCon(false);
                        return;
                    }
                    if(str.equals("SERVER - CONTEST_SUCCESSFUL")) {
                        JOptionPane.showMessageDialog(null, "Connection still there!", "SUCCESS: Connection still alive!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            catch (SocketException e) {
                closeCon(false);
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        while(!msg.equals("SERVER - STOP"));
    }

    public boolean sendToServer(Object object) {
        if(output == null || connection == null)
            return false;

        try {
            output.writeObject(object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void closeCon(boolean didLeave) {
        try {
            if(input == null && output == null && connection == null) {
                thread.interrupt();
                return;
            }
            if(output != null) {
                if(didLeave)
                    output.writeUTF("CLIENT - LEFT");

                output.close();
            }
            output = null;
            if(input != null)
                input.close();
            input = null;
            if(connection != null)
                connection.close();
            connection = null;

            thread.interrupt();

            Client.setConnectState(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}