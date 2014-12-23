package com.lordmau5.harvest.shared.serialization;

public interface ISerializerFactory {
    ISerializer getSerializer(Class clazz);

    <T> ISerializer<T> getTypedSerializer(Class clazz);
}
