package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.TextView;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;

public class TransactionDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        viewHolder.tv_change.setText(CommonUtils.getString(R.string.ic_exchange_alt) + " 切换");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transaction;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView tv_name;
        public IconFontTextview tv_change;
        public TextView tv_all;
        public TextView tv_freeze;
        public CommonTabLayout tl_2;
        public NoSlideViewPager vp_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_change = (IconFontTextview) rootView.findViewById(R.id.tv_change);
            this.tv_all = (TextView) rootView.findViewById(R.id.tv_all);
            this.tv_freeze = (TextView) rootView.findViewById(R.id.tv_freeze);
            this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
        }

    }
}