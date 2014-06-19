package com.lordmau5.harvest.shared.network.packet.player.action;

import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.network.packet.player.PacketPlayerAction;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 19.06.2014 - 11:28
 */
public class PacketPlayerPickupPlace extends PacketPlayerAction {


    public PacketPlayerPickupPlace(String username, Tile tile, boolean placeDown) {

    }

    @Override
    public void encode(ByteBuf buffer) {

    }

    @Override
    public void decode(ByteBuf buffer) {

    }

}
