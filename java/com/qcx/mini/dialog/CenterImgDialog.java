package com.qcx.mini.dialog;

import android.view.View;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;

/**
 * Created by Administrator on 2018/2/1.
 */

public class CenterImgDialog extends BaseDialog {
    private CenterImg img;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_center_img;
    }

    @Override
    public void initView(View view) {
        ImageView imageView=view.findViewById(R.id.dialog_center_img);
        if(img!=null) {
            imageView.setImageResource(img.getImgId());
        }else {
            imageView.setImageResource(R.mipmap.icon_pop_follow_confirm);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public int getTheme() {
        return R.style.style_alpha_dialog;
    }

    @Override
    public float getDimAmount() {
        return 0f;
    }

    public CenterImgDialog setImg(CenterImg img) {
        this.img = img;
        return this;
    }

    public enum CenterImg{
        ATTENTION("关注成功",R.mipmap.icon_pop_follow_confirm),
        CANCEL("取消成功",R.mipmap.icon_pop_cancel_confirm);

        private String info;
        private int imgId;

        CenterImg(String info, int imgId) {
            this.info = info;
            this.imgId = imgId;
        }

        public String getInfo() {
            return info;
        }

        public int getImgId() {
            return imgId;
        }
    }

    public interface OnDialogDismissListener{
        void onDismiss();
    }
}
