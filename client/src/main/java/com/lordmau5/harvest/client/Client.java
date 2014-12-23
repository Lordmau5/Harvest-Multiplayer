package com.lordmau5.harvest.client;

import com.lordmau5.harvest.client.net.HarvestClient;

public class Client {
    public static void main(String[] args) {
        HarvestClient client = new HarvestClient("localhost", 1234);
        client.connect();
    }
}
