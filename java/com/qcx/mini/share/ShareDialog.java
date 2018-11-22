package com.qcx.mini.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.ShareGroupToPYQActivity;
import com.qcx.mini.activity.ShareTravelAvtivity;
import com.qcx.mini.dialog.BaseDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ShareGroupToPYQDialog;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.H5PageUtil;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ShareDialog extends BaseDialog implements View.OnClickListener {
    private static String travel_img_name = "travel.jpg";
    private ShareHelper mHelper;
    private Type type;
    private ShareParmas shareParmas;
    private ShareType shareType;
    private ShareTravelEntity travel;
    View v_travel;

    private long groupId;
    private int groupType;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_share;
    }

    @Override
    public void initView(View view) {
        mHelper = new ShareHelper(getContext());
        view.findViewById(R.id.dialog_share_cancel).setOnClickListener(this);
        view.findViewById(R.id.dialog_share_packageInfo).setOnClickListener(this);
        view.findViewById(R.id.dialog_share_wechat).setOnClickListener(this);
        view.findViewById(R.id.dialog_share_wechat_moment).setOnClickListener(this);
        v_travel = view.findViewById(R.id.share_travel_view);
        showTravel(travel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_share_cancel:
                dismiss();
                break;
            case R.id.dialog_share_packageInfo:
                H5PageUtil.toRedPackageIntroducePage(getContext());
                break;
            case R.id.dialog_share_wechat:
                share_weChat();
                break;
            case R.id.dialog_share_wechat_moment:
                share_weChat_moment();
                break;
        }
    }


    public int getGravity() {
        return Gravity.BOTTOM;
    }

    public int getBackgroundDrawableResource() {
        return R.drawable.bg_circular_white_top;
    }


    public ShareDialog setType(Type type) {
        this.type = type;
        return this;
    }

    public ShareDialog setShareParmas(ShareParmas shareParmas) {
        this.shareParmas = shareParmas;
        return this;
    }

    public ShareDialog setTravel(ShareTravelEntity travel) {
        this.travel = travel;
        return this;
    }

    public ShareDialog setGroupParams(long id, int type) {
        this.groupId = id;
        this.groupType = type;
        return this;
    }

    private void share_weChat() {
        if (shareParmas == null) {
            ToastUtil.showToast("分享失败");
        } else {
            shareParmas.setShareType(ShareType.WX_CHAT);
            share(shareParmas);
        }
    }

    private void share_weChat_moment() {
        if (shareParmas == null) {
            ToastUtil.showToast("分享失败");
        } else {
            shareParmas.setShareType(ShareType.WX_PYQ);
            share(shareParmas);
        }
    }

    private void shareTravel(ShareTravelEntity travel) {
        if (travel != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("travelId", travel.getTravelId());
            params.put("travelType", travel.getTravelType());
            Request.post(URLString.travelShare, params, new EntityCallback(null) {
                @Override
                public void onSuccess(Object t) {

                }
            });

        }
    }


    private void share(ShareParmas shareParmas) {
        switch (type) {
            case TRAVEL:
                if (shareParmas.getShareType() == ShareType.WX_CHAT) {
                    mHelper.shareWXMiniProgram(shareParmas, getCacheBitmapFromView(v_travel));
                    shareTravel(travel);
                } else {
                    Intent intent = new Intent(getContext(), ShareTravelAvtivity.class);
                    intent.putExtra("travel", travel);
                    startActivity(intent);
                }
                break;
            case GROUP:
                if (shareParmas.getShareType() == ShareType.WX_CHAT) {
                    mHelper.shareWXMiniProgram(shareParmas);
                } else {//朋友圈
                    Intent intent = new Intent(getContext(), ShareGroupToPYQActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("groupType", groupType);
                    intent.putExtra("title", shareParmas.getTitle());
                    startActivity(intent);
                }
                break;
            default:
                ToastUtil.showToast("不支持该分享");
                break;
        }
        dismiss();
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

    private void showTravel(ShareTravelEntity travel) {
        if (travel != null) {
            ImageView iv_icon = v_travel.findViewById(R.id.view_share_travel_icon);
            if(!TextUtils.isEmpty(travel.getIcon())){
                Picasso.with(getContext())
                        .load(travel.getIcon())
                        .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(iv_icon);
            }
            ((TextView) v_travel.findViewById(R.id.view_share_travel_name)).setText(travel.getName());
            ((TextView) v_travel.findViewById(R.id.view_share_travel_start_time)).setText(travel.getStartTime());
            ((TextView) v_travel.findViewById(R.id.view_share_travel_start_address)).setText(travel.getStartAddress());
            ((TextView) v_travel.findViewById(R.id.view_share_travel_end_address)).setText(travel.getEndAddress());
            String info;
            String text;
            if (travel.getTravelType() == ConstantValue.TravelType.DRIVER) {
                ((TextView) v_travel.findViewById(R.id.view_share_travel_car)).setText(travel.getCar());
                info = String.format(Locale.CHINA, "余座%s 每座%s元", travel.getSurplusSeats(), CommonUtil.formatPrice(travel.getPrice(),0));
                text = "订座";
            } else {
                ((TextView) v_travel.findViewById(R.id.view_share_travel_car)).setText(travel.getAge());
                info = String.format(Locale.CHINA, "人数%s 车费%s元", travel.getSeatsNum(), CommonUtil.formatPrice(travel.getPrice(),0));
                text = "抢单";
            }
            ImageView iv_sex = v_travel.findViewById(R.id.view_share_travel_sex);
            switch (travel.getSex()) {
                case ConstantValue.SexType.MAN:
                    iv_sex.setVisibility(View.VISIBLE);
                    iv_sex.setImageResource(R.mipmap.img_men);
                    break;
                case ConstantValue.SexType.WOMAN:
                    iv_sex.setVisibility(View.VISIBLE);
                    iv_sex.setImageResource(R.mipmap.img_women);
                    break;
                default:
                    iv_sex.setVisibility(View.GONE);
                    break;
            }
            ((TextView) v_travel.findViewById(R.id.view_share_travel_info)).setText(info);
            ((TextView) v_travel.findViewById(R.id.view_share_travel_oper)).setText(text);
        }
    }

    public enum Type {
        TRAVEL, GROUP;
    }

}
