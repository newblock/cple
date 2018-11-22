package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.LineTravelActivity;
import com.qcx.mini.activity.PersonalLinesActivity;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.activity.SearchActivity;
import com.qcx.mini.activity.TravelUnfinishedActivity;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemLineViewHolder_3;
import com.qcx.mini.adapter.viewHolder.ItemTravelViewHolder_3;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.entity.ReleasePageEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemTravelClickListener;
import com.qcx.mini.listener.ItemTravelClickListenerImp;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.NotDriverView;
import com.qcx.mini.widget.itemDecoration.DividerDecoration;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/6.
 * 首页
 */

public class HomeFragment extends BaseFragment {
    private SimpleRecyclerViewAdapter<PersonalLineEntity> linesAdapter;
    DividerDecoration dividerDecoration;
    private List<DriverAndTravelEntity> travels;
    private List<PersonalLineEntity> lines;
    private Handler mHandler = new Handler();

    private ItemTravelClickListener travelClickListener;
    private ItemTravelViewHolder_3 travelViewHolder1;
    private ItemTravelViewHolder_3 travelViewHolder2;

    private int type;//身份类型

    @BindView(R.id.fragment_home_travels_view)
    View v_travels;
    @BindView(R.id.fragment_home_travel_1)
    View v_travel1;
    @BindView(R.id.fragment_home_travel_2)
    View v_travel2;
    @BindView(R.id.fragment_home_no_line)
    View v_noLine;
    @BindView(R.id.fragment_home_lines_view)
    RecyclerView v_lines;
    @BindView(R.id.fragment_home_not_driver)
    NotDriverView v_notDriver;
    @BindView(R.id.fragment_home_content_view)
    View v_content;
    @BindView(R.id.fragment_home_scrollView)
    View v_scrollView;



    @OnClick(R.id.fragment_home_travel_all)
    void allTravel() {
        startActivity(new Intent(getContext(), TravelUnfinishedActivity.class));
    }

    @OnClick(R.id.fragment_home_line_all)
    void allLine() {
        startActivity(new Intent(getContext(), PersonalLinesActivity.class));
    }

    @OnClick(R.id.fragment_home_no_line)
    void addLine() {
        Intent intent = new Intent(getContext(), ReleaseTravel_2Activity.class);
        ReleasePageEntity pageEntity = type == ConstantValue.TravelType.DRIVER ?
                ReleasePageEntity.Factory.lineDriverData() :
                ReleasePageEntity.Factory.linePassengerData();
        intent.putExtra("data", pageEntity);
        startActivity(intent);
    }

    @OnClick(R.id.fragment_home_search_travel)
    void searchTravel() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        travelClickListener = new ItemTravelClickListenerImp(getContext());
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        v_lines.setLayoutManager(manager);

        linesAdapter=new SimpleRecyclerViewAdapter<>(getContext(),ItemLineViewHolder_3.class,R.layout.item_line_3);
        v_lines.setAdapter(linesAdapter);
        linesAdapter.setListener(new ItemLineViewHolder_3.OnLineCLickListener() {
            @Override
            public void line(PersonalLineEntity line) {
                Intent intent=new Intent(getContext(),LineTravelActivity.class);
                intent.putExtra("type",line.getType());
                intent.putExtra("lineId",line.getLineId());
                startActivity(intent);
            }
        });
         dividerDecoration=new DividerDecoration(UiUtils.getSize(1));
        dividerDecoration.setPaddingLeft(UiUtils.getSize(16));
        dividerDecoration.setColor(getResources().getColor(R.color.gray_line));
        v_lines.addItemDecoration(dividerDecoration);
        v_lines.setFocusable(false);
        reView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(v_notDriver!=null){
            v_notDriver.setListener(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mHandler.postDelayed(runnable, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(!hidden){
//            reView();
//        }
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(isVisibleToUser){
//            reView();
//        }
//    }

    private void reView(){
        if(getActivity()==null){
            LogUtil.i("fffffffffffffffff homeFragment getActivity==null");
            return;
        }
        if(type==ConstantValue.TravelType.DRIVER&&User.getInstance().getDriverStatus()!=ConstantValue.AuthStatus.PASS){
            v_notDriver.setVisibility(View.VISIBLE);
            v_content.setVisibility(View.GONE);
            v_notDriver.setListener(new OnDriverAuthClickListener(getActivity()));
            v_scrollView.setBackgroundColor(Color.WHITE);
        }else {
            v_notDriver.setVisibility(View.GONE);
            v_content.setVisibility(View.VISIBLE);
            v_scrollView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void dataChanged() {
        reView();
        travelChanged();
        lineChanged();
    }

    private void travelChanged() {
        if (travels == null || travels.size() < 1) {//行程为空
            v_travels.setVisibility(View.GONE);

        } else if (travels.size() == 1) {//有一条行程
            v_travels.setVisibility(View.VISIBLE);
            v_travel1.setVisibility(View.VISIBLE);
            v_travel2.setVisibility(View.GONE);
            if (travelViewHolder1 == null) {
                travelViewHolder1 = new ItemTravelViewHolder_3(v_travel1);
                travelViewHolder1.setHolederListener(travelClickListener);
            }
            travelViewHolder1.bindData(travels.get(0));

        } else {
            v_travels.setVisibility(View.VISIBLE);
            v_travel1.setVisibility(View.VISIBLE);
            v_travel2.setVisibility(View.VISIBLE);
            if (travelViewHolder1 == null) {
                travelViewHolder1 = new ItemTravelViewHolder_3(v_travel1);
                travelViewHolder1.setHolederListener(travelClickListener);
            }
            if (travelViewHolder2 == null) {
                travelViewHolder2 = new ItemTravelViewHolder_3(v_travel2);
                travelViewHolder2.setHolederListener(travelClickListener);
            }
            travelViewHolder1.bindData(travels.get(0));
            travelViewHolder2.bindData(travels.get(1));

        }
    }

    private void lineChanged() {
        if (lines == null || lines.size() < 1) {//行程为空
            v_noLine.setVisibility(View.VISIBLE);
            v_lines.setVisibility(View.GONE);
            return;

        }
        v_noLine.setVisibility(View.GONE);
        v_lines.setVisibility(View.VISIBLE);
        dividerDecoration.setStopPosition(lines.size()-2);
        linesAdapter.setDatas(lines);
    }

    public void setType(int type) {
        this.type = type;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            travelChanged();
            mHandler.postDelayed(this, 999);
        }
    };

    public void setLines(List<PersonalLineEntity> lines) {
        this.lines = lines;
        dataChanged();
    }

    public void setTravels(List<DriverAndTravelEntity> travels) {
        this.travels = travels;
        dataChanged();
    }
}
