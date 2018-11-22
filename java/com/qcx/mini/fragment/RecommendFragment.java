package com.qcx.mini.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.RecommendTravelsEndtity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder.tag;

/**
 * Created by Administrator on 2017/12/25.
 */

public class RecommendFragment extends BaseFragment {
    private String mToken = User.getInstance().getToken();

    private ItemDriverAndTravelClickListenerImp travelClickListener;
    private ItemDriverAndTravelAdapter recommendTravelAdapter;
    private int travelPage = 1;//推荐行程页码
    private boolean isLoadingReTravel = false;//是否正在加载推荐行程

    @BindView(R.id.recommend_travels_list)
    RecyclerView recommend_travels_list;
    @BindView(R.id.recommend_travels_refresh)
    SmartRefreshLayout refreshLayout;

    protected int getLayoutID() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        travelClickListener = new ItemDriverAndTravelClickListenerImp(getActivity());
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        manager2.setRecycleChildrenOnDetach(true);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        recommend_travels_list.setLayoutManager(manager2);
        recommendTravelAdapter = new ItemDriverAndTravelAdapter(getContext());
        recommend_travels_list.setAdapter(recommendTravelAdapter);
        recommendTravelAdapter.setListener(travelClickListener);

        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                outRect.top = UiUtils.getSize(15);
                outRect.left=UiUtils.getSize(8);
                outRect.right=UiUtils.getSize(8);
                if (position == state.getItemCount() - 1) {
                    outRect.bottom = UiUtils.getSize(20);
                }
            }
        };
        recommend_travels_list.addItemDecoration(decoration);
        recommend_travels_list.setItemViewCacheSize(7);
        recommend_travels_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableAutoLoadmore(false);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getTravelsData(travelPage);
            }
        });
        recommend_travels_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getContext()).resumeTag(tag);
                } else {
                    Picasso.with(getContext()).pauseTag(tag);
                }
            }
        });
        getTravelsData(travelPage);
        showLoadingDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        mToken = "";
        if (mToken != null && !mToken.equals(User.getInstance().getToken())) {
            mToken = User.getInstance().getToken();
            getTravelsData(1);
        }
    }

    private int type= ConstantValue.TravelType.DRIVER;

    public void setType(int type) {
        if(type!=this.type){
            this.type = type;
            getTravelsData(1);
        }

    }

    public void getTravelsData(final int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", page);
        params.put("travelType", type);
        Request.post(URLString.recommendTravel, params, new EntityCallback(RecommendTravelsEndtity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                RecommendTravelsEndtity travelsEndtity = (RecommendTravelsEndtity) t;
                List<DriverAndTravelEntity> travels = travelsEndtity.getRecommendTravel();
                if (page == 1) {
                    travelPage = 1;
                    recommendTravelAdapter.setDatas(travels);
                } else {
                    recommendTravelAdapter.addDatas(travels);
                }
                if (travels != null && travels.size() > 0) {
                    travelPage++;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void finishLoading() {
        hideLoadingDialog();
        if (refreshLayout != null) {
            refreshLayout.finishLoadmore();
        }
    }


    void attention(final DriverAndTravelEntity data, final ImageView iv_attention) {
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
                    new CenterImgDialog().show(getFragmentManager(), "");
                } else {
                    onError("操作失败");
                }
            }

        });
    }

    void cancelAttention(final DriverAndTravelEntity data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(getActivity(), data.getPhone(), new EntityCallback(IntEntity.class) {
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
