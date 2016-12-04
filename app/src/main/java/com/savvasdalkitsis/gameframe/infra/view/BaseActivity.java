package com.savvasdalkitsis.gameframe.infra.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.savvasdalkitsis.gameframe.R;
import com.shazam.android.aspects.base.activity.AspectAppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AspectAppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SpannableString s = new SpannableString(getTitle());
        s.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.toolbar_text_size)),
                0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
