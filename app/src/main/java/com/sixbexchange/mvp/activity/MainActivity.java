package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UUIDS;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.MainBinder;
import com.sixbexchange.mvp.delegate.MainDelegate;
import com.sixbexchange.mvp.fragment.AssetsFragment;
import com.sixbexchange.mvp.fragment.MineFragment;
import com.sixbexchange.mvp.fragment.TransactionFragment;
import com.sixbexchange.mvp.fragment.UserOrderFragment;

public class MainActivity extends BaseDataBindActivity<MainDelegate, MainBinder> {

    @Override
    protected Class<MainDelegate> getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    public MainBinder getDataBinder(MainDelegate viewDelegate) {
        return new MainBinder(viewDelegate);
    }

    String uid;

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        uid = UUIDS.getUUID() + System.currentTimeMillis();

    }


    boolean isHaveNoSaved = false;
    int showPosition = 0;

    @Override
    protected void bindEvenListenerBuyState(Bundle savedInstanceState) {
        super.bindEvenListenerBuyState(savedInstanceState);
        if (savedInstanceState != null) {
            showPosition = savedInstanceState.getInt("position", 0);
        }
        isHaveNoSaved = savedInstanceState == null;
        initFragment(isHaveNoSaved);
    }


    //添加主页4个基础页面
    public void initFragment(boolean isInit) {
        //设置 以哪个FrameLayout 作为展示
        // getIntentData();
        viewDelegate.initBottom(showPosition, new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                showFragment(position);
            }
        });
        viewDelegate.initAddFragment(R.id.fl_root, getSupportFragmentManager());
        if ((isInit && ListUtils.isEmpty(getSupportFragmentManager().getFragments()))) {
            if (showPosition == 0) {
                viewDelegate.addFragment(transactionFragment = new TransactionFragment(),
                        "index1", 0);
            } else if (showPosition == 1) {
                viewDelegate.addFragment(userOrderFragment = new UserOrderFragment(),
                        "index2", 1);
            } else if (showPosition == 2) {
                viewDelegate.addFragment(assetsFragment = new AssetsFragment(),
                        "index3", 2);
            } else if (showPosition == 3) {
                viewDelegate.addFragment(mineFragment = new MineFragment(),
                        "index4", 3);
            }
        } else {
            transactionFragment = (TransactionFragment) viewDelegate.getFragmentByTag(
                    "index1", 0);
            userOrderFragment = (UserOrderFragment) viewDelegate.getFragmentByTag(
                    "index2", 1);
            assetsFragment = (AssetsFragment) viewDelegate.getFragmentByTag(
                    "index3", 2);
            mineFragment = (MineFragment) viewDelegate.getFragmentByTag(
                    "index4", 3);
        }
        showFragment(showPosition);
        setDoubleClickExit(true);
    }

    TransactionFragment transactionFragment;
    MineFragment mineFragment;
    AssetsFragment assetsFragment;
    UserOrderFragment userOrderFragment;

    public void showFragment(int position) {
        if (viewDelegate != null && viewDelegate.getFragmentList() != null) {
            if (viewDelegate.getFragmentList().get(position) == null) {
                if (position == 0) {
                    viewDelegate.addFragment(transactionFragment = new TransactionFragment(),
                            "index1", 0);
                } else if (position == 1) {
                    viewDelegate.addFragment(userOrderFragment = new UserOrderFragment(),
                            "index2", 1);
                } else if (position == 2) {
                    viewDelegate.addFragment(assetsFragment = new AssetsFragment(),
                            "index3", 2);
                } else if (position == 3) {
                    viewDelegate.addFragment(mineFragment = new MineFragment(),
                            "index4", 3);
                }
            }
            viewDelegate.showFragment(position);
            viewDelegate.viewHolder.mainBottomNavigation.setSelectPosition(position);
        }
        showPosition = position;
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

    public void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            Intent intent = null;
            try {
                intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (intent != null) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
                    //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
                    if (!hasIgnored) {
                        startActivity(intent);
                    }
                }
            }
        }
    }
}
