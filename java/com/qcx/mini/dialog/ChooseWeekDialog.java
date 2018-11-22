package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/2.
 */

public class ChooseWeekDialog extends BaseDialog {
    private boolean[] check={false,false,false,false,false,false,false};
    private boolean[] lastCheck={false,false,false,false,false,false,false};
    private ImageView[] imageViews;
    private OnChooseWeekListener listener;
    private boolean isOnWeeks=false;

    @BindView(R.id.dialog_choose_week_1_img)
    ImageView iv_1;
    @BindView(R.id.dialog_choose_week_2_img)
    ImageView iv_2;
    @BindView(R.id.dialog_choose_week_3_img)
    ImageView iv_3;
    @BindView(R.id.dialog_choose_week_4_img)
    ImageView iv_4;
    @BindView(R.id.dialog_choose_week_5_img)
    ImageView iv_5;
    @BindView(R.id.dialog_choose_week_6_img)
    ImageView iv_6;
    @BindView(R.id.dialog_choose_week_7_img)
    ImageView iv_7;

    @OnClick(R.id.dialog_choose_week_1_view)
    void week1(){
        check[0]=!check[0];
        if(check[0]){
            iv_1.setImageResource(R.mipmap.icon_check);
        }else {
            iv_1.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_2_view)
    void week2(){
        check[1]=!check[1];
        if(check[1]){
            iv_2.setImageResource(R.mipmap.icon_check);
        }else {
            iv_2.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_3_view)
    void week3(){
        check[2]=!check[2];
        if(check[2]){
            iv_3.setImageResource(R.mipmap.icon_check);
        }else {
            iv_3.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_4_view)
    void week4(){
        check[3]=!check[3];
        if(check[3]){
            iv_4.setImageResource(R.mipmap.icon_check);
        }else {
            iv_4.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_5_view)
    void week5(){
        check[4]=!check[4];
        if(check[4]){
            iv_5.setImageResource(R.mipmap.icon_check);
        }else {
            iv_5.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_6_view)
    void week6(){
        check[5]=!check[5];
        if(check[5]){
            iv_6.setImageResource(R.mipmap.icon_check);
        }else {
            iv_6.setImageResource(R.mipmap.icon_uncheck);
        }
    }
    @OnClick(R.id.dialog_choose_week_7_view)
    void week7(){
        check[6]=!check[6];
        if(check[6]){
            iv_7.setImageResource(R.mipmap.icon_check);
        }else {
            iv_7.setImageResource(R.mipmap.icon_uncheck);
        }
    }

    @OnClick(R.id.dialog_choose_week_cancel)
    void cancel(){
        dismiss();
    }
    @OnClick(R.id.dialog_choose_week_sure)
    void sure(){
        setLastCheck();
        if(listener!=null){
            isOnWeeks=true;
            listener.weeks(lastCheck,this);
        }else {
            dismiss();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_choose_week;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public void initView(View view) {
        imageViews=new ImageView[]{iv_1,iv_2,iv_3,iv_4,iv_5,iv_6,iv_7};
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listener!=null){
            listener.onDismiss(ChooseWeekDialog.this,isOnWeeks);
        }
    }

    private void init(){
        isOnWeeks=false;
        for(int i=0;i<lastCheck.length;i++){
            LogUtil.i(lastCheck[i]+"");
            check[i]=lastCheck[i];
            if(lastCheck[i]){
                imageViews[i].setImageResource(R.mipmap.icon_check);
            }else {
                imageViews[i].setImageResource(R.mipmap.icon_uncheck);
            }
        }
    }

    private void setLastCheck(){
        for(int i=0;i<check.length;i++){
            lastCheck[i]=check[i];
        }
    }

    public ChooseWeekDialog setWeek(List<Integer> weeks){
        if(weeks!=null){
            lastCheck=new boolean[]{false,false,false,false,false,false,false};
            for(int i=0;i<weeks.size();i++){
                if(weeks.get(i)==0){
                    lastCheck[6]=true;
                }else {
                    lastCheck[weeks.get(i)-1]=true;
                }
            }
        }
        return this;
    }

    public ChooseWeekDialog setListener(OnChooseWeekListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnChooseWeekListener{
        void weeks(boolean[] status,ChooseWeekDialog dialog);
        void onDismiss(ChooseWeekDialog dialog,boolean isOnWeeks);
    }
}
