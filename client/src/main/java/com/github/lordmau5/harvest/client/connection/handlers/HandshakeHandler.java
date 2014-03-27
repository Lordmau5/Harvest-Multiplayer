package com.github.lordmau5.harvest.client.connection.handlers;

import com.github.lordmau5.harvest.client.Client;
import com.github.lordmau5.harvest.client.connection.NetworkHandler;
import com.github.lordmau5.harvest.client.connection.ServerConnection;
import com.github.lordmau5.harvest.network.codec.CodecRegistry;
import com.github.lordmau5.harvest.network.codec.PacketCodec;
import com.github.lordmau5.harvest.network.packet.handshake.PacketClientHandshake;
import com.github.lordmau5.harvest.network.packet.handshake.PacketCloseConnection;
import com.github.lordmau5.harvest.network.packet.handshake.PacketServerHandshake;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * No description given
 *
 * @author jk-5
 */
public class HandshakeHandler extends ChannelInboundHandlerAdapter {

    private final String host;
    private final int port;

    public HandshakeHandler(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(new PacketClientHandshake(this.host, this.port, NetworkHandler.PROTOCOL_VERSION, Client.playerName));
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if(msg instanceof PacketCloseConnection){
            System.out.println("Server connection closed: " + ((PacketCloseConnection) msg).reason);
        }else if(msg instanceof PacketServerHandshake){
            PacketServerHandshake packet = (PacketServerHandshake) msg;
            PacketCodec codec = CodecRegistry.getCodecForVersion(packet.protocolVersion);
            if(codec == null){
                ctx.writeAndFlush(new PacketCloseConnection("Unknown protocol version")).addListener(ChannelFutureListener.CLOSE);
                return;
            }
            ctx.pipeline().addAfter(ctx.name(), "packetCodec", codec);
            ctx.pipeline().remove(ctx.name());
            ctx.pipeline().remove("handshakeCodec");
            NetworkHandler.connection = new ServerConnection(ctx.channel());
            NetworkHandler.onHandshakeComplete();
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
