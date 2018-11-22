package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.utils.KeybordS;


/**
 * Created by Administrator on 2018/1/9.
 */

public class InputDialog extends BaseDialog {
    private EditText et_input;
    private TextView tv_sure;
    private CharSequence hint="";
    private CharSequence defaultText="";
    private OnInputDialogListener listener;
    private View v_input;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_input;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getTheme() {
        return 0;
    }


    @Override
    public void initView(View view) {
        et_input = view.findViewById(R.id.dialog_input_edit);
        tv_sure=view.findViewById(R.id.dialog_input_sure);
        et_input.setText(defaultText);
        et_input.setHint(hint);
        v_input=view.findViewById(R.id.dialog_input_view);
        if(defaultText!=null){
            et_input.setSelection(defaultText.length());
        }

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSureClick(et_input.getText().toString(),InputDialog.this);
                }
            }
        });

        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null) {
                    listener.onInputChanged(s != null ? s.toString() : "",InputDialog.this);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        KeybordS.openKeybord(et_input, getContext());
        v_input.animate().translationY(0).setDuration(600);
    }

    @Override
    protected void initDialog(Dialog dialog) {
        if (dialog == null) return;
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    dismiss();
                }
                return false;
            }
        });
        dialog.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (et_input != null) {
                    KeybordS.closeKeybord(et_input, getContext());
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener!=null){
            listener.onDismiss(et_input.getText().toString(),InputDialog.this);
        }
    }

    public InputDialog setListener(OnInputDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public InputDialog setHint(CharSequence hint) {
        this.hint = hint;
        return this;
    }

    public InputDialog setDefaultText(CharSequence defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    public interface OnInputDialogListener {
        void onInputChanged(String text,InputDialog dialog);

        void onSureClick(String text,InputDialog dialog);

        void onDismiss(String text,InputDialog dialog);
    }
}
