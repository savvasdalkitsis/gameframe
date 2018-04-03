package com.savvasdalkitsis.gameframe.feature.workspace.injector

import com.savvasdalkitsis.gameframe.infra.injector.TopActivityProviderInjector.topActivityProvider
import com.savvasdalkitsis.gameframe.feature.workspace.navigation.AndroidWorkspaceNavigator
import com.savvasdalkitsis.gameframe.feature.workspace.navigation.WorkspaceNavigator
import com.savvasdalkitsis.gameframe.infra.injector.ApplicationInjector.application

object WorkspaceInjector {

    fun workspaceNavigator(): WorkspaceNavigator = AndroidWorkspaceNavigator(topActivityProvider(), application())
}