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
import com.qcx.mini.entity.CommentListEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ItemCommentAdapter extends BaseRecyclerViewAdapter<CommentListEntity.Comment,ItemCommentAdapter.ItemCommentViewHolder> {
    private  OnItemClickListener listener;
    private BaseRecycleViewHolder.Params params;
    public ItemCommentAdapter(Context context) {
        super(context);
    }

    public ItemCommentAdapter(Context context, List<CommentListEntity.Comment> datas) {
        super(context, datas);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public ItemCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemCommentViewHolder(inflater.inflate(R.layout.item_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemCommentViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemCommentViewHolder extends BaseRecycleViewHolder {
        private CommentListEntity.Comment comment;
        private ImageView iv_icon;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
        private ImageView iv_redPoint;

        public ItemCommentViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.item_news_icon);
            tv_title=itemView.findViewById(R.id.item_news_title);
            tv_content=itemView.findViewById(R.id.item_news_describe);
            tv_time=itemView.findViewById(R.id.item_news_time);
            iv_redPoint=itemView.findViewById(R.id.item_news_red_point);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&comment!=null){
                        listener.onItemClick(comment,iv_redPoint);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            try {
                comment= (CommentListEntity.Comment) data;
                Picasso.with(context)
                        .load(comment.getPicture())
                        .error(R.mipmap.img_me)
                        .placeholder(R.mipmap.img_me)
                        .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                        .into(iv_icon);
                tv_title.setText(comment.getNickName());
                tv_content.setText(comment.getContent());
                tv_time.setText(comment.getCreatTime());
                if(comment.getCommentStatus()==ConstantValue.CommentStatus.IS_READED){
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
        void onItemClick(CommentListEntity.Comment comment,ImageView point);
    }
}
