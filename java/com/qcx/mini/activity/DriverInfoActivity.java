package com.qcx.mini.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.DriverAuthInfo;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.QuCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;

/**
 * 车主认证信息
 */
public class DriverInfoActivity extends BaseActivity {
    @BindView(R.id.driver_info_img_1)
    ImageView iv_1;
    @BindView(R.id.driver_info_img_2)
    ImageView iv_2;
    @BindView(R.id.driver_info_name)
    TextView tv_name;
    @BindView(R.id.driver_info_IDCard)
    TextView tv_IDCard;
    @BindView(R.id.driver_info_time_1)
    TextView tv_time1;
    @BindView(R.id.driver_info_car_num)
    TextView tv_carNum;
    @BindView(R.id.driver_info_car_name)
    TextView tv_carName;
    @BindView(R.id.driver_info_time_2)
    TextView tv_time2;
    @BindView(R.id.driver_info_car_info)
    TextView tv_carInfo;

    @Override
    public int getLayoutID() {
        return R.layout.activity_driver_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("车主认证", true, false);
        NetUtil.authenInfo(new QuCallback<DriverAuthInfo>() {
            @Override
            public void onSuccess(DriverAuthInfo driverAuthInfo) {
                setInfo(driverAuthInfo.getFeedback());
            }
        });
    }

    private void setInfo(DriverAuthInfo.DriverInfo driverInfo) {
        Picasso.with(this)
                .load(driverInfo.getDriverLicencePictureMain())
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .placeholder(R.mipmap.group_5)
                .transform(new BlurTransformation(this))
                .into(iv_1);
        Picasso.with(this)
                .load(driverInfo.getDrivingLicensePictureMain())
                .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                .placeholder(R.mipmap.group_16)
                .transform(new BlurTransformation(this))
                .into(iv_2);
        tv_name.setText(driverInfo.getRealName());
        tv_IDCard.setText(driverInfo.getIdCard());
        tv_time1.setText(driverInfo.getLicenseDate());
        tv_carNum.setText(driverInfo.getCarNumber());
        tv_carName.setText(driverInfo.getCarOwner());
        tv_time2.setText(driverInfo.getRegisteDate());
        tv_carInfo.setText(driverInfo.getCar());
    }

    public class BlurTransformation implements Transformation {

        RenderScript rs;

        public BlurTransformation(Context context) {
            super();
            rs = RenderScript.create(context);
        }

        @SuppressLint("NewApi")
        @Override
        public Bitmap transform(Bitmap bitmap) {
            // 创建一个Bitmap作为最后处理的效果Bitmap
            Bitmap blurredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // 分配内存
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // 根据我们想使用的配置加载一个实例
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // 设置模糊半径
            script.setRadius(10);

            //开始操作
            script.forEach(output);

            // 将结果copy到blurredBitmap中
            output.copyTo(blurredBitmap);

            //释放资源
            bitmap.recycle();

            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blur";
        }
    }


}
