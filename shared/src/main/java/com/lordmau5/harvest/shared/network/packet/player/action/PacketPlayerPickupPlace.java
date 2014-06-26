package com.lordmau5.harvest.shared.network.packet.player.action;

import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.PacketUtils;
import com.lordmau5.harvest.shared.objects.Entity;
import io.netty.buffer.ByteBuf;

/**
 * @author: Lordmau5
 * @time: 19.06.2014 - 11:28
 */
public class PacketPlayerPickupPlace extends PacketPlayerAction {

    public String username;
    public Tile tile;
    public boolean placeDown = false;
    public String entityName;

    public PacketPlayerPickupPlace() {}

    public PacketPlayerPickupPlace(String username, Tile tile, Entity entity) {
        this.username = username;
        this.tile = tile;
        this.placeDown = false;
        if(entity != null) {
            this.placeDown = true;
            this.entityName = entity.name;
        }
    }

    @Override
    public void encode(ByteBuf buffer) {
        PacketUtils.writeString(this.username, buffer);
        buffer.writeInt(this.tile.getX());
        buffer.writeInt(this.tile.getY());
        buffer.writeBoolean(this.placeDown);
        if(this.placeDown && !entityName.isEmpty())
            PacketUtils.writeString(this.entityName, buffer);
    }

    @Override
    public void decode(ByteBuf buffer) {
        this.username = PacketUtils.readString(buffer);
        this.tile = new Tile(buffer.readInt(), buffer.readInt());
        this.placeDown = buffer.readBoolean();
        if(this.placeDown)
            this.entityName = PacketUtils.readString(buffer);
    }

    @Override
    public void process(ConnectionContext ctx) {

    }

}
