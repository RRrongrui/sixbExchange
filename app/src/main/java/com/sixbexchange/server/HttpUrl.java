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
        SaveUtil.getInstance().saveString("auth", token);
    }

    public String getToken() {
        return SaveUtil.getInstance().getString("auth");
    }

    public boolean isHaveToken() {
        return !TextUtils.isEmpty(SaveUtil.getInstance().getString("token"));
    }


    /**
     * 行情
     */
    String dealUrl = AppConst.isTest ? "" : "/api";

    /**
     * 登录
     */
    public String login = AppConst.app2BaseUrl + dealUrl + "/app/login";
    /**
     * 注册
     */
    public String signup = AppConst.app2BaseUrl + dealUrl + "/app/open/signup";
    /**
     * 用户信息
     */
    public String userinfo = AppConst.app2BaseUrl + dealUrl + "/app/user/info";
    /**
     * 注册验证码
     */
    public String vcode = AppConst.app2BaseUrl + dealUrl + "/app/open/vcode";
    /**
     * 找回密码验证码
     */
    public String preReset = AppConst.app2BaseUrl + dealUrl + "/app/open/pass/preReset";
    /**
     * 重置密码
     */
    public String setpassword = AppConst.app2BaseUrl + dealUrl + "/app/user/set/password";
    /**
     * 获取钱包列表
     */
    public String getAccount = AppConst.app2BaseUrl + dealUrl + "/app/user/getAccount";
    /**
     * 跟单列表
     */
    public String followlist = AppConst.app2BaseUrl + dealUrl + "/app/follow/list";
    /**
     * 我的跟单
     */
    public String MyFollow = AppConst.app2BaseUrl + dealUrl + "/app/follow/MyFollow";
    /**
     * 参与跟单
     */
    public String followattend = AppConst.app2BaseUrl + dealUrl + "/app/follow/attend";
    /**
     * 跟单详情
     */
    public String followdetail = AppConst.app2BaseUrl + dealUrl + "/app/follow/detail";
    /**
     * 钱包资金明细
     */
    public String getAccountDetail = AppConst.app2BaseUrl + dealUrl + "/app/user/getAccountDetail";
    /**
     * 提币地址
     */
    public String addrinfo = AppConst.app2BaseUrl + dealUrl + "/app/addr/info";
    /**
     * bitmex充值地址
     */
    public String depositAddress = AppConst.app2BaseUrl + dealUrl + "/app/bitmex/depositAddress";
    /**
     * 提币地址列表
     */
    public String extractAddr = AppConst.app2BaseUrl + dealUrl + "/app/addr/extractAddr";
    /**
     * 删除提币地址
     */
    public String delExtractAddr = AppConst.app2BaseUrl + dealUrl + "/app/addr/delExtractAddr";
    /**
     * 前登录用户手机号
     */
    public String mobile = AppConst.app2BaseUrl + dealUrl + "/app/user/mobile";
    /**
     * 新增提币地址
     */
    public String addExtractAddr = AppConst.app2BaseUrl + dealUrl + "/app/addr/addExtractAddr";
    /**
     * 提币页面
     */
    public String extract = AppConst.app2BaseUrl + dealUrl + "/app/account/extract";
    /**
     * 发送提币请求
     */
    public String sendExtract = AppConst.app2BaseUrl + dealUrl + "/app/account/sendExtract";
    /**
     * 版本更新
     */
    public String getlatestversion = AppConst.app2BaseUrl + dealUrl + "/app/open/appversion/getlatestversion";
    /**
     * 交易所币种列表
     */
    public String allCoins = AppConst.app2BaseUrl + dealUrl + "/app/open/allCoins";
    /**
     * 资金划转
     */
    public String accounttrans = AppConst.app2BaseUrl + dealUrl + "/app/account/trans";
    /**
     * 获取持仓
     */
    public String accountgetAccount = AppConst.app2BaseUrl + dealUrl + "/app/account/getAccount";
    /**
     * 用户订单
     */
    public String accountgetOrders = AppConst.app2BaseUrl + dealUrl + "/app/account/getOrders";
    /**
     * 交易信息
     */
    public String tradedetail = AppConst.app2BaseUrl + dealUrl + "/app/trade/detail";
    /**
     * 交易所列表
     */
    public String exchangeList = AppConst.app2BaseUrl + dealUrl + "/app/trade/exchangeList";
    /**
     * 交易所交易对
     */
    public String tradelist = AppConst.app2BaseUrl + dealUrl + "/app/trade/list";
    /**
     * 获取开仓可用
     */
    public String accountopen = AppConst.app2BaseUrl + dealUrl + "/app/account/open";
    /**
     * 获取平单可用
     */
    public String accountclose = AppConst.app2BaseUrl + dealUrl + "/app/account/close";
    /**
     * 获取单个币种持仓
     */
    public String getCoinPosition = AppConst.app2BaseUrl + dealUrl + "/app/account/getCoinPosition";
    /**
     * 下单
     */
    public String placeOrder = AppConst.app2BaseUrl + dealUrl + "/app/account/placeOrder";
    /**
     * 撤单
     */
    public String cancelOrder = AppConst.app2BaseUrl + dealUrl + "/app/account/cancelOrder";
    /**
     * 全部撤单
     */
    public String cancelAllOrder = AppConst.app2BaseUrl + dealUrl + "/app/account/cancelAllOrder";
    /**
     * 持仓页面下拉框数据
     */
    public String tradeCoins = AppConst.app2BaseUrl + dealUrl + "/app/open/tradeCoins";
    /**
     * 修改杠杆
     */
    public String changeLeverage = AppConst.app2BaseUrl + dealUrl + "/app/account/changeLeverage";
    /**
     * 获取当前杠杆
     */
    public String getLeverage = AppConst.app2BaseUrl + dealUrl + "/app/account/getLeverage";
    /**
     * 获取k线
     */
    public String candles = AppConst.app2BaseUrl + dealUrl + "/app/trade/candles";
    /**
     * 交易所交易对
     */
    public String tradeall = AppConst.app2BaseUrl + dealUrl + "/app/trade/all";
    /**
     * 管理保证金
     */
    public String transferMargin = AppConst.app2BaseUrl + dealUrl + "/app/account/transferMargin";
    /**
     * 开启bitmex
     */
    public String bitmexbind = AppConst.app2BaseUrl + dealUrl + "/app/bitmex/bind";
    /**
     * bitmex开启状态
     */
    public String bitmexbindStatus = AppConst.app2BaseUrl + dealUrl + "/app/bitmex/bindStatus";
}
