package com.savvasdalkitsis.gameframe.ip.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.savvasdalkitsis.gameframe.R;
import com.savvasdalkitsis.gameframe.ip.model.IpAddress;

import static com.savvasdalkitsis.gameframe.ip.model.IpAddress.Builder.ipAddress;

public class IpTextView extends LinearLayout {

    private TextView part1;
    private TextView part2;
    private TextView part3;
    private TextView part4;
    private IpChangedListener ipChangedListener = IpChangedListener.NO_OP;
    private IpAddress ipAddress = ipAddress().build();

    public IpTextView(Context context) {
        super(context);
    }

    public IpTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IpTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        part1 = (TextView) findViewById(R.id.view_ip_text_view_part_1);
        part2 = (TextView) findViewById(R.id.view_ip_text_view_part_2);
        part3 = (TextView) findViewById(R.id.view_ip_text_view_part_3);
        part4 = (TextView) findViewById(R.id.view_ip_text_view_part_4);
        setup(part1);
        setup(part2);
        setup(part3);
        setup(part4);
    }

    private void setup(TextView textView) {
        textView.addTextChangedListener(new IpTextChanged(textView));
        textView.setSelectAllOnFocus(true);
    }

    private void rebuildIpAddress() {
        ipAddress = ipAddress()
                .part1(part1.getText().toString())
                .part2(part2.getText().toString())
                .part3(part3.getText().toString())
                .part4(part4.getText().toString())
                .build();
    }

    public void setOnIpChangedListener(IpChangedListener ipChangedListener) {
        this.ipChangedListener = ipChangedListener;
    }

    public void bind(IpAddress ipAddress) {
        part1.setText(ipAddress.getPart1());
        part2.setText(ipAddress.getPart2());
        part3.setText(ipAddress.getPart3());
        part4.setText(ipAddress.getPart4());
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    private class IpTextChanged implements TextWatcher {

        private TextView textView;

        IpTextChanged(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            rebuildIpAddress();
            ipChangedListener.onIpChangedListener(ipAddress);
            if (editable.toString().length() == 3) {
                textView.focusSearch(View.FOCUS_FORWARD).requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    }
}
