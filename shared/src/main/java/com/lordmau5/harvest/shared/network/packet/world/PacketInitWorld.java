package com.lordmau5.harvest.shared.network.packet.world;

import com.lordmau5.harvest.shared.Tile;
import com.lordmau5.harvest.shared.World;
import com.lordmau5.harvest.shared.farmable.crops.CropRegistry;
import com.lordmau5.harvest.shared.farmable.crops.CropState;
import com.lordmau5.harvest.shared.farmable.crops.ICrop;
import com.lordmau5.harvest.shared.floor.Farmland;
import com.lordmau5.harvest.shared.network.ConnectionContext;
import com.lordmau5.harvest.shared.network.PacketUtils;
import com.lordmau5.harvest.shared.network.packet.Packet;
import com.lordmau5.harvest.shared.objects.Entity;
import io.netty.buffer.ByteBuf;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Lordmau5
 * @time: 19.06.2014 - 18:55
 */
public class PacketInitWorld extends Packet {

    public Map<Tile, Entity> objects = new HashMap<>();
    public Map<Tile, Farmland> farmLand = new HashMap<>();

    public PacketInitWorld() {

    }

    public PacketInitWorld(Map<Tile, Entity> objects, Map<Tile, Farmland> farmLand) {
        this.objects = objects;
        this.farmLand = farmLand;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(this.objects.size());
        for(Map.Entry<Tile, Entity> entry : this.objects.entrySet()) {
            Tile tile = entry.getKey();
            buffer.writeInt(tile.getX());
            buffer.writeInt(tile.getY());

            Entity entity = entry.getValue();
            PacketUtils.writeString(entity.name, buffer);

            Shape boundingBox = entity.getBoundingBox();
            buffer.writeFloat(boundingBox.getX());
            buffer.writeFloat(boundingBox.getY());
            buffer.writeFloat(boundingBox.getWidth());
            buffer.writeFloat(boundingBox.getHeight());
        }

        buffer.writeInt(this.farmLand.size());
        for(Map.Entry<Tile, Farmland> entry : this.farmLand.entrySet()) {
            Tile tile = entry.getKey();
            buffer.writeInt(tile.getX());
            buffer.writeInt(tile.getY());

            Farmland land = entry.getValue();
            buffer.writeBoolean(land.isTilled());
            buffer.writeBoolean(land.isWatered());

            buffer.writeBoolean(land.hasCrop());
            if(land.hasCrop()) {
                ICrop crop = land.getCrop();

                PacketUtils.writeString(crop.getSeeds().getSeedName(), buffer);
                buffer.writeInt(crop.getCropState().ordinal());
                buffer.writeInt(crop.getDaysUnwatered());
            }
        }
    }

    @Override
    public void decode(ByteBuf buffer) {
        int size = buffer.readInt();
        for(int i=0; i<size; i++) {
            int tX = buffer.readInt();
            int tY = buffer.readInt();

            Tile tile = new Tile(tX, tY);
            Entity entity = World.entities.get(PacketUtils.readString(buffer)).newInstance();
            entity.updatePos(tile.getX(), tile.getY());

            Shape boundingBox = new Rectangle(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
            entity.setBoundingBox(boundingBox);

            this.objects.put(tile, entity);
        }

        size = buffer.readInt();
        for(int i=0; i<size; i++) {
            int tX = buffer.readInt();
            int tY = buffer.readInt();

            Tile tile = new Tile(tX, tY);

            Farmland land = new Farmland(tX, tY);
            land.setTilled(buffer.readBoolean());
            land.setWatered(buffer.readBoolean());

            boolean hasCrop = buffer.readBoolean();
            if(hasCrop) {
                ICrop crop = CropRegistry.buildCrop(CropRegistry.getCrop(PacketUtils.readString(buffer)));

                crop.setCropState(CropState.values()[buffer.readInt()]);
                crop.setDaysUnwatered(buffer.readInt());
            }
            this.farmLand.put(tile, land);
        }
    }

    @Override
    public void process(ConnectionContext ctx) {

    }
}
