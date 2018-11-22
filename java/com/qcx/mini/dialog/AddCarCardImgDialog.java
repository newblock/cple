package com.qcx.mini.dialog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.R;
import com.qcx.mini.utils.PictureUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/8.
 */

public class AddCarCardImgDialog extends BaseDialog {
    String cameraName;
    public final static int TYPE_JIASHI=0;
    public final static int TYPE_XINGSHI=1;
    private int type;

    @BindView(R.id.dialog_add_car_card_img_img)
    ImageView iv_img;

    @OnClick(R.id.dialog_add_car_card_img_camera)
    void camera() {
        PictureUtil.takeCameraOnly(getActivity(), cameraName);
        dismiss();
    }

    @OnClick(R.id.dialog_add_car_card_img_choose)
    void choose() {
        PictureUtil.secletImage(getActivity());
        dismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_add_car_card_img;
    }

    @Override
    public void initView(View view) {
        if(type==TYPE_JIASHI){
            iv_img.setImageResource(R.mipmap.group_5);
        }else {
            iv_img.setImageResource(R.mipmap.group_16);
        }
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    public AddCarCardImgDialog setCameraName(String cameraName) {
        this.cameraName = cameraName;
        return this;
    }

    public AddCarCardImgDialog setType(int type) {
        this.type = type;
        return this;
    }
}
