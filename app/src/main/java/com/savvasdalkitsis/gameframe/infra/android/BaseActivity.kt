package com.savvasdalkitsis.gameframe.infra.android

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import butterknife.ButterKnife
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers

import com.savvasdalkitsis.gameframe.R
import io.fabric.sdk.android.Fabric

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics(), Answers())
        setContentView(layoutId)
        ButterKnife.bind(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        title = SpannableString(title).apply {
            setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.toolbar_text_size)),
                    0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
