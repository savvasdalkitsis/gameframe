package com.savvasdalkitsis.gameframe.feature.workspace.navigation

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v4.content.FileProvider
import android.util.Log
import com.savvasdalkitsis.gameframe.infra.navigation.AndroidNavigator
import com.savvasdalkitsis.gameframe.infra.navigation.FeedbackNavigator
import com.savvasdalkitsis.gameframe.infra.TopActivityProvider
import io.reactivex.Completable
import java.io.File

class AndroidWorkspaceNavigator(topActivityProvider: TopActivityProvider, application: Application): WorkspaceNavigator, AndroidNavigator(topActivityProvider, application) {

    override fun navigateToPlayStore() {
        val appPackageName = context.packageName
        try {
            context.startActivity(intentForUrl("market://details?id=$appPackageName"))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(intentForUrl("https://play.google.com/store/apps/details?id=$appPackageName"))
        }
    }

    override fun navigateToShareImageFile(file: File, name: String): Completable = Completable.create { emitter ->
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        try {
            context.startActivity(Intent.createChooser(intent, name))
            emitter.onComplete()
        } catch (e: android.content.ActivityNotFoundException) {
            Log.w(FeedbackNavigator::class.java.name, "Could not share image", e)
            emitter.onError(e)
        }
    }

}