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
package com.savvasdalkitsis.gameframe.feature.ip.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.savvasdalkitsis.gameframe.feature.ip.R
import com.savvasdalkitsis.gameframe.feature.networking.model.IpAddress

class IpTextView : LinearLayout {

    private lateinit var part1: TextView
    private lateinit var part2: TextView
    private lateinit var part3: TextView
    private lateinit var part4: TextView
    private var ipChangedListener: IpChangedListener? = null
    var ipAddress: IpAddress = IpAddress()
        private set

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        part1 = findViewById(R.id.view_ip_text_view_part_1)
        part2 = findViewById(R.id.view_ip_text_view_part_2)
        part3 = findViewById(R.id.view_ip_text_view_part_3)
        part4 = findViewById(R.id.view_ip_text_view_part_4)
        listOf(part1, part2, part3, part4).forEach { setup(it) }
    }

    private fun setup(textView: TextView) {
        textView.addTextChangedListener(IpTextChanged(textView))
        textView.setSelectAllOnFocus(true)
    }

    private fun rebuildIpAddress() {
        ipAddress = IpAddress(
                part1 = part1.text.toString(),
                part2 = part2.text.toString(),
                part3 = part3.text.toString(),
                part4 = part4.text.toString()
        )
    }

    fun setOnIpChangedListener(ipChangedListener: IpChangedListener) {
        this.ipChangedListener = ipChangedListener
    }

    fun bind(ipAddress: IpAddress) {
        part1.text = ipAddress.part1
        part2.text = ipAddress.part2
        part3.text = ipAddress.part3
        part4.text = ipAddress.part4
        part1.requestFocus()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        part1.isEnabled = enabled
        part2.isEnabled = enabled
        part3.isEnabled = enabled
        part4.isEnabled = enabled
    }

    private inner class IpTextChanged internal constructor(private val textView: TextView) : TextWatcher {

        @SuppressLint("WrongConstant")
        override fun afterTextChanged(editable: Editable) {
            rebuildIpAddress()
            ipChangedListener?.onIpChangedListener(ipAddress)
            editable.toString().let {
                if (it.length == 3 || it == "0") {
                    textView.focusSearch(View.FOCUS_FORWARD)?.requestFocus()
                }
            }
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    }
}
