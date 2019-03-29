package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.AssetsBinder;
import com.sixbexchange.mvp.delegate.AssetsDelegate;

public class AssetsFragment extends BaseDataBindFragment<AssetsDelegate, AssetsBinder> {

    @Override
    protected Class<AssetsDelegate> getDelegateClass() {
        return AssetsDelegate.class;
    }

    @Override
    public AssetsBinder getDataBinder(AssetsDelegate viewDelegate) {
        return new AssetsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("我的资产")
                .setmRightImg1(CommonUtils.getString(R.string.ic_file_search) + "账单"));
        viewDelegate.setToolColor(R.color.mark_color, false);

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
