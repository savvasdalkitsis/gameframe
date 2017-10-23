package com.savvasdalkitsis.gameframe.feature.changelog.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.savvasdalkitsis.gameframe.R
import us.feras.mdv.MarkdownView


class ChangeLogDialogFragment: DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val content = layoutInflater.inflate(R.layout.fragment_changelog, null)
        val changelog = content.findViewById<MarkdownView>(R.id.changelog)
        changelog.loadMarkdownFile("file:///android_asset/changelog.md")

        return AlertDialog.Builder(activity, R.style.AlertDialog_Changelog)
                .setTitle(R.string.changelog)
                .setView(content)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                .create()
    }

}