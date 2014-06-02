package com.lordmau5.harvest.server.config;

import java.io.*;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 13:13
 */
public class Configuration {
    public String worldName;
    public String serverHost = "0.0.0.0";
    public int serverPort;
    public int maxPlayers;
    private String configLocation = new File(".").getAbsolutePath();
    private String configFileName = "server.config";

    public Configuration() {
        configLocation = configLocation.substring(0, configLocation.length() - 1) + "config/";
        readConfiguration(new File(configLocation + configFileName));
    }

    public void readConfiguration(File configFile) {
        if (!configFile.exists()) {
            getDefaultConfiguration();
        }

        String currentLine;
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(configFile));
            while ((currentLine = reader.readLine()) != null) {
                String[] lineSplit = currentLine.split("=");
                switch (lineSplit[0]) {
                    case "world-name": {
                        worldName = lineSplit[1];
                        break;
                    }

                    case "host": {
                        serverHost = lineSplit[1];
                        break;
                    }

                    case "port": {
                        try {
                            serverPort = Integer.parseInt(lineSplit[1]);
                            if (serverPort < 1 || serverPort > 65535) {
                                serverPort = 8095;
                            }
                        } catch (NumberFormatException e) {
                            serverPort = 8095;
                        }
                        break;
                    }

                    case "max-players": {
                        try {
                            maxPlayers = Integer.parseInt(lineSplit[1]);
                            if (maxPlayers > 4) {
                                maxPlayers = 4;
                            }
                        } catch (NumberFormatException e) {
                            maxPlayers = 4;
                        }
                        break;
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.serverHost == null) {
            this.serverHost = "0.0.0.0";
        }
    }

    public void getDefaultConfiguration() {
        worldName = "world";
        serverPort = 8095;
        maxPlayers = 4;

        new File(configLocation).mkdir();
        PrintWriter writer;
        try {
            writer = new PrintWriter(new File(configLocation + configFileName), "UTF-8");
            writer.println("host=" + this.serverHost);
            writer.println("world-name=" + worldName);
            writer.println("port=" + serverPort);
            writer.println("max-players=" + maxPlayers);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
