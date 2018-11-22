package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.CityEntity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ItemCityAdapter extends BaseRecyclerViewAdapter<CityEntity,ItemCityAdapter.ItemCityViewHolder> {
    private Map<String,Integer> positions;
    private OnItemClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemCityAdapter(Context context) {
        super(context);
        positions=new HashMap<>();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public void setDatas(List<CityEntity> datas) {
        super.setDatas(datas);
        for(int i=0;i<datas.size();i++){
            if(!datas.get(i).getLetter().equals(i>0?getItem(i-1).getLetter():" ")){
                positions.put(datas.get(i).getLetter(),i);
            }
        }
    }

    @Override
    public ItemCityAdapter.ItemCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemCityViewHolder(inflater.inflate(R.layout.item_city,parent,false));
    }

    public Map<String,Integer> getLettersPosition(){
        return positions;
    }

    @Override
    public void onBindViewHolder(ItemCityAdapter.ItemCityViewHolder holder, int position) {
        boolean isShow=((position==0)||!getItem(position-1).getLetter().equals(getItem(position).getLetter()));
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),isShow,params);
    }

    public class ItemCityViewHolder extends BaseRecycleViewHolder{
        CityEntity cityEntity;
        private TextView tv_latter;
        private TextView tv_name;
        public ItemCityViewHolder(View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.item_city_text);
            tv_latter=itemView.findViewById(R.id.item_city_letter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClick(cityEntity.getName());
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            cityEntity= (CityEntity) data;
            tv_name.setText(cityEntity.getName());
            tv_latter.setText(cityEntity.getLetter());
        }
        public void bindData(Object data,boolean isShowLetter,Params params){
            if(isShowLetter){
                tv_latter.setVisibility(View.VISIBLE);
            }else {
                tv_latter.setVisibility(View.GONE);
            }
            bindData(data,params);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(String city);
    }
}
