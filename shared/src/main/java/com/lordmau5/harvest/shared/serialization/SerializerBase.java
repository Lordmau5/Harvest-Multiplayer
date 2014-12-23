package com.lordmau5.harvest.shared.serialization;

// TODO input probably will be changed to a stream
public abstract class SerializerBase<T extends SerializableBase> {
    public abstract T deserialize(byte[] input);

    public abstract byte[] serialize(T input);
}
