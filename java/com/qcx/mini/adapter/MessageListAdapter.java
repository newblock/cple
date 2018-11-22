package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.MessageListEntity;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/8.
 */

public class MessageListAdapter extends BaseRecyclerViewAdapter<MessageListEntity.MessageEntity,MessageListAdapter.MessageListViewHolder> {
    private OnMessageListItemClickListener listener;
    private Context context;

    public void setListener(OnMessageListItemClickListener listener) {
        this.listener = listener;
    }

    public MessageListAdapter(Context context) {
        super(context);
        this.context=context;
    }

    public MessageListAdapter(Context context, List<MessageListEntity.MessageEntity> datas) {
        super(context, datas);
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==datas.size()){
            return 3;
        }else if(datas.get(position).getComment().getLevel()==1){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.i(viewType+" ");
        if(viewType==1){
            return new MessageListViewHolder1(inflater.inflate(R.layout.item_message_lv1,parent,false));
        }else if(viewType==2){
            return new MessageListViewHolder2(inflater.inflate(R.layout.item_message_lv2,parent,false));
        }else {
            return new MessageListViewHolder2(inflater.inflate(R.layout.layout_null,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(MessageListViewHolder holder, int position) {
        if(position<datas.size())holder.setDatta(datas.get(position));
    }

    class MessageListViewHolder1 extends MessageListViewHolder {
        View v_view;
        ImageView iv_icon;
        TextView tv_name,tv_message,tv_time;
        public MessageListViewHolder1(View itemView) {
            super(itemView);
            v_view=itemView.findViewById(R.id.item_message_lv1_view);
            tv_name=itemView.findViewById(R.id.item_message_lv1_name);
            tv_message=itemView.findViewById(R.id.item_message_lv1_message);
            tv_time=itemView.findViewById(R.id.item_message_lv1_time);
            iv_icon=itemView.findViewById(R.id.item_message_lv1_icon);

        }

        @Override
        void setDatta(final MessageListEntity.MessageEntity data) {
            Picasso.with(context)
                    .load(data.getPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(data.getNickName());
            tv_message.setText(data.getComment().getContent());
            tv_time.setText(DateUtil.getTimeStr(data.getComment().getCreateTime(),"MM月dd日HH:mm"));

            v_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(data);
                    }
                }
            });
        }
    }

    class MessageListViewHolder2 extends MessageListViewHolder {
        View v_view;
        ImageView iv_icon;
        TextView tv_name,tv_message,tv_time;
        public MessageListViewHolder2(View itemView) {
            super(itemView);
            v_view=itemView.findViewById(R.id.item_message_lv2_view);
            tv_name=itemView.findViewById(R.id.item_message_lv2_name);
            tv_message=itemView.findViewById(R.id.item_message_lv2_message);
            tv_time=itemView.findViewById(R.id.item_message_lv2_time);
            iv_icon=itemView.findViewById(R.id.item_message_lv2_icon);

        }

        @Override
        void setDatta(final MessageListEntity.MessageEntity data) {
            Picasso.with(context)
                    .load(data.getPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(data.getNickName());
            tv_message.setText(data.getComment().getContent());
            tv_time.setText(DateUtil.getTimeStr(data.getComment().getCreateTime(),"MM月dd日HH:mm"));

            v_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(data);
                    }
                }
            });
        }
    }

    public abstract  class MessageListViewHolder extends ViewHolder{

        public MessageListViewHolder(View itemView) {
            super(itemView);
        }

        abstract void setDatta(MessageListEntity.MessageEntity data);
    }

    public interface OnMessageListItemClickListener{
        void onClick(MessageListEntity.MessageEntity message);
    }
}
