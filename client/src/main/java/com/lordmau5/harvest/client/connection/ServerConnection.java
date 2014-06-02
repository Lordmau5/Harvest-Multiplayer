package com.lordmau5.harvest.client.connection;

import com.lordmau5.harvest.network.ConnectionContext;
import io.netty.channel.Channel;

/**
 * No description given
 *
 * @author jk-5
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
