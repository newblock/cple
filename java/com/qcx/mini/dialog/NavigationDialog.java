package com.qcx.mini.dialog;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.utils.Navigation;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/8.
 */

public class NavigationDialog extends BaseDialog {
    private int navigationType= SharedPreferencesUtil.getUserSharedPreferences().getInt(ConstantString.SharedPreferencesKey.SP_NAVIGATION_TYPE);

    private double[] leftStart;
    private double[] leftEnd;
    private String leftStartLocationName;
    private String leftEndtLocationName;
    private double[] rightStart;
    private double[] rightEnd;
    private String rightStartLocationName;
    private String rightEndLocationName;

    @BindView(R.id.dialog_navigation_set_navigation_type_text)
    TextView tv_NavType;

    @OnClick(R.id.dialog_navigation_left_navigation)
    void onLeftNav(){
        if(leftEnd!=null&&leftEnd.length==2&&getContext()!=null){
            Navigation.navigation(leftEnd[0],leftEnd[1],leftEndtLocationName,getContext(),navigationType,ConstantValue.NavigationMode.DRIVE);
        }else {
            ToastUtil.showToast("无效的位置信息");
        }
        dismiss();
    }

    @OnClick(R.id.dialog_navigation_right_navigation)
    void onLeftRightNav(){
        if(rightEnd!=null&&rightEnd.length==2&&getContext()!=null){
            Navigation.navigation(rightEnd[0],rightEnd[1],rightEndLocationName,getContext(),navigationType,ConstantValue.NavigationMode.DRIVE);
        }else {
            ToastUtil.showToast("无效的位置信息");
        }
        dismiss();
    }

    @OnClick(R.id.dialog_navigation_set_navigation_type)
    void setNavType(){
        if(getContext()==null){
            return;
        }
        String[] items=new String[]{"高德地图","百度地图"};
        final  ItemsDialog dialog = new ItemsDialog(getContext(), items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        navigationType= ConstantValue.NavigationType.GAODE;
                        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_NAVIGATION_TYPE,navigationType);
                        tv_NavType.setText("高德地图");
                        dialog.dismiss();
                        break;
                    case 1:
                        navigationType= ConstantValue.NavigationType.BAIDU;
                        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_NAVIGATION_TYPE,navigationType);
                        tv_NavType.setText("百度地图");
                        dialog.dismiss();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.dialog_navigation_close)
    void close(){
        dismiss();
    }


    @Override
    public int getLayoutId() {
        return R.layout.dialog_navigation;
    }

    @Override
    public void initView(View view) {
        if(navigationType==-1){
            navigationType=ConstantValue.NavigationType.GAODE;
        }
        if(navigationType==ConstantValue.NavigationType.GAODE){
            tv_NavType.setText("高德地图");
        }else {
            tv_NavType.setText("百度地图");
        }
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }


    public NavigationDialog setLeftStart(double[] leftStart) {
        this.leftStart = leftStart;
        return this;
    }

    public NavigationDialog setLeftEnd(double[] leftEnd) {
        this.leftEnd = leftEnd;
        return this;
    }

    public NavigationDialog setLeftStartLocationName(String leftStartLocationName) {
        this.leftStartLocationName = leftStartLocationName;
        return this;
    }

    public NavigationDialog setLeftEndtLocationName(String leftEndtLocationName) {
        this.leftEndtLocationName = leftEndtLocationName;
        return this;
    }

    public NavigationDialog setRightStart(double[] rightStart) {
        this.rightStart = rightStart;
        return this;
    }

    public NavigationDialog setRightEnd(double[] rightEnd) {
        this.rightEnd = rightEnd;
        return this;
    }

    public NavigationDialog setRightStartLocationName(String rightStartLocationName) {
        this.rightStartLocationName = rightStartLocationName;
        return this;
    }

    public NavigationDialog setRightEndLocationName(String rightEndLocationName) {
        this.rightEndLocationName = rightEndLocationName;
        return this;
    }
}
