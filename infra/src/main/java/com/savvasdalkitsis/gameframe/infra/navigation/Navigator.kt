package com.savvasdalkitsis.gameframe.infra.navigation

import io.reactivex.Completable
import java.io.File

interface Navigator {
    fun navigateToAccount()
    fun navigateToIpSetup()
    fun navigateToPlayStore()
    fun navigateToShareImageFile(file: File, name: String): Completable
    fun navigateToFeedback()
}