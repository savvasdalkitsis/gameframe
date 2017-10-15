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
package com.savvasdalkitsis.gameframe.feature.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.infra.android.BaseActivity
import com.savvasdalkitsis.gameframe.infra.android.FragmentSelectedListener
import com.savvasdalkitsis.gameframe.injector.feature.navigation.NavigatorInjector
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), ColorChooserDialog.ColorCallback {

    private val navigator = NavigatorInjector.navigator()

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this::onOptionsItemSelected)
        notifyFragmentSelected(R.id.fragment_manage)
        val actionBar = supportActionBar
        actionBar?.run {
            title = ""
            setLogo(R.drawable.ic_logo)
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_control, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_setup_ip -> {
                navigator.navigateToIpSetup()
                notifyAllFragmentsUnselected()
                return true
            }
            R.id.action_manage -> {
                if (fragment_switcher.displayedChild != 0) {
                    fragment_switcher.displayedChild = 0
                    notifyFragmentUnselected(R.id.fragment_workspace)
                    notifyFragmentSelected(R.id.fragment_manage)
                    return true
                }
            }
            R.id.action_draw -> {
                if (fragment_switcher.displayedChild != 1) {
                    fragment_switcher.displayedChild = 1
                    notifyFragmentUnselected(R.id.fragment_manage)
                    notifyFragmentSelected(R.id.fragment_workspace)
                    return true
                }
            }
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    private fun notifyAllFragmentsUnselected() {
        supportFragmentManager.fragments
                .filterNotNull()
                .forEach { (it as FragmentSelectedListener).onFragmentUnselected() }
    }

    private fun notifyFragmentSelected(fragmentId: Int) {
        val fragment = supportFragmentManager.findFragmentById(fragmentId)
        if (fragment != null) {
            (fragment as FragmentSelectedListener).onFragmentSelected()
        }
    }

    private fun notifyFragmentUnselected(fragmentId: Int) {
        val fragment = supportFragmentManager.findFragmentById(fragmentId)
        if (fragment != null) {
            (fragment as FragmentSelectedListener).onFragmentUnselected()
        }
    }

    override fun onColorSelection(dialog: ColorChooserDialog, @ColorInt selectedColor: Int) {
        (supportFragmentManager.findFragmentById(R.id.fragment_workspace) as ColorChooserDialog.ColorCallback).onColorSelection(dialog, selectedColor)
    }
}
