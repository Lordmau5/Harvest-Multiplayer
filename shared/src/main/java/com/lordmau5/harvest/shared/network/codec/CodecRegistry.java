package com.lordmau5.harvest.shared.network.codec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Lordmau5
 * @time: 18.06.2014 - 15:34
 */
public class CodecRegistry {
    private static final Map<Integer, Class<? extends PacketCodec>> versionCodec = new HashMap<>();

    static {
        versionCodec.put(1, PlayCodecV1.class);
    }

    public static PacketCodec getCodecForVersion(int version) {
        try {
            return versionCodec.get(version).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}