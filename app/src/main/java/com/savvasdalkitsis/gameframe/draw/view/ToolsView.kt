package com.savvasdalkitsis.gameframe.draw.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class ToolsView : RecyclerView {

    private lateinit var tools: ToolsAdapter

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        tools = ToolsAdapter()
        layoutManager = GridLayoutManager(context, 4)
        setHasFixedSize(true)
        adapter = tools
    }

    fun setOnToolSelectedListener(toolSelectedListener: ToolSelectedListener) {
        tools.toolSelectedListener = toolSelectedListener
    }
}