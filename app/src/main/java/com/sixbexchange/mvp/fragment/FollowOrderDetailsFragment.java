package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.FollowOrderDetailsBinder;
import com.sixbexchange.mvp.delegate.FollowOrderDetailsDelegate;

public class FollowOrderDetailsFragment extends BaseDataBindFragment<FollowOrderDetailsDelegate, FollowOrderDetailsBinder> {

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
        switch (requestCode) {
        }
    }

}
