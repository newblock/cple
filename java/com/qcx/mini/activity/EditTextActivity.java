package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.EditTextUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.verify.VerifyException;
import com.qcx.mini.verify.VerifyUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class EditTextActivity extends BaseActivity {
    @BindView(R.id.edit_btn_go)
    Button edit_btn_go;
    int type;

    public EditTextActivity() {
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_edit_text;
    }

    @BindView(R.id.edittext_edit)
    EditText edittext_edit;

    @Override
    public void initView(Bundle savedInstanceState) {
        EditTextUtil.setEditTextInputSpace(edittext_edit);
        type=getIntent().getIntExtra("type", 0);
        switch (type) {
            case 1:
                initTitleBar("昵称",true,false);
                edittext_edit.setHint("请输入您的昵称");
                break;
            default:
                initTitleBar("出现错误");
                edittext_edit.setHint("请输入您的%s");
        }
        String value = getIntent().getStringExtra("val");
        if (!TextUtils.isEmpty(value) && !value.equals("null"))
            edittext_edit.setText(value);
    }

    @OnClick(R.id.edit_btn_delete)
    void edit_btn_delete() {
        edittext_edit.setText("");
    }

    @OnClick(R.id.edit_btn_go)
    void edit_btn_go() {
        String val = edittext_edit.getText().toString();
        if (TextUtils.isEmpty(val)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (getIntent().getIntExtra("type", 0)) {
            case 1:
                if (edittext_edit.length() > 6) {
                    Toast.makeText(this, "限制6个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case 2:
                if (edittext_edit.length() > 12) {
                    Toast.makeText(this, "限制12个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case 3:
                if (edittext_edit.length() > 6) {
                    Toast.makeText(this, "限制6个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
        try {
            VerifyUtil.stringFilter(val);
            setResult(RESULT_OK, new Intent().putExtra("text", val));
            finish();
        } catch (VerifyException e) {
            e.printStackTrace();
            ToastUtil.showToast(e.getMessage());
        }
    }
}
