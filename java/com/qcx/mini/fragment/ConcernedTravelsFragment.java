package com.qcx.mini.fragment;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.PayActivity;
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.activity.TravelDetail_PassengerActivity;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.adapter.MyMainTravlesAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.AttentionTravelsEntity;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.MainUnfinishTravelsEntity;
import com.qcx.mini.listener.EndlessRecyclerOnScrollListener;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.QuRefreshHeader;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder.tag;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ConcernedTravelsFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener{
    private MyMainTravlesAdapter myTravelsAdapter;
    private Handler mHandler = new Handler();
    private boolean isLoadingMyTravel=false;
    private int myTravelPage=1;
    private ItemDriverAndTravelAdapter otherTravelAdapter;
    private int otherTravelPage=1;
    private String mToken;
    private int travelType= ConstantValue.TravelType.PASSENGER;

    @BindView(R.id.fragment_concerned_appBar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fragment_concerned_other_travel_list)
    RecyclerView otherTravelList;
    @BindView(R.id.fragment_concerned_my_travel_list)
    RecyclerView myTravelList;
    @BindView(R.id.fragment_concerned_refresh)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.fragment_concerned_travels_coordinatorLayout)
    CoordinatorLayout  coordinatorLayout;
    @BindView(R.id.fragment_concerned_other_travel_noData1)
    View  v_noData1;
    @BindView(R.id.fragment_concerned_other_travel_noData2)
    View  v_noData2;
    @BindView(R.id.fragment_concerned_passenger_title)
    TextView tv_passenger;
    @BindView(R.id.fragment_concerned_driver_title)
    TextView tv_driver;

    @OnClick(R.id.fragment_concerned_passenger_title)
    void onPassenger(){
        if(travelType!=ConstantValue.TravelType.PASSENGER){
            travelType=ConstantValue.TravelType.PASSENGER;
            setTitleBack(travelType);
            getOtherTravels(1);
        }
    }
    @OnClick(R.id.fragment_concerned_driver_title)
    void onDriver(){
        if(travelType!=ConstantValue.TravelType.DRIVER){
            travelType=ConstantValue.TravelType.DRIVER;
            setTitleBack(travelType);
            getOtherTravels(1);
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        mToken=User.getInstance().getToken();
        QuRefreshHeader refreshHeader=new QuRefreshHeader(getContext());
        mSmartRefreshLayout.setRefreshHeader(refreshHeader);
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        mAppBarLayout.addOnOffsetChangedListener(this);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        otherTravelList.setLayoutManager(manager);

        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                outRect.top = UiUtils.getSize(15);
                outRect.left=UiUtils.getSize(8);
                outRect.right=UiUtils.getSize(8);
                if (position == state.getItemCount() - 1) {
                    outRect.bottom = UiUtils.getSize(20);
                }
            }
        };
        otherTravelList.addItemDecoration(decoration);
        otherTravelAdapter=new ItemDriverAndTravelAdapter(getContext());
        otherTravelList.setAdapter(otherTravelAdapter);
        otherTravelAdapter.setListener(new ItemDriverAndTravelClickListenerImp(getActivity()));
        LinearLayoutManager manager2=new LinearLayoutManager(getContext());
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        myTravelList.setLayoutManager(manager2);
        myTravelsAdapter=new MyMainTravlesAdapter(getActivity());
        myTravelsAdapter.setListener(new MyMainTravlesAdapter.OnTravelClickListener() {
            @Override
            public void onDriverTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
                Intent intent = new Intent(getContext(), TravelDetail_DriverActivity.class);
                intent.putExtra("travelID", unFinishTravel.getTravelId());
                startActivity(intent);
            }

            @Override
            public void onPassengerTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
                Intent intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                intent.putExtra("travelID", unFinishTravel.getTravelId());
                intent.putExtra("ordersID", unFinishTravel.getOrdersTravelId());
                startActivity(intent);
            }

            @Override
            public void onNoPayTravelClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
                Intent intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                intent.putExtra("travelID", unFinishTravel.getTravelId());
                intent.putExtra("ordersID", unFinishTravel.getOrdersTravelId());
                startActivity(intent);
            }

            @Override
            public void onNoPayOrderClick(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
                onNoPayTravelClick(unFinishTravel);
            }

            @Override
            public void onNoDriver(MainUnfinishTravelsEntity.UnFinishTravelEntity unFinishTravel) {
                Intent intent = new Intent(getContext(), TravelNoneActivity.class);
                intent.putExtra("travelId", unFinishTravel.getTravelId());
                intent.putExtra("travelType", unFinishTravel.getType());
                startActivity(intent);
            }
        });

        myTravelList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (myTravelsAdapter.getItemCount() % 10 == 0) getMyTravels(++myTravelPage);
            }
        });
        myTravelList.setAdapter(myTravelsAdapter);

        mSmartRefreshLayout.setEnableRefresh(false);
        mSmartRefreshLayout.setEnableAutoLoadmore(false);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMyTravels(1);
                getOtherTravels(1);
            }
        });
        mSmartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOtherTravels(otherTravelPage);
            }
        });

        otherTravelList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    Picasso.with(getContext()).resumeTag(tag);
                }
                else
                {
                    Picasso.with(getContext()).pauseTag(tag);
                }
            }
        });
//        getOtherTravels(1);
        showLoadingDialog();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_concerned_travels;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(verticalOffset>-20){
            mSmartRefreshLayout.setEnableRefresh(true);
        }else {
            mSmartRefreshLayout.setEnableRefresh(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myTravelPage = 1;
        getMyTravels(1);
        isCu = true;
        mToken="";
        if ((mToken == null || !mToken.equals(User.getInstance().getToken()))) {
            mToken = User.getInstance().getToken();
            getOtherTravels(1);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getOtherTravels(1);
            if(isGetMyTravels){
                getMyTravels(1);
            }
        }
    }

    public void toTop(){
        if(coordinatorLayout!=null){
            CoordinatorLayout.Behavior behavior=((CoordinatorLayout.LayoutParams)(mAppBarLayout.getLayoutParams())).getBehavior();
            behavior.onNestedPreScroll(coordinatorLayout,mAppBarLayout,mSmartRefreshLayout,0,10,new int[]{-0,-0}, ViewCompat.TYPE_TOUCH);
//            behavior.onNestedScroll(coordinatorLayout,mAppBarLayout,mSmartRefreshLayout,0,50,0,100,0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isCu = false;
    }

    boolean isGetMyTravels=false;
    public void getMyTravels(){
        if(isVisible()){
            getMyTravels(1);
        }else {
            isGetMyTravels=true;
        }
    }

    private void getMyTravels(int page) {
        isGetMyTravels=false;
        if (isLoadingMyTravel) return;

        myTravelPage=page;
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", page);
        if (page != 1) {
            params.put("startTime", myTravelsAdapter.getItem(myTravelsAdapter.getItemCount() - 1).getStartTime());
            params.put("createTime", myTravelsAdapter.getItem(myTravelsAdapter.getItemCount() - 1).getCreateTime());
        }
        isLoadingMyTravel = true;
        Request.post(URLString.feedUnFinalList, params, new EntityCallback(MainUnfinishTravelsEntity.class) {
            @Override
            public void onSuccess(Object t) {
                isLoadingMyTravel = false;
                MainUnfinishTravelsEntity myTravels = (MainUnfinishTravelsEntity) t;
                if (myTravelPage == 1) {
                    myTravelsAdapter.setDatas(myTravels.getFeedUnFinalList());
                    mHandler.removeCallbacks(runnable);
                    mHandler.postDelayed(runnable, 1000);
                } else {
                    myTravelsAdapter.addDatas(myTravels.getFeedUnFinalList());
                }
                if ((myTravels.getFeedUnFinalList() == null || myTravels.getFeedUnFinalList().size() < 1) && myTravelPage != 1) {
                    myTravelPage--;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isLoadingMyTravel = false;
            }
        });
    }

    private Instrumentation initMove(int x,int y){

        Instrumentation inst = new Instrumentation();
        long dowTime = SystemClock.uptimeMillis();
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime,
                MotionEvent.ACTION_DOWN, x, y,0));
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime,
                MotionEvent.ACTION_MOVE, x, y,0));
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime+20,
                MotionEvent.ACTION_MOVE, x+20, y,0));
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime+30,
                MotionEvent.ACTION_MOVE, x+40, y,0));
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime+40,
                MotionEvent.ACTION_MOVE, x+60, y,0));
        inst.sendPointerSync(MotionEvent.obtain(dowTime,dowTime+40,
                MotionEvent.ACTION_UP, x+60, y,0));
        return inst;
    }

    private void getOtherTravels(final int page){
        if(page==1){
            otherTravelPage=1;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("pageNo",page);
        params.put("type",travelType);
        setTitleBack(travelType);
        Request.post(URLString.attentionTravels, params, new EntityCallback(AttentionTravelsEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                AttentionTravelsEntity travelsEntity= (AttentionTravelsEntity) t;
                List<DriverAndTravelEntity> travels=travelsEntity.getTravels();
                if(travels!=null){
                    for(int i=0;i<travels.size();i++){
                        travels.get(i).setHideAttentionView(true);
                    }
                }
                if(page==1){
                    otherTravelAdapter.setDatas(travelsEntity.getTravels());
                }else {
                    otherTravelAdapter.addDatas(travelsEntity.getTravels());
                }
                if(travelsEntity.getTravels()!=null&&travelsEntity.getTravels().size()>0){
                    otherTravelPage++;
                }
                showData();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
                showData();
            }
        });
    }

    private void finishLoading(){
        hideLoadingDialog();
        if(mSmartRefreshLayout!=null){
            mSmartRefreshLayout.finishRefresh();
            mSmartRefreshLayout.finishLoadmore();
        }
    }

    boolean isCu = true;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCu) {
                if (myTravelsAdapter != null)
                    myTravelsAdapter.dateChanged();
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void showData(){
        if(otherTravelAdapter==null||otherTravelAdapter.getItemCount()<1){
            v_noData1.setVisibility(View.VISIBLE);
            v_noData2.setVisibility(View.VISIBLE);
        }else {
            v_noData1.setVisibility(View.GONE);
            v_noData2.setVisibility(View.GONE);
        }
    }

    private void setTitleBack(int travelType){
        if(travelType==ConstantValue.TravelType.DRIVER){
            tv_driver.setTextColor(Color.WHITE);
            tv_driver.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray_6));
            tv_passenger.setTextColor(0xFF939499);
            tv_passenger.setBackgroundColor(Color.TRANSPARENT);
        }else {
            tv_passenger.setTextColor(Color.WHITE);
            tv_passenger.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray_6));
            tv_driver.setTextColor(0xFF939499);
            tv_driver.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}
