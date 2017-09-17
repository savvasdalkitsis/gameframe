package com.savvasdalkitsis.gameframe.infra.android;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.savvasdalkitsis.gameframe.R;

/**
 * Loosely based on https://medium.com/@nullthemall/floatingactionmenu-floatingactionbutton-behavior-dd33cc0d9ba7
 */
@SuppressWarnings("unused")
public class RespectSnackbarBehavior extends CoordinatorLayout.Behavior<ViewGroup> {

    @SuppressWarnings("unused")
    RespectSnackbarBehavior() {
        super();
    }

    @SuppressWarnings("unused")
    public RespectSnackbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        View snackbar = findSnackbar(dependency);
        if (snackbar != null) {
            updateTranslationForSnackbar(child, snackbar);
        } else {
            child.setTranslationY(0);

        }
        return false;
    }

    private void updateTranslationForSnackbar(ViewGroup child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
    }

    private View findSnackbar(View dependency) {
        return dependency.getRootView().findViewById(R.id.snackbar);
    }
}
