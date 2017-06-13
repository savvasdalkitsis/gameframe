package com.savvasdalkitsis.gameframe.draw.model;

import android.support.annotation.DrawableRes;

import com.savvasdalkitsis.gameframe.R;

public enum Tools {

    PENCIL(new PencilTool(), "Pencil", R.drawable.ic_create_black_48px),
    FILL(new FillTool(), "Fill", R.drawable.ic_format_color_fill_black_48px),
    CLEAR(new ClearTool(), "Clear", R.drawable.ic_format_paint_black_48px),
    ERASER(new EraseTool(), "Eraser", R.drawable.ic_eraser_variant),
    MOVE(new MoveTool(), "Move", R.drawable.ic_open_with_black_48px),
    RECTANGLE(new RectangleTool(), "Rectangle", R.drawable.ic_crop_din_black_48px),
    FILL_RECTANGLE(new FillRectangleTool(), "Fill Rectangle", R.drawable.ic_rect_black_48px),
    OVAL(new OvalTool(), "Oval", R.drawable.ic_panorama_fish_eye_black_48px),
    FILL_OVAL(new FillOvalTool(), "Fill Oval", R.drawable.ic_disk_black_48px),
    LINE(new LineTool(), "Line", R.drawable.ic_alias_line_black_48px),
    ANTIALIAS_LINE(new AntialiasLineTool(), "Antialias Line", R.drawable.ic_line_black_48px);


    private final DrawingTool tool;
    private final String name;
    private final int icon;

    Tools(DrawingTool tool, String name, @DrawableRes int icon) {
        this.tool = tool;
        this.name = name;
        this.icon = icon;
    }

    public DrawingTool getTool() {
        return tool;
    }

    public String getName() {
        return name;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }

    public static Tools defaultTool() {
        return PENCIL;
    }
}
