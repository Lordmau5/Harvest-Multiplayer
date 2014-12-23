package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.net.packet.IPacketFactory;
import com.lordmau5.harvest.shared.net.packet.PacketBase;
import com.lordmau5.harvest.shared.serialization.ISerializer;
import com.lordmau5.harvest.shared.serialization.ISerializerFactory;
import io.netty.buffer.ByteBuf;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Iterator;

public class PacketSerializer implements ISerializer<PacketBase> {
    private final IPacketFactory packetFactory;
    private final ISerializerFactory serializerFactory;
    private final ISerializer<Integer> classIdSerializer;

    public PacketSerializer(IPacketFactory packetFactory) {
        this.packetFactory = packetFactory;
        serializerFactory = new PacketSerializerFactory();
        classIdSerializer = serializerFactory.getTypedSerializer(Integer.class);
    }

    @Override
    public PacketBase deserialize(ByteBuf input) throws Exception {
        int classId = classIdSerializer.deserialize(input);
        PacketBase packet = packetFactory.createPacket(classId);
        if (packet == null)
            return null;

        Iterator<Field> iterator = packet.getSerializableFields();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            ISerializer serializer = serializerFactory.getSerializer(field.getType());
            field.set(packet, serializer.deserialize(input));
        }

        return packet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(OutputStream stream, PacketBase input) throws Exception {
        int classId = input.getClassId();
        classIdSerializer.serialize(stream, classId);

        Iterator<Field> iterator = input.getSerializableFields();
        while (iterator.hasNext()) {
            Field field = iterator.next();
            Object value = field.get(input);
            serializerFactory.getSerializer(field.getType()).serialize(stream, value);
        }
    }

    @Override
    public Class getSerializeClass() {
        return PacketBase.class;
    }
}
