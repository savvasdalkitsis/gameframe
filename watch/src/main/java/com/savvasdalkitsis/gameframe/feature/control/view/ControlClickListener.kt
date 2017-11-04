package com.savvasdalkitsis.gameframe.feature.control.view

import com.savvasdalkitsis.gameframe.feature.control.model.ControlModel

interface ControlClickListener {

    fun onControlClicked(controlModel: ControlModel)
}