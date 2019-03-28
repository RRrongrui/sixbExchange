package com.sixbexchange.mvp.activity;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.FollowOrderDetailsBinder;
import com.sixbexchange.mvp.delegate.FollowOrderDetailsDelegate;

public class FollowOrderDetailsActivity extends BaseDataBindActivity<FollowOrderDetailsDelegate, FollowOrderDetailsBinder> {

    @Override
    protected Class<FollowOrderDetailsDelegate> getDelegateClass() {
        return FollowOrderDetailsDelegate.class;
    }

    @Override
    public FollowOrderDetailsBinder getDataBinder(FollowOrderDetailsDelegate viewDelegate) {
        return new FollowOrderDetailsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle(""));

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        super.onServiceError(data, info, status, requestCode);
        switch (requestCode) {
        }
    }

}
