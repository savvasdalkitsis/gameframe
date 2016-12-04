package com.savvasdalkitsis.gameframe.infra.view;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.andrewlord1990.snackbarbuilder.SnackbarBuilder;
import com.savvasdalkitsis.gameframe.R;

public class Snackbars {

    public static Snackbar success(View view, int message) {
        return new SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .backgroundColorRes(R.color.success)
                .build();
    }

    public static Snackbar error(View view, int message) {
        return new SnackbarBuilder(view)
                .message(message)
                .duration(Snackbar.LENGTH_LONG)
                .backgroundColorRes(R.color.error)
                .build();
    }
}
