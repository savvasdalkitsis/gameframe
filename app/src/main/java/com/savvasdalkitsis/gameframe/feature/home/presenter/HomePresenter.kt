package com.savvasdalkitsis.gameframe.feature.home.presenter

import com.savvasdalkitsis.gameframe.feature.changelog.usecase.ChangeLogUseCase
import com.savvasdalkitsis.gameframe.feature.home.view.HomeView

class HomePresenter(private val changeLogUseCase: ChangeLogUseCase) {

    private lateinit var view: HomeView

    fun bindView(view: HomeView) {
        this.view = view
    }

    fun start() {
        if (!changeLogUseCase.hasSeenChangeLog()) {
            changeLogUseCase.markChangeLogSeen()
            view.displayChangeLog()
        }
    }
}