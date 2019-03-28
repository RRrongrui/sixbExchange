package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.sixbexchange.R;
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

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
