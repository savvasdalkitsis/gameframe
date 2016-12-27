package com.savvasdalkitsis.gameframe.composition.model;

import rx.functions.Func2;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

/**
 * Implementing blending modes described at https://www.w3.org/TR/compositing-1/
 */
public enum AvailableBlendMode implements BlendMode {

    NORMAL("normal", (source, dest) -> source),

    MULTIPLY("multiply", (source, dest) -> source * dest),

    SCREEN("screen", (source, dest) -> dest + source - (dest * source)),

    HARD_LIGHT("hardLight", (source, dest) -> {
        if (source <= 0.5) {
            return MULTIPLY.blendComponent.call(2 * source, dest);
        } else {
            return SCREEN.blendComponent.call(2 * source - 1, dest);
        }
    }),

    OVERLAY("overlay", (source, dest) -> HARD_LIGHT.blendComponent.call(dest, source)),

    DARKEN("darken", Math::min),

    LIGHTEN("lighten", Math::max),

    COLOR_DOGE("colorDodge", (source, dest) -> {
        if (dest == 0) {
            return 0f;
        } else if (source == 1) {
            return 1f;
        }
        return min(1, dest / (1 - source));
    }),

    COLOR_BURN("colorBurn", (source, dest) -> {
        if (dest == 1) {
            return 1f;
        } else if (source == 0) {
            return 0f;
        }
        return 1 - min(1, (1 - dest) / source);
    }),

    SOFT_LIGHT("softLight", (source, dest) -> {
        if (source <= 0.5) {
            return dest - (1 - 2 * source) * dest * (1 - dest);
        } else {
            return dest + (2 * source - 1) * (d(dest) - dest);
        }
    }),

    DIFFERENCE("difference", (source, dest) -> abs(dest - source)),

    EXCLUSION("exclusion", (source, dest) -> dest + source - 2 * dest * source);

    private String key;
    private Func2<Float, Float, Float> blendComponent;

    AvailableBlendMode(String key, Func2<Float, Float, Float> blendComponent) {
        this.key = key;
        this.blendComponent = blendComponent;
    }

    @Override
    public ARGB blend(ARGB source, ARGB dest) {
        return new ARGB(
                blendWith(blendComponent, source.a, dest.a),
                blendWith(blendComponent, source.r, dest.r),
                blendWith(blendComponent, source.g, dest.g),
                blendWith(blendComponent, source.b, dest.b)
        );
    }

    public static AvailableBlendMode defaultMode() {
        return NORMAL;
    }

    public static AvailableBlendMode from(String key) {
        for (AvailableBlendMode availableBlendMode : values()) {
            if (availableBlendMode.key.equals(key)) {
                return availableBlendMode;
            }
        }
        return defaultMode();
    }

    public static int indexOf(BlendMode blendMode) {
        AvailableBlendMode[] values = values();
        for (int i = 0; i < values.length; i++) {
            AvailableBlendMode mode = values[i];
            if (mode == blendMode) {
                return i;
            }
        }
        return indexOf(defaultMode());
    }

    private static int blendWith(Func2<Float, Float, Float> blendComponent, int source, int dest) {
        return toRGB(blendComponent.call(toFloat(source), toFloat(dest)));
    }

    static int toRGB(float inRange) {
        return (int) (inRange * 255);
    }

    static float d(float value) {
        if (value <= 0.25) {
            return ((16 * value - 12) * value + 4) * value;
        } else {
            return (float) sqrt(value);
        }
    }

    private static float toFloat(int inBytes) {
        return inBytes / 255f;
    }
}
