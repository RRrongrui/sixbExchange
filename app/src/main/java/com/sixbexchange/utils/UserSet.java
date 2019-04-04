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
}
