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
package com.savvasdalkitsis.gameframe.infra.android

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.MenuItem
import com.savvasdalkitsis.gameframe.infra.R
import com.savvasdalkitsis.gameframe.infra.base.BasePresenter
import com.savvasdalkitsis.gameframe.infra.base.BaseView
import com.savvasdalkitsis.gameframe.infra.injector.InfrastructureInjector
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity<V: BaseView, out P: BasePresenter<V>> : AppCompatActivity() {

    private val navigator = InfrastructureInjector.feedbackNavigator()
    abstract val layoutId: Int
    abstract val presenter: P
    abstract val view: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        presenter.bindView(view)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        title = SpannableString(title).apply {
            setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.toolbar_text_size)),
                    0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(view)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.action_feedback -> {
            navigator.navigateToFeedback()
            true
        }
        else -> false
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
