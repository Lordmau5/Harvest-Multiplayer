package com.lordmau5.harvest.network.codec;

import java.util.HashMap;
import java.util.Map;

/**
 * No description given
 *
 * @author jk-5
 */
public class CodecRegistry {
    private static final Map<Integer, Class<? extends PacketCodec>> versionCodec = new HashMap<Integer, Class<? extends PacketCodec>>();

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
