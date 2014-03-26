package client;

import client.connection.ConnectionHandler;
import client.util.documents.MaxLengthDocument;
import client.util.documents.OnlyIntegerDocument;
import shared.util.BufferedLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends JFrame implements Runnable {

    public static String playerName;
    private static ConnectionHandler conHandler;
    public static BufferedLoader loader = new BufferedLoader();

    private static JLabel playerName_label;
    private static JTextField playerName_box;
    private static JLabel serverAdress;
    private static JTextField serverAdress_box;
    private static JLabel serverAdressPort;
    private static JTextField serverAdressPort_box;

    private static JButton connect;
    private static JButton testConnection;

    public Client() {
        initComponents();

        setTitle("Harvest Moon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(800, 600));

        JPanel logon = new JPanel();
        logon.setLayout(new GridLayout(12, 0));
        logon.add(playerName_label);
        logon.add(playerName_box);
        logon.add(new JLabel());

        logon.add(serverAdress);
        logon.add(serverAdress_box);

        logon.add(serverAdressPort);
        logon.add(serverAdressPort_box);

        logon.add(new JLabel());
        logon.add(connect);
        logon.add(new JLabel());
        logon.add(new JLabel());
        logon.add(testConnection);

        contentPanel.add(logon);

        setSize(800, 600);

        add(contentPanel);
        //pack();
        setResizable(false);
        setLocation(50, 50);
    }

    public static void setConnectState(boolean state) {
        connect.setEnabled(state);
        if(state) {
            JOptionPane.showMessageDialog(null, "Server closed the connection!", "ERROR: Server closed connection!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        playerName_label = new JLabel("Username:");
        playerName_label.setPreferredSize(new Dimension(75, 20));
        playerName_label.setHorizontalAlignment(SwingConstants.CENTER);
        playerName_box = new JTextField();
        playerName_box.setPreferredSize(new Dimension(200, 20));
        playerName_box.setDocument(new MaxLengthDocument(22));

        serverAdress = new JLabel("Server Adress:");
        serverAdress.setPreferredSize(new Dimension(100, 20));
        serverAdress.setHorizontalAlignment(SwingConstants.CENTER);
        serverAdress_box = new JTextField();
        serverAdress_box.setPreferredSize(new Dimension(200, 20));

        serverAdressPort = new JLabel("Server Port:");
        serverAdressPort.setPreferredSize(new Dimension(100, 20));
        serverAdressPort.setHorizontalAlignment(SwingConstants.CENTER);
        serverAdressPort_box = new JTextField();
        serverAdressPort_box.setPreferredSize(new Dimension(200, 20));
        serverAdressPort_box.setDocument(new OnlyIntegerDocument());

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(100, 20));
        connect.setHorizontalAlignment(SwingConstants.CENTER);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkConnection()) {
                    playerName = playerName_box.getText();

                    try {
                        connectToServer(serverAdress_box.getText(), Integer.parseInt(serverAdressPort_box.getText()));
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        testConnection = new JButton("Test Connection");
        testConnection.setPreferredSize(new Dimension(100, 20));
        testConnection.setHorizontalAlignment(SwingConstants.CENTER);
        testConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(conHandler == null || conHandler.connection == null || conHandler.connection.isClosed()) {
                    JOptionPane.showMessageDialog(null, "You are not connected!", "ERROR: Not connected!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else
                    conHandler.sendToServer("CLIENT - CONTEST");
            }
        });
    }

    private boolean checkConnection() {
        if(playerName_box.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a username to play!", "ERROR: No username defined!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(serverAdress_box.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a server to play on!", "ERROR: No server defined!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(serverAdressPort_box.getText().isEmpty()) {
            serverAdressPort_box.setText("8095");
        }
        return true;
    }

    private void connectToServer(String adress, int port) throws IOException {
        try {
            conHandler = new ConnectionHandler(adress, port);
        }
        catch(ConnectException e) {
            JOptionPane.showMessageDialog(null, "Can't reach server. Did you misspell something?", "ERROR: Couldn't connect to server!", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new Client().setVisible(true);
            }
        });
    }

    @Override
    public void run() {}
}