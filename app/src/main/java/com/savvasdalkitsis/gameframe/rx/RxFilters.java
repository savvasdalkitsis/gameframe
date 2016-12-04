package com.savvasdalkitsis.gameframe.rx;

import rx.functions.Func1;

public class RxFilters {

    public static <T> Func1<T, Boolean> notNull() {
        return item -> item != null;
    }
}
