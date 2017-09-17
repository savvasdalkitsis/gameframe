package com.savvasdalkitsis.gameframe.infra.android

import android.support.annotation.StringRes
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityManager
import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.infra.kotlin.ViewAction

object Snackbars {

    private var overridden = false

    fun success(view: View, @StringRes message: Int): Snackbar = snack(view, message) {
        backgroundColorRes(R.color.success)
    }

    fun error(view: View, @StringRes message: Int): Snackbar = snack(view, message) {
        backgroundColorRes(R.color.error)
    }

    fun actionError(view: View, @StringRes message: Int, @StringRes actionText: Int, actionClick: ViewAction): Snackbar =
            snack(view, message) {
                actionText(actionText)
                        .actionClickListener(actionClick)
                        .backgroundColorRes(R.color.error)
            }

    private fun snack(view: View, message: Int, build: SnackbarBuilder.() -> SnackbarBuilder): Snackbar {
        return SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .apply { build(this) }
                .build()
                .setId()
                .overrideAnimationForAccessibility()
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
