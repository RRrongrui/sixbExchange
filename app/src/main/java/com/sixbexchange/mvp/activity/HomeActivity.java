package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.HomeBinder;
import com.sixbexchange.mvp.delegate.HomeDelegate;
import com.sixbexchange.mvp.fragment.MainFragment;
import com.sixbexchange.server.TraceServiceImpl;
import com.xdandroid.hellodaemon.DaemonEnv;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class HomeActivity extends BaseDataBindActivity<HomeDelegate, HomeBinder> {

    @Override
    protected Class<HomeDelegate> getDelegateClass() {
        return HomeDelegate.class;
    }

    @Override
    public HomeBinder getDataBinder(HomeDelegate viewDelegate) {
        return new HomeBinder(viewDelegate);
    }

    public static boolean isLogin = false;

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initFragment();
        TraceServiceImpl.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(TraceServiceImpl.class);
        ignoreBatteryOptimization(this);
    }

    private void initFragment() {
        if (isLogin) {
            if (findFragment(MainFragment.class) == null) {
                loadRootFragment(R.id.fl_container, new MainFragment());
            }
        } else {
            startActivity(new Intent(this, LoginAndRegisteredActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFragment();
    }


    @Override
    public void onBackPressedSupport() {
        ISupportFragment topFragment = getTopFragment();
        if (topFragment instanceof MainFragment) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
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
