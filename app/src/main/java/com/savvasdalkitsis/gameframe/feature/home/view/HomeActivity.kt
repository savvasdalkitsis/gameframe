package com.savvasdalkitsis.gameframe.feature.home.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.ip.model.IpAddress
import com.savvasdalkitsis.gameframe.infra.android.BaseActivity
import com.savvasdalkitsis.gameframe.infra.android.FragmentSelectedListener
import com.savvasdalkitsis.gameframe.injector.feature.navigation.NavigatorInjector
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), HomeView, ColorChooserDialog.ColorCallback {

    private val presenter = PresenterInjector.mainPresenter()
    private val navigator = NavigatorInjector.navigator()

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        bottom_navigation.setOnNavigationItemSelectedListener(this::onOptionsItemSelected)
        notifyFragmentSelected(R.id.fragment_manage)
        presenter.bindView(this)
        val actionBar = supportActionBar
        actionBar?.run {
            title = ""
            setLogo(R.drawable.ic_logo)
            setDisplayUseLogoEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadIpAddress()
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

    override fun ipAddressLoaded(ipAddress: IpAddress) {
        view_fab_control.visibility = View.VISIBLE
    }

    override fun ipCouldNotBeFound(throwable: Throwable) {
        view_fab_control.visibility = View.GONE
    }

    override fun onColorSelection(dialog: ColorChooserDialog, @ColorInt selectedColor: Int) {
        (supportFragmentManager.findFragmentById(R.id.fragment_workspace) as ColorChooserDialog.ColorCallback).onColorSelection(dialog, selectedColor)
    }
}
