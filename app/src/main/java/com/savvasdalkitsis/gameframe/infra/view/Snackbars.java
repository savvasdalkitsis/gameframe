package com.savvasdalkitsis.gameframe.infra.view;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.savvasdalkitsis.gameframe.R;

public class Snackbars {

    public static Snackbar success(View view, @StringRes int message) {
        return new SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .backgroundColorRes(R.color.success)
                .build();
    }

    public static Snackbar error(View view, @StringRes int message) {
        return new SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .backgroundColorRes(R.color.error)
                .build();
    }

    public static Snackbar actionError(View view, @StringRes int message, @StringRes int actionText, View.OnClickListener actionClick) {
        return new SnackbarBuilder(view)
                .message(message)
                .actionText(actionText)
                .actionClickListener(actionClick)
                .duration(Snackbar.LENGTH_LONG)
                .backgroundColorRes(R.color.error)
                .build();

    }
}
