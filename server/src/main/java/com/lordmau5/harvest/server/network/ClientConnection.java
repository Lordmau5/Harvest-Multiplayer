package com.lordmau5.harvest.server.network;

import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.packet.handshake.PacketCloseConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:39
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