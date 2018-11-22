package com.qcx.mini.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.widget.wheel.IPickerViewData;
import com.qcx.mini.widget.wheel.WheelAdapter;
import com.qcx.mini.widget.wheel.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SingleWheelDialog<T extends IPickerViewData>  extends DialogFragment {
    private WheelView wheelPicker;
    private TextView tv_left,tv_right;
    private OnSingleWheelDialogListener<T> listener;
    private List<T> datas;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_single_wheel, null);

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

    public SingleWheelDialog<T> setListener(OnSingleWheelDialogListener<T> listener) {
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

    private void init(View view){
        wheelPicker=view.findViewById(R.id.dialog_single_wheel_wheelPicker);
        wheelPicker.setCyclic(false);
        tv_left=view.findViewById(R.id.dialog_single_wheel_cancel);
        tv_right=view.findViewById(R.id.dialog_single_wheel_sure);

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onRightClick(datas.get(wheelPicker.getCurrentItem()),SingleWheelDialog.this,wheelPicker.getCurrentItem());
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
        List<String> showData=new ArrayList<>();
        for(int i=0;i<datas.size();i++){
            showData.add(datas.get(i).getPickerViewText());
        }
        setShowData(showData);
    }

    public SingleWheelDialog<T> setData(List<T> data){
        if(data==null) data=new ArrayList<>();
        this.datas=data;
        return this;
    }

    private void setShowData(final List<String> data){
        wheelPicker.setAdapter(new SingleWheelDialogAdapter<String>(data));
    }

    public interface OnSingleWheelDialogListener<T extends IPickerViewData>{
        void onRightClick(T t,SingleWheelDialog dialog,int position);
    }

    class SingleWheelDialogAdapter<T> implements WheelAdapter<T>{
        List<T> datas;

        public SingleWheelDialogAdapter(List<T> datas) {
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
