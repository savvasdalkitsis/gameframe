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
import com.savvasdalkitsis.gameframe.feature.bitmap.injector.BitmapInjector.bitmapFileUseCase
import com.savvasdalkitsis.gameframe.feature.changelog.injector.ChangelogInjector.changeLogUseCase
import com.savvasdalkitsis.gameframe.feature.composition.CompositionInjector.blendUseCase
import com.savvasdalkitsis.gameframe.feature.device.injector.DeviceInjector.gameFrameUseCase
import com.savvasdalkitsis.gameframe.feature.home.presenter.HomePresenter
import com.savvasdalkitsis.gameframe.feature.injector.WidgetInjector
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipNavigator
import com.savvasdalkitsis.gameframe.feature.ip.injector.IpInjector.ipRepository
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector.messageDisplay
import com.savvasdalkitsis.gameframe.feature.networking.injector.NetworkingInjector.wifiUseCase
import com.savvasdalkitsis.gameframe.feature.widget.presenter.WidgetPresenter
import com.savvasdalkitsis.gameframe.feature.workspace.injector.WorkspaceInjector.workspaceNavigator
import com.savvasdalkitsis.gameframe.feature.workspace.presenter.WorkspacePresenter
import com.savvasdalkitsis.gameframe.injector.feature.workspace.WorkspaceStorageInjector.firebaseWorkspaceStorage
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.authenticationUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.stringUseCase
import com.savvasdalkitsis.gameframe.injector.usecase.UseCaseInjector.workspaceUseCase

object PresenterInjector {

    fun workspacePresenter() = WorkspacePresenter<Menu, View>(gameFrameUseCase(),
            blendUseCase(), workspaceUseCase(), stringUseCase(), messageDisplay(), workspaceNavigator(),
            ipNavigator(), bitmapFileUseCase(), wifiUseCase())

    fun homePresenter() = HomePresenter(changeLogUseCase())

    fun accountPresenter() = AccountPresenter(authenticationUseCase(), workspaceUseCase(), firebaseWorkspaceStorage(), messageDisplay())
}
