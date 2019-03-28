package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.RegisteredDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class RegisteredBinder extends BaseDataBind<RegisteredDelegate> {

    public RegisteredBinder(RegisteredDelegate viewDelegate) {
        super(viewDelegate);
    }


}