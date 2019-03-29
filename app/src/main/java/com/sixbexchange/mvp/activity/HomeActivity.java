package com.sixbexchange.mvp.activity;

import android.content.Intent;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.HomeBinder;
import com.sixbexchange.mvp.delegate.HomeDelegate;
import com.sixbexchange.mvp.fragment.MainFragment;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class HomeActivity extends BaseDataBindActivity<HomeDelegate, HomeBinder> {

    @Override
    protected Class<HomeDelegate> getDelegateClass() {
        return HomeDelegate.class;
    }

    @Override
    public HomeBinder getDataBinder(HomeDelegate viewDelegate) {
        return new HomeBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, new MainFragment());
        }
    }

    @Override
    public void onBackPressedSupport() {
        ISupportFragment topFragment = getTopFragment();
        if (topFragment instanceof MainFragment) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
