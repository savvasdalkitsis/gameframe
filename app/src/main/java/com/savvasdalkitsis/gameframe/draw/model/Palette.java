package com.savvasdalkitsis.gameframe.draw.model;

import java.util.Arrays;

import static com.savvasdalkitsis.gameframe.draw.model.Palette.Builder.palette;

public class Palette implements Moment<Palette> {

    private int[] colors;

    private Palette(Builder builder) {
        colors = builder.colors;
    }

    public int[] getColors() {
        return colors;
    }

    @Override
    public Palette replicateMoment() {
        int[] colorsCopy = new int[colors.length];
        System.arraycopy(colors, 0, colorsCopy, 0,  colors.length);
        return palette()
                .colors(colorsCopy)
                .build();
    }

    @Override
    public boolean isIdenticalTo(Palette moment) {
        return Arrays.equals(colors, moment.colors);
    }

    public void changeColor(int index, int color) {
        colors[index] = color;
    }

    public static final class Builder {
        private int[] colors;

        private Builder() {
        }

        static Builder palette() {
            return new Builder();
        }

        Builder colors(int[] val) {
            colors = val;
            return this;
        }

        public Palette build() {
            return new Palette(this);
        }
    }
}
