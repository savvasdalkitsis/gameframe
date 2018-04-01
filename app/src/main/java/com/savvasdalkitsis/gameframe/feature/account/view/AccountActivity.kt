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
package com.savvasdalkitsis.gameframe.feature.account.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.feature.account.model.SignedInAccount
import com.savvasdalkitsis.gameframe.feature.account.presenter.AccountPresenter
import com.savvasdalkitsis.gameframe.infra.android.BaseActivity
import com.savvasdalkitsis.gameframe.kotlin.Action
import com.savvasdalkitsis.gameframe.kotlin.gone
import com.savvasdalkitsis.gameframe.kotlin.visible
import com.savvasdalkitsis.gameframe.feature.message.injector.MessageDisplayInjector
import com.savvasdalkitsis.gameframe.injector.presenter.PresenterInjector
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_account_logged_in.*
import kotlinx.android.synthetic.main.activity_account_logged_out.*

class AccountActivity : BaseActivity<AccountView, AccountPresenter<Intent>>(), AccountView {

    override val layoutId = R.layout.activity_account
    override val presenter: AccountPresenter<Intent> = PresenterInjector.accountPresenter()
    override val view = this

    private val messageDisplay = MessageDisplayInjector.messageDisplay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        view_account_log_in.setOnClickListener { presenter.logIn() }
        view_account_log_out.setOnClickListener { presenter.logOut() }
        view_account_re_upload.setOnClickListener { presenter.reUpload() }
        view_account_delete_account.setOnClickListener { presenter.deleteAccount() }
    }

    public override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setup_ip, menu)
        return true
    }

    override fun displayLoading() {
        switchTo(view_account_progress)
    }

    @Suppress("DEPRECATION")
    override fun displaySignedInAccount(account: SignedInAccount) {
        view_account_name.text = getString(R.string.hello_name, account.name)
        account.image?.let {
            view_account_image.setImageURI(it)
        }
        switchTo(view_account_logged_in)
    }

    override fun displaySignedOut() {
        switchTo(view_account_logged_out)
    }

    override fun displayErrorLoadingAccount() {
        messageDisplay.show(R.string.sign_in_error)
    }

    override fun askUserToVerifyAccountDeletion(onVerified: () -> Unit) {
        MaterialDialog.Builder(this)
                .title(R.string.delete_account)
                .content(R.string.delete_account_verification)
                .positiveText(R.string.delete)
                .negativeText(R.string.cancel)
                .onPositive { _, _ -> onVerified() }
                .show()
    }

    override fun askUserToUploadSavedProjects(onUpload: Action) {
        MaterialDialog.Builder(this)
                .title(R.string.upload_saved_projects)
                .content(R.string.upload_saved_projects_description)
                .positiveText(R.string.upload)
                .negativeText(R.string.skip)
                .onPositive { _, _ -> onUpload() }
                .show()
    }

    private fun switchTo(view: View) {
        listOf(view_account_logged_in, view_account_logged_out, view_account_progress).forEach {
            it.gone()
        }
        view.visible()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.handleResultForLoginRequest(requestCode, resultCode, data)
    }
}