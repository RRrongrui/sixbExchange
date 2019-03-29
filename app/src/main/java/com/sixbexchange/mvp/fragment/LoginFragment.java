package com.sixbexchange.mvp.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.mvp.activity.HomeActivity;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
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
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_pass.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                HomeActivity.isLogin = true;
                startActivity(new Intent(getActivity(), HomeActivity.class));
                ActivityUtils.finishActivity(LoginAndRegisteredActivity.class);
            }
        });
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
