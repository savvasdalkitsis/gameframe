package com.savvasdalkitsis.gameframe.feature.control.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.control.model.ControlModel
import com.savvasdalkitsis.gameframe.feature.messaging.usecase.GoogleApiMessagingUseCase
import com.savvasdalkitsis.gameframe.infra.android.BaseActivity
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import kotlinx.android.synthetic.main.activity_control.*

class ControlActivity : BaseActivity(), ControlClickListener {

    private val presenter = PresenterInjector.controlPresenter()
    private lateinit var googleApi: GoogleApiClient

    override val layoutId = R.layout.activity_control

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAmbientEnabled()
        view_control_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ControlAdapter(this@ControlActivity)
            LinearSnapHelper().attachToRecyclerView(this)
        }
        googleApi = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build()
        presenter.bind(GoogleApiMessagingUseCase(googleApi))
    }

    override fun onStart() {
        super.onStart()
        googleApi.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApi.disconnect()
    }

    override fun onControlClicked(controlModel: ControlModel) = when (controlModel) {
        ControlModel.POWER -> presenter.power()
        ControlModel.MENU -> presenter.menu()
        ControlModel.NEXT -> presenter.next()
    }
}