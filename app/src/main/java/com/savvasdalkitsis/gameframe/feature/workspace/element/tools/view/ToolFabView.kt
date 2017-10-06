package com.savvasdalkitsis.gameframe.feature.workspace.element.tools.view

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.DrawingTool
import com.savvasdalkitsis.gameframe.feature.workspace.element.tools.model.Tools

class ToolFabView : FloatingActionButton {

    var drawingTool: DrawingTool? = null
        private set

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        size = SIZE_MINI
    }

    fun bind(tool: Tools) {
        this.drawingTool = tool.tool
        setImageResource(tool.iconLight)
    }
}