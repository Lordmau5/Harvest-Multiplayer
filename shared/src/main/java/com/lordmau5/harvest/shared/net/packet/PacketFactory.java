package com.lordmau5.harvest.shared.net.packet;

import com.lordmau5.harvest.shared.serialization.annotations.AnnotationHelper;

import java.util.HashMap;

public class PacketFactory {
    HashMap<Integer, Class<? extends PacketBase>> packetHashMap;

    public PacketFactory() {
        packetHashMap = new HashMap<>();
        registerPackets();
    }

    private void registerPackets() {
        packetHashMap.clear();
        register(PacketPing.class);
    }

    public void register(Class<? extends PacketBase> packetClass) {
        packetHashMap.put(AnnotationHelper.getClassId(packetClass), packetClass);
    }

    public PacketBase createPacket(int classId) {
        Class<? extends PacketBase> clazz = packetHashMap.getOrDefault(classId, null);

        try {
            if (clazz != null)
                return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
