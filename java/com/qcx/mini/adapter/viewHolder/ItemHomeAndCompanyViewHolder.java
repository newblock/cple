package com.qcx.mini.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.HomeAndCommpanyEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemHomeAndCompanyClickListener;
import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ItemHomeAndCompanyViewHolder extends BaseRecycleViewHolder implements View.OnClickListener{
    private Tip  home,company;
    private ImageView iv_home,iv_company;
    private TextView tv_home,tv_company;
    private View v_home,v_company;
    private ItemHomeAndCompanyClickListener listener;
    public ItemHomeAndCompanyViewHolder(View itemView) {
        super(itemView);
        iv_home=itemView.findViewById(R.id.item_home_and_company_home_address_img);
        iv_company=itemView.findViewById(R.id.item_home_and_company_company_address_img);
        tv_home=itemView.findViewById(R.id.item_home_and_company_home_address_text);
        tv_company=itemView.findViewById(R.id.item_home_and_company_company_address_text);
        v_home=itemView.findViewById(R.id.item_home_and_company_home_address);
        v_company=itemView.findViewById(R.id.item_home_and_company_company_address);
        v_company.setOnClickListener(this);
        v_home.setOnClickListener(this);
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener = (ItemHomeAndCompanyClickListener) holederListener;
    }


    @Override
    public void bindData(Object data,Params params) {
        try {
            if(data!=null){
                HomeAndCommpanyEntity homeAndCommpanyEntity = (HomeAndCommpanyEntity) data;
                HomeAndCommpanyEntity.Address address = homeAndCommpanyEntity.getDriver();
                if(address!=null&&address.getLocation_home()!=null&&address.getLocation_home().length==2){
                    home = new Tip();
                    home.setPostion(new LatLonPoint(address.getLocation_home()[1], address.getLocation_home()[0]));
                    home.setName(address.getAddr_home());
                    tv_home.setText(home.getName());
                }
                if(address!=null&&address.getLocation_company()!=null&&address.getLocation_company().length==2){
                    company = new Tip();
                    company.setPostion(new LatLonPoint(address.getLocation_company()[1], address.getLocation_company()[0]));
                    company.setName(address.getAddr_company());
                    tv_company.setText(company.getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_home_and_company_home_address:
                if(listener!=null){
                    listener.onHomeClick(home);
                }
                break;
            case R.id.item_home_and_company_company_address:
                if(listener!=null){
                    listener.onHomeClick(company);
                }
                break;
        }
    }
}
