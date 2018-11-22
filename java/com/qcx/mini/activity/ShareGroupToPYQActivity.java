package com.qcx.mini.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class ShareGroupToPYQActivity extends BaseActivity {

    private static String appid = "wx63cb1dcf8f5b1d6d";
    private static String secret = "d13c9406f70013fc5303e503ba1072f8";
    private static String URLStr = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";
    private String title;
    private long groupId;
    private int groupType;
    private static String travel_img_name = "group.jpg";

    @BindView(R.id.share_group_to_pyq_img)
    ImageView iv_pro;
    @BindView(R.id.share_group_to_pyq_title)
    TextView tv_title;
    @BindView(R.id.share_group_to_pyq_shareView)
    View shareView;

    @OnClick(R.id.share_travel_submit)
    void onSubmit(){
        saveBitmap(getCacheBitmapFromView(shareView));
        ShareUtil.shareTravelImg(this, ConstantString.FilePath.getImageFilePath() + File.separator + travel_img_name);
    }
    @OnClick(R.id.share_travel_cancel)
    void cancel(){
        finish();
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_share_group_to_pyq;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        groupId=getIntent().getLongExtra("groupId",0);
        groupType=getIntent().getIntExtra("groupType",0);
        title=getIntent().getStringExtra("title");
        tv_title.setText(title);
        getToken();
        showLoadingDialog();
    }

    private void getToken() {
        String url = String.format(Locale.CHINA, "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);
        OkGo.<String>get(url).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                try {
                    String s = response.body();
                    JSONObject jsonObject = new JSONObject(s);
                    String token = jsonObject.getString("access_token");
                    if (token != null) {
                        getMiniPro(token);
                    }else {
                        hideLoadingDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoadingDialog();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                hideLoadingDialog();
            }
        });
    }

    void getMiniPro(String token) {
        String url = URLStr.concat(token);
        Map<String, Object> params = new HashMap<>();
        String scene=String.format(Locale.CHINA,"%d,%d",groupId,groupType);
        params.put("scene",scene);
        LogUtil.i("id="+groupId+"   type="+groupType+" scene="+scene);
//        params.put("page", String.format(Locale.CHINA,"/src/lineInfo/lineInfo?groupId=%d&groupType=%d",groupId,groupType));
        params.put("page", String.format(Locale.CHINA,"src/lineInfo/lineInfo"));
        params.put("auto_color", true);
        params.put("width", 500);
        Map<String, Integer> color = new HashMap<>();
        color.put("r", 200);
        color.put("g", 0);
        color.put("b", 0);
        params.put("line_color", color);
        try {
            OkGo.<Bitmap>post(url)
                    .upJson(new JSONObject(params))
                    .execute(new BitmapCallback() {
                        @Override
                        public void onSuccess(Response<Bitmap> response) {
                            iv_pro.setImageBitmap(response.body());
                            hideLoadingDialog();
                        }

                        @Override
                        public void onError(Response<Bitmap> response) {
                            super.onError(response);
                            hideLoadingDialog();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            hideLoadingDialog();
        }
    }

    public void saveBitmap(final Bitmap bm) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request("android.permission.WRITE_EXTERNAL_STORAGE")
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            File f = new File(ConstantString.FilePath.getImageFilePath(), travel_img_name);
                            if (f.exists()) {
                                f.delete();
                            } else {
                                try {
                                    f.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                FileOutputStream out = new FileOutputStream(f);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.flush();
                                out.close();
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            DialogUtil.showSetPermissionDialog(ShareGroupToPYQActivity.this,getSupportFragmentManager(), "SD卡读写权限");
                        }
                    }
                });


    }

    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
