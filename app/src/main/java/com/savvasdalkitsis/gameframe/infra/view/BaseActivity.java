package com.savvasdalkitsis.gameframe.infra.view;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;

import com.shazam.android.aspects.base.activity.AspectAppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;

public class BaseActivity extends AspectAppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        SpannableString s = new SpannableString(getTitle());
        s.setSpan(new CalligraphyTypefaceSpan(Typeface.createFromAsset(getAssets(), "fonts/Pixel-Noir.ttf")),
                0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setTitle(s);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
