package com.qcx.mini.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.BalanceInfoActivity;
import com.qcx.mini.activity.RealNameActivity;
import com.qcx.mini.activity.RealNameInfoActivity;
import com.qcx.mini.activity.SetActivity;
import com.qcx.mini.activity.TravelHistoryActivity;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.PersonalInfoEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.ToastUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/10.
 */

public class MeFragment extends BaseFragment {
    private String mToken = User.getInstance().getToken();
    private PersonalInfoEntity personalInfo;
    private static final int INFO_CHANGE_CODE = 22;
    private ItemTaskViewHolder driverTaskViewHolder;
    private ItemTaskViewHolder realNameTaskViewHolder;

    @BindView(R.id.fragment_me_icon)
    ImageView iv_icon;
    @BindView(R.id.fragment_me_name)
    TextView tv_name;
    @BindView(R.id.fragment_me_personal_info)
    TextView tv_personalInfo;
    @BindView(R.id.fragment_me_unfinished_travel_text)
    TextView tv_unfinishedTravelNum;
    @BindView(R.id.fragment_me_money_text)
    TextView tv_money;
    @BindView(R.id.fragment_me_CPLE_text)
    TextView tv_CPLE;
    @BindView(R.id.fragment_me_all_travel_num)
    TextView tv_allTravelNum;
    @BindView(R.id.fragment_me_task_driver)
    View v_taskDriver;
    @BindView(R.id.fragment_me_task_real_name)
    View v_taskRealName;

    @OnClick(R.id.fragment_me_task_driver)
    void driverAuth() {
        new OnDriverAuthClickListener(getActivity()).onClick();
    }

    @OnClick(R.id.fragment_me_task_real_name)
    void realName() {
        if(User.getInstance().isAuthenStatus()){
            startActivity(new Intent(getContext(), RealNameInfoActivity.class));
        }else {
            Intent intent=new Intent(getContext(), RealNameActivity.class);
            intent.putExtra("type",RealNameActivity.TYPE_NORMAL);
            startActivity(intent);
        }
    }

    @OnClick(R.id.fragment_me_not_finished_travel_view)
    void travel() {
        ToastUtil.showToast("我的行程");
        startActivity(new Intent(getContext(), TravelHistoryActivity.class));
    }

    @OnClick(R.id.fragment_me_money_view)
    void balance() {
        startActivity(new Intent(getContext(), BalanceInfoActivity.class));
    }

    @OnClick(R.id.fragment_me_CPLE_view)
    void cple() {
        ToastUtil.showToast("我的CPLE");
    }

    @OnClick(R.id.fragment_me_kefu)
    void kefu() {
        DialogUtil.call(getContext(), null);
    }

    @OnClick(R.id.fragment_me_set)
    void set() {
        startActivityForResult(new Intent(getContext(), SetActivity.class), INFO_CHANGE_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((mToken != null && !mToken.equals(User.getInstance().getToken()))) {
            userChange();
        }
        dataChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if ((mToken != null && !mToken.equals(User.getInstance().getToken()))) {
                userChange();
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        driverTaskViewHolder = new ItemTaskViewHolder(v_taskDriver);
        realNameTaskViewHolder = new ItemTaskViewHolder(v_taskRealName);
        getPersonalInfo();
        dataChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INFO_CHANGE_CODE) {
                getPersonalInfo();
            }
        }
    }

    private void getPersonalInfo() {
        NetUtil.getPersonalInfo(User.getInstance().getPhoneNumber(), new QuCallback<PersonalInfoEntity>() {
            @Override
            public void onSuccess(PersonalInfoEntity personalInfoEntity) {
                personalInfo = personalInfoEntity;
                setPersonalInfoToUI(personalInfo);
            }
        });
    }

    private void setPersonalInfoToUI(final PersonalInfoEntity personal) {
        if (personal == null || personal.getPersonalInfo() == null) return;
        PersonalInfoEntity.PersonalEntity personalEntity = personal.getPersonalInfo();
        Picasso.with(getContext())
                .load(personalEntity.getPicture())
                .resize(ConstantValue.ICON_RESIZE_V2, ConstantValue.ICON_RESIZE_V2)
                .error(R.mipmap.img_me)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(iv_icon);
        tv_name.setText(personalEntity.getNickName());
        tv_unfinishedTravelNum.setText(String.valueOf(personalEntity.getGoNum()));
        tv_allTravelNum.setText(String.valueOf(personalEntity.getGoNum()));
        tv_money.setText(String.valueOf(personalEntity.getMoneyCard()));
        tv_CPLE.setText(String.valueOf(personalEntity.getQC()));
        String info = CommonUtil.appendStringInPoint(CommonUtil.getSexText(personalEntity.getSex()), personalEntity.getAge());
        tv_personalInfo.setText(info);


    }

    private void userChange() {
        mToken = User.getInstance().getToken();
        getPersonalInfo();
    }

    private void dataChanged(){
        driverTaskViewHolder.bindData(getTask(true));
        realNameTaskViewHolder.bindData(getTask(false));
    }


    private Task getTask(boolean isDrverTask) {
        Task task = new Task();
        if (isDrverTask) {
            task.setType(Task.TaskType.DRIVER);
            switch (User.getInstance().getDriverStatus()) {
                case ConstantValue.AuthStatus.PASS:
                    task.setDescribe("车主信息经过严格核实");
                    task.setStatus(Task.Status.PASS);
                    task.setStatusText("已认证");
                    break;
                case ConstantValue.AuthStatus.CHECKING:
                    task.setDescribe("提交资料成功，后台审核中");
                    task.setStatus(Task.Status.NOT_PASS);
                    task.setStatusText("+100原力");
                    break;
                case ConstantValue.AuthStatus.NOTPASS:
                    task.setDescribe("认证失败，请重新上传资料");
                    task.setStatus(Task.Status.NOT_PASS);
                    task.setStatusText("+100原力");
                    break;
                default:
                    task.setDescribe("认证成为车主,开启顺风出行");
                    task.setStatus(Task.Status.NOT_PASS);
                    task.setStatusText("+100原力");
                    break;
            }
        }else {
            task.setType(Task.TaskType.REAL_NAME);
            if(User.getInstance().isAuthenStatus()){
                task.setStatus(Task.Status.PASS);
                task.setDescribe("已完成实名认证");
                task.setStatusText("已认证");
            }else {
                task.setStatus(Task.Status.NOT_PASS);
                task.setDescribe("完成实名认证,平台免费为你出行投保");
                task.setStatusText("+100原力");
            }
        }
        return task;
    }

    private class ItemTaskViewHolder {
        private ImageView iv_img;
        private TextView tv_taskName;
        private TextView tv_taskStatus;
        private TextView tv_describe;

        public ItemTaskViewHolder(View view) {
            iv_img = view.findViewById(R.id.item_task_authen_img);
            tv_taskName = view.findViewById(R.id.item_task_authen_name);
            tv_taskStatus = view.findViewById(R.id.item_task_authen_status);
            tv_describe = view.findViewById(R.id.item_task_authen_describe);
        }

        public void bindData(@NonNull Task task) {
            switch (task.getType()) {
                case DRIVER:
                    iv_img.setImageResource(R.mipmap.home_icon_sign_shezhu);
                    break;
                case REAL_NAME:
                    iv_img.setImageResource(R.mipmap.home_icon_sign_id);
                    break;
            }
            tv_taskName.setText(task.getType().getTypeName());
            tv_describe.setText(task.getDescribe());
            tv_taskStatus.setText(task.getStatusText());
            switch (task.getStatus()) {
                case PASS:
                    tv_taskStatus.setBackgroundResource(R.drawable.bg_border_green);
                    tv_taskStatus.setTextColor(0xFF4CAF50);
                    break;
                case NOT_PASS:
                    tv_taskStatus.setBackgroundResource(R.drawable.bg_circular_yellow_3);
                    tv_taskStatus.setTextColor(Color.WHITE);
                    break;
            }

        }
    }

    public static class Task {
        private TaskType type;
        private Status status;
        private String statusText;
        private String describe;

        public enum TaskType {
            DRIVER("车主认证"), REAL_NAME("实名认证");

            private String typeName;

            TaskType(String typeName) {
                this.typeName = typeName;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }
        }

        public enum Status {
            PASS, NOT_PASS;
        }

        public TaskType getType() {
            return type;
        }

        public void setType(TaskType type) {
            this.type = type;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }

}
