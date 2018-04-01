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
package io.github.yavski.fabspeeddial

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.savvasdalkitsis.gameframe.infra.R
import com.savvasdalkitsis.gameframe.kotlin.Action

class CustomFabSpeedDial(context: Context, attrs: AttributeSet): FabSpeedDial(context, attrs) {

    private val animations: MutableSet<ViewPropertyAnimatorCompat> = HashSet()

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
                    }.apply {
                animations.add(this)
                start()
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
                }.apply {
            animations.add(this)
            start()
        }
    }

    fun stopProgress(@DrawableRes restingDrawableId: Int) {
        animations.forEach { it.cancel() }
        animations.clear()
        fab.clearAnimation()
        fab.animate().rotation(0f).withEndAction {
            setImageResource(restingDrawableId)
        }
    }
}
