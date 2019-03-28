package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.FollowOrderDetailsDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class FollowOrderDetailsBinder extends BaseDataBind<FollowOrderDetailsDelegate> {

    public FollowOrderDetailsBinder(FollowOrderDetailsDelegate viewDelegate) {
        super(viewDelegate);
    }


}