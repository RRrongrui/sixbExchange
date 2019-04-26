package com.sixbexchange.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.sixbexchange.R;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class UserSet {
    private static class userSet {
        private static UserSet userSet = new UserSet();
    }

    public static UserSet getinstance() {
        return userSet.userSet;
    }

    public void setSystemVersion(String setSystemVersion) {
        SaveUtil.getInstance().saveString("setSystemVersion", setSystemVersion);
    }

    public String getSystemVersion() {
        String setSystemVersion = SaveUtil.getInstance().getString("setSystemVersion");
        if (TextUtils.isEmpty(setSystemVersion)) {
            return AppUtils.getAppVersionName();
        }
        return setSystemVersion;
    }
    public int getDropColor() {
        boolean redRise = isRedRise();
        int i = redRise ? R.color.decreasing_color : R.color.increasing_color;
        return i;
    }

    public int getRiseColor() {
        boolean redRise = isRedRise();
        int i = redRise ? R.color.increasing_color : R.color.decreasing_color;
        return i;
    }

    public boolean isRedRise() {
        return SaveUtil.getInstance().getBoolean("isRedRise");
    }

    public void setRedRise(boolean isRedRise) {
        SaveUtil.getInstance().saveBoolean("isRedRise", isRedRise);
    }
    //用户设置k线分钟
    public void setKTime(String kTime) {
        SaveUtil.getInstance().saveString("kTime", kTime);
    }

    public String getKTime() {
        String kTime = SaveUtil.getInstance().getString("kTime");
        return TextUtils.isEmpty(kTime) ? "5m" : kTime;
    }
    //用户设置k线缩放级别
    public float getKlineScale() {
        String KlineScale = SaveUtil.getInstance().getString("KlineScale");
        if (TextUtils.isEmpty(KlineScale)) {
            return 1f;
        } else {
            return Float.parseFloat(KlineScale);
        }
    }

    public void setKlineScale(float KlineScale) {
        SaveUtil.getInstance().saveString("KlineScale", KlineScale + "");
    }
}
