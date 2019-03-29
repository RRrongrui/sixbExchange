package com.sixbexchange.mvp.fragment;

import android.text.TextUtils;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
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
        viewDelegate.viewHolder.tv_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_code.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_code.getText().toString())) {
                    ToastUtil.show("请设置密码");
                    return;
                }
                if (viewDelegate.viewHolder.et_code.getText().toString().length() < 6) {
                    ToastUtil.show("密码长度不能小于6位");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_invite.getText().toString())) {
                    ToastUtil.show("请输入邀请码");
                    return;
                }

            }
        });
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
            }
        });
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
