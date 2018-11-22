package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.PushHistorEntity;
import com.qcx.mini.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */

public class ItemNewsAdapter extends BaseRecyclerViewAdapter<PushHistorEntity.PushContent,ItemNewsAdapter.ItemNewsViewHolder> {
    private OnItemClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemNewsAdapter(Context context) {
        super(context);
    }

    public ItemNewsAdapter(Context context, List<PushHistorEntity.PushContent> datas) {
        super(context, datas);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public ItemNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemNewsViewHolder(inflater.inflate(R.layout.item_news,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemNewsViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemNewsViewHolder extends BaseRecycleViewHolder {

        private PushHistorEntity.PushContent pushContent;
        private ImageView iv_icon;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        private ImageView iv_redPoint;

        public ItemNewsViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.item_news_icon);
            tv_title=itemView.findViewById(R.id.item_news_title);
            tv_content=itemView.findViewById(R.id.item_news_describe);
            tv_time=itemView.findViewById(R.id.item_news_time);
            iv_redPoint=itemView.findViewById(R.id.item_news_red_point);
            iv_icon.setImageResource(R.mipmap.icon_quchuxing);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&pushContent!=null){
                        listener.onItemClick(pushContent,iv_redPoint);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            try {
                pushContent= (PushHistorEntity.PushContent) data;
                tv_title.setText(pushContent.getTitle());
                tv_content.setText(pushContent.getMsg());
                tv_time.setText(pushContent.getCreateTime());
                if (pushContent.isHaveRead()){
                    iv_redPoint.setVisibility(View.GONE);
                }else {
                    iv_redPoint.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public interface OnItemClickListener{
        void onItemClick(PushHistorEntity.PushContent pushContent,ImageView point);
    }
}
