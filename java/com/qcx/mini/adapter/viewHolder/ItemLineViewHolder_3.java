package com.qcx.mini.adapter.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.listener.ItemClickListener;

/**
 * Created by Administrator on 2018/8/14.
 */

public class ItemLineViewHolder_3 extends BaseRecycleViewHolder<PersonalLineEntity> {
    private PersonalLineEntity line;

    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_startAddress;
    private TextView tv_endAddress;

    private OnLineCLickListener listener;

    public ItemLineViewHolder_3(View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.item_line_3_name);
        tv_time=itemView.findViewById(R.id.item_line_3_time);
        tv_startAddress=itemView.findViewById(R.id.item_line_3_startAddress);
        tv_endAddress=itemView.findViewById(R.id.item_line_3_endAddress);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&line!=null){
                    listener.line(line);
                }
            }
        });
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener = (OnLineCLickListener) holederListener;
    }

    @Override
    public void bindData(PersonalLineEntity data, Params params) {
        this.line=data;
        if(data==null){
            return;
        }
        String name=line.getLineDesc();
        if(TextUtils.isEmpty(name)){
            name="常用";
        }
        tv_name.setText(name);
        tv_time.setText(line.getStartTime());
        tv_startAddress.setText(line.getStartAddress());
        tv_endAddress.setText(line.getEndAddress());
    }

    public interface OnLineCLickListener extends ItemClickListener{
        void line(PersonalLineEntity line);
    }
}
