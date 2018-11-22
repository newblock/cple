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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/5.
 */

public class WelcomePageAdapter extends PagerAdapter {
    private int[] images;
    private List<View> views;


    public WelcomePageAdapter(Context context,int[] images){
        LayoutInflater inflater=LayoutInflater.from(context);
        if(images!=null){
            this.images=images;
            views=new ArrayList<>();
            for(int i=0;i<images.length&&i<5;i++){
                View view=inflater.inflate(R.layout.page_welcome,null);
                view.setTag(new WelcomePageHolder(view));
                views.add(view);
            }
        }else {
            this.images=new int[0];
        }
    }

    @Override
    public int getCount() {
        return images==null?0:images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=views.get(position % views.size());
        WelcomePageHolder holder=(WelcomePageHolder)view.getTag();
        container.addView(view);
        holder.setData(images[position]);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position % views.size()));
    }

    class WelcomePageHolder{
        ImageView imageView;
        WelcomePageHolder(View view){
            imageView=view.findViewById(R.id.page_welcome_imageView);
        }

        void setData(int imgId){
            imageView.setImageResource(imgId);
        }

    }
}
