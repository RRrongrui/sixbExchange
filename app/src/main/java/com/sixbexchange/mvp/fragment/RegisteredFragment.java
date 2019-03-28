package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.sixbexchange.mvp.databinder.RegisteredBinder;
import com.sixbexchange.mvp.delegate.RegisteredDelegate;

public class RegisteredFragment extends BaseDataBindFragment<RegisteredDelegate, RegisteredBinder> {

    @Override
    protected Class<RegisteredDelegate> getDelegateClass() {
        return RegisteredDelegate.class;
    }

    @Override
    public RegisteredBinder getDataBinder(RegisteredDelegate viewDelegate) {
        return new RegisteredBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
