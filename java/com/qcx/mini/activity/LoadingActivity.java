package com.qcx.mini.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.qcx.mini.BuildConfig;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.entity.VersionInfoEntity;
import com.qcx.mini.listener.LocationListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.service.UpLoadLocationService;
import com.qcx.mini.utils.Location;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.SystemUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.functions.Action1;


/**
 * 启动、加载页
 */
public class LoadingActivity extends BaseActivity {
    private Location location = Location.getInstance();


    private static final String LOCATION = "location";
    private static final String VERSION_CODE = "version_code";
    private static final String WRITE_MAP_STYLE = "write_map_style";
    private Map<String, Boolean> loadOver;

    private void initLoadParams() {
        loadOver = new HashMap<>();
        loadOver.put(LOCATION, false);
        loadOver.put(VERSION_CODE, false);
        loadOver.put(WRITE_MAP_STYLE, false);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_loading;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                finishLocation();
            }

            @Override
            public void onError(int errorCode) {
                finishLocation();
            }

            @Override
            public void onNoLocationPermission() {
                finishLocation();
            }
        };
        initLoadParams();

        getVersionCode();
        initMapStyle();
        location();
        if (!BuildConfig.DEBUG){
            Intent intent = new Intent(getApplicationContext(), UpLoadLocationService.class);
            getApplicationContext().startService(intent);
        }
    }

    //将style.data写入SD卡
    private void initMapStyle() {
        new RxPermissions(this)
                .request("");
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            String mapStylePath = ConstantString.FilePath.getMapStyleFilePath();
                            File file = new File(mapStylePath);
                            if (file.exists() && SharedPreferencesUtil.getAppSharedPreferences().getInt(ConstantString.SharedPreferencesKey.SP_MAP_STYLE_CODE) == BuildConfig.VERSION_CODE) {
                                finishWriteMapStyle();
                                return;
                            } else if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                InputStream in = getResources().openRawResource(R.raw.style);
                                FileOutputStream out = new FileOutputStream(file);
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while ((len = (in.read(buffer))) != -1) {
                                    out.write(buffer, 0, len);
                                }
                                LogUtil.i("put map code=" + BuildConfig.VERSION_CODE);
                                SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_MAP_STYLE_CODE, BuildConfig.VERSION_CODE);
                                in.close();
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            finishWriteMapStyle();
                        } else {
                            finishWriteMapStyle();
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listener = null;
        if(location!=null){
            location.setListener(null);
        }
    }

    private void getVersionCode() {
        Map<String, Object> params = new HashMap<>();
        params.put("flag", 0);
        Request.post(URLString.version, params, new EntityCallback(VersionInfoEntity.class) {
            @Override
            public void onSuccess(Object t) {
                VersionInfoEntity versionInfoEntity = (VersionInfoEntity) t;
                VersionInfoEntity.Version version = versionInfoEntity.getVersion();
                int code = BuildConfig.VERSION_CODE;
                if (version.getVersionNumberMax() > code && code > version.getVersionNumberMin()) {
                    new QAlertDialog().setBTN(QAlertDialog.BTN_TWOBUTTON)
                            .setImg(QAlertDialog.IMG_ALERT)
                            .setTitleText("发现新版本")
                            .setContentText("是否更新?")
                            .setCancelAble(false)
                            .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                @Override
                                public void onClick(QAlertDialog dialog) {
                                    SystemUtil.toMarket(LoadingActivity.this);
                                }
                            })
                            .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                                @Override
                                public void onClick(QAlertDialog dialog) {
                                    finishGetVersionCode();
                                }
                            })
                            .show(getSupportFragmentManager(), "");
                } else if (version.getVersionNumberMin() > code) {
                    new QAlertDialog().setBTN(QAlertDialog.BTN_ONEBUTTON)
                            .setImg(QAlertDialog.IMG_ALERT)
                            .setTitleText("发现新版本")
                            .setContentText("去更新")
                            .setCancelAble(false)
                            .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                @Override
                                public void onClick(QAlertDialog dialog) {
                                    SystemUtil.toMarket(LoadingActivity.this);
                                }
                            })
                            .show(getSupportFragmentManager(), "");
                } else {
                    finishGetVersionCode();
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishGetVersionCode();
            }
        });
    }

    private void finishGetVersionCode() {
        loadOver.put(VERSION_CODE, true);
        requestFinishLoadingActivity();
    }

    private void finishLocation() {
        loadOver.put(LOCATION, true);
        listener = null;
        requestFinishLoadingActivity();
    }

    private void finishWriteMapStyle() {
        loadOver.put(WRITE_MAP_STYLE, true);
        requestFinishLoadingActivity();
    }

    boolean isFinish = false;

    private void requestFinishLoadingActivity() {
        if (isFinish) {
            return;
        }
        Set<String> keys = loadOver.keySet();
        for (String s : keys) {
            LogUtil.i(s + " " + loadOver.get(s));
        }
        for (Boolean b : loadOver.values()) {
            if (!b) {
                return;
            }
        }
        isFinish = true;
        startActivity(new Intent(LoadingActivity.this, WelcomePageActivity.class));
        finish();
    }

    private void location() {
        SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY, "");
        mRxPermissions.request("android.permission.ACCESS_COARSE_LOCATION")
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            location.startLocation(listener, null);
                        } else {
                            if (listener != null) {
                                listener.onNoLocationPermission();
                            }
                        }
                    }
                });
    }

    private LocationListener listener;

    //已发布版本的 versionName和versionCode
    private int versionCode(String versionName) {
        Map<String, Integer> versionCode = new HashMap<>();
        versionCode.put("3.0.0", 180302);
        versionCode.put("3.0.1", 180316);
        versionCode.put("3.0.2", 180417);
        versionCode.put("3.0.3", 180418);
        versionCode.put("3.0.4", 180428);
        versionCode.put("3.0.5", 180511);
        versionCode.put("3.0.6", 180522);
        versionCode.put("3.0.7", 180615);
        return versionCode.get(versionName);
    }
}
