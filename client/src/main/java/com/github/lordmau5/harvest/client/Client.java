package com.github.lordmau5.harvest.client;

import com.github.lordmau5.harvest.client.connection.NetworkHandler;
import com.github.lordmau5.harvest.client.util.documents.MaxLengthDocument;
import com.github.lordmau5.harvest.util.BufferedLoader;

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
    public static BufferedLoader loader = new BufferedLoader();

    private static JLabel playerName_label;
    private static JTextField playerName_box;
    private static JLabel serverAdress;
    private static JTextField serverAdress_box;

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

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(100, 20));
        connect.setHorizontalAlignment(SwingConstants.CENTER);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkConnection()) {
                    playerName = playerName_box.getText();
                    connectToServer(serverAdress_box.getText());
                }
            }
        });

        testConnection = new JButton("Test Connection");
        testConnection.setPreferredSize(new Dimension(100, 20));
        testConnection.setHorizontalAlignment(SwingConstants.CENTER);
        testConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(NetworkHandler.isConnected()){
                    JOptionPane.showMessageDialog(null, "Connected", "Connected", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Not Connected", "Not Connected", JOptionPane.INFORMATION_MESSAGE);
                }
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
        return true;
    }

    private void connectToServer(String adress) {
        String[] parts = adress.split(":", 2);
        String host = parts[0];
        int port = 8095;
        if(parts.length == 2){
            try{
                port = Integer.parseInt(parts[1]);
            }catch(NumberFormatException ignored){

            }
        }
        NetworkHandler.connect(host, port);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                new Client().setVisible(true);
            }
        });
    }
}