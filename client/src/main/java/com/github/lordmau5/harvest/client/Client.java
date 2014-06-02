package com.github.lordmau5.harvest.client;

import com.github.lordmau5.harvest.client.connection.NetworkHandler;
import com.github.lordmau5.harvest.client.util.documents.MaxLengthDocument;
import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 11:43
 */
public class Client extends JFrame {
    public static String playerName;

    private static JLabel playerName_label;
    private static JTextField playerName_box;
    private static JLabel serverAddress;
    private static JTextField serverAddress_box;

    private static JButton connect;
    private static JButton disconnect;
    private static JButton testConnection;

    public Client() {
        initComponents();

        setTitle("Harvest Moon");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(800, 600));

        JPanel logon = new JPanel();
        logon.setLayout(new GridLayout(12, 0));
        logon.add(playerName_label);
        logon.add(playerName_box);
        logon.add(new JLabel());

        logon.add(serverAddress);
        logon.add(serverAddress_box);

        logon.add(new JLabel());
        logon.add(connect);
        logon.add(disconnect);
        logon.add(new JLabel());
        logon.add(testConnection);

        contentPanel.add(logon);

        setSize(800, 600);

        add(contentPanel);
        //pack();
        setResizable(false);
        setLocation(50, 50);
        setConnectableState(true);
    }

    public static void setConnectableState(boolean state) {
        connect.setEnabled(state);
        disconnect.setEnabled(!state);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new Client().setVisible(true);
            }
        });
    }

    private void initComponents() {
        playerName_label = new JLabel("Username:");
        playerName_label.setPreferredSize(new Dimension(75, 20));
        playerName_label.setHorizontalAlignment(SwingConstants.CENTER);
        playerName_box = new JTextField();
        playerName_box.setPreferredSize(new Dimension(200, 20));
        playerName_box.setDocument(new MaxLengthDocument(22));

        serverAddress = new JLabel("Server Adress:");
        serverAddress.setPreferredSize(new Dimension(100, 20));
        serverAddress.setHorizontalAlignment(SwingConstants.CENTER);
        serverAddress_box = new JTextField();
        serverAddress_box.setPreferredSize(new Dimension(200, 20));

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(100, 20));
        connect.setHorizontalAlignment(SwingConstants.CENTER);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkConnection()) {
                    playerName = playerName_box.getText();
                    connectToServer(serverAddress_box.getText());
                }
            }
        });

        disconnect = new JButton("Disconnect");
        disconnect.setPreferredSize(new Dimension(100, 20));
        disconnect.setHorizontalAlignment(SwingConstants.CENTER);
        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (NetworkHandler.isConnected()) {
                    NetworkHandler.sendPacket(new PacketCloseConnection("Disconnect"));
                }
            }
        });

        testConnection = new JButton("Test Connection");
        testConnection.setPreferredSize(new Dimension(100, 20));
        testConnection.setHorizontalAlignment(SwingConstants.CENTER);
        testConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (NetworkHandler.isConnected()) {
                    JOptionPane.showMessageDialog(null, "Connected", "Connected", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Not Connected", "Not Connected", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean checkConnection() {
        if (playerName_box.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a username to play!", "ERROR: No username defined!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (serverAddress_box.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "You have to enter a server to play on!", "ERROR: No server defined!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void connectToServer(String address) {
        String[] parts = address.split(":", 2);
        String host = parts[0];
        int port = 8095;
        if (parts.length == 2) {
            try {
                port = Integer.parseInt(parts[1]);
            } catch (NumberFormatException ignored) {

            }
        }
        NetworkHandler.connect(host, port);
    }
}