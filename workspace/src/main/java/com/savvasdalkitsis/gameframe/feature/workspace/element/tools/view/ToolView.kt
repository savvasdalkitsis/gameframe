/**
 * Copyright 2017 Savvas Dalkitsis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 'Game Frame' is a registered trademark of LEDSEQ
 */
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
