package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.MessageListAdapter;
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

import static android.view.Gravity.BOTTOM;

/**
 * Created by Administrator on 2018/3/7.
 */

public class CommentDialog extends BaseDialog {
    private TouchMoveLayout v_commentView;
    private TextView tv_send;
    private TextView et_input;
    private RecyclerView rv_comment;
    private TextView tv_commentNum;

    private LinearLayoutManager manager;
    private MessageListAdapter adapter;

    private OnCommentSuccessListener listener;

    private long travelId;
    private int commentNum;
    private long targetPhone;
    private long parentID;
    private int level = 1;
    private String headText;//"回复@"

    @Override
    public int getGravity() {
        return BOTTOM;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_comment;
    }

    @Override
    public void initView(View view) {
        v_commentView = view.findViewById(R.id.comment_view);
        v_commentView.setVisibility(View.VISIBLE);
        tv_send = view.findViewById(R.id.comment_send);
        et_input = view.findViewById(R.id.comment_edit);
        rv_comment = view.findViewById(R.id.comment_recy);
        tv_commentNum = view.findViewById(R.id.comment_commentNum);

        tv_commentNum.setText("评论".concat(String.valueOf(commentNum)));
        et_input.setHint("添加评论");
        et_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(et_input.getHint(),et_input.getText());
            }
        });

        view.findViewById(R.id.comment_commentView_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_comment.setLayoutManager(manager);
        adapter = new MessageListAdapter(getActivity());
        rv_comment.setAdapter(adapter);
        adapter.setListener(new MessageListAdapter.OnMessageListItemClickListener() {
            @Override
            public void onClick(MessageListEntity.MessageEntity message) {
                et_input.setHint("回复@".concat(message.getNickName()));
                targetPhone = message.getComment().getOwnerPhone();
                if (message.getComment().getLevel() == 1) {
                    parentID = message.getComment().getId();
                } else {
                    parentID = message.getComment().getParentID();
                }
                level = 2;
                headText = "回复@" + message.getNickName() + "：";
                showInputDialog(et_input.getHint(),et_input.getText());
            }
        });
        v_commentView.setListener(new TouchMoveLayout.OnMoveDistanceListener() {
            @Override
            public void onMoveY(float y) {
                if (y > 100) {
                    out();
                }
            }
        });
        v_commentView.setmRecycleView(rv_comment);
        v_commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });
        getCommentData(travelId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(inputDialog!=null){
            inputDialog.dismiss();
        }
    }

    private InputDialog inputDialog;
    private void showInputDialog(CharSequence hint, CharSequence text) {
        if(inputDialog!=null){
            inputDialog.dismiss();
        }
        inputDialog= new InputDialog()
                .setHint(hint)
                .setDefaultText(text)
                .setListener(inputListener);

        inputDialog.show(getFragmentManager(), "");
    }


    public CommentDialog initData(int commentNum, long targetPhone, long travelId) {
        level = 1;
        this.travelId = travelId;
        this.parentID = travelId;
        this.targetPhone = targetPhone;
        this.commentNum = commentNum;
        headText = "";
        return this;
    }

    public void out() {
        dismiss();
    }

    private void getCommentData(long travelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("pageNo", 1);

        Request.post(URLString.listComments, params, new EntityCallback(MessageListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                MessageListEntity entity = (MessageListEntity) t;
                adapter.setDatas(entity.getComments());
                rv_comment.scrollToPosition(0);
            }
        });
    }

    private void sendComment() {
        if(TextUtils.isEmpty(et_input.getText())){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("targetPhone", targetPhone);
        params.put("content", headText + String.valueOf(et_input.getText()));
        params.put("parentID", parentID);
        params.put("level", level);
        Request.post(URLString.addComment, params, new EntityCallback(MessageListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                MessageListEntity messageList = (MessageListEntity) t;
                et_input.setText("");
                adapter.setDatas(messageList.getComments());
                if (listener != null) {
                    listener.onSuccess(messageList.getCommentNum());
                }
                commentNum=messageList.getCommentNum();
                tv_commentNum.setText("评论".concat(String.valueOf(commentNum)));
//                KeybordS.closeKeybord(et_input, getContext());
            }

        });
    }

    InputDialog.OnInputDialogListener inputListener = new InputDialog.OnInputDialogListener() {
        @Override
        public void onInputChanged(String text, InputDialog dialog) {
            et_input.setText(text);
        }

        @Override
        public void onSureClick(String text, InputDialog dialog) {
            sendComment();
            dialog.dismiss();
        }

        @Override
        public void onDismiss(String text, InputDialog dialog) {

        }
    };

    public CommentDialog setListener(OnCommentSuccessListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnCommentSuccessListener {
        void onSuccess(int commentNum);
    }

}
