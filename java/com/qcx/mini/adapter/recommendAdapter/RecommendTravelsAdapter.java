package com.qcx.mini.adapter.recommendAdapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.PageRecycleView;
import com.qcx.mini.widget.pageRecyclerView.PagingScrollHelper;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RecommendTravelsAdapter extends BaseRecyclerViewAdapter<MainRecommendTravelsEntity.TravelsListEntity, RecommendTravelsAdapter.RecommendTravelsViewHolder> {
    private Context context;
    private int titlePosition = -1;
    private OnRecommendTravelsOnClicklistener listener;

    public OnRecommendTravelsOnClicklistener getListener() {
        return listener;
    }


    public RecommendTravelsAdapter(Context context, OnRecommendTravelsOnClicklistener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void setDatas(List<MainRecommendTravelsEntity.TravelsListEntity> datas) {
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        super.datas.addAll(datas);
        notifyDataSetChanged();
//        super.setDatas(datas);
    }

    public RecommendTravelsAdapter(Context context, List<MainRecommendTravelsEntity.TravelsListEntity> datas, OnRecommendTravelsOnClicklistener listener) {
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
    public RecommendTravelsAdapter.RecommendTravelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendTravelsAdapter.RecommendTravelsViewHolder(inflater.inflate(R.layout.item_recommend_travels, parent, false));
    }

    @Override
    public void onBindViewHolder(RecommendTravelsAdapter.RecommendTravelsViewHolder holder, final int position) {
        holder.setEntitys(getItem(position));
    }

    class RecommendTravelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,PagingScrollHelper.onPageChangeListener {
        MainRecommendTravelsEntity.TravelsListEntity entitys;
        ItemTravelsAdapter adapter;
        private int lastPosition;
        ImageView iv_icon;
        TextView tv_name, tv_info;
//        View  v_topRightBtn0, v_topRightBtn1;
        View  v_topRightBtn1;

//        ViewPager pager;
        PageRecycleView rv_itemTravels;

        private boolean isScroll=false;//用于判断是否是手势滑动翻页

        public RecommendTravelsViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.item_recommend_travels_icon);
            tv_name = itemView.findViewById(R.id.item_recommend_travels_name);
            tv_info = itemView.findViewById(R.id.item_recommend_travels_info);

//            v_topRightBtn0 = itemView.findViewById(R.id.item_recommend_travels_top_right_type0);
            v_topRightBtn1 = itemView.findViewById(R.id.item_recommend_travels_top_right_type1);

            setViewListener();
            adapter = new ItemTravelsAdapter(context, listener);

            rv_itemTravels = itemView.findViewById(R.id.item_recommend_travels_item_travels);
            LinearLayoutManager manager=new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);

            rv_itemTravels.addItemDecoration(new SpaceItemDecoration(8 * UiUtils.getPixelH() / 375));
            rv_itemTravels.setLayoutManager(manager);

//            PagingScrollHelper helper=new PagingScrollHelper();
//            helper.setUpRecycleView(rv_itemTravels);
//            helper.setOnPageChangeListener(this);

            rv_itemTravels.setAdapter(adapter);
        }

        void setEntitys(MainRecommendTravelsEntity.TravelsListEntity entitys) {
            this.entitys = entitys;
            bindData();
        }

        private void bindData() {
            adapter.setDatas(entitys.getTravelVoList());
            adapter.setIcon(entitys.getOwnerPicture());
            adapter.setName(entitys.getOwnerNickName());
            rv_itemTravels.setPage(2);

            Picasso.with(context)
                    .load(entitys.getOwnerPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(entitys.getOwnerNickName());
            String info = String.format("%s %s", entitys.getOwnerCar(), DateUtil.formatTime2String(entitys.getLastTimeOnline(),false));
            tv_info.setText(info);
            if(entitys.isAttentioned()){
//                v_topRightBtn0.setVisibility(View.VISIBLE);
                v_topRightBtn1.setVisibility(View.GONE);
            }else {
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
        public void onClick(View v) {
            if (listener == null) return;
            switch (v.getId()) {
                case R.id.item_recommend_travels_icon:
                    listener.onHeadIconClick("");
                    break;
//                case R.id.item_recommend_travels_top_right_type0:
//                    listener.onTopRightBtnClick(true,entitys,v_topRightBtn0,v_topRightBtn1);
//                    break;
                case R.id.item_recommend_travels_top_right_type1:
                    listener.onTopRightBtnClick(false,entitys,v_topRightBtn1,v_topRightBtn1);
                    break;
            }
        }

        @Override
        public void onPageChange(int index) {
            int count=rv_itemTravels.getAdapter().getItemCount();
            if (index == count - 2 && count % 10 == 0 && lastPosition < index) {
                Map<String, Object> parame = new HashMap<>();
                parame.put("token", User.getInstance().getToken());
                parame.put("feedPhone", entitys.getTravelVoList().get(index).getTravelPhone());
                parame.put("pageNo", count / 10 + 1);
                parame.put("startTime", entitys.getTravelVoList().get(count - 1).getStartTime());
                parame.put("createTime", entitys.getTravelVoList().get(count - 1).getCreateTime());
                Request.post(URLString.eachFeedList, parame, new EntityCallback(MainRecommendItemTravelsEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        try {
                            MainRecommendItemTravelsEntity items = (MainRecommendItemTravelsEntity) t;
                            if (items.getStatus() == 200) {
                                if (items.getEachFeedList() != null && items.getEachFeedList().getTravelVoList() != null && items.getEachFeedList().getTravelVoList().size() > 0) {
                                    adapter.addDatas(items.getEachFeedList().getTravelVoList());
                                }
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onError(String errorInfo) {

                    }
                });
            }
            lastPosition=index;
        }
    }


    public interface OnRecommendTravelsOnClicklistener{
        void onLikesClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, ImageView likeViw, TextView likeNum);
        void onMessageClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, TextView messageNum);
        void onShareClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data, TextView shareNum, String icon, String name);
        void onTravelOperClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data);
        void onTravelClick(MainRecommendTravelsEntity.MainRecommendItemTravelEntity data);
        void onHeadIconClick(String phoneNum);
        void onTopRightBtnClick(boolean isAttention, MainRecommendTravelsEntity.TravelsListEntity data, View attention, View noAttention);
    }
}
