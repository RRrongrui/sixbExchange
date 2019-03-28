package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.FrameLayout;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.widget.MainBottomNavigation;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

public class MainDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    private ArrayList<CustomTabEntity> mTabEntities;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initBottom(int position, DefaultClickLinsener defaultClickLinsener) {
        if (ListUtils.isEmpty(mTabEntities)) {
            String[] mTitles = CommonUtils.getStringArray(R.array.sa_select_bottom_title);
            int[] mIconSelectIds = {
                    R.drawable.icon1_active,
                    R.drawable.icon2_active,
                    R.drawable.icon3_active,
                    R.drawable.icon4_active,
            };
            int[] mIconUnSelectIds = {
                    R.drawable.icon1,
                    R.drawable.icon2,
                    R.drawable.icon3,
                    R.drawable.icon4,
            };
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < mTitles.length; i++) {
                mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
            }
            viewHolder.mainBottomNavigation.setmTabEntities(mTabEntities);
            viewHolder.mainBottomNavigation.setDefaultClickLinsener(defaultClickLinsener);
            viewHolder.mainBottomNavigation.setSelectPosition(position);
        }
    }


    public static class ViewHolder {
        public View rootView;
        public FrameLayout fl_root;
        public MainBottomNavigation mainBottomNavigation;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.fl_root = (FrameLayout) rootView.findViewById(R.id.fl_root);
            this.mainBottomNavigation = (MainBottomNavigation) rootView.findViewById(R.id.mainBottomNavigation);
        }

    }
}