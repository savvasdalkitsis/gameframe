package com.savvasdalkitsis.gameframe.draw.model;

public class Palette {

    private int[] colors;

    private Palette(Builder builder) {
        colors = builder.colors;
    }

    public int[] getColors() {
        return colors;
    }

    public static final class Builder {
        private int[] colors;

        private Builder() {
        }

        public static Builder palette() {
            return new Builder();
        }

        public Builder colors(int[] val) {
            colors = val;
            return this;
        }

        public Palette build() {
            return new Palette(this);
        }
    }
}
