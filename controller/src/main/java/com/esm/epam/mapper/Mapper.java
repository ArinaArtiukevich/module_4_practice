package com.esm.epam.mapper;

public interface Mapper<T, V> {
    V mapEntity(T t);
}
