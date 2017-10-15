package com.savvasdalkitsis.gameframe.infra.kotlin

import android.view.View

fun <T: View> T.visible() {
    this.visibility = View.VISIBLE
}

fun <T: View> T.visibleOrGone(visible: Boolean) = if (visible) visible() else gone()

fun <T: View> T.gone() {
    this.visibility = View.GONE
}