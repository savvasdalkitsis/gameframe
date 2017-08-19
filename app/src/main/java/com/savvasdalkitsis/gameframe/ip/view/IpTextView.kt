package com.savvasdalkitsis.gameframe.ip.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.savvasdalkitsis.gameframe.R
import com.savvasdalkitsis.gameframe.ip.model.IpAddress

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
            if (editable.toString().length == 3) {
                textView.focusSearch(View.FOCUS_FORWARD).requestFocus()
            }
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

    }
}
