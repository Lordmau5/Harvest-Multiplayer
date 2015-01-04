package com.lordmau5.harvest.shared.serialization.serializer;

import com.lordmau5.harvest.shared.serialization.ISerializer;
import io.netty.buffer.ByteBuf;

import java.io.OutputStream;

public class StringSerializer implements ISerializer<String> {
    @Override
    public String deserialize(ByteBuf input) throws Exception {
        int len = input.readByte();
        if (len >= 254) {
            len = input.readByte() & 0xFF;
            len += (input.readByte() << 8) & 0xFF;
            len += (input.readByte() << 16) & 0xFF;
        }

        byte[] arr = new byte[len];
        input.readBytes(arr);
        return new String(arr);
    }

    @Override
    public void serialize(OutputStream stream, String input) throws Exception {
        byte[] arr = input.getBytes();
        if (arr.length >= 254) {
            stream.write(254);
            stream.write(arr.length & 0xFF);
            stream.write((arr.length >> 8) & 0xFF);
            stream.write((arr.length >> 16) & 0xFF);
        } else {
            stream.write(arr.length);
        }

        stream.write(arr, 0, arr.length);
    }

    @Override
    public Class getSerializeClass() {
        return String.class;
    }
}
