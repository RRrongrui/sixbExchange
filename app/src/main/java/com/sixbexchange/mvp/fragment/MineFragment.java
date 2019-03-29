package com.sixbexchange.mvp.fragment;

import android.content.Intent;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.sixbexchange.R;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.mvp.databinder.MineBinder;
import com.sixbexchange.mvp.delegate.MineDelegate;

public class MineFragment extends BaseDataBindFragment<MineDelegate, MineBinder> {

    @Override
    protected Class<MineDelegate> getDelegateClass() {
        return MineDelegate.class;
    }

    @Override
    public MineBinder getDataBinder(MineDelegate viewDelegate) {
        return new MineBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("个人中心")
                .setmRightImg1(CommonUtils.getString(R.string.ic_envelope) + " 通知中心").setShowBack(false));
        viewDelegate.viewHolder.iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginAndRegisteredActivity.class));
            }
        });
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
