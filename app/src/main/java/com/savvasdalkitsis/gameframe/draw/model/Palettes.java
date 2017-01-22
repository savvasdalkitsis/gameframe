package com.savvasdalkitsis.gameframe.draw.model;

import android.content.res.Resources;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.injector.ApplicationInjector;

public class Palettes {

    private static final Resources resources = ApplicationInjector.application().getResources();
    private static final int DEFAULT_INDEX = 5;

    public static Palette[] preLoaded() {
        return new Palette[]{
                palette("Empty", R.array.palette_empty),
                palette("Apple II", R.array.palette_apple_II),
                palette("Commodore 64", R.array.palette_c64),
                palette("CGA", R.array.palette_cga),
                palette("Gameboy", R.array.palette_gameboy),
                palette("MS Paint", R.array.palette_ms_paint),
                palette("MSX", R.array.palette_msx),
                palette("NES 1", R.array.palette_nes1),
                palette("NES 2", R.array.palette_nes2),
                palette("NES 3", R.array.palette_nes3),
                palette("NES 4", R.array.palette_nes4),
                palette("ZX Spectrum", R.array.palette_zx_spectrum),
        };
    }

    static Palette.PaletteBuilder defaultPalette() {
        return Palette.from(preLoaded()[DEFAULT_INDEX]);
    }

    private static Palette palette(String title, int resource) {
        return Palette.builder()
                .title(title)
                .colors(resources.getIntArray(resource))
                .build();
    }
}
