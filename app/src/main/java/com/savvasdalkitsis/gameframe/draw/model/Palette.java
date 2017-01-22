package com.savvasdalkitsis.gameframe.draw.model;

import com.savvasdalkitsis.gameframe.model.Moment;

import java.util.Arrays;

import lombok.Getter;
import lombok.experimental.Builder;

@Builder
@Getter
public class Palette implements Moment<Palette> {

    private String title;
    private boolean isSelected;
    private int[] colors;

    public void changeColor(int index, int color) {
        colors[index] = color;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    @Override
    public Palette replicateMoment() {
        int[] colorsCopy = copy(colors);
        return builder()
                .colors(colorsCopy)
                .isSelected(isSelected)
                .title(title)
                .build();
    }

    @Override
    public boolean isIdenticalTo(Palette moment) {
        return isSelected == moment.isSelected
                && title.equals(moment.title)
                && Arrays.equals(colors, moment.colors);
    }

    public static PaletteBuilder from(Palette palette) {
        return builder()
                .title(palette.title)
                .isSelected(palette.isSelected)
                .colors(copy(palette.colors));
    }

    private static int[] copy(int[] colors) {
        int[] colorsCopy = new int[colors.length];
        System.arraycopy(colors, 0, colorsCopy, 0,  colors.length);
        return colorsCopy;
    }
}
