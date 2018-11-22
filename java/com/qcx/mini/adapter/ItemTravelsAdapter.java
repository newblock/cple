package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.entity.MainRecommendTravelsEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class ItemTravelsAdapter extends PagerAdapter {
    private int mChildCount = 0;
    LayoutInflater inflater;
    Context context;
    private int maxViewsLength = 6;
    private String icon;
    private String name;
    private List<MainRecommendTravelsEntity.MainRecommendItemTravelEntity> datas;
    private List<View> views;
    private RecommendTravelsAdapter2.OnRecommendTravelsOnClicklistener listener;

    public ItemTravelsAdapter(Context context, RecommendTravelsAdapter2.OnRecommendTravelsOnClicklistener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        datas = new ArrayList<>();
        views = new ArrayList<>();
        this.listener = listener;
        for (int i = views.size(); i < maxViewsLength; i++) {
            View view = inflater.inflate(R.layout.item_user_travels_2, null);
            ItemTravelsViewHolder holder = new ItemTravelsViewHolder(view);
            view.setTag(holder);
            views.add(view);
        }
    }

    public ItemTravelsAdapter(Context context, List<MainRecommendTravelsEntity.MainRecommendItemTravelEntity> datas, RecommendTravelsAdapter2.OnRecommendTravelsOnClicklistener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        views = new ArrayList<>();
        this.listener = listener;
        this.datas = datas;
//        if (views.size() < datas.size() && views.size() < maxViewsLength) {
//            for (int i = views.size(); i < maxViewsLength; i++) {
//                View view = inflater.inflate(R.layout.item_user_travels_2, null);
//                ItemTravelsViewHolder holder = new ItemTravelsViewHolder(view);
//                view.setTag(holder);
//                views.add(view);
//            }
//        }
    }

    public void setDatas(List<MainRecommendTravelsEntity.MainRecommendItemTravelEntity> datas) {
        if (datas != null) {
            this.datas = datas;
        } else datas.clear();
//        if (views.size() < datas.size() && views.size() < maxViewsLength) {
//            for (int i = views.size(); i < maxViewsLength; i++) {
//                View view = inflater.inflate(R.layout.item_user_travels_2, null);
//                ItemTravelsViewHolder holder = new ItemTravelsViewHolder(view);
//                view.setTag(holder);
//                views.add(view);
//            }
//        }
        notifyDataSetChanged();
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainRecommendTravelsEntity.MainRecommendItemTravelEntity getDatas(int position) {
        return datas.get(position);
    }

    public void addDatas(List<MainRecommendTravelsEntity.MainRecommendItemTravelEntity> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
//            if (views.size() < this.datas.size() && views.size() < maxViewsLength) {
//                for (int i = views.size(); i < maxViewsLength; i++) {
//                    View view = inflater.inflate(R.layout.item_user_travels_2, null);
//                    ItemTravelsViewHolder holder = new ItemTravelsViewHolder(view);
//                    view.setTag(holder);
//                    views.add(view);
//                }
//            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position % views.size());
        ItemTravelsViewHolder holder = (ItemTravelsViewHolder) view.getTag();
        container.addView(view);
        if (position < datas.size()) holder.setData(datas.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position % views.size()));
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
//        if (mChildCount > 0) {
//            mChildCount--;
        return POSITION_NONE;
//        }
//        return super.getItemPosition(object);
    }


    class ItemTravelsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MainRecommendTravelsEntity.MainRecommendItemTravelEntity itemTravel;
        View view;
        TextView tv_time, tv_dingzuo, tv_startAddress, tv_endAddress, tv_price;
        ImageView iv_type;
        ImageView iv_head1, iv_head2, iv_head3, iv_head4;
        ImageView[] heads;
        TextView tv_peoples;
        View v_peoples;
        TextView tv_watchNum, tv_redPackage, tv_message, tv_good, tv_share;
        View v_message, v_share, v_good;
        ImageView iv_good;
        ImageView iv_status;

        public ItemTravelsViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.item_user_travels_start_time);
            tv_dingzuo = view.findViewById(R.id.item_user_travels_dingzuo);
            tv_startAddress = view.findViewById(R.id.item_user_travels_startAddress);
            tv_endAddress = view.findViewById(R.id.item_user_travels_endAddress);
            tv_price = view.findViewById(R.id.item_user_travels_price);
            iv_head1 = view.findViewById(R.id.item_user_travels_headImg1);
            iv_head2 = view.findViewById(R.id.item_user_travels_headImg2);
            iv_head3 = view.findViewById(R.id.item_user_travels_headImg3);
            iv_head4 = view.findViewById(R.id.item_user_travels_headImg4);
            iv_type = view.findViewById(R.id.item_user_travels_type);
            this.view = view.findViewById(R.id.item_user_travels_view);
            tv_peoples = view.findViewById(R.id.item_user_travels_peoples_text);
            v_peoples = view.findViewById(R.id.item_user_travels_peoples_view);
            iv_status = view.findViewById(R.id.item_user_travels_status);
            heads = new ImageView[4];
            heads[0] = iv_head1;
            heads[1] = iv_head2;
            heads[2] = iv_head3;
            heads[3] = iv_head4;


            tv_watchNum = itemView.findViewById(R.id.item_recommend_travels_watchNum);
            tv_redPackage = itemView.findViewById(R.id.item_recommend_travels_red_package);
            tv_message = itemView.findViewById(R.id.item_recommend_travels_message_text);
            tv_good = itemView.findViewById(R.id.item_recommend_travels_good_text);
            tv_share = itemView.findViewById(R.id.item_recommend_travels_share_text);
            iv_good = itemView.findViewById(R.id.item_recommend_travels_good_img);
            v_good = itemView.findViewById(R.id.item_recommend_travels_good_view);
            v_message = itemView.findViewById(R.id.item_recommend_travels_message_view);
            v_share = itemView.findViewById(R.id.item_recommend_travels_share_view);

            setListener();
        }

        private void setData(final MainRecommendTravelsEntity.MainRecommendItemTravelEntity entity) {
            itemTravel = entity;
            tv_time.setText(DateUtil.getTimeStr(entity.getStartTime(), "MM月dd日HH:mm"));
            tv_startAddress.setText(entity.getStartAddress());
            tv_endAddress.setText(entity.getEndAddress());
            tv_price.setText(CommonUtil.formatPrice(entity.getTravelPrice(),2));
            if (entity.getType() == 0) {//车主行程
                iv_type.setImageResource(R.mipmap.icon_findmen_mini);
                view.setBackground(context.getResources().getDrawable(R.drawable.bg_gradient_blue));
                tv_dingzuo.setText("订座");
                tv_dingzuo.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_blue));
                setHeadImg(entity.getSeats(), entity.getHeadPictures());
                v_peoples.setVisibility(View.GONE);
                switch (entity.getStatus()) {
                    case 1://未发车
                        if (0 == entity.getSurplusSeats()) {
                            setStatus(R.mipmap.img_soldout);
                        } else {
                            setStatus(0);
                        }
                        break;
                    case 3://已发车
                        setStatus(R.mipmap.img_depart);
                        break;
                    case 4://完成
                        setStatus(R.mipmap.img_complete);
                        break;
                    default:
                        setStatus(0);
                        break;
                }
            } else {
                view.setBackground(context.getResources().getDrawable(R.drawable.bg_gradient_red));
                iv_type.setImageResource(R.mipmap.icon_findcar_mini);
                tv_dingzuo.setText("抢单");
                tv_dingzuo.setBackground(context.getResources().getDrawable(R.drawable.bg_circular_orange));
                setHeadImg(0, null);
                v_peoples.setVisibility(View.VISIBLE);
                tv_peoples.setText(String.valueOf(entity.getSeats()));

                switch (entity.getStatus()) {
                    case 3://等待抢单
                        setStatus(0);
                        break;
                    case 4://被抢单
                        setStatus(R.mipmap.img_soldout_chengke);
                        break;
                    default:
                        setStatus(0);
                        break;
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTravelClick(entity);
                    }
                }
            });

            tv_dingzuo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTravelOperClick(entity);
                    }
                }
            });


            tv_watchNum.setText(String.format("%s人浏览", String.valueOf(entity.getPageViews())));
            tv_message.setText(String.valueOf(entity.getCommentsNum()));
            tv_good.setText(String.valueOf(entity.getLikesNum()));
            tv_share.setText(String.valueOf(entity.getSharesNum()));
            if (entity.isLiked()) {
                iv_good.setImageResource(R.mipmap.btn_goodyes_mini);
            } else {
                iv_good.setImageResource(R.mipmap.btn_good_mini);
            }

            double redPackage = entity.getRedPacketPrice();
            if (redPackage > 0) {
                tv_redPackage.setVisibility(View.VISIBLE);
                tv_redPackage.setText(String.format("赚%s元", CommonUtil.formatPrice(redPackage,1)));
            } else {
                tv_redPackage.setText("分享");
            }

        }

        private void setListener() {
            v_good.setOnClickListener(this);
            v_message.setOnClickListener(this);
            v_share.setOnClickListener(this);
        }

        private void setStatus(int imgID) {
            if (imgID != 0) {
                tv_dingzuo.setVisibility(View.GONE);
                iv_status.setVisibility(View.VISIBLE);
                iv_status.setImageResource(imgID);
            } else {
                tv_dingzuo.setVisibility(View.VISIBLE);
                iv_status.setVisibility(View.GONE);
            }
        }

        private void setHeadImg(int seats, List<String> picURLs) {
            for (int i = 0; i < heads.length; i++) {
                if (i < seats) {
                    heads[i].setVisibility(View.VISIBLE);
                    if (picURLs != null && i < picURLs.size()) {
                        setHeadImg(heads[i], picURLs.get(i));
                    } else {
                        heads[i].setImageResource(R.mipmap.img_haveseat);
                    }
                } else {
                    heads[i].setVisibility(View.GONE);
                }
            }
        }

        private void setHeadImg(ImageView iv, String picURL) {
            try {
                Picasso.with(context)
                        .load(picURL)
                        .error(R.mipmap.img_me)
                        .resize(70, 70)
                        .into(iv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_recommend_travels_good_text:
                case R.id.item_recommend_travels_good_view:
                    listener.onLikesClick(itemTravel, iv_good, tv_good);
                    break;
                case R.id.item_recommend_travels_message_text:
                case R.id.item_recommend_travels_message_view:
                    listener.onMessageClick(itemTravel, tv_message);
                    break;
                case R.id.item_recommend_travels_share_text:
                case R.id.item_recommend_travels_share_view:
                    listener.onShareClick(itemTravel, tv_share, icon, name);
                    break;
            }
        }
    }
}
