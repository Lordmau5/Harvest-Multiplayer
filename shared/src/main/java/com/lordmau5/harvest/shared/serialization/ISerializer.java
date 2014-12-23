package com.lordmau5.harvest.shared.serialization;

import io.netty.buffer.ByteBuf;

import java.io.OutputStream;

public interface ISerializer<T> {
    public T deserialize(ByteBuf input) throws Exception;

    public void serialize(OutputStream stream, T input) throws Exception;

    public Class getSerializeClass();
}
