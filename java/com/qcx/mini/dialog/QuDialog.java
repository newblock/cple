package com.qcx.mini.dialog;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qcx.mini.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/3.
 */

public class QuDialog extends BaseDialog {
    private String title;
    private String message;
    private String leftBtnText;
    private String rightBtnText;
    private OnClickListener leftClickListener;
    private OnClickListener rightClickListener;

    @BindView(R.id.dialog_qu_text_state_title)
    TextView tv_title;
    @BindView(R.id.dialog_qu_text_state_message)
    TextView tv_message;
    @BindView(R.id.dialog_qu_text_state_leftBtn)
    TextView tv_left;
    @BindView(R.id.dialog_qu_text_state_rightBtn)
    TextView tv_right;

    @BindView(R.id.dialog_qu_text_state_line)
    View v_line;
    @BindView(R.id.dialog_qu_text_state_btn_view)
    View v_btns;

    @OnClick(R.id.dialog_qu_text_state_leftBtn)
    void leftClick(){
        if(leftClickListener!=null){
            leftClickListener.onClick(this);
        }
    }

    @OnClick(R.id.dialog_qu_text_state_rightBtn)
    void rightClick(){
        if(rightClickListener!=null){
            rightClickListener.onClick(this);
        }
    }

    @Override
    public int getTheme() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_qu_text_state;
    }

    @Override
    public void initView(View view) {
        initData();
    }

    @Override
    public int getBackgroundDrawableResource() {
        return R.drawable.bg_circular_white;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    private void initData(){
        showOrHideTextView(tv_title,title);
        showOrHideTextView(tv_message,message);
        showOrHideTextView(tv_left,leftBtnText);
        showOrHideTextView(tv_right,rightBtnText);
        if(TextUtils.isEmpty(leftBtnText)&&TextUtils.isEmpty(rightBtnText)){
            v_btns.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
        }else {
            v_btns.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.VISIBLE);
        }
    }

    private void showOrHideTextView(TextView textView,String text){

        if(TextUtils.isEmpty(text)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public QuDialog setTitle(String title){
        this.title=title;
        return this;
    }

    public QuDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public QuDialog setLeftBtn(String leftBtnText,OnClickListener clickListener) {
        this.leftBtnText = leftBtnText;
        this.leftClickListener=clickListener;
        return this;
    }

    public QuDialog setRightBtn(String rightBtnText,OnClickListener clickListener) {
        this.rightBtnText = rightBtnText;
        this.rightClickListener=clickListener;
        return this;
    }

    public interface OnClickListener{
        void onClick(QuDialog dialog);
    }
}
