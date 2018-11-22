package com.qcx.mini.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.utils.H5PageUtil;
import com.qcx.mini.widget.wheel.IPickerViewReminderData;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/3.
 */

public class ReminderSingleWheelDialog<T extends IPickerViewReminderData>  extends DialogFragment {

    private WheelView wheelPicker;
    private TextView tv_left,tv_right;
    private OnSelectPriceDialogListener<T> listener;
    private List<T> datas;
    private boolean isShowReminderText=true;
    private boolean isShowTopView=true;
    private int position;
    private View topView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reminder_single_wheel, null);

        final Dialog dialog = new Dialog(getActivity(), R.style.style_bottom_dialog);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        init(view);
        return dialog;
    }

    private void init(View view){
        topView=view.findViewById(R.id.dialog_reminder_single_wheel_topView);
        if(isShowTopView){
            topView.setVisibility(View.VISIBLE);
        }else {
            topView.setVisibility(View.GONE);
        }
        wheelPicker=view.findViewById(R.id.dialog_reminder_single_wheel_wheelPicker);
        wheelPicker.setCyclic(false);
        wheelPicker.setShowReminderText(isShowReminderText);
        wheelPicker.setCurrentItem(position);

        tv_left=view.findViewById(R.id.dialog_reminder_single_wheel_cancel);
        tv_right=view.findViewById(R.id.dialog_reminder_single_wheel_sure);

        view.findViewById(R.id.dialog_reminder_single_wheel_topView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onTopViewClick();
                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onRightClick(datas.get(wheelPicker.getCurrentItem()),ReminderSingleWheelDialog.this,wheelPicker.getCurrentItem());
                }
            }
        });
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    dismiss();
                }
            }
        });
        topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H5PageUtil.toRedPackageIntroducePage(getContext());
            }
        });
        setShowData(datas);
    }

    public ReminderSingleWheelDialog<T> setListener(OnSelectPriceDialogListener<T> listener) {
        this.listener = listener;
        return this;
    }

    public TextView getTv_left() {
        return tv_left;
    }

    public TextView getTv_right() {
        return tv_right;
    }

    public int getdPosition() {
        return wheelPicker.getCurrentItem();
    }

    public ReminderSingleWheelDialog<T> setShowReminderText(boolean showReminderText) {
        isShowReminderText = showReminderText;
        return this;
    }

    public ReminderSingleWheelDialog<T> setData(List<T> data){
        if(data==null) data=new ArrayList<>();
        this.datas=data;
        return this;
    }

    public ReminderSingleWheelDialog<T> setShowTopView(boolean showTopView) {
        isShowTopView = showTopView;
        return  this;
    }

    public ReminderSingleWheelDialog<T> setPosition(int position) {
        this.position = position;
        return  this;
    }

    public ReminderSingleWheelDialog<T> setPosition(String itemText) {
        if(datas==null) return this;
        for(int i=0;i<datas.size();i++){
            if(datas.get(i)!=null){
                String text=datas.get(i).getPickerViewText();
                if(!TextUtils.isEmpty(text)&&text.equals(itemText)){
                    this.position = i;
                    break;
                }
            }
        }
        return  this;
    }

    private void setShowData(final List<T> data){
        wheelPicker.setAdapter(new SelectPriceDialogAdapter<>(data));
    }

    public interface OnSelectPriceDialogListener<T extends IPickerViewReminderData>{
        void onRightClick(T t, ReminderSingleWheelDialog dialog, int position);
        void onTopViewClick();
    }

    class SelectPriceDialogAdapter<T extends IPickerViewReminderData> implements WheelAdapter<T> {
        List<T> datas;

        public SelectPriceDialogAdapter(List<T> datas) {
            if(datas==null) datas=new ArrayList<>();
            this.datas = datas;
        }

        @Override
        public int getItemsCount() {
            return datas.size();
        }

        @Override
        public T getItem(int index) {
            return datas.get(index);
        }

        @Override
        public int indexOf(T o) {
            for(int i=0;i<datas.size();i++){
                if(datas.get(i)==o){
                    return i;
                }
            }
            return 0;
        }
    }
}
