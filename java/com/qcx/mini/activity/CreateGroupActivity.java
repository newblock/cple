package com.qcx.mini.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.lzy.okgo.OkGo;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.CreateGroupEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ImageCompress;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.PictureUtil;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建群
 */
public class CreateGroupActivity extends BaseActivity {
    private static final String CAMERA_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "cameraName.jpg";
    private static final String ICON_FILE_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "groupIcon.jpg";
//    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final int REQUEST_END_ADDRESS_CODE = 10;
    private final int REQUEST_START_ADDRESS_CODE = 11;

    private Tip startAddress;
    private Tip endAddress;
    private String groupTitle;
    private int advisePrice;
    private int groupType;
    private String groupNotice;
    private String groupManager;
    private String groupBanner;

    @BindView(R.id.create_group_start_address_input)
    TextView tv_start;
    @BindView(R.id.create_group_end_address_input)
    TextView tv_end;
    @BindView(R.id.create_group_title1)
    TextView tv_title1;
    @BindView(R.id.create_group_title2)
    TextView tv_title2;
    @BindView(R.id.create_group_title3)
    TextView tv_title3;
    @BindView(R.id.create_group_icon_img)
    ImageView iv_icon;
    @BindView(R.id.create_group_submit)
    TextView tv_submit;
    @BindView(R.id.create_group_name)
    EditText et_name;
    @BindView(R.id.create_group_describe)
    EditText et_describe;
    @BindView(R.id.create_group_manager_num)
    EditText et_manager;
    @BindView(R.id.create_group_price)
    EditText et_price;
    @BindView(R.id.create_group_line)
    View v_line;
    @BindView(R.id.create_group_startImg)
    View v_startImg;
    @BindView(R.id.create_group_exchange)
    View v_exchange;
    @BindView(R.id.create_group_price_view)
    View v_price;

    @OnClick(R.id.create_group_submit)
    void submit() {
        if (canSubmit()) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
            params.put("endAddress", endAddress.getName());
            params.put("endCity", endAddress.getDistrict());
            params.put("groupNotice", groupNotice);
            params.put("groupTitle", groupTitle);
            params.put("groupManager", groupManager);
            params.put("groupType", groupType);
            if (groupType != ConstantValue.GroupType.SCENIC) {
                params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
                params.put("startAddress", startAddress.getName());
                params.put("startCity", startAddress.getDistrict());
                params.put("advisePrice", advisePrice);
            }
            showLoadingDialog();
            com.qcx.mini.net.Request.post(URLString.groupCreate, params, new EntityCallback(CreateGroupEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    CreateGroupEntity entity = (CreateGroupEntity) t;
                    if (entity.getStatus() == 200) {
                        submitImg(entity.getGroupId());
                    } else {
                        hideLoadingDialog();
                    }
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    hideLoadingDialog();
                }
            });
        }
    }

    @OnClick(R.id.create_group_icon_view)
    void getImg() {
        File file = new File(ICON_FILE_NAME);
        if (!file.exists()) {
            try {
                boolean dd=file.createNewFile();
                if(dd){
                    LogUtil.i("createNewFile return false");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DialogUtil.pictureDialog(this, CAMERA_NAME);
    }

    @OnClick(R.id.create_group_start_address_input)
    void changeStartAddress() {
        Intent intent = new Intent(this, SetAddressActivity.class);
        String startHint = "";
        intent.putExtra("inputHint", startHint);
        startActivityForResult(intent, REQUEST_START_ADDRESS_CODE);
    }

    @OnClick(R.id.create_group_end_address_input)
    void changeEndAddress() {
        Intent intent = new Intent(this, SetAddressActivity.class);
        String endHint = "";
        intent.putExtra("inputHint", endHint);
        startActivityForResult(intent, REQUEST_END_ADDRESS_CODE);
    }

    @OnClick(R.id.create_group_exchange)
    void exchange() {
        Tip tip;
        tip = startAddress;
        startAddress = endAddress;
        endAddress = tip;
        addressChanged();
    }

    @OnClick(R.id.create_group_title1)
    void title1() {
        setPageTitelBack(0);
        groupType = ConstantValue.GroupType.WORK;
        showAddressEdit(true);
        canSubmit();
    }

    @OnClick(R.id.create_group_title2)
    void title2() {
        setPageTitelBack(1);
        groupType = ConstantValue.GroupType.ACROSS_CITY;
        showAddressEdit(true);
        canSubmit();
    }

    @OnClick(R.id.create_group_title3)
    void title3() {
        setPageTitelBack(2);
        groupType = ConstantValue.GroupType.SCENIC;
        showAddressEdit(false);
        canSubmit();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_create_group;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("创建线路群", true, false);
        setPageTitelBack(0);
        et_name.addTextChangedListener(watcher);
        et_describe.addTextChangedListener(watcher);
        et_manager.addTextChangedListener(watcher);
        et_price.addTextChangedListener(watcher);

        et_describe.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et_describe.setGravity(Gravity.CENTER_VERTICAL);
        et_describe.setSingleLine(false);
        et_describe.setHorizontallyScrolling(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_START_ADDRESS_CODE:
                    startAddress = data.getParcelableExtra("tip");
                    getCityFromTip(startAddress);
                    addressChanged();
                    break;
                case REQUEST_END_ADDRESS_CODE:
                    endAddress = data.getParcelableExtra("tip");
                    getCityFromTip(endAddress);
                    addressChanged();
                    break;

                case PictureUtil.REQUEST_CODE_PHOTO:
                    if (data != null) {
                        Uri uri = data.getData();
                        compressImage(uri, ICON_FILE_NAME);
                    }
                    break;
                case PictureUtil.REQUEST_CODE_CAMERA:
                    compressImage(PictureUtil.getImageContentUri(new File(CAMERA_NAME), this), ICON_FILE_NAME);
                    break;
            }
        }
        canSubmit();
    }

    private boolean canSubmit() {
        try {
            groupTitle = et_name.getText().toString();
            groupNotice = et_describe.getText().toString();
            groupManager = et_manager.getText().toString();
            if (!TextUtils.isEmpty(et_price.getText().toString())) {
                advisePrice = Integer.parseInt(et_price.getText().toString());
            } else {
                advisePrice = 0;
            }
            if ((groupType != ConstantValue.GroupType.SCENIC && (startAddress == null || advisePrice == 0))
                    || endAddress == null
                    || TextUtils.isEmpty(groupTitle)
                    || TextUtils.isEmpty(groupNotice)
                    || TextUtils.isEmpty(groupManager)
                    || TextUtils.isEmpty(groupBanner)) {
                tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray1));
                return false;
            }
            if (groupNotice.length() > 60) {
                ToastUtil.showToast("群介绍最多输入60个字符");
                return false;
            }
            if (groupTitle.length() > 15) {
                ToastUtil.showToast("群名称最多输入15个字符");
                return false;
            }
            if (groupManager.length() > 20) {
                ToastUtil.showToast("运营微信最多输入20个字符");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray1));
            return false;
        }
        tv_submit.setBackground(getResources().getDrawable(R.drawable.bg_circular_gradient_blue));
        return true;
    }

    private void addressChanged() {
        if (startAddress != null) {
            tv_start.setText(startAddress.getName());
        } else {
            tv_start.setText("");
        }
        if (endAddress != null) {
            tv_end.setText(endAddress.getName());
        } else {
            tv_end.setText("");
        }
    }

    private void setPageTitelBack(int pagePostion) {
        if (pagePostion == 0) {
            tv_title1.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title1.setTextColor(0xFF484848);
            tv_title2.setBackgroundColor(0);
            tv_title2.setTextColor(0xFFB9BDC3);
            tv_title3.setBackgroundColor(0);
            tv_title3.setTextColor(0xFFB9BDC3);
        } else if (pagePostion == 1) {
            tv_title1.setBackgroundColor(0);
            tv_title1.setTextColor(0xFFB9BDC3);
            tv_title2.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title2.setTextColor(0xFF484848);
            tv_title3.setBackgroundColor(0);
            tv_title3.setTextColor(0xFFB9BDC3);
        } else if (pagePostion == 2) {
            tv_title1.setBackgroundColor(0);
            tv_title1.setTextColor(0xFFB9BDC3);
            tv_title2.setBackgroundColor(0);
            tv_title2.setTextColor(0xFFB9BDC3);
            tv_title3.setBackground(getResources().getDrawable(R.drawable.bg_circular_white));
            tv_title3.setTextColor(0xFF484848);
        }
    }

    private void compressImage(final Uri filePath, final String reFilePath) {
        LogUtil.i(filePath + "     " + reFilePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageCompress compress = new ImageCompress();
                ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();
                options.uri = filePath;
                options.maxHeight = 1800;
                options.maxWidth = 3600;
                options.destFile = new File(reFilePath);
                compress.compressFromUri(CreateGroupActivity.this, options);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(CreateGroupActivity.this)
                                .load(new File(reFilePath))
                                .error(R.mipmap.img_me)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(iv_icon);
                        groupBanner = reFilePath;
                        canSubmit();
                    }
                });
            }
        }).start();
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            canSubmit();
        }
    };

    private void showAddressEdit(boolean isShowStart) {
        if (isShowStart) {
            v_line.setVisibility(View.VISIBLE);
            v_exchange.setVisibility(View.VISIBLE);
            v_startImg.setVisibility(View.VISIBLE);
            tv_start.setVisibility(View.VISIBLE);
            v_price.setVisibility(View.VISIBLE);
        } else {
            v_line.setVisibility(View.GONE);
            v_exchange.setVisibility(View.GONE);
            v_startImg.setVisibility(View.GONE);
            tv_start.setVisibility(View.GONE);
            v_price.setVisibility(View.GONE);
        }
    }

    private void submitImg(final long groupId) {
        OkGo.<String>post(URLString.uploadGroupBanner)
                .isMultipart(true)
                .params("token", User.getInstance().getToken())
                .params("groupId", groupId)
                .params("groupBanner", new File(groupBanner))
                .execute(new EntityCallback(IntEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        hideLoadingDialog();
                        Intent intent = new Intent(CreateGroupActivity.this, AddGroupMembersActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("groupType", groupType);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String errorInfo) {
                        super.onError(errorInfo);
                        hideLoadingDialog();
                        Intent intent = new Intent(CreateGroupActivity.this, AddGroupMembersActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("groupType", groupType);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void getCityFromTip(final Tip tip) {
        if (tip == null || tip.getPoint() == null) {
            return;
        }
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null) {
                    RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
                    if (address != null) {
                        tip.setDistrict(address.getCity());
                    }
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLng = new LatLonPoint(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(latLng, 5, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }
}
