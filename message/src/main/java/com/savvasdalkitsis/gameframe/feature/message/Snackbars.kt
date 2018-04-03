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
package com.savvasdalkitsis.gameframe.feature.message

import android.graphics.Color
import android.support.annotation.StringRes
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector
import com.savvasdalkitsis.gameframe.infra.R
import com.savvasdalkitsis.gameframe.kotlin.ViewAction
import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector


object Snackbars {

    private var overridden = false
    private val topActivity get() = TopActivityProviderInjector.topActivityProvider().topActivity
    private val toastMessageDisplay = MessageDisplayInjector.toastMessageDisplay()

    fun success(view: View, @StringRes message: Int) = snack(view, message) {
        backgroundColorRes(R.color.success)
    }

    fun error(view: View, @StringRes message: Int) = snack(view, message) {
        backgroundColorRes(R.color.error)
    }

    fun actionError(view: View, @StringRes message: Int, @StringRes actionText: Int, actionClick: ViewAction) =
            snack(view, message) {
                actionText(actionText)
                        .actionClickListener(actionClick)
                        .backgroundColorRes(R.color.error)
            }

    fun topInfo(@StringRes message: Int) {
        val view = topActivity?.findViewById<View>(R.id.view_coordinator)
        if (view != null) {
            TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT).apply {
                val content = this.view
                content.setBackgroundResource(R.color.colorAccent)
                val textView = content.findViewById<TextView>(com.androidadvance.topsnackbar.R.id.snackbar_text)
                textView.setTextColor(Color.WHITE)
            }.show()
        } else {
            toastMessageDisplay.show(message)
        }
    }

    private fun snack(view: View, message: Int, build: SnackbarBuilder.() -> SnackbarBuilder) {
        SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .apply { build(this) }
                .build()
                .setId()
                .overrideAnimationForAccessibility()
                .show()
    }

    private fun Snackbar.setId(): Snackbar {
        view.id = R.id.snackbar
        return this
    }

    // https://stackoverflow.com/a/43811447/56242
    private fun Snackbar.overrideAnimationForAccessibility(): Snackbar {
        if (overridden) return this
        try {
            val accessibilityManagerField = BaseTransientBottomBar::class.java.getDeclaredField("mAccessibilityManager")
            accessibilityManagerField.isAccessible = true
            val accessibilityManager = accessibilityManagerField.get(this)
            val isEnabledField = AccessibilityManager::class.java.getDeclaredField("mIsEnabled")
            isEnabledField.isAccessible = true
            isEnabledField.setBoolean(accessibilityManager, false)
            accessibilityManagerField.set(this, accessibilityManager)
            overridden = true
        } catch (e: Exception) {
            Log.d("Snackbar", "Reflection error: $e")
        }
        return this
    }
}
