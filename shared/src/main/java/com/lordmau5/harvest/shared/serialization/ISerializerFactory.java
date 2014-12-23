package com.lordmau5.harvest.shared.serialization;

public interface ISerializerFactory {
    public ISerializer getSerializer(Class clazz);

    public <T> ISerializer<T> getTypedSerializer(Class clazz);
}
