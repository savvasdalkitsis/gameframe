package com.savvasdalkitsis.gameframe.infra.android

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.infra.kotlin.ViewAction

object Snackbars {

    fun success(view: View, @StringRes message: Int): Snackbar = SnackbarBuilder(view)
            .message(message)
            .duration(Snackbar.LENGTH_LONG)
            .backgroundColorRes(R.color.success)
            .build()

    fun error(view: View, @StringRes message: Int): Snackbar = SnackbarBuilder(view)
            .message(message)
            .duration(Snackbar.LENGTH_LONG)
            .backgroundColorRes(R.color.error)
            .build()

    fun actionError(view: View, @StringRes message: Int, @StringRes actionText: Int, actionClick: ViewAction): Snackbar =
            SnackbarBuilder(view)
                    .message(message)
                    .actionText(actionText)
                    .actionClickListener(actionClick)
                    .duration(Snackbar.LENGTH_LONG)
                    .backgroundColorRes(R.color.error)
                    .build()
}
