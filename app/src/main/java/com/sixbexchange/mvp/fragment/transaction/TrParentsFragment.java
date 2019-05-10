package com.sixbexchange.mvp.fragment.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.mvp.databinder.TrParentsBinder;
import com.sixbexchange.mvp.delegate.TrParentsDelegate;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrParentsFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrParentsFragment;
import com.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class TrParentsFragment extends BaseDataBindFragment<TrParentsDelegate, TrParentsBinder> {
    private String[] mTitles = {" OKEX合约 ", "BitMEX合约"};
    private ArrayList<Fragment> mFragments;

    @Override
    protected Class<TrParentsDelegate> getDelegateClass() {
        return TrParentsDelegate.class;
    }

    @Override
    public TrParentsBinder getDataBinder(TrParentsDelegate viewDelegate) {
        return new TrParentsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("").setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mFragments = new ArrayList<>();
        mFragments.add(new OkexTrParentsFragment());
        mFragments.add(new BMTrParentsFragment());
        viewDelegate.viewHolder.tl_1.setTabData(mTitles);

        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(
                getChildFragmentManager(), mFragments, mTitles);
        viewDelegate.viewHolder.vp_root.setAdapter(innerPagerAdapter);
        viewDelegate.viewHolder.tl_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewDelegate.viewHolder.vp_root.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
