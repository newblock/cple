package com.qcx.mini.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.AddCarCardImgDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ImageCompress;
import com.qcx.mini.utils.PictureUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.verify.AuthenticationVerify;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车主认证
 */
public class AuthenticationActivity extends BaseActivity {
    private final String PICTURE_1 = ConstantString.FilePath.getImageFilePath() + File.separator + "auth_img1.jpg";
    private final String PICTURE_2 = ConstantString.FilePath.getImageFilePath() + File.separator + "auth_img2.jpg";

    private final String DRIVER_LICENCE = "driverLicencePictureMain";// 驾驶证照片
    private final String DRIVING_LICENSE = "drivingLicensePictureMain";// 行驶证照片

    private static final String camera = ConstantString.FilePath.getImageFilePath() + File.separator + "camera.jpg";
    private boolean isOnePicture = true;
    boolean[] picture;
    private AddCarCardImgDialog choosePictureDialog;

    RxPermissions rxPermissions;
    @BindView(R.id.auth_image1)
    ImageView auth_image1;
    @BindView(R.id.auth_image2)
    ImageView auth_image2;
    @BindView(R.id.authentication_carNum)
    EditText tv_carNum;
    @BindView(R.id.authentication_carColor)
    EditText tv_carColor;
    @BindView(R.id.authentication_carType)
    EditText tv_carType;

    @OnClick(R.id.auth_image1)
    void selectedImage1() {
//        DialogUtil.pictureDialog(this, camera);
        showChooseImgDialog(AddCarCardImgDialog.TYPE_JIASHI);
        isOnePicture = true;

    }

    @OnClick(R.id.auth_image2)
    void selectedImage2() {
//        DialogUtil.pictureDialog(this, camera);
        showChooseImgDialog(AddCarCardImgDialog.TYPE_XINGSHI);
        isOnePicture = false;
    }

    //提交数据
    @OnClick(R.id.authentication_submit)
    void submit() {
        try {
            String car = tv_carType.getText().toString();
            String carNumber = tv_carNum.getText().toString();
            String carColor = tv_carColor.getText().toString();

            AuthenticationVerify.verifyCarNum(carNumber);
            AuthenticationVerify.verifyCarType(car);
            AuthenticationVerify.verifyCarColor(carColor);
            AuthenticationVerify.verifyImg(picture);
            showLoadingDialog();
            OkGo.<String>post(URLString.uploadAuth)
                    .isMultipart(true)
                    .params("token", User.getInstance().getToken())
                    .params(DRIVER_LICENCE, new File(PICTURE_1))
                    .params(DRIVING_LICENSE, new File(PICTURE_2))
                    .params("car", car.concat(" ").concat(carColor))
                    .params("carNumber", carNumber.replace(" ", ""))
                    .params("auditType", String.valueOf(1))
                    .execute(new EntityCallback(IntEntity.class) {
                        @Override
                        public void onSuccess(Object t) {
                            IntEntity intEntity = (IntEntity) t;
                            if (intEntity.getStatus() == 200) {
                                User.getInstance().setDriverStatus(ConstantValue.AuthStatus.CHECKING);
                                setResult(RESULT_OK);
                                startActivity(new Intent(AuthenticationActivity.this, AuthenticationStep3Activity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onError(String errorInfo) {

                        }
                    });
        } catch (Exception e) {
            ToastUtil.showToast(e.getMessage());
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_authentication;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("车主认证", true, false);
        rxPermissions = new RxPermissions(this);
        picture = new boolean[]{false, false};
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String fileName;
        if (isOnePicture) {
            fileName = PICTURE_1;
        } else {
            fileName = PICTURE_2;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureUtil.REQUEST_CODE_PHOTO:
                    if (data != null) {
                        Uri uri = data.getData();
                        compressImage(uri, fileName);
                    }
                    break;
                case PictureUtil.REQUEST_CODE_CAMERA:
                    compressImage(camera, fileName);
                    break;
            }
        }
    }

    private void compressImage(final String filePath, final String reFilePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageCompress compress = new ImageCompress();
                final ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                options.uri = Uri.fromFile(new File(filePath));
                options.maxHeight = 1800;
                options.maxWidth = 3600;
                options.destFile = new File(reFilePath);
                compress.compressFromUri(AuthenticationActivity.this, options);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPicture(reFilePath);
                    }
                });
            }
        }).start();
    }

    private void compressImage(final Uri filePath, final String reFilePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageCompress compress = new ImageCompress();
                ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                options.uri = filePath;
                options.maxHeight = 1800;
                options.maxWidth = 3600;
                options.destFile = new File(reFilePath);
                compress.compressFromUri(AuthenticationActivity.this, options);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPicture(reFilePath);
                    }
                });
            }
        }).start();
    }

    public void showPicture(String fileName) {
        if (isOnePicture) {
            Picasso.with(AuthenticationActivity.this)
                    .load(new File(fileName))
                    .error(R.mipmap.img_jiashizheng)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(auth_image1);
            picture[0] = true;
        } else {
            Picasso.with(AuthenticationActivity.this)
                    .load(new File(fileName))
                    .error(R.mipmap.img_xingshizheng)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(auth_image2);
            picture[1] = true;
        }
    }

    private void showChooseImgDialog(int type) {
        if (choosePictureDialog == null) {
            choosePictureDialog =
                    new AddCarCardImgDialog().
                            setCameraName(camera);
        }
        choosePictureDialog.setType(type);
        choosePictureDialog.show(getSupportFragmentManager(), "");
    }

}
