package com.qcx.mini.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemCommentContentAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.InputDialog;
import com.qcx.mini.entity.CommentEntity;
import com.qcx.mini.entity.MessageListEntity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.QuRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论详情
 */
public class CommentInfoActivity extends BaseActivity {
    private ItemCommentContentAdapter adapter;
    private int pageNum = 1;
    private long travelId;
    CommentEntity data;
    private InputDialog inputDialog;

    @BindView(R.id.comment_info_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comment_info_list)
    RecyclerView rv_list;
    @BindView(R.id.comment_info_no_data)
    TextView tv_noData;
    @BindView(R.id.comment_info_text)
    TextView tv_input;

    @OnClick(R.id.comment_info_text)
    void input() {
        if(parentID==0){
            ToastUtil.showToast("该行程暂时无法评论");
            return;
        }
        if(inputDialog!=null){
            inputDialog.dismiss();
        }
        inputDialog = new InputDialog()
                .setDefaultText(tv_input.getText())
                .setHint(tv_input.getHint())
                .setListener(inputListener);
        inputDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_comment_info;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(inputDialog!=null){
            inputDialog.dismiss();
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("详情", true, false);
        travelId = getIntent().getLongExtra("travelId", 0);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        adapter = new ItemCommentContentAdapter(this);
        adapter.setHeadListener(new ItemDriverAndTravelClickListenerImp(this));
        adapter.setCommentClickListener(new ItemCommentContentAdapter.OnItemClickListener() {
            @Override
            public void onCommentClick(CommentEntity.CommentContent commentContent) {
                LogUtil.i("回复 " + commentContent.getNickName() + " id=" + commentContent.getComment().getId()+" phone="+commentContent.getComment().getOwnerPhone());
                headText = "回复@" + commentContent.getNickName() + "：";
                tv_input.setHint(headText);
                if (commentContent.getComment().getLevel() == 1) {
                    parentID = commentContent.getComment().getId();
                } else {
                    parentID = commentContent.getComment().getParentID();
                }
                targetPhone = commentContent.getComment().getOwnerPhone();
                level = 2;
                input();
            }
        });
        rv_list.setAdapter(adapter);


        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(1);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(pageNum);
            }
        });

        getData(pageNum);
        showLoadingDialog();
    }

    private void getData(final int page) {
        if(page==1){
            pageNum=1;
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("pageNo", page);
        Request.post(URLString.comments, params, new EntityCallback(CommentEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                CommentEntity entity = (CommentEntity) t;
                data = entity;
                if(page==1){
                    adapter.setHeadData(entity.getSelfTravel(), entity.getCommentNum(), 1);
                    adapter.setDatas(entity.getComments());
                }else {
                    adapter.addDatas(entity.getComments());
                }
                if(entity.getComments()!=null&&entity.getComments().size()>0){
                    pageNum++;
                }
                showData();
                initParams();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });

    }

    private void showData() {
        if (adapter.getItemCount() == 0) {
            tv_noData.setVisibility(View.VISIBLE);
        } else {
            tv_noData.setVisibility(View.GONE);
        }
    }

    InputDialog.OnInputDialogListener inputListener = new InputDialog.OnInputDialogListener() {
        @Override
        public void onInputChanged(String text, InputDialog dialog) {
            tv_input.setText(text);
            content = text;
        }

        @Override
        public void onSureClick(String text, InputDialog dialog) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            sendComment();
            dialog.dismiss();
            getData(1);
        }

        @Override
        public void onDismiss(String text, InputDialog dialog) {
            if (TextUtils.isEmpty(text)) {
                initParams();
            }
        }
    };

    private String headText = "";
    private long targetPhone;
    private String content;
    private long parentID;
    private int level;

    private void initParams() {
        LogUtil.i("回复 travel" + " id=" + travelId);
        try{
            headText = "";
            targetPhone = data.getSelfTravel().getPhone();
            parentID = data.getSelfTravel().getTravelVo().getTravelId();
            level = 1;
            tv_input.setHint("说点什么吧..");
            tv_input.setText("");
        }catch (Exception e){
            e.printStackTrace();
            parentID=0;
        }
    }

    private void sendComment() {
        if (TextUtils.isEmpty(tv_input.getText())) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("targetPhone", targetPhone);
        params.put("content", headText + content);
        params.put("parentID", parentID);
        params.put("level", level);
        Request.post(URLString.addComment, params, new EntityCallback(MessageListEntity.class) {
            @Override
            public void onSuccess(Object t) {

            }

        });
    }

    private void finishLoading(){
        hideLoadingDialog();
        if(refreshLayout!=null){
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }

}
