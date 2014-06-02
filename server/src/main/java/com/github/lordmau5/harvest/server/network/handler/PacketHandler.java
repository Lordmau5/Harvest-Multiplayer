package com.github.lordmau5.harvest.server.network.handler;

import com.github.lordmau5.harvest.network.packet.Packet;
import com.github.lordmau5.harvest.server.network.NetworkServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * No description given
 *
 * @author jk-5
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        msg.process(ctx.channel().attr(NetworkServer.clientConnection).get());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NetworkServer.onConnectionClosed(ctx.channel().attr(NetworkServer.clientConnection).get());
        ctx.fireChannelInactive();
    }
}
