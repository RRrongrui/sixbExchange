package com.sixbexchange.mvp.delegate;

import com.sixbexchange.R;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;

import android.view.View;

public class MineDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


    public static class ViewHolder {
        public View rootView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

        }

    }
}