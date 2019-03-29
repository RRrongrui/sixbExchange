package com.sixbexchange.server;

import android.text.TextUtils;

import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.sixbexchange.base.AppConst;


/**
 * Created by 郭青枫 on 2019/1/12 0012.
 */

public class HttpUrl {
    static HttpUrl httpUrl = new HttpUrl();

    public static HttpUrl getIntance() {
        if (httpUrl == null) {
            httpUrl = new HttpUrl();
        }
        return httpUrl;
    }

    public void setToken(String token) {
        SaveUtil.getInstance().saveString("token", token);
    }

    public String getToken() {
        return SaveUtil.getInstance().getString("token");
    }

    public boolean isHaveToken() {
        return !TextUtils.isEmpty(SaveUtil.getInstance().getString("token"));
    }


    /**
     * 行情
     */
    String dealUrl = "/deal";

    /**
     * 登录
     */
    public String login = AppConst.app2BaseUrl + dealUrl + "/app/login";
    /**
     * 注册
     */
    public String signup = AppConst.app2BaseUrl + dealUrl + "/app/open/signup";
}
