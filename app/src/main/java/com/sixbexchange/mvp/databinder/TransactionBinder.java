package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.TransactionDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class TransactionBinder extends BaseDataBind<TransactionDelegate> {

    public TransactionBinder(TransactionDelegate viewDelegate) {
        super(viewDelegate);
    }


}