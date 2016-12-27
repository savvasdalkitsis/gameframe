package com.savvasdalkitsis.gameframe.composition.model;

import android.graphics.Color;

public class ARGB {
    public final int a;
    public final int r;
    public final int g;
    public final int b;

    public ARGB(int color) {
        a = Color.alpha(color);
        r = Color.red(color);
        g = Color.green(color);
        b = Color.blue(color);
    }

    public ARGB(int a, int r, int g, int b) {
        this.a = clip(a);
        this.r = clip(r);
        this.g = clip(g);
        this.b = clip(b);
    }

    public ARGB multiply(float m) {
        return new ARGB((int) (a * m), (int)(r * m), (int)(g * m), (int)(b * m));
    }

    public ARGB add(ARGB argb) {
        return new ARGB(a + argb.a, r + argb.r, g + argb.g, b + argb.b);
    }

    private int clip(int value) {
        return Math.max(0, Math.min(value, 255));
    }

    public int color() {
        return Color.argb(a, r, g, b);
    }

    public ARGB multiplyAlpha(float alpha) {
        return new ARGB((int) (a * alpha), r, g, b);
    }
}
