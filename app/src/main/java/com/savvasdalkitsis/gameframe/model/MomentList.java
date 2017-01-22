package com.savvasdalkitsis.gameframe.model;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;

import rx.Observable;

import static java.util.Collections.singletonList;

public class MomentList<M extends Moment<M>> extends ArrayList<M> implements Moment<MomentList<M>> {

    @SuppressWarnings("unused")
    public MomentList() {
    }

    @SuppressWarnings("unused")
    public MomentList(int initialCapacity) {
        super(initialCapacity);
    }

    @SuppressWarnings("WeakerAccess")
    public MomentList(Collection<? extends M> c) {
        super(c);
    }

    public MomentList(M m) {
        this(singletonList(m));
    }

    @Override
    public MomentList<M> replicateMoment() {
        return new MomentList<>(Observable.from(this)
                .map(M::replicateMoment)
                .toList()
                .toBlocking()
                .first());
    }

    @Override
    public boolean isIdenticalTo(MomentList<M> moment) {
        return size() == moment.size() &&
                Observable.zip(Observable.from(this), Observable.from(moment), Pair::create)
                        .filter(pair -> !pair.first.isIdenticalTo(pair.second)).isEmpty()
                        .toBlocking()
                        .first();
    }
}
