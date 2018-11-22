package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.MainRecommendItemTravelsEntity;
import com.qcx.mini.entity.MainRecommendTravelsEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RecommendTravelsAdapter2 extends BaseRecyclerViewAdapter<MainRecommendTravelsEntity.TravelsListEntity, RecommendTravelsAdapter2.RecommendTravelsViewHolder> {
    private Context context;
    private int titlePosition = -1;
    private OnRecommendTravelsOnClicklistener listener;
    private List<RecommendTravelsAdapter2.RecommendTravelsViewHolder> inUsedHolders;
    private List<RecommendTravelsAdapter2.RecommendTravelsViewHolder> notUsedHolders;

    public OnRecommendTravelsOnClicklistener getListener() {
        return listener;
    }


    public RecommendTravelsAdapter2(Context context, OnRecommendTravelsOnClicklistener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        notUsedHolders = new ArrayList<>();
        inUsedHolders = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            notUsedHolders.add(new RecommendTravelsAdapter2.RecommendTravelsViewHolder(inflater.inflate(R.layout.item_recommend_travels, null)));
        }
    }

    public RecommendTravelsAdapter2(Context context, List<MainRecommendTravelsEntity.TravelsListEntity> datas, OnRecommendTravelsOnClicklistener listener) {
        super(context, datas);
        this.context = context;
        this.listener = listener;
    }

    public void setTitlePosition(int titlePosition) {
        this.titlePosition = titlePosition;
    }

    public int getTitlePosition() {
        return titlePosition;
    }

    @Override
    public RecommendTravelsAdapter2.RecommendTravelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(notUsedHolders.size()>0){
            return notUsedHolders.get(0);
        }else {
            RecommendTravelsAdapter2.RecommendTravelsViewHolder holder=new RecommendTravelsAdapter2.RecommendTravelsViewHolder(inflater.inflate(R.layout.item_recommend_travels, null));
            notUsedHolders.add(holder);
            return holder;
        }
    }

    @Override
    public void setDatas(List<MainRecommendTravelsEntity.TravelsListEntity> datas) {
        super.setDatas(datas);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(RecommendTravelsViewHolder holder) {
        super.onViewRecycled(holder);
        inUsedHolders.remove(holder);
        notUsedHolders.add(holder);
    }

    @Override
    public void onBindViewHolder(RecommendTravelsAdapter2.RecommendTravelsViewHolder holder, final int position) {
        if (position < datas.size()){
            holder.setEntitys(getItem(position));
            inUsedHolders.add(holder);
            notUsedHolders.remove(holder);
        }
    }

    class RecommendTravelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ViewPager.OnPageChangeListener {
        MainRecommendTravelsEntity.TravelsListEntity entitys;
        ItemTravelsAdapter adapter;
        int lastPagePosition;
        ViewPager pager;
        ImageView iv_icon;
        TextView tv_name, tv_info;
        //        View  v_topRightBtn0,
        View v_topRightBtn1;
        private boolean isScroll = false;//用于判断是否是手势滑动翻页

        public RecommendTravelsViewHolder(View itemView) {
            super(itemView);
            pager = itemView.findViewById(R.id.item_recommend_travels_item_travels);
            iv_icon = itemView.findViewById(R.id.item_recommend_travels_icon);
            tv_name = itemView.findViewById(R.id.item_recommend_travels_name);
            tv_info = itemView.findViewById(R.id.item_recommend_travels_info);

//            v_topRightBtn0 = itemView.findViewById(R.id.item_recommend_travels_top_right_type0);
            v_topRightBtn1 = itemView.findViewById(R.id.item_recommend_travels_top_right_type1);

            setViewListener();
            adapter = new ItemTravelsAdapter(context, listener);
            pager.addOnPageChangeListener(this);
            pager.setOffscreenPageLimit(2);
            pager.setPageMargin(-24 * UiUtils.getPixelH() / 375);
            pager.setAdapter(adapter);
        }

        void setEntitys(MainRecommendTravelsEntity.TravelsListEntity entitys) {
            this.entitys = entitys;
            bindData();
        }


        private void bindData() {
            adapter.setDatas(entitys.getTravelVoList());
            adapter.setIcon(entitys.getOwnerPicture());
            adapter.setName(entitys.getOwnerNickName());
            pager.setCurrentItem(entitys.getPageNum(), true);

            Picasso.with(context)
                    .load(entitys.getOwnerPicture())
                    .error(R.mipmap.img_me)
                    .resize(70, 70)
                    .into(iv_icon);
            tv_name.setText(entitys.getOwnerNickName());
            String info = String.format("%s %s", entitys.getOwnerCar(), DateUtil.formatTime2String(entitys.getLastTimeOnline(), false));
            tv_info.setText(info);
            if (entitys.isAttentioned()) {
//                v_topRightBtn0.setVisibility(View.VISIBLE);
                v_topRightBtn1.setVisibility(View.GONE);
            } else {
//                v_topRightBtn0.setVisibility(View.GONE);
                v_topRightBtn1.setVisibility(View.VISIBLE);
            }
        }

        void setViewListener() {
            iv_icon.setOnClickListener(this);
//            v_topRightBtn0.setOnClickListener(this);
            v_topRightBtn1.setOnClickListener(this);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            lastPagePosition = position;
        }

        @Override
        public void onPageSelected(int position) {
            if (isScroll) {
                entitys.setPageNum(position);
            }
            if (position == pager.getAdapter().getCount() - 2 && pager.getAdapter().getCount() % 10 == 0 && lastPagePosition < position) {
                Map<String, Object> parame = new HashMap<>();
                parame.put("token", User.getInstance().getToken());
                parame.put("feedPhone", entitys.getTravelVoList().get(position).getTravelPhone());
                parame.put("pageNo", pager.getAdapter().getCount() / 10 + 1);
                parame.put("startTime", entitys.getTravelVoList().get(pager.getAdapter().getCount() - 1).getStartTime());
                parame.put("createTime", entitys.getTravelVoList().get(pager.getAdapter().getCount() - 1).getCreateTime());
                Request.post(URLString.eachFeedList, parame, new EntityCallback(MainRecommendItemTravelsEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        MainRecommendItemTravelsEntity items = (MainRecommendItemTravelsEntity) t;
                        if (items.getStatus() == 200) {
                            if (items.getEachFeedList() != null && items.getEachFeedList().getTravelVoList() != null && items.getEachFeedList().getTravelVoList().size() > 0) {
                                adapter.addDatas(items.getEachFeedList().getTravelVoList());
                            }
                        }
                    }

                    @Override
                    public void onError(String errorInfo) {

                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                isScroll = false;
            }
            if (state == 1) {
                isScroll = true;
            }
        }

        @Override
        public void onClick(View v) {
            if (listener == null) return;
            switch (v.getId()) {
                case R.id.item_recommend_travels_icon:
                    listener.onHeadIconClick(String.valueOf(entitys.getOwnerPhone()));
                    break;
//                case R.id.item_recommend_travels_top_right_type0:
////                    listener.onTopRightBtnClick(true,entitys,v_topRightBtn0,v_topRightBtn1);
//                    break;
                case R.id.item_recommend_travels_top_right_type1:
                    listener.onTopRightBtnClick(false, entitys, v_topRightBtn1, v_topRightBtn1);
                    break;
            }
        }
    }


    public interface OnRecommendTravelsOnClicklistener {

        void onLikesClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, ImageView likeViw, TextView likeNum);

        void onMessageClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, TextView messageNum);

        void onShareClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, TextView shareNum, String icon, String name);

        void onTravelOperClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data);

        void onTravelClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data);

        void onHeadIconClick(String phoneNum);

        void onTopRightBtnClick(boolean isAttention, MainRecommendTravelsEntity.TravelsListEntity data, View attention, View noAttention);
    }
}
