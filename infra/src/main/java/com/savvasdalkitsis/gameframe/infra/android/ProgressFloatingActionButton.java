/*
 * Copyright 2016.  Dmitry Malkovich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.savvasdalkitsis.gameframe.infra.android;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.savvasdalkitsis.gameframe.infra.R;

/**
 * ProgressFloatingActionButton.java
 * Created by: Dmitry Malkovich
 * <p/>
 * A circular loader is integrated with a floating action button.
 */
public class ProgressFloatingActionButton extends FrameLayout {

    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private FloatingActionButton firstFab;
    private FloatingActionButton secondFab;

    public ProgressFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() == 0 || getChildCount() > 3) {
            throw new IllegalStateException("Specify only 2 or 3 views.");
        }

        firstFab = (FloatingActionButton) getChildAt(0);
        int progressIndex = 1;
        if (getChildCount() == 3) {
            progressIndex = 2;
            secondFab = (FloatingActionButton) getChildAt(1);
        }
        mProgressBar = (ProgressBar) getChildAt(progressIndex);
        mFab = firstFab;

        LayoutParams mFabParams1 = ((LayoutParams) firstFab.getLayoutParams());
        LayoutParams mProgressParams = ((LayoutParams) mProgressBar.getLayoutParams());

        int additionSize = getResources().getDimensionPixelSize(R.dimen.progress_bar_size);
        mProgressBar.getLayoutParams().height = mFab.getHeight() + additionSize;
        mProgressBar.getLayoutParams().width = mFab.getWidth() + additionSize;

        mFabParams1.gravity = Gravity.CENTER;
        mProgressParams.gravity = Gravity.CENTER;
        if (secondFab != null) {
            LayoutParams mFabParams2 = ((LayoutParams) secondFab.getLayoutParams());
            mFabParams2.gravity = Gravity.CENTER;
        }
    }

    public void setActiveFab(int index) {
        if (index < 1) {
            mFab = firstFab;
        } else {
            mFab = secondFab;
        }
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mFab != null && mProgressBar != null) {
            LayoutParams mFabParams1 = ((LayoutParams) firstFab.getLayoutParams());
            LayoutParams mProgressParams = ((LayoutParams) mProgressBar.getLayoutParams());

            int additionSize = getResources().getDimensionPixelSize(R.dimen.progress_bar_size);
            mProgressBar.getLayoutParams().height = mFab.getHeight() + additionSize;
            mProgressBar.getLayoutParams().width = mFab.getWidth() + additionSize;

            mFabParams1.gravity = Gravity.CENTER;
            mProgressParams.gravity = Gravity.CENTER;
            if (secondFab != null) {
                LayoutParams mFabParams2 = ((LayoutParams) secondFab.getLayoutParams());
                mFabParams2.gravity = Gravity.CENTER;
            }
        }
    }

}
