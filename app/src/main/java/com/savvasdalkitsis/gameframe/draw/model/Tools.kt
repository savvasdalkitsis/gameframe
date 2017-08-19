package com.savvasdalkitsis.gameframe.draw.model

import android.support.annotation.DrawableRes

import com.savvasdalkitsis.gameframe.R

enum class Tools(val tool: DrawingTool, val label: String, @param:DrawableRes val icon: Int) {

    PENCIL(PencilTool(), "Pencil", R.drawable.ic_create_black_48px),
    FILL(FillTool(), "Fill", R.drawable.ic_format_color_fill_black_48px),
    CLEAR(ClearTool(), "Clear", R.drawable.ic_format_paint_black_48px),
    ERASER(EraseTool(), "Eraser", R.drawable.ic_eraser_variant),
    MOVE(MoveTool(), "Move", R.drawable.ic_open_with_black_48px),
    RECTANGLE(RectangleTool(), "Rectangle", R.drawable.ic_crop_din_black_48px),
    FILL_RECTANGLE(FillRectangleTool(), "Fill Rectangle", R.drawable.ic_rect_black_48px),
    OVAL(OvalTool(), "Oval", R.drawable.ic_panorama_fish_eye_black_48px),
    FILL_OVAL(FillOvalTool(), "Fill Oval", R.drawable.ic_disk_black_48px),
    LINE(LineTool(), "Line", R.drawable.ic_alias_line_black_48px),
    ANTIALIAS_LINE(AntialiasLineTool(), "Antialias Line", R.drawable.ic_line_black_48px);


    companion object {
        fun defaultTool() = PENCIL
    }
}
