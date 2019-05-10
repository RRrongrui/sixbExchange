package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.TrParentsDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class TrParentsBinder extends BaseDataBind<TrParentsDelegate> {

    public TrParentsBinder(TrParentsDelegate viewDelegate) {
        super(viewDelegate);
    }


}