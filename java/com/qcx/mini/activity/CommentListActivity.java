package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemCommentAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.CommentListEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
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

/**
 * 评论列表
 */
public class CommentListActivity extends BaseActivity {
    private ItemCommentAdapter adapter;
    private int pageNum=1;

    @BindView(R.id.comment_list_refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.comment_list_list)
    RecyclerView rv_comment;

    @Override
    public int getLayoutID() {
        return R.layout.activity_comment_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("评论",true,false);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_comment.setLayoutManager(manager);
        adapter=new ItemCommentAdapter(this);
        adapter.setListener(new ItemCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CommentListEntity.Comment comment, ImageView point) {
                Intent intent=new Intent(CommentListActivity.this,CommentInfoActivity.class);
                intent.putExtra("travelId",comment.getTravelId());
                startActivity(intent);
                comment.setCommentStatus(1);
                point.setVisibility(View.GONE);
                changCommentStatus(comment.getCommentId());
            }
        });
        rv_comment.setAdapter(adapter);
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
        getData(1);
    }

    private void getData(final int page){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo",page);
        Request.post(URLString.commentsTravelList, params, new EntityCallback(CommentListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                CommentListEntity entity= (CommentListEntity) t;
                if(page==1){
                   if(entity!=null){
                       adapter.setDatas(entity.getCommentsTravelList());
                   }
                }else {
                    if(entity!=null)adapter.addDatas(entity.getCommentsTravelList());
                }

                if(entity!=null&&entity.getCommentsTravelList()!=null&&entity.getCommentsTravelList().size()>0){
                    pageNum++;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void finishLoading(){
        if(refreshLayout!=null){
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }

    private void changCommentStatus(long commentId){
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("commentId",commentId);
        params.put("status", ConstantValue.CommentStatus.IS_READED);
        Request.post(URLString.changCommentStatus, params, new EntityCallback(null) {
            @Override
            public void onSuccess(Object t) {

            }
        });
    }

}
