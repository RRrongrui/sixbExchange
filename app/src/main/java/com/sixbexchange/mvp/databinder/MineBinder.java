package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.MineDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class MineBinder extends BaseDataBind<MineDelegate> {

    public MineBinder(MineDelegate viewDelegate) {
        super(viewDelegate);
    }


}