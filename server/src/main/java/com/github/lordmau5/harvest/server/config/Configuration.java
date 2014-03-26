package server.config;

import java.io.*;

/**
 * Author: Lordmau5
 * Date: 25.03.14
 * Time: 13:13
 */
public class Configuration {

    private String configLocation = new File(".").getAbsolutePath();
    private String configFileName = "server.config";

    public String worldName;
    public int serverPort;
    public int maxPlayers;

    public Configuration() {
        configLocation = configLocation.substring(0, configLocation.length() - 1) + "config/";
        readConfiguration(new File(configLocation + configFileName));
    }

    public void readConfiguration(File configFile) {
        if(!configFile.exists())
            getDefaultConfiguration();

        String currentLine;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(configFile));
            while((currentLine = br.readLine()) != null) {
                String[] lineSplit = currentLine.split("=");
                if(lineSplit[0].equals("world-name"))
                    worldName = lineSplit[1];
                if(lineSplit[0].equals("port")) {
                    try {
                        serverPort = Integer.parseInt(lineSplit[1]);
                        if(serverPort < 1 || serverPort > 65535)
                            serverPort = 8095;
                    }
                    catch(NumberFormatException e) {
                        serverPort = 8095;
                    }
                }
                if(lineSplit[0].equals("max-players")) {
                    try {
                        maxPlayers = Integer.parseInt(lineSplit[1]);
                        if(maxPlayers > 4)
                            maxPlayers = 4;
                    }
                    catch(NumberFormatException e) {
                        maxPlayers = 4;
                    }
                }
            }
            br.close();
        }
        catch(FileNotFoundException e) {}
        catch(IOException e) {}
    }

    public void getDefaultConfiguration() {
        worldName = "world";
        serverPort = 8095;
        maxPlayers = 4;

        new File(configLocation).mkdir();
        PrintWriter writer;
        try {
            writer = new PrintWriter(new File(configLocation + configFileName), "UTF-8");
            writer.println("world-name=" + worldName);
            writer.println("port=" + serverPort);
            writer.println("max-players=" + maxPlayers);
            writer.close();
        }
        catch (FileNotFoundException e) {}
        catch (UnsupportedEncodingException e) {}
    }

}
