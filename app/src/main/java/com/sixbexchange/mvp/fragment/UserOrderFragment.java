package com.sixbexchange.mvp.fragment;

import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.UserOrderBinder;
import com.sixbexchange.mvp.delegate.UserOrderDelegate;

public class UserOrderFragment extends BaseDataBindFragment<UserOrderDelegate, UserOrderBinder> {

    @Override
    protected Class<UserOrderDelegate> getDelegateClass() {
        return UserOrderDelegate.class;
    }

    @Override
    public UserOrderBinder getDataBinder(UserOrderDelegate viewDelegate) {
        return new UserOrderBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("").setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
