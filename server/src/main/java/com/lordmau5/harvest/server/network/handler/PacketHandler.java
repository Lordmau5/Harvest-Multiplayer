package com.lordmau5.harvest.server.network.handler;

import com.lordmau5.harvest.server.network.ClientConnection;
import com.lordmau5.harvest.server.network.NetworkServer;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.network.packet.player.PacketPlayerMovement;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketPlayerJoin;
import com.lordmau5.harvest.shared.network.packet.playercon.PacketRequestPlayers;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:40
 */
@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        if(msg instanceof PacketPlayerMovement) {
            for(ClientConnection con : NetworkServer.connections)
                con.channel().writeAndFlush(msg);
            return;
        }
        else if(msg instanceof PacketRequestPlayers) {
            for(ClientConnection con : NetworkServer.connections)
                if(!con.username().equals(((PacketRequestPlayers) msg).username))
                    ctx.channel().writeAndFlush(new PacketPlayerJoin(con.username()));
            return;
        }
        msg.process(ctx.channel().attr(NetworkServer.clientConnection).get());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NetworkServer.onConnectionClosed(ctx.channel().attr(NetworkServer.clientConnection).get());
        ctx.fireChannelInactive();
    }
}