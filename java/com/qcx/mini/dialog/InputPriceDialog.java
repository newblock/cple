package com.qcx.mini.dialog;

import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.utils.FastBlurUtility;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.numSoftKeyboart.NumSoftKeyboard;
import com.qcx.mini.widget.numSoftKeyboart.OnNumInputListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/16.
 */

public class InputPriceDialog extends BaseDialog {
    private int maxPrice = 10000;
    private int minPrice = 5;
    int price;
    private OnNumListener listener;

    @BindView(R.id.dialog_input_price_keyboard)
    NumSoftKeyboard mNumSoftKeyboard;
    @BindView(R.id.dialog_input_price_text)
    TextView tv_price;
    @BindView(R.id.dialog_input_price_backView)
    ImageView v_back;
    @BindView(R.id.dialog_input_price_keyboard_back_view)
    View v_keyboardBack;


    @OnClick(R.id.dialog_input_price_cancel)
    void cancel(){
        dismiss();
    }
    @OnClick(R.id.dialog_input_price_submit)
    void submit(){
        if(listener!=null){
            listener.onNum(price);
        }
        dismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_input_price;
    }

    @Override
    public void initView(View view) {
        tv_price.setHint(String.format(Locale.CHINA,"请输入%d~%d之间的整数金额",minPrice,maxPrice));
        mNumSoftKeyboard.setListener(new OnNumInputListener() {
            @Override
            public void onInputNum(int num) {
                int inputPrice = price * 10 + num;
                if (inputPrice <= maxPrice) {
                    price = inputPrice;
                }
                if (price == 0) {
                    tv_price.setText("");
                } else {
                    tv_price.setText(String.valueOf(price));
                }
            }

            @Override
            public void onDeleteNum() {
                price = price / 10;
                if (price == 0) {
                    tv_price.setText("");
                } else {
                    tv_price.setText(String.valueOf(price));
                }
            }
        });
        v_back.setImageBitmap(FastBlurUtility.getBlurBackgroundDrawer(getActivity(), UiUtils.getSize(53),UiUtils.getSize(162),UiUtils.getSize(270),UiUtils.getSize(165)));
       int startY=UiUtils.getPixelV()-UiUtils.getSize(214);
       int h=UiUtils.getSize(214);
        v_keyboardBack.setBackground(new BitmapDrawable(getResources(),
                FastBlurUtility.getBlurBackgroundDrawer(getActivity(),0,startY,UiUtils.getPixelH(),h)));
    }


    @Override
    public float getDimAmount() {
        return 0.6f;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    public interface OnNumListener{
        void onNum(int num);
    }
}
