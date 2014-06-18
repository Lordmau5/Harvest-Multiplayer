package com.lordmau5.harvest.shared.network;

import io.netty.channel.Channel;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:24
 */
public interface ConnectionContext {
    public Channel channel();
}
