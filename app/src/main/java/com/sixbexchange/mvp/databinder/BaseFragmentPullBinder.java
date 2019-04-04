package com.sixbexchange.mvp.databinder;


import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

/**
 * Created by 郭青枫 on 2017/9/27.
 * 统一的 fragment列表接口 代理
 */

public class BaseFragmentPullBinder extends BaseDataBind<BaseFragentPullDelegate> {
    public BaseFragmentPullBinder(BaseFragentPullDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable followlist(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().followlist)
                .setShowDialog(false)
                .setRequestName("跟单列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable followattend(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().followattend)
                .setShowDialog(false)
                .setRequestName("参与跟单")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable MyFollow(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().MyFollow)
                .setShowDialog(false)
                .setRequestName("我的跟单")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable getAccountDetail(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getAccountDetail)
                .setShowDialog(true)
                .setRequestName("钱包资金明细")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}