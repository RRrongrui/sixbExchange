package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.AssetsDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class AssetsBinder extends BaseDataBind<AssetsDelegate> {

    public AssetsBinder(AssetsDelegate viewDelegate) {
        super(viewDelegate);
    }


}