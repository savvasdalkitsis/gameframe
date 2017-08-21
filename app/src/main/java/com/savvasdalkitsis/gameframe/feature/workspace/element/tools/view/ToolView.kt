package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.DrawingTool
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.Tools

class ToolView : ImageView {

    var drawingTool: DrawingTool? = null
        private set

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun bind(tool: Tools) {
        this.drawingTool = tool.tool
        setImageResource(tool.icon)
    }
}
