package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.CommentEntity;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListener;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ItemCommentContentAdapter extends BaseRecyclerViewAdapter<CommentEntity.CommentContent, BaseRecycleViewHolder> {
    private final int VIEW_HEAD = 10;
    private final int VIEW_COMMENT_V1 = 11;
    private final int VIEW_COMMENT_V2 = 12;
    private int headSize;
    private int commentNum;
    private DriverAndTravelEntity headData;
    private OnItemClickListener commentClickListener;
    private ItemDriverAndTravelClickListener headListener;
    private BaseRecycleViewHolder.Params params;

    public ItemCommentContentAdapter(Context context) {
        super(context);
    }

    public ItemCommentContentAdapter(Context context, List<CommentEntity.CommentContent> datas) {
        super(context, datas);
    }

    public void setCommentClickListener(OnItemClickListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    public void setHeadListener(ItemDriverAndTravelClickListener headListener) {
        this.headListener = headListener;
    }

    public void setHeadData(DriverAndTravelEntity headData, int commentNum, int headSize) {
        this.headData = headData;
        this.commentNum=commentNum;
        this.headSize=headSize;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+headSize;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (position < headSize) {
            type = VIEW_HEAD;
        } else if (getItem(position - headSize).getComment().getLevel() == 1) {
            type = VIEW_COMMENT_V1;
        } else if (getItem(position - headSize).getComment().getLevel() == 2) {
            type = VIEW_COMMENT_V2;
        }
        return type;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HEAD:
                CommentHeadViewHolder headViewHolder=new CommentHeadViewHolder(inflater.inflate(R.layout.item_comment_content_head,parent,false));
               headViewHolder.setHolderContext(context);
                return headViewHolder;
            case VIEW_COMMENT_V1:
                return new CommentLV1ViewHolder(inflater.inflate(R.layout.item_comment_content_v1,parent,false));
            case VIEW_COMMENT_V2:
                return new CommentLV2ViewHolder(inflater.inflate(R.layout.item_comment_content_v2,parent,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        if (position < headSize) {
            params.setPosition(position);
            holder.bindData(headData,params);
            holder.setHolederListener(headListener);
        } else {
            params.setPosition(position-headSize);
            holder.bindData(getItem(position-headSize),params);
        }
    }

    public class CommentHeadViewHolder extends ItemDriverAndTravelViewHolder {
        private TextView tv_commentNum;
        private TextView tv_driverInfo;
        public CommentHeadViewHolder(View itemView) {
            super(itemView);
            tv_commentNum=itemView.findViewById(R.id.item_comment_content_head_comment_num);
            tv_driverInfo = itemView.findViewById(R.id.item_driver_travel_info);
        }

        @Override
        public void bindData(Object data,Params params) {
            super.bindData(data,params);
            hideBottomView();
            tv_commentNum.setText("评论：".concat(String.valueOf(commentNum)));

            DriverAndTravelEntity e = (DriverAndTravelEntity) data;
            TravelEntity travelEntity = e.getTravelVo();
            tv_name.setText(e.getNickName());
            tv_driverInfo.setText(DateUtil.formatTime2String(e.getLastTimeOnline(),false));
            if (travelEntity != null) {
                if (travelEntity.getType() == 0) {//车主行程
                    tv_travelPriceType.setText("票价");
                }else {
                    tv_travelPriceType.setText("车费");
                }
            }
        }
    }

    public class CommentLV1ViewHolder extends BaseRecycleViewHolder {
        CommentEntity.CommentContent commentContent;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_content;
        private TextView tv_time;
        public CommentLV1ViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.item_comment_content_v1_icon);
            tv_name=itemView.findViewById(R.id.item_comment_content_v1_name);
            tv_content=itemView.findViewById(R.id.item_comment_content_v1_content);
            tv_time=itemView.findViewById(R.id.item_comment_content_v1_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commentClickListener!=null){
                        commentClickListener.onCommentClick(commentContent);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data,Params params) {
            try {
                commentContent= (CommentEntity.CommentContent) data;
                Picasso.with(context)
                        .load(commentContent.getPicture())
                        .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                        .error(R.mipmap.img_me)
                        .placeholder(R.mipmap.img_me)
                        .into(iv_icon);
                tv_name.setText(commentContent.getNickName());
                tv_time.setText(DateUtil.formatTime2String(commentContent.getComment().getCreateTime(),false));
                tv_content.setText(commentContent.getComment().getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CommentLV2ViewHolder extends BaseRecycleViewHolder {
        CommentEntity.CommentContent commentContent;
        private TextView tv_name1;
        private TextView tv_content;
        private TextView tv_time;
        public CommentLV2ViewHolder(View itemView) {
            super(itemView);
            tv_name1=itemView.findViewById(R.id.item_comment_content_v2_name1);
            tv_content=itemView.findViewById(R.id.item_comment_content_v2_content);
            tv_time=itemView.findViewById(R.id.item_comment_content_v2_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commentClickListener!=null){
                        commentClickListener.onCommentClick(commentContent);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            try {
                commentContent= (CommentEntity.CommentContent) data;
                tv_name1.setText(commentContent.getNickName());
                tv_time.setText(DateUtil.formatTime2String(commentContent.getComment().getCreateTime(),false));
                tv_content.setText(commentContent.getComment().getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnItemClickListener{
        void onCommentClick(CommentEntity.CommentContent commentContent);
    }
}
