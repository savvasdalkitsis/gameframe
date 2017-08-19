package com.savvasdalkitsis.gameframe.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.infra.view.BaseActivity
import com.savvasdalkitsis.gameframe.infra.view.FragmentSelectedListener
import com.savvasdalkitsis.gameframe.injector.infra.navigation.NavigatorInjector
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import com.savvasdalkitsis.gameframe.ip.model.IpAddress
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView, ColorChooserDialog.ColorCallback {

    private val presenter = PresenterInjector.mainPresenter()
    private val navigator = NavigatorInjector.navigator()

    override val layoutId: Int
        get() = R.layout.activity_main

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
                return true
            }
            R.id.action_manage -> {
                fragment_switcher.displayedChild = 0
                notifyAllFragmentsUnselected()
                notifyFragmentSelected(R.id.fragment_manage)
                return true
            }
            R.id.action_draw -> {
                fragment_switcher.displayedChild = 1
                notifyFragmentSelected(R.id.fragment_draw)
                return true
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

    override fun ipAddressLoaded(ipAddress: IpAddress) {
        view_fab_container.visibility = View.VISIBLE
    }

    override fun ipCouldNotBeFound(throwable: Throwable) {
        view_fab_container.visibility = View.GONE
    }

    override fun onColorSelection(dialog: ColorChooserDialog, @ColorInt selectedColor: Int) {
        (supportFragmentManager.findFragmentById(R.id.fragment_draw) as ColorChooserDialog.ColorCallback).onColorSelection(dialog, selectedColor)
    }
}
