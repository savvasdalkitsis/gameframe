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
package com.savvasdalkitsis.gameframe.injector.presenter

import android.view.Menu
import android.view.View
import com.savvasdalkitsis.gameframe.feature.account.presenter.AccountPresenter
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector.bitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.changelog.injector.ChangelogInjector.changeLogUseCase
import com.savvasdalkitsis.gameframe.feature.control.presenter.ControlPresenter
import com.savvasdalkitsis.gameframe.feature.home.presenter.HomePresenter
import com.savvasdalkitsis.gameframe.feature.ip.presenter.IpSetupPresenter
import com.savvasdalkitsis.gameframe.feature.widget.presenter.WidgetPresenter
import com.savvasdalkitsis.gameframe.feature.workspace.presenter.WorkspacePresenter
import com.savvasdalkitsis.gameframe.injector.feature.ip.repository.IpRepositoryInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector.messageDisplay
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.wifiUseCase
import com.savvasdalkitsis.gameframe.injector.feature.navigation.NavigatorInjector.navigator
import com.savvasdalkitsis.gameframe.injector.feature.workspace.WorkspaceStorageInjector.firebaseWorkspaceStorage
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.authenticationUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.blendUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.gameFrameUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.ipDiscoveryUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.stringUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.workspaceUseCase

object PresenterInjector {

    fun ipSetupPresenter() =
            IpSetupPresenter(gameFrameUseCase(), ipRepository(), ipDiscoveryUseCase(), wifiUseCase())

    fun controlPresenter() = ControlPresenter(gameFrameUseCase(), ipRepository(), navigator(), wifiUseCase())

    fun widgetPresenter() =
            WidgetPresenter(gameFrameUseCase(), ipRepository(), navigator())

    fun workspacePresenter() = WorkspacePresenter<Menu, View>(gameFrameUseCase(),
            blendUseCase(), workspaceUseCase(), stringUseCase(), messageDisplay(), navigator(),
            bitmapFileUseCase(), wifiUseCase())

    fun homePresenter() = HomePresenter(changeLogUseCase())

    fun accountPresenter() = AccountPresenter(authenticationUseCase(), workspaceUseCase(), firebaseWorkspaceStorage(), messageDisplay())
}
