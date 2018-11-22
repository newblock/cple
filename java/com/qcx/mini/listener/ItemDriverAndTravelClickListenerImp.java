package com.qcx.mini.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.TravelDetail_NoPayActivity;
import com.qcx.mini.activity.UserInfoActivity;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.CommentDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/2.
 */

public class ItemDriverAndTravelClickListenerImp implements ItemDriverAndTravelClickListener {
    private FragmentActivity activity;

    public ItemDriverAndTravelClickListenerImp(FragmentActivity activity){
        this.activity=activity;
    }

    @Override
    public void onIconClick(long phoneNum) {
        if(activity==null) return;
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra("phone", phoneNum);
        activity.startActivity(intent);
    }

    @Override
    public void onAttentionClick(DriverAndTravelEntity data, ImageView iv_attention) {
        if (activity==null) return;
        if (data.isAttention()) {
            cancelAttention(data, iv_attention);
        } else {
            attention(data, iv_attention);
        }
    }

    @Override
    public void onTravelClick(TravelEntity data) {
        if(data==null||activity==null) {
            LogUtil.i("activity=null");
            return;
        }
        Intent intent=new Intent(activity, TravelDetail_NoPayActivity.class);
        intent.putExtra("travelId",data.getTravelId());
        intent.putExtra("travelType",data.getType());
        activity.startActivity(intent);
    }

    @Override
    public void onLikesClick(DriverAndTravelEntity data, ImageView likeViw, TextView likeNum) {
        likes(data, likeViw, likeNum);
    }

    @Override
    public void onMessageClick(DriverAndTravelEntity data, final TextView messageNum) {
        if (data == null || data.getTravelVo() == null||activity==null) return;
        final TravelEntity travelEntity = data.getTravelVo();
        new CommentDialog()
                .initData(travelEntity.getCommentsNum(), data.getPhone(), travelEntity.getTravelId())
                .setListener(new CommentDialog.OnCommentSuccessListener() {
                    @Override
                    public void onSuccess(int commentNum) {
                        travelEntity.setCommentsNum(commentNum);
                        messageNum.setText(String.valueOf(travelEntity.getCommentsNum()));
                    }
                })
                .show(activity.getSupportFragmentManager(), "");
    }

    @Override
    public void onShareClick(DriverAndTravelEntity data) {

        if (data == null || data.getTravelVo() == null||activity==null) {
            return;
        }
        TravelEntity travelEntity = data.getTravelVo();
        ShareTravelEntity travel = new ShareTravelEntity();
        travel.setStart(travelEntity.getStart());
        travel.setEnd(travelEntity.getEnd());
        travel.setStartAddress(travelEntity.getStartAddress());
        travel.setEndAddress(travelEntity.getEndAddress());
        travel.setIcon(data.getPicture());
        travel.setName(data.getNickName());
        travel.setPrice(travelEntity.getTravelPrice());
        travel.setSeatsNum(String.valueOf(travelEntity.getSeats()));
        travel.setTravelId(travelEntity.getTravelId());
        travel.setTravelType(travelEntity.getType());
        travel.setStartTime(travelEntity.getStartTimeTxt());
        travel.setCar(data.getCar());
        travel.setSurplusSeats(data.getTravelVo().getSurplusSeats());
        travel.setAge(data.getAge());
        travel.setSex(data.getSex());
        travel.setWaypoints(travelEntity.getWaypoints());
        ShareUtil.shareTravel(activity.getSupportFragmentManager(), User.getInstance().getPhoneNumber(), travel);
    }

    private void attention(final DriverAndTravelEntity data, final ImageView iv_attention) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());//attentione
        params.put("attentione", data.getPhone());
        Request.post(URLString.changAttention, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(true);
                    iv_attention.setImageResource(R.mipmap.btn_followed_mini);
                    new CenterImgDialog().show(activity.getSupportFragmentManager(), "");
                } else {
                    onError("操作失败");
                }
            }

        });
    }

    public void cancelAttention(final DriverAndTravelEntity data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(activity, data.getPhone(), new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(false);
                    iv_attention.setImageResource(R.mipmap.btn_follow_mini);
                } else {
                    onError("操作失败");
                }
            }
        });
    }

    public void likes(final DriverAndTravelEntity data, final ImageView likeViw, final TextView likeNum) {
        if (data == null || data.getTravelVo() == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", data.getTravelVo().getTravelId());
        String url;
        if (data.getTravelVo().isLiked()) {
            url = URLString.travelUnlike;
        } else {
            url = URLString.travelLike;
        }

        Request.post(url, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    if (data.getTravelVo().isLiked()) {
                        likeViw.setImageResource(R.mipmap.btn_good_mini);
                        data.getTravelVo().setLikesNum(data.getTravelVo().getLikesNum() - 1);
                        likeNum.setText(String.valueOf(data.getTravelVo().getLikesNum()));
                        data.getTravelVo().setLiked(false);
                    } else {
                        likeViw.setImageResource(R.mipmap.btn_goodyes_mini);
                        data.getTravelVo().setLikesNum(data.getTravelVo().getLikesNum() + 1);
                        likeNum.setText(String.valueOf(data.getTravelVo().getLikesNum()));
                        data.getTravelVo().setLiked(true);
                        ToastUtil.showToast("点赞成功");
                    }
                }
            }

            @Override
            public void onError(String errorInfo) {
            }
        });
    }
}
