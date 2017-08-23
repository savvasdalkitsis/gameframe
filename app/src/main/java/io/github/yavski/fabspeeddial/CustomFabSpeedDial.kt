package io.github.yavski.fabspeeddial

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.infra.kotlin.Action

class CustomFabSpeedDial(context: Context, attrs: AttributeSet): FabSpeedDial(context, attrs) {

    fun setImageResource(resId: Int, scale: Boolean = true, endAction: Action? = null) {
        if (scale) {
            ViewCompat.animate(fab)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(50)
                    .withLayer()
                    .withEndAction {
                        setImageResource(resId, false)
                        ViewCompat.animate(fab)
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(50)
                                .withLayer()
                                .withEndAction { endAction?.invoke() }
                    }
        } else {
            fab.setImageResource(resId)
            endAction?.invoke()
        }
    }

    fun startProgress() {
        startProgress(true)
    }

    private fun startProgress(scale: Boolean) {
        setImageResource(R.drawable.ic_settings_backup_restore_white_48px, scale) {
            rotate()
        }
    }

    private fun rotate() {
        ViewCompat.animate(fab)
                .rotation(-360f)
                .withLayer()
                .setDuration(500)
                .setInterpolator(LinearInterpolator())
                .withEndAction {
                    fab.rotation = 0f
                    startProgress(false)
                }
                .start()
    }

    fun stopProgress(@DrawableRes restingDrawableId: Int) {
        fab.clearAnimation()
        fab.animate().rotation(0f).withEndAction {
            setImageResource(restingDrawableId)
        }
    }
}
