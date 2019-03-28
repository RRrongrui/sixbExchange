package com.fivefivelike.mybaselibrary.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.UUIDS;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.yanzhenjie.nohttp.Headers;

/**
 * @创建者 CSDN_LQR
 * @描述 Application基类
 */
public abstract class BaseApp extends Application {
    protected static BaseApp instance;

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    public static synchronized BaseApp getInstance() {
        return instance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        //        Configuration config=new Configuration();
        //        config.setToDefaults();
        //        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    public abstract void startCustomerService(Activity activity);

    //获取登录页面class
    public abstract Class getLoginActivityClass();
    public abstract Class getMainActivityClass();

    public abstract void returnHeader(Headers headers);

    @Override
    public void onCreate() {
        super.onCreate();
        //对全局属性赋值
        if (isMainProcess()) {
            instance = this;
            GlobleContext.init(this);
            UUIDS.buidleID(this).check();
            preinitX5WebCore();
            Utils.init(this);
        }
    }

    public abstract String getHttpKey();

    private void preinitX5WebCore() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.i("QbSdk", " onViewInitFinished is " + arg0);
                Log.i("QbSdk", WebView.getCrashExtraMessage(BaseApp.getInstance().getApplicationContext()));
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }

    /**
     * OnLowMemory是Android提供的API，在系统内存不足，
     * 所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //垃圾回收
        System.gc();
    }

    /**
     * 获取当前进程名
     */
    public String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public boolean isMainProcess() {
        return getApplicationContext().getPackageName().equals(getCurrentProcessName());
    }
}
