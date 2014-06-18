package com.lordmau5.harvest.client.network;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import io.netty.channel.Channel;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:33
 */
public class ServerConnection implements ConnectionContext {
    private final Channel channel;

    public ServerConnection(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }
}