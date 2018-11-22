package com.qcx.mini.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.AlmightyRecyclerViewAdapter;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.adapter.SearchAddressListAdapter;
import com.qcx.mini.adapter.viewHolder.ItemHomeAndCompanyViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemSearchAddressTipViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemSearchGroupViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.ReleaseTypeDialog;
import com.qcx.mini.entity.HaveCreateGroupAuthEntity;
import com.qcx.mini.entity.HomeAndCommpanyEntity;
import com.qcx.mini.entity.SearchTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.listener.ItemHomeAndCompanyClickListener;
import com.qcx.mini.listener.ItemSearchAddressTipClickListener;
import com.qcx.mini.listener.ItemSearchGroupClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.KeybordS;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.QuRefreshHeader;
import com.qcx.mini.widget.itemDecoration.FirstTextItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder.tag;
import static com.qcx.mini.net.NetUtil.joinGroup;

/**
 * 搜索行程、输入地址
 */
public class SearchActivity extends BaseActivity implements Inputtips.InputtipsListener {
    private AlmightyRecyclerViewAdapter addressAdapter;
    private ItemDriverAndTravelAdapter travelAdapter;
    private int pageNum;

    private boolean isOnlyInput = false;//是否只用作地址输入

    private final int INPUT_TYPE_START = 0;
    private final int INPUT_TYPE_END = 1;
    private int type;
    private boolean isChangedStartAddressText = true;
    private boolean isChangedEndAddressText = true;

    private Tip startAddress;
    private Tip endAddress;

    @BindView(R.id.search_start_address_input)
    EditText et_start;
    @BindView(R.id.search_end_address_input)
    EditText et_end;
    @BindView(R.id.search_list)
    RecyclerView rv_list;
    @BindView(R.id.search_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.search_release_view)
    View v_releaseView;
    @BindView(R.id.search_no_travel)
    View v_noTravel;

    @OnClick(R.id.search_close)
    void close() {
        finish();
    }

    @OnClick({R.id.search_no_travel_release, R.id.search_travel_release})
    void releaseTravel() {
        Intent intent = new Intent(this, ReleaseTravel_2Activity.class);
        intent.putExtra("start", startAddress);
        intent.putExtra("end", endAddress);
        Random random = new Random();
        int i = random.nextInt(2);
        intent.putExtra("travelType", i);
        intent.putExtra("type", random.nextInt(2));
        startActivity(intent);
    }

    @OnClick(R.id.search_exchange)
    void exchange() {
        Tip tip;
        tip = startAddress;
        startAddress = endAddress;
        endAddress = tip;
        addressChanged(true);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, Color.WHITE, 0);
        isOnlyInput = getIntent().getBooleanExtra("isOnlyInput", false);
        startAddress = getIntent().getParcelableExtra("start");
        endAddress = getIntent().getParcelableExtra("end");
        if (startAddress != null) {
            addressChanged(false);
            if (endAddress == null) {
                et_end.requestFocus();
                type = INPUT_TYPE_END;
            }
        }

        et_start.addTextChangedListener(startAddressWatcher);
        et_end.addTextChangedListener(endAddressWatcher);
        et_start.setOnTouchListener(touch);
        et_end.setOnTouchListener(touch);

        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getSearchData(1);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getSearchData(pageNum);
            }
        });
        et_start.setOnEditorActionListener(editorActionListener);
        et_end.setOnEditorActionListener(editorActionListener);
        addressAdapter = new AlmightyRecyclerViewAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(addressAdapter);
        enableRefresh(false);

        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getApplicationContext()).resumeTag(tag);
                } else {
                    Picasso.with(getApplicationContext()).pauseTag(tag);
                }
            }
        });
        getAddress();
    }

    private ItemSearchAddressTipClickListener addressListener = new ItemSearchAddressTipClickListener() {
        @Override
        public void onAddressClick(Tip tip) {
            changeAddress(tip);
            addressAdapter.clear(ItemSearchAddressTipViewHolder.class);
        }

        @Override
        public void onClearClick() {

        }
    };
    private ItemHomeAndCompanyClickListener homeAndCompanyClickListener = new ItemHomeAndCompanyClickListener() {
        @Override
        public void onHomeClick(Tip homeTip) {
            if (homeTip != null) {
                changeAddress(homeTip);
            }
        }

        @Override
        public void onCompanyClick(Tip companyTip) {
            if (companyTip != null) {
                changeAddress(companyTip);
            }
        }
    };

    View.OnTouchListener touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            rv_list.setAdapter(addressAdapter);
            enableRefresh(false);
            rv_list.removeItemDecoration(decoration);
            rv_list.removeItemDecoration(typeDecoration);
            v_releaseView.setVisibility(View.GONE);
            v_noTravel.setVisibility(View.GONE);
            if (v == et_end) {
                type = INPUT_TYPE_END;
                et_end.setCursorVisible(true);
            } else if (v == et_start) {
                type = INPUT_TYPE_START;
                et_start.setCursorVisible(true);
            }
            return false;
        }
    };

    private void changeAddress(Tip tip) {
        if(tip.getPoint()==null){
            tip=null;
        }
        if (type == INPUT_TYPE_START) {
            startAddress = tip;
            et_start.setCursorVisible(false);
            if (endAddress == null) {
                et_end.requestFocus();
                et_end.setCursorVisible(true);
                type = INPUT_TYPE_END;
            }
        } else {
            et_end.setCursorVisible(false);
            endAddress = tip;
            et_end.setCursorVisible(false);
        }
        addressChanged(true);
    }

    TextWatcher startAddressWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChangedStartAddressText) {
                type = INPUT_TYPE_START;
                queryTips(s);
            } else {
                isChangedStartAddressText = true;
            }
        }
    };
    TextWatcher endAddressWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isChangedEndAddressText) {
                type = INPUT_TYPE_END;
                queryTips(s);
            } else {
                isChangedEndAddressText = true;
            }
        }
    };
    TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                addressChanged(true);
//                return true;
            }
            return false;
        }
    };

    public void queryTips(Editable s) {
        String newText = s.toString().trim();
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY));
            Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPoint() == null) {
                    list.remove(i);
                    i--;
                }
                if (rv_list.getAdapter() != addressAdapter) {
                    rv_list.setAdapter(addressAdapter);
                    enableRefresh(false);
                    rv_list.removeItemDecoration(decoration);
                    rv_list.removeItemDecoration(typeDecoration);
                    v_releaseView.setVisibility(View.GONE);
                }
                addressAdapter.clear(ItemSearchAddressTipViewHolder.class);
                addressAdapter.addData(ItemSearchAddressTipViewHolder.class, list, R.layout.item_set_address, addressListener);
            }
        }
    }

    private void addressChanged(boolean isGetData) {
        isChangedStartAddressText = false;
        isChangedEndAddressText = false;
        if (startAddress != null) {
            et_start.setText(startAddress.getName());
        } else {
            et_start.setText("");
        }
        if (endAddress != null) {
            et_end.setText(endAddress.getName());
        } else {
            et_end.setText("");
        }
        if (!isGetData) {
            return;
        }
        if (startAddress != null && endAddress != null) {
            if (!isOnlyInput) {
                showLoadingDialog();
                getSearchData(1);
            } else {
                inputFinsh();
            }
        }
    }

    private void getSearchData(final int page) {
        if (startAddress == null || endAddress == null) {
            return;
        }

        if (page == 1) {
            pageNum = 1;
        }
        KeybordS.closeKeybord(et_end, this);
        KeybordS.closeKeybord(et_start, this);
        if (travelAdapter == null) {
            travelAdapter = new ItemDriverAndTravelAdapter(this);
            travelAdapter.setHeadListener(listener);
            travelAdapter.setListener(new TravelClick(this));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", page);

        params.put("type", ConstantValue.TravelType.PASSENGER);
        params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
        params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});

//        params.put("startAddress", startAddress.getName());
//        params.put("startLocation", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
//        params.put("endAddress", endAddress.getName());
//        params.put("endLocation", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
        Request.post(URLString.searchTravel_2, params, new EntityCallback(SearchTravelEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                finishLoading();
                SearchTravelEntity data = (SearchTravelEntity) t;
                if (page == 1) {
                    if (rv_list.getAdapter() != travelAdapter) {
                        rv_list.setAdapter(travelAdapter);
                        enableRefresh(true);
                        rv_list.addItemDecoration(decoration);
                        rv_list.addItemDecoration(typeDecoration);
                        decoration.setStartPosition(0);
                    }
                    if (data.getTravelRecomment() == null || data.getTravelRecomment().size() < 1) {
                        v_noTravel.setVisibility(View.VISIBLE);
                        v_releaseView.setVisibility(View.GONE);
                    } else {
                        v_releaseView.setVisibility(View.VISIBLE);
                        v_noTravel.setVisibility(View.GONE);
                        if (typeDecoration != null) {
                            typeDecoration.setText(data.getTravelRecomment().get(0).getTravelVo().getType() == ConstantValue.TravelType.DRIVER ? "车主行程" : "乘客行程");
                        }
                    }
                    travelAdapter.setDatas(data.getTravelRecomment());
                } else {
                    travelAdapter.addDatas(data.getTravelRecomment());
                }
                if (data.getTravelRecomment() != null && data.getTravelRecomment().size() > 0) {
                    pageNum = page + 1;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
                finishLoading();
            }
        });

    }

    private void inputFinsh() {
        if (startAddress == null || endAddress == null) {
            return;
        }
        if (isOnlyInput) {
            Intent intent = new Intent();
            intent.putExtra("start", startAddress);
            intent.putExtra("end", endAddress);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    private ItemSearchGroupClickListener listener = new ItemSearchGroupClickListener() {
        @Override
        public void onGroupClick(long groupId, int groupType) {
            Intent intent = new Intent(SearchActivity.this, WayInfoActivity.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupType", groupType);
            startActivity(intent);
        }

        @Override
        public void onJoinClick(long groupId, View view) {
            joinGroup(groupId, view);
        }

        @Override
        public void onReleaseTravelClick() {
            new ReleaseTypeDialog().show(getSupportFragmentManager(), "");
        }
    };

    private void getAddress() {
        if (User.getInstance().isLogin()) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            Request.post(URLString.homeAndCompanyAddress, params, new EntityCallback(HomeAndCommpanyEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    hideLoadingDialog();
                    HomeAndCommpanyEntity entity = (HomeAndCommpanyEntity) t;
                    List<HomeAndCommpanyEntity> entities = new ArrayList<>();
                    entities.add(entity);
                    addressAdapter.addData(0, ItemHomeAndCompanyViewHolder.class, entities, R.layout.item_home_and_company, homeAndCompanyClickListener);
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    hideLoadingDialog();
                }
            });
        }

    }

    private void enableRefresh(boolean enable) {
        if (refreshLayout != null) {
            refreshLayout.setEnableLoadmore(enable);
            refreshLayout.setEnableRefresh(enable);
        }
    }

    private void finishLoading() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        private int startPosition;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if (position >= startPosition) {

                outRect.top = UiUtils.getSize(15);
                outRect.left = UiUtils.getSize(8);
                outRect.right = UiUtils.getSize(8);
                if (position == state.getItemCount() - 1) {
                    outRect.bottom = UiUtils.getSize(8);
                }
            }
        }

        public void setStartPosition(int position) {
            this.startPosition = position;
        }
    }

    private MyItemDecoration decoration = new MyItemDecoration();

    private class TravelClick extends ItemDriverAndTravelClickListenerImp {

        public TravelClick(FragmentActivity activity) {
            super(activity);
        }

        @Override
        public void onTravelClick(TravelEntity data) {
            if (data == null) {
                LogUtil.i("activity=null");
                return;
            }
            Intent intent = new Intent(SearchActivity.this, TravelDetail_NoPayActivity.class);
            intent.putExtra("travelId", data.getTravelId());
            intent.putExtra("travelType", data.getType());
            intent.putExtra("start", startAddress);
            intent.putExtra("end", endAddress);
            startActivity(intent);
        }
    }

    private FirstTextItemDecoration typeDecoration = new FirstTextItemDecoration("车主行程", UiUtils.getSize(14), Color.BLACK, UiUtils.getSize(36), UiUtils.getSize(8));
}
