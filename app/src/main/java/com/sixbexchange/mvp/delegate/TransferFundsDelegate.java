package com.sixbexchange.mvp.delegate;

import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;

public class TransferFundsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transfer_funds;
    }


    public static class ViewHolder {
        public View rootView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

        }

    }
}