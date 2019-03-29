package com.sixbexchange.mvp.fragment;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.sixbexchange.mvp.activity.HomeActivity;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.mvp.activity.WelcomeActivity;
import com.sixbexchange.mvp.databinder.LoginBinder;
import com.sixbexchange.mvp.delegate.LoginDelegate;

public class LoginFragment extends BaseDataBindFragment<LoginDelegate, LoginBinder> {

    @Override
    protected Class<LoginDelegate> getDelegateClass() {
        return LoginDelegate.class;
    }

    @Override
    public LoginBinder getDataBinder(LoginDelegate viewDelegate) {
        return new LoginBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.viewHolder.tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
                ActivityUtils.finishActivity(LoginAndRegisteredActivity.class);
            }
        });
        ActivityUtils.finishActivity(WelcomeActivity.class);
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
