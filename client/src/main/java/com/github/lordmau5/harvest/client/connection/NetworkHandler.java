package com.github.lordmau5.harvest.client.connection;

import com.github.lordmau5.harvest.client.Client;
import com.github.lordmau5.harvest.network.packet.play.ExamplePlayPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * No description given
 *
 * @author jk-5
 */
public class NetworkHandler {

    private static EventLoopGroup worker = new NioEventLoopGroup();
    private static Channel channel;
    private static boolean connected = false;

    public static ServerConnection connection;

    public static final byte PROTOCOL_VERSION = 1;

    public static void connect(String host, int port){
        Bootstrap b = new Bootstrap().group(worker).channel(NioSocketChannel.class).handler(new Initializer(host, port));
        channel = b.connect(host, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception{
                System.out.println("Connected to server!");
                Client.setConnectableState(false);
                connected = true;
            }
        }).channel();
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception{
                System.out.println("Channel closed!");
                Client.setConnectableState(true);
                connected = false;
            }
        });
    }

    /**
     * This method is fired when the player logs in
     */
    public static void onHandshakeComplete(){
        sendPacket(new ExamplePlayPacket());
    }

    public static void sendPacket(Object packet) {
        channel.writeAndFlush(packet);
    }

    public static boolean isConnected(){
        return connected;
    }
}
