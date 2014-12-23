package com.lordmau5.harvest.shared.net.serialization;

import com.lordmau5.harvest.shared.net.SerializableBase;

// TODO input probably will be changed to a stream
public abstract class SerializerBase<T extends SerializableBase> {
    public abstract T deserialize(byte[] input);

    public abstract byte[] serialize(T input);
}
