package com.savvasdalkitsis.gameframe.composition.model;

import rx.functions.Func1;

/**
 * Implementing Porter Duff operators described at https://www.w3.org/TR/compositing-1/
 */
public enum AvailablePorterDuffOperator implements PorterDuffOperator {

    CLEAR("clear", a -> 0f, a -> 0f),
    COPY("copy", a -> 1f, a -> 0f),
    DESTINATION("destination", a -> 0f, a -> 1f),
    SOURCE_OVER("source_over", a -> 1f, a -> 1 - a),
    DESTINATION_OVER("destination_over", a -> 1 - a, a -> 1f),
    SOURCE_IN("source_in", a -> a, a -> 0f),
    DESTINATION_IN("destination_in", a -> 0f, a -> a),
    SOURCE_OUT("source_out", a -> 1 - a, a -> 0f),
    DESTINATION_OUT("destination_out", a -> 0f, a -> 1 - a),
    SOURCE_ATOP("source_atop", a -> a, a -> 1 - a),
    DESTINATION_ATOP("destination_atop", a -> 1 - a, a -> a),
    XOR("xor", a -> 1 - a, a -> 1 - a),
    LIGHTER("lighter", a -> 1f, a -> 1f);

    private String key;
    private final Func1<Float, Float> fa;
    private final Func1<Float, Float> fb;

    AvailablePorterDuffOperator(String key, Func1<Float, Float> fa, Func1<Float, Float> fb) {
        this.key = key;
        this.fa = fa;
        this.fb = fb;
    }

    @Override
    public float fa(ARGB argb) {
        return fa.call(toFloat(argb.a));
    }

    @Override
    public float fb(ARGB argb) {
        return fb.call(toFloat(argb.a));
    }

    public static int indexOf(PorterDuffOperator porterDuffOperator) {
        AvailablePorterDuffOperator[] values = values();
        for (int i = 0; i < values.length; i++) {
            AvailablePorterDuffOperator mode = values[i];
            if (mode == porterDuffOperator) {
                return i;
            }
        }
        return indexOf(defaultOperator());
    }

    public static PorterDuffOperator defaultOperator() {
        return SOURCE_OVER;
    }

    private static float toFloat(int inBytes) {
        return inBytes / 255f;
    }
}
