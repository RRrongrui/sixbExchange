package com.sixbexchange.mvp.delegate;

import android.view.View;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.SegmentTabLayout;

public class TrParentsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tr_parents;
    }


    public static class ViewHolder {
        public View rootView;
        public SegmentTabLayout tl_1;
        public NoSlideViewPager vp_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tl_1 = (SegmentTabLayout) rootView.findViewById(R.id.tl_1);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
        }

    }
}