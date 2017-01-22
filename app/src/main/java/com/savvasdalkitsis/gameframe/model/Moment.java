package com.savvasdalkitsis.gameframe.model;

public interface Moment<T> {

    T replicateMoment();

    boolean isIdenticalTo(T moment);
}
