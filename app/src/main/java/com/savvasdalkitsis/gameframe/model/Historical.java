package com.savvasdalkitsis.gameframe.model;

import java.util.Deque;
import java.util.LinkedList;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class Historical<T extends Moment<T>> {

    private final Deque<T> past;
    private T present;
    private final Deque<T> future;
    private BehaviorSubject<T> subject = BehaviorSubject.create();

    public Historical(T epoch) {
        past = new LinkedList<>();
        present = epoch;
        future = new LinkedList<>();
        announcePresent();
    }

    public Observable<T> observe() {
        return subject;
    }

    public void progressTimeWithoutAnnouncing() {
        past.add(present().replicateMoment());
        future.clear();
    }

    public void progressTime() {
        progressTimeWithoutAnnouncing();
        announcePresent();
    }

    public void stepBackInTime() {
        if (hasPast()) {
            future.add(present);
            present = past.removeLast();
            announcePresent();
        }
    }

    public void stepForwardInTime() {
        if (hasFuture()) {
            past.add(present);
            present = future.removeLast();
            announcePresent();
        }
    }

    public void announcePresent() {
        subject.onNext(present());
    }

    public boolean hasPast() {
        return !past.isEmpty();
    }

    public boolean hasFuture() {
        return !future.isEmpty();
    }

    public T present() {
        return present;
    }

    public void collapsePresentWithPastIfTheSame() {
        if (hasPast()) {
            if (present().isIdenticalTo(past.peekLast())) {
                forgetPresent();
            }
        }
    }

    private void forgetPresent() {
        if (hasPast()) {
            present = past.removeLast();
            announcePresent();
        }
    }
}
