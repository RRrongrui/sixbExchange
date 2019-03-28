package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.LoginDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class LoginBinder extends BaseDataBind<LoginDelegate> {

    public LoginBinder(LoginDelegate viewDelegate) {
        super(viewDelegate);
    }


}