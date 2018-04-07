package com.savvasdalkitsis.gameframe.feature.workspace.navigation

import io.reactivex.Completable
import java.io.File

interface WorkspaceNavigator {

    fun navigateToPlayStore()
    fun navigateToShareImageFile(file: File, name: String): Completable
}