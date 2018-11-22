package com.qcx.mini.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.MessageListAdapter;
import com.qcx.mini.entity.MainRecommendTravelsEntity;
import com.qcx.mini.entity.MessageListEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.KeybordS;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.TouchMoveLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/17.
 */

public class CommentView {
    private Activity activity;
    private View mView;
    LinearLayoutManager manager;

    private OnCommentSuccessListener listener;
    private OnStatusChangeListener onStatusChangeListener;

    private long travelId;
    private MessageListAdapter adapter;

    View commentView;
    TouchMoveLayout commentViews;
    boolean commentViewIsShow = false;
    EditText edit;
    RecyclerView lv_comments;

    private int commentNum;
    private long targetPhone;
    private long parentID;
    private int level = 1;
    private TextView tv_send;
    private TextView tv_commentNum;
    private String headText;//"回复@"

    public CommentView(Activity activity, View view) {
        this.activity = activity;
        this.mView = view;
        initCommentView();
    }

    private void initCommentView() {
        commentView = mView.findViewById(R.id.comment_commentView);
        commentViews = mView.findViewById(R.id.comment_view);
        tv_send = mView.findViewById(R.id.comment_send);
        edit = mView.findViewById(R.id.comment_edit);
        lv_comments = mView.findViewById(R.id.comment_recy);
        tv_commentNum = mView.findViewById(R.id.comment_commentNum);
        mView.findViewById(R.id.comment_commentView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });

        commentViews.setVisibility(View.INVISIBLE);
        commentView.post(new Runnable() {
            @Override
            public void run() {
                commentView.animate().translationY(commentView.getHeight()).setDuration(0);
            }
        });

        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("dddddd click");
                out();
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_comments.setLayoutManager(manager);
        adapter = new MessageListAdapter(activity);
        lv_comments.setAdapter(adapter);
        adapter.setListener(new MessageListAdapter.OnMessageListItemClickListener() {
            @Override
            public void onClick(MessageListEntity.MessageEntity message) {
                edit.setHint("回复@".concat(message.getNickName()));
                targetPhone = message.getComment().getOwnerPhone();
                if (message.getComment().getLevel() == 1) {
                    parentID = message.getComment().getId();
                } else {
                    parentID = message.getComment().getParentID();
                }
                level = 2;
                headText = "回复@" + message.getNickName() + "：";
                edit.requestFocus();
                KeybordS.openKeybord(edit, activity);
            }
        });
        commentViews.setListener(new TouchMoveLayout.OnMoveDistanceListener() {
            @Override
            public void onMoveY(float y) {
                if (y > 100) {
                    out();
                }
            }
        });
        commentViews.setmRecycleView(lv_comments);
        commentViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });
    }

    public void show(int commentNum, long targetPhone, long travelId) {
        if (!commentViewIsShow && travelId != 0) {
            level = 1;
            this.travelId = travelId;
            this.parentID = travelId;
            this.targetPhone = targetPhone;
            this.commentNum = commentNum;
            tv_commentNum.setText("评论".concat(String.valueOf(commentNum)));
            headText = "";
            edit.setHint("添加评论");
            commentViews.setVisibility(View.VISIBLE);
            adapter.setDatas(null);
            getCommentData(travelId);
            commentView.animate().translationY(0).setDuration(300).setListener(inAnimatorListener);
            commentViewIsShow = true;
            UiUtils.setStatusBarGray(activity);
        } else {
            LogUtil.i("显示评论列表失败");
        }
    }

    public void out() {
        LogUtil.i("commentViewIsShow=" + commentViewIsShow);
        if (commentViewIsShow) {
            commentViews.animate().alpha(0).setDuration(200);
            commentView.animate().translationY(commentView.getHeight()).setDuration(200).setListener(outAnimatorListener);
            commentViewIsShow = false;
            KeybordS.closeKeybord(edit, activity);
        }
    }

    private Animator.AnimatorListener outAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(2);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            commentViews.setVisibility(View.INVISIBLE);
            UiUtils.setStatusBarWHITE(activity);
            commentViews.animate().alpha(100).setListener(null);
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(3);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animator.AnimatorListener inAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(0);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (onStatusChangeListener != null) {
                onStatusChangeListener.onStatusChange(1);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void getCommentData(long travelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("pageNo", 1);

        Request.post(URLString.listComments, params, new EntityCallback(MessageListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    MessageListEntity entity = (MessageListEntity) t;
                    adapter.setDatas(entity.getComments());
                    lv_comments.scrollToPosition(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

    private void sendComment() {
        if(TextUtils.isEmpty(edit.getText())){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("targetPhone", targetPhone);
        params.put("content", headText + String.valueOf(edit.getText()));
        params.put("parentID", parentID);
        params.put("level", level);
        Request.post(URLString.addComment, params, new EntityCallback(MessageListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                try {
                    MessageListEntity messageList = (MessageListEntity) t;
                    edit.setText("");
                    adapter.setDatas(messageList.getComments());
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    commentNum++;
                    tv_commentNum.setText("评论".concat(String.valueOf(commentNum)));
                    KeybordS.closeKeybord(edit, activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorInfo) {

            }
        });
    }

    public void setListener(OnCommentSuccessListener listener) {
        this.listener = listener;
    }

    public boolean isShow() {
        return commentViewIsShow;
    }

    private boolean recycleViewisTop(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || !recyclerView.canScrollVertically(-1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.onStatusChangeListener = onStatusChangeListener;
    }

    public interface OnCommentSuccessListener {
        void onSuccess();
    }

    public interface OnStatusChangeListener {
        void onStatusChange(int status);//0 显示动画开始，1 显示,2 隐藏动画开始, 3 隐藏
    }
}
