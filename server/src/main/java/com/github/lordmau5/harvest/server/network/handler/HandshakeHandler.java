package com.github.lordmau5.harvest.server.network.handler;

import com.github.lordmau5.harvest.network.codec.CodecRegistry;
import com.github.lordmau5.harvest.network.codec.PacketCodec;
import com.github.lordmau5.harvest.network.packet.handshake.PacketClientHandshake;
import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;
import com.github.lordmau5.harvest.network.packet.handshake.PacketServerHandshake;
import com.github.lordmau5.harvest.server.network.ClientConnection;
import com.github.lordmau5.harvest.server.network.NetworkServer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * No description given
 *
 * @author jk-5
 */
public class HandshakeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        //Only do packet handling with instanceof in this handshakehandler, just because it's easier
        if(msg instanceof PacketClientHandshake){
            PacketClientHandshake packet = (PacketClientHandshake) msg;
            PacketCodec codec = CodecRegistry.getCodecForVersion(packet.protocolVersion);
            if(codec == null){
                ctx.writeAndFlush(new PacketCloseConnection("Unknown protocol version")).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            ctx.writeAndFlush(new PacketServerHandshake(NetworkServer.PROTOCOL_VERSION));
            ctx.pipeline().addAfter(ctx.name(), "packetCodec", codec);
            ctx.pipeline().remove(ctx.name());
            ctx.pipeline().remove("handshakeCodec");
            ClientConnection connection = new ClientConnection(packet.protocolVersion, packet.username, ctx.channel());
            ctx.channel().attr(NetworkServer.clientConnection).set(connection);
            NetworkServer.onConnectionOpened(connection);
        }else if(msg instanceof PacketCloseConnection){
            System.out.println("Client closed connection. Reason: " + ((PacketCloseConnection) msg).reason);
            ctx.close();
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
