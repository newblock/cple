package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.adapter.ItemPersonalLineAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.QuDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.entity.PersonalLineListEntity;
import com.qcx.mini.entity.ReleasePageEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/8/17.
 *
 */

public class PersonalLinesFragment extends BaseFragment {
    private final static int LINE_CHANGE = 20;
    private ItemPersonalLineAdapter adapter;
    private int type;

    @BindView(R.id.fragment_personal_line_list)
    RecyclerView rv_list;
    @BindView(R.id.fragment_personal_line_add_view)
    View v_add;
    @BindView(R.id.fragment_personal_lines_no_data_view)
    View v_noData;

    @OnClick(R.id.personal_line_add)
    void add() {
        if(User.getInstance().getDriverStatus()!=ConstantValue.AuthStatus.PASS&&type==ConstantValue.TravelType.DRIVER){
            DialogUtil.getAuthDialog(getActivity()).show(getChildFragmentManager(),"");
            return;
        }
        if(adapter.getItemCount()>=4){
            new QuDialog().setTitle("最多可添加4条路线")
                    .setMessage("车主/乘客常用路线最多可添加4条；如需\r\n添加新路线,可修改原路线或删除原路线后\r\n再添加新路线")
                    .setRightBtn("知道了", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .show(getFragmentManager(),"");
            return;
        }
        Intent intent = new Intent(getContext(), ReleaseTravel_2Activity.class);
        ReleasePageEntity pageData = type == ConstantValue.TravelType.PASSENGER ?
                ReleasePageEntity.Factory.linePassengerData() :
                ReleasePageEntity.Factory.lineDriverData();
        intent.putExtra("data", pageData);
        startActivityForResult(intent, LINE_CHANGE);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_personal_lines;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt("type");
        }
        getData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", type);
    }

    private QuDialog deleteDialog;
    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        adapter = new ItemPersonalLineAdapter(getContext());
        rv_list.setAdapter(adapter);
        rv_list.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int s = (int) (16 * UiUtils.SCREENRATIO);
                outRect.top = s;
                outRect.right = s;
                outRect.left = s;
                int position = parent.getChildAdapterPosition(view);
                if (position == state.getItemCount() - 1) {
                    outRect.bottom = (int) (80 * UiUtils.SCREENRATIO);
                }
            }
        });
        adapter.setListener(new ItemPersonalLineAdapter.OnLineClickListener() {
            @Override
            public void onDelete(final PersonalLineEntity line) {
                if(deleteDialog==null){
                    deleteDialog=new QuDialog();
                    deleteDialog.setTitle("删除常用路线")
                            .setLeftBtn("取消", new QuDialog.OnClickListener() {
                                @Override
                                public void onClick(QuDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                }
                deleteDialog.setRightBtn("确定", new QuDialog.OnClickListener() {
                    @Override
                    public void onClick(QuDialog dialog) {
                        deleteLines(line);
                        dialog.dismiss();
                    }
                });
                showDialog(deleteDialog,getFragmentManager());
            }

            @Override
            public void onDetail(PersonalLineEntity line) {
                Intent intent = new Intent(getContext(), ReleaseTravel_2Activity.class);
                ReleasePageEntity pageData=ReleasePageEntity.Factory.getPageData(line);
                intent.putExtra("data",pageData);
                startActivityForResult(intent, LINE_CHANGE);
            }
        });
    }

    private void showDialog(DialogFragment dialog, FragmentManager manager){
        if(dialog!=null&&!dialog.isAdded()&&!dialog.isVisible()&&!dialog.isRemoving()){
            dialog.show(manager,"");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == LINE_CHANGE) {
            getData();
        }
    }

    private void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("type", type);
        Request.post(URLString.personalLineListByRole, params, new EntityCallback(PersonalLineListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                PersonalLineListEntity entity = (PersonalLineListEntity) t;
                adapter.setDatas(entity.getPersonalTravelLineList());
                dataChanged();
            }
        });
    }

    public void setType(int type) {
        this.type = type;
    }

    private void deleteLines(final PersonalLineEntity line) {
        List<Long> linesIds = new ArrayList<>();
        linesIds.add(line.getLineId());
        if (linesIds.size() <= 0) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("lineIds", linesIds);
        showLoadingDialog();
        Request.post(URLString.personalLineDelete, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                hideLoadingDialog();
                if (intEntity.getStatus() == 200) {
                    adapter.setDeleteAble(false);
                    adapter.remove(line);
                    dataChanged();
                } else {
                    onError("删除失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private void dataChanged(){

//        if(adapter==null||adapter.getItemCount()<=0){
//            rv_list.setVisibility(View.GONE);
//            v_noData.setVisibility(View.VISIBLE);
//        }else {
//            rv_list.setVisibility(View.VISIBLE);
//            v_noData.setVisibility(View.GONE);
//        }

    }
}
