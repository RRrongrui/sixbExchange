package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.HomeDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class HomeBinder extends BaseDataBind<HomeDelegate> {

    public HomeBinder(HomeDelegate viewDelegate) {
        super(viewDelegate);
    }
    public Disposable getlatestversion(
            String version,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("type",1);
        put("version", version);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getlatestversion)
                .setShowDialog(false)
                .setRequestName("版本更新")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

}