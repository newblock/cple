package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.ReleaseLinesEntity;
import com.qcx.mini.fragment.ConcernedTravelsFragment;
import com.qcx.mini.fragment.ForceFragment;
import com.qcx.mini.fragment.MainFragment;
import com.qcx.mini.fragment.NewsListFragment;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.fragment.MeFragment;
import com.qcx.mini.fragment.QuConversationListFragment;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.test.TestActivity;
import com.qcx.mini.utils.CasheUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.MainActionBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.plugin.location.AMapLocationActivity;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity {
    //    boolean isShow = true;//动画显示
    private String lastToken;
    private Fragment mContentFragment = null;
    //    RecommendFragment mRecommendFragment;
    private int pagePosition;
    MainFragment mMainFragment;
    ForceFragment mForceFragment;

    MeFragment mMeFragment;
    ConcernedTravelsFragment mConcernedTravelsFragment;
    NewsListFragment mNewsListFragment;

    @BindView(R.id.main_action_bar)
    MainActionBarView main_actionBar;

    @BindView(R.id.main_framelayout)
    FrameLayout mFrameLayout;

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
//        UiUtils.setFitsSystemWindows(this, false);
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        LogUtil.i("mainActivity onCreate");
        mMainFragment = new MainFragment();
        mForceFragment = new ForceFragment();
        mMeFragment = new MeFragment();
        mNewsListFragment = new NewsListFragment();

        mConcernedTravelsFragment = new ConcernedTravelsFragment();
        mContentFragment = new Fragment();

        switchContent(mMainFragment);
        main_actionBar.setCurrentPosition(0);
        main_actionBar.setListener(new MainActionBarView.OnActionBarClickListener() {
            @Override
            public void onClick(int position) {
                pagePosition = position;
                switch (position) {
                    case 0:
                        switchContent(mMainFragment);
                        break;
                    case 1:
                        switchContent(mConcernedTravelsFragment);
                        main_actionBar.setPointVisible(false);
                        break;
                    case 2:
                        switchContent(mNewsListFragment);
                        break;
                    case 3:
                        switchContent(mMeFragment);
                        break;
                }
            }

            @Override
            public void onStartClick() {
                switchContent(mForceFragment);
//                new ReleaseTypeDialog()
//                        .setListener(new ReleaseTypeDialog.OnReleaseSuccessListener() {
//                            @Override
//                            public void onSuccess() {
//                                if(mConcernedTravelsFragment!=null){
//                                    mConcernedTravelsFragment.getMyTravels();
//                                }
//                            }
//                        })
//                        .show(getSupportFragmentManager(), "");
//                startActivity(new Intent(MainActivity.this,QCInfoActivity.class));
//                startActivity(new Intent(MainActivity.this,VerifyPhoneNumActivity.class));
//                startActivity(new Intent(MainActivity.this,BalanceInfoActivity.class));
//                Intent intent=new Intent(MainActivity.this,RealNameActivity.class);
//                intent.putExtra("type",RealNameActivity.TYPE_DRIVER);
//                startActivity(intent);
//                startActivity(new Intent(MainActivity.this,AuthenticationActivity.class));
//                startActivity(new Intent(MainActivity.this,DriverInfoActivity.class));
//                startActivity(new Intent(MainActivity.this,TravelUnfinishedActivity.class));
//                startActivity(new Intent(MainActivity.this,AuthenticationStep3Activity.class));
//                startActivity(new Intent(MainActivity.this,BankAccountListActivity.class));
                startActivity(new Intent(MainActivity.this,TestActivity.class));
//                startActivity(new Intent(MainActivity.this,AMapLocationActivity.class));
//                new QCSoldDialog().show(getSupportFragmentManager(),"");
//                new QuDialog()
//                        .setTitle("这里是标题")
//                        .show(getSupportFragmentManager(),"");


//                new InputPriceDialog().show(getSupportFragmentManager(),"");

            }
        });
        main_actionBar.setPointVisible(false);
        getLines();
        NetUtil.updateDriverStatus();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//        transaction.remove(mMainFragment).remove(mForceFragment).remove(mMeFragment).remove(mNewsListFragment).remove(mConcernedTravelsFragment).commit();
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ReleaseTravelActivity.RELEASE_SUCCESS:
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(mConcernedTravelsFragment).commit();
                    switchContent(mConcernedTravelsFragment);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastToken = User.getInstance().getToken();
        LogUtil.i("main_activity___________________ onResume" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("main_activity___________________ onPause" + DateUtil.getTimeStr(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS"));
    }

    private void switchContent(Fragment to) {
        if (to == null) {
            LogUtil.i("ttttttttttttttt fragment==null");
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mContentFragment != to) {
            if (mContentFragment != null) {
                transaction.hide(mContentFragment);
            }
            if (!to.isAdded()) {
                transaction.add(R.id.main_framelayout, to).commit();
            } else {
                transaction.show(to).commit();
            }
            mContentFragment = to;
        } else {
            transaction.show(to).commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventStatus status) {
        if (status == EventStatus.SHOW_MAIN_RED_POINT) {
            if (main_actionBar != null && pagePosition != 1) {
                main_actionBar.setPointVisible(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取常用线路信息
     */
    private void getLines() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.personalLinesRelease, params, new EntityCallback(ReleaseLinesEntity.class) {
            @Override
            public void onSuccess(Object t) {
                ReleaseLinesEntity linesEntity = (ReleaseLinesEntity) t;
                CasheUtil.putReleaseLinesEntity(linesEntity);
            }
        });
    }
}
