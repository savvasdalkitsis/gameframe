package com.savvasdalkitsis.gameframe.feature.control.view

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import com.savvasdalkitsis.gameframe.feature.control.model.ControlModel

data class ControlViewModel(@get:DrawableRes val layoutId: Int,
                            @get:StringRes val title: Int,
                            val controlModel: ControlModel
)