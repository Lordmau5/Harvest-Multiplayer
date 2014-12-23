package com.lordmau5.harvest.shared.serialization.serializer;

import com.lordmau5.harvest.shared.serialization.ISerializer;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;

public class IntegerSerializer implements ISerializer<Integer> {
    @Override
    public Integer deserialize(ByteBuf input) {
        int a = input.readByte() & 0xFF;
        int b = input.readByte() & 0xFF;
        int c = input.readByte() & 0xFF;
        int d = input.readByte() & 0xFF;

        return a + (b << 8) + (c << 16) + (d << 24);
    }

    @Override
    public void serialize(OutputStream stream, Integer input) throws IOException {
        stream.write((byte) (input & 0xFF));
        stream.write((byte) ((input >> 8) & 0xFF));
        stream.write((byte) ((input >> 16) & 0xFF));
        stream.write((byte) ((input >> 24) & 0xFF));
    }

    @Override
    public Class getSerializeClass() {
        return Integer.class;
    }
}
