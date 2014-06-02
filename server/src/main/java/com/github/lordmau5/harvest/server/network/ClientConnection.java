package com.github.lordmau5.harvest.server.network;

import com.github.lordmau5.harvest.network.ConnectionContext;
import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * No description given
 *
 * @author jk-5
 */
public class ClientConnection implements ConnectionContext {
    private final int protocolVersion;
    private final String username;
    private final Channel channel;

    public ClientConnection(int protocolVersion, String username, Channel channel) {
        this.protocolVersion = protocolVersion;
        this.username = username;
        this.channel = channel;
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    public String username() {
        return this.username;
    }

    public void kick(String message) {
        this.channel.writeAndFlush(new PacketCloseConnection(message)).addListener(ChannelFutureListener.CLOSE);
    }
}
