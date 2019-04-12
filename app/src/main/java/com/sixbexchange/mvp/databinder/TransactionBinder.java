package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.TransactionDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class TransactionBinder extends BaseDataBind<TransactionDelegate> {

    public TransactionBinder(TransactionDelegate viewDelegate) {
        super(viewDelegate);
    }
    public Disposable exchangeList(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().exchangeList)
                .setShowDialog(false)
                .setRequestName("交易所列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

}