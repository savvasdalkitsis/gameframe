package com.savvasdalkitsis.gameframe.math;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MathExtras {

    public static int clip(int from, int value, int to) {
        return max(from, min(value, to));
    }
}
