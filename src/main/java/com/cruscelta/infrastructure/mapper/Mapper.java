package com.cruscelta.infrastructure.mapper;

public interface Mapper<T, S> {
    T toTarget(S source);

    S toSource(T target);
}
