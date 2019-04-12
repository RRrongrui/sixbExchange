package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.TrTransactionDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class TrTransactionBinder extends BaseDataBind<TrTransactionDelegate> {

    public TrTransactionBinder(TrTransactionDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable accountgetOrders(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("state", "end");
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountgetOrders)
                .setShowDialog(false)
                .setRequestName("获取持仓")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable tradedetail(
            String exchange,
            String id,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("id", id);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradedetail)
                .setShowDialog(false)
                .setRequestName("交易信息")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradelist(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradelist)
                .setShowDialog(false)
                .setRequestName("交易所交易对")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable accountopen(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountopen)
                .setShowDialog(false)
                .setRequestName("获取开多可用")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}