package com.qcx.mini.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseListViewAdapter;
import com.qcx.mini.entity.ItemizedAccountEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class ItemizedAccountListAdapter extends BaseListViewAdapter<ItemizedAccountEntity> {
    public ItemizedAccountListAdapter(Context context) {
        super(context);
    }

    public ItemizedAccountListAdapter(Context context, List<ItemizedAccountEntity> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemizedAccountListViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.item_itemized_account,null);
            holder=new ItemizedAccountListViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder=(ItemizedAccountListViewHolder)convertView.getTag();
        }
        return convertView;
    }

    class ItemizedAccountListViewHolder{
        ItemizedAccountListViewHolder(View view){

        }
    }
}
