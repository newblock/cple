package com.qcx.mini.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.ReminderSingleWheelDialog;
import com.qcx.mini.entity.DriverDetailEntity;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.ImageCompress;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.PictureUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.H5PageUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class SetActivity extends BaseActivity {
    private static final String CAMERA_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "cameraName.jpg";
    private static final String ICON_FILE_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "icon.jpg";
    private static final int REQUSET_CODE_AUTH=10;
    private DriverDetailEntity.Driver driverDetail;
    private boolean isChange = false;

    @BindView(R.id.set_icon)
    ImageView iv_icon;
    @BindView(R.id.set_name_text)
    TextView tv_name;
    @BindView(R.id.set_sex_text)
    TextView tv_sex;
    @BindView(R.id.set_age_text)
    TextView tv_age;
    @BindView(R.id.set_zmxy_text)
    TextView tv_zmxy;
    @BindView(R.id.set_auth_text)
    TextView tv_auth;
    @BindView(R.id.set_phone_text)
    TextView tv_phone;

    @OnClick({R.id.set_icon,R.id.set_icon_view})
    void setIcon() {
        if (isUpLoadingImage) {
            ToastUtil.showToast("正在上传头像，请稍后再试");
            return;
        }
        File file = new File(ICON_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DialogUtil.pictureDialog(this, CAMERA_NAME);
    }

    private OnDriverAuthClickListener listener;
    @OnClick(R.id.set_auth)
    void auth() {
        if (driverDetail == null) return;
        if(listener==null){
            listener=new OnDriverAuthClickListener(this);
        }
        User.getInstance().setDriverStatus(driverDetail.getStatus());
        listener.onClick();
    }

    @OnClick(R.id.set_name_view)
    void setName() {
        Intent intent = new Intent(this, EditTextActivity.class);
        intent.putExtra("type", 1);
        startActivityForResult(intent, 50);
    }

    @OnClick(R.id.set_sex_view)
    void setSex() {
        new ReminderSingleWheelDialog<WheelIntEntity>()
                .setData(getSexData())
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_sex.setText(wheelIntEntity.getPickerViewText());
                        changeDriverDetail(wheelIntEntity.getData(), "", 2);
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {

                    }
                })
                .setPosition(tv_sex.getText().toString())
                .setShowTopView(false)
                .show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.set_age_view)
    void setAge() {
        new ReminderSingleWheelDialog<WheelIntEntity>()
                .setData(getAgeData())
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_age.setText(wheelIntEntity.getPickerViewText());
                        changeDriverDetail(0, wheelIntEntity.getPickerViewText(), 1);
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {

                    }
                })
                .setPosition(tv_age.getText().toString())
                .setShowTopView(false)
                .show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.set_address)
    void setAddress() {
        Intent intent = new Intent(this, SetHomeAndCompanyActivity.class);
        startActivityForResult(intent, 50);
    }

    @OnClick(R.id.set_line)
    void setLine() {
        Intent intent = new Intent(this, PersonalLinesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.set_zmxy_view)
    void zmxy() {
        if(driverDetail==null) return;
        if (driverDetail.getZmxy_status() == 1) {
            ToastUtil.showToast("已认证，芝麻分".concat(String.valueOf(driverDetail.getZmxy())));
        } else {
            Intent intent = new Intent(this, ZhiMaAuthActivity.class);
            startActivityForResult(intent, 50);
        }
    }

    @OnClick(R.id.set_clause_view)
    void clause() {
        H5PageUtil.toClausePage(this);
    }

    @OnClick(R.id.set_about_view)
    void about() {
        startActivity(new Intent(this, QCXAboutActivity.class));
    }

    @OnClick(R.id.set_logout)
    void logout() {
        new QAlertDialog()
                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                .setTitleText("确定退出登录吗?")
                .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                    @Override
                    public void onClick(QAlertDialog dialog) {
                        User.getInstance().signOut();
                        startActivity(new Intent(SetActivity.this, LoginActivity.class));
                        finishActivity(MainActivity.class.getSimpleName());
                        dialog.dismiss();
                        finish();
                    }
                })
                .show(getSupportFragmentManager(),"");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_set;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("设置", true, false);
        EventBus.getDefault().register(this);
        setPhone(String.valueOf(User.getInstance().getPhoneNumber()));
        getDriverDetail();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("requestCode=" + requestCode + " resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureUtil.REQUEST_CODE_PHOTO:
                    if (data != null) {
                        Uri uri = data.getData();
                        ImageCompress.cropImg(this, 3, uri, ICON_FILE_NAME);
                    }
                    break;
                case PictureUtil.REQUEST_CODE_CAMERA:
                    ImageCompress.cropImg(this, 3, PictureUtil.getImageContentUri(new File(CAMERA_NAME), this), ICON_FILE_NAME);
                    break;
                case 3:
                    if (data != null) {
                        File img = new File(ICON_FILE_NAME);
                        Picasso.with(this)
                                .load(img)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(iv_icon);
                        uploadingIcon(img);
                    }
                    break;
                case 50:
                    if (data != null) {
                        String text = data.getStringExtra("text");
                        if (!TextUtils.isEmpty(text)) {
                            tv_name.setText(text);
                            changeDriverDetail(0, text, 0);
                        }
                    }
                    break;
                case REQUSET_CODE_AUTH:
                    getDriverDetail();
                    break;
            }
        }
    }

    void setPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            try {
                String p = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                tv_phone.setText(p);
            } catch (Exception e) {
                e.printStackTrace();
                tv_phone.setText(phone);
            }
        }
    }

    private List<WheelIntEntity> getSexData() {
        List<WheelIntEntity> data = new ArrayList<>();
        data.add(new WheelIntEntity("男", 1));
        data.add(new WheelIntEntity("女", 2));
        return data;
    }

    private List<WheelIntEntity> getAgeData() {
        List<WheelIntEntity> data = new ArrayList<>();
        data.add(new WheelIntEntity("50后", 1));
        data.add(new WheelIntEntity("60后", 2));
        data.add(new WheelIntEntity("70后", 3));
        data.add(new WheelIntEntity("80后", 4));
        data.add(new WheelIntEntity("90后", 5));
        data.add(new WheelIntEntity("00后", 6));
        return data;
    }

    boolean isUpLoadingImage = false;

    private void uploadingIcon(File file) {
        if (file == null) {
            ToastUtil.showToast("请重新选择图片");
            return;
        }
        isUpLoadingImage = true;
        showLoadingDialog();
        Request.uploadingPicture(URLString.addPicture, file, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                isUpLoadingImage = false;
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() != 200) {
                    onError("");
                } else {
                    isChange = true;
                }
            }

            @Override
            public void onError(String errorInfo) {
                hideLoadingDialog();
                isUpLoadingImage = false;
                ToastUtil.showToast("头像上传失败");
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventStatus status) {
        if (status == EventStatus.ZMXY_AUTH_SUCCESS) {
            tv_zmxy.setText("已认证");
        }
    }

    @Override
    public void finish() {
        if (isChange) {
            setResult(RESULT_OK);
            LogUtil.i(" RESULT_OK ");
        } else {
            setResult(RESULT_CANCELED);
            LogUtil.i(" RESULT_CANCELED ");
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getDriverDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        showLoadingDialog();
        Request.post(URLString.driverDetail, params, new EntityCallback(DriverDetailEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                DriverDetailEntity driverDetailEntity = (DriverDetailEntity) t;
                showData(driverDetailEntity.getDriver());
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private void showData(DriverDetailEntity.Driver driverDetail) {
        if (driverDetail != null) {
            this.driverDetail = driverDetail;
            Picasso.with(this)
                    .load(driverDetail.getPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(driverDetail.getNickName());
            tv_age.setText(driverDetail.getAge());
            switch (driverDetail.getSex()) {
                case 0://保密
                    tv_sex.setText("保密");
                    break;
                case 1://男
                    tv_sex.setText("男");
                    break;
                case 2://女
                    tv_sex.setText("女");
                    break;
            }
            if (driverDetail.getZmxy_status() == 1) {
                tv_zmxy.setText("已认证");
            } else {
                tv_zmxy.setText("未认证");
            }
            switch (driverDetail.getStatus()) {
                case 0://
                    tv_auth.setText("审核中");
                    break;
                case 1://
                    tv_auth.setText("未认证");
                    break;
                case 2://
                    tv_auth.setText("未通过");
                    break;
                case 3://
                    tv_auth.setText("被拉黑");
                    break;
                case 4://
                    tv_auth.setText("已认证");
                    break;
            }
        }
    }

    private void changeDriverDetail(int sex, final String info, final int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        switch (type) {
            case 0://姓名
                params.put("nickName", info);
                break;
            case 1://年龄
                params.put("age", info);
                break;
            case 2://性别
                params.put("sex", sex);
                break;
            default:
                break;
        }

        Request.post(URLString.changeDriverDetail, params, new EntityCallback(DriverDetailEntity.class) {
            @Override
            public void onSuccess(Object t) {
                DriverDetailEntity driverDetailEntity = (DriverDetailEntity) t;
                if (driverDetailEntity.getStatus() == 200) {
                    driverDetail = driverDetailEntity.getDriver();
                    showData(driverDetail);
                    isChange = true;
                }
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

}
