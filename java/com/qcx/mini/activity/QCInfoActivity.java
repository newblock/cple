package com.qcx.mini.activity;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.AlmightyRecyclerViewAdapter;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemQCDetailViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QCSoldDialog;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.LineGraphView;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class QCInfoActivity extends BaseActivity {
    private SimpleRecyclerViewAdapter<LineGraphView.Coordinate> adapter;

    @BindView(R.id.qc_info_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.qc_info_line_graph)
    LineGraphView mLineGraphView;

    @OnClick(R.id.qc_info_sold)
    void sold() {

        new QCSoldDialog().show(getSupportFragmentManager(), "");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_qcinfo;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("我的QC");
        StatusBarUtil.setColor(this, Color.WHITE, 0);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        adapter = new SimpleRecyclerViewAdapter<>(this, ItemQCDetailViewHolder.class, R.layout.item_qc_detail);
        adapter.setDatas(getC());
        mRecyclerView.setAdapter(adapter);
        QuItemDecoration decoration = new QuItemDecoration(0, 0, UiUtils.getSize(15), UiUtils.getSize(15), UiUtils.getSize(1), UiUtils.getSize(1));
        decoration.setColor(getResources().getColor(R.color.gray_line));
        mRecyclerView.addItemDecoration(decoration);

        initLineGraph();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        getMyQC();
    }

    private void initLineGraph() {
        mLineGraphView.setmXCoordinates(getX());
        mLineGraphView.setmCoordinates(getC());
    }

    private void getMyQC(){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.myQC, params, new EntityCallback(null) {
            @Override
            public void onSuccess(Object t) {

            }
        });
    }





    private List<LineGraphView.Coordinate> getC() {
        List<LineGraphView.Coordinate> mCoordinates = new ArrayList<>();

        mCoordinates.add(new LineGraphView.Coordinate(0, 1.4f, "07-01", "1.4元"));
        mCoordinates.add(new LineGraphView.Coordinate(1, 1.8f, "07-02", "1.8元"));
        mCoordinates.add(new LineGraphView.Coordinate(2, 1.5f, "07-03", "1.5元"));
        mCoordinates.add(new LineGraphView.Coordinate(3, 2.4f, "07-04", "2.4元"));
        mCoordinates.add(new LineGraphView.Coordinate(4, 2.3f, "07-05", "2.3元"));
        mCoordinates.add(new LineGraphView.Coordinate(5, 1.6f, "07-06", "1.6元"));
        mCoordinates.add(new LineGraphView.Coordinate(6, 1.7f, "07-07", "1.7元"));
        mCoordinates.add(new LineGraphView.Coordinate(7, 1.9f, "07-08", "1.9元"));
        mCoordinates.add(new LineGraphView.Coordinate(8, 1.9f, "07-09", "1.9元"));
        mCoordinates.add(new LineGraphView.Coordinate(9, 1.9f, "07-10", "1.9元"));
        mCoordinates.add(new LineGraphView.Coordinate(10, 2.1f, "07-11", "2.1元"));
        mCoordinates.add(new LineGraphView.Coordinate(11, 2.0f, "07-12", "2.0元"));
        mCoordinates.add(new LineGraphView.Coordinate(12, 2.8f, "07-13", "2.8元"));
        mCoordinates.add(new LineGraphView.Coordinate(13, 1.2f, "07-14", "1.2元"));
        mCoordinates.add(new LineGraphView.Coordinate(14, 1.4f, "07-15", "1.4元"));
        mCoordinates.add(new LineGraphView.Coordinate(15, 1.9f, "07-16", "1.9元"));
        mCoordinates.add(new LineGraphView.Coordinate(16, 2.0f, "07-17", "2.0元"));
        mCoordinates.add(new LineGraphView.Coordinate(17, 2.3f, "07-18", "2.3元"));
        mCoordinates.add(new LineGraphView.Coordinate(18, 2.1f, "07-19", "2.3元"));
        mCoordinates.add(new LineGraphView.Coordinate(19, 2.0f, "07-20", "2.3元"));
        mCoordinates.add(new LineGraphView.Coordinate(20, 2.1f, "07-21", "2.1元"));
        mCoordinates.add(new LineGraphView.Coordinate(21, 2.0f, "07-22", "2.0元"));
        mCoordinates.add(new LineGraphView.Coordinate(22, 2.8f, "07-23", "2.8元"));
        mCoordinates.add(new LineGraphView.Coordinate(23, 1.2f, "07-24", "1.2元"));
        mCoordinates.add(new LineGraphView.Coordinate(24, 1.4f, "07-25", "1.4元"));
        mCoordinates.add(new LineGraphView.Coordinate(25, 1.9f, "07-26", "1.9元"));
        mCoordinates.add(new LineGraphView.Coordinate(26, 2.0f, "07-27", "2.0元"));
        mCoordinates.add(new LineGraphView.Coordinate(27, 2.3f, "07-28", "2.3元"));
        mCoordinates.add(new LineGraphView.Coordinate(28, 2.1f, "07-29", "2.1元"));
        mCoordinates.add(new LineGraphView.Coordinate(29, 2.0f, "07-30", "2.0元"));
        mCoordinates.add(new LineGraphView.Coordinate(30, 2.0f, "07-31", "2.0元"));
        return mCoordinates;
    }

    private List<LineGraphView.Coordinate> getX() {
        List<LineGraphView.Coordinate> mXCoordinates = new ArrayList<>();
        mXCoordinates.add(new LineGraphView.Coordinate(0, 0, "07-01", "0"));
//        mXCoordinates.add(new LineGraphView.Coordinate(6,0,"07-07","0"));
//        mXCoordinates.add(new LineGraphView.Coordinate(12,0,"07-13","0"));
//        mXCoordinates.add(new LineGraphView.Coordinate(18,0,"07-19","0"));
//        mXCoordinates.add(new LineGraphView.Coordinate(24,0,"07-25","0"));
//        mXCoordinates.add(new LineGraphView.Coordinate(30,0,"07-31","0"));
        mXCoordinates.add(new LineGraphView.Coordinate(10, 0, "07-11", "0"));
        mXCoordinates.add(new LineGraphView.Coordinate(20, 0, "07-21", "0"));
        mXCoordinates.add(new LineGraphView.Coordinate(30, 0, "07-31", "0"));
        return mXCoordinates;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("QCInfoActivity  onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.i("QCInfoActivity  onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("QCInfoActivity  onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("QCInfoActivity  onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("QCInfoActivity  onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("QCInfoActivity  onDestroy");
    }
}
