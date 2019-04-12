package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.UUIDS;
import com.fivefivelike.mybaselibrary.view.BottomBar;
import com.fivefivelike.mybaselibrary.view.BottomBarTab;
import com.sixbexchange.R;
import com.sixbexchange.base.AppConst;
import com.sixbexchange.mvp.databinder.MainBinder;
import com.sixbexchange.mvp.delegate.MainDelegate;

import me.yokeyword.fragmentation.SupportFragment;

/*
*主页
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:17
* @Param
* @return
**/
public class MainFragment extends BaseDataBindFragment<MainDelegate, MainBinder> {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;

    private SupportFragment[] mFragments;


    @Override
    protected Class<MainDelegate> getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    public MainBinder getDataBinder(MainDelegate viewDelegate) {
        return new MainBinder(viewDelegate);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {

    }

    String uid;

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        uid = UUIDS.getUUID() + System.currentTimeMillis();
        WebSocketRequest.getInstance().intiWebSocket(AppConst.wsAddress,
                this.getClass().getName(),
                null);
        initView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mFragments = new SupportFragment[4];
        SupportFragment firstFragment = findChildFragment(UserOrderFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = transactionFragment = new TransactionFragment();
            mFragments[SECOND] = userOrderFragment = new UserOrderFragment();
            mFragments[THIRD] = assetsFragment = new AssetsFragment();
            mFragments[FOUR] = mineFragment = new MineFragment();

            loadMultipleRootFragment(R.id.fl_tab_container, SECOND,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = findChildFragment(TransactionFragment.class);
            mFragments[SECOND] = firstFragment;
            mFragments[THIRD] = findChildFragment(AssetsFragment.class);
            mFragments[FOUR] = findChildFragment(MineFragment.class);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    TransactionFragment transactionFragment;
    UserOrderFragment userOrderFragment;
    AssetsFragment assetsFragment;
    MineFragment mineFragment;

    private void initView() {
        String[] mTitles = CommonUtils.getStringArray(R.array.sa_select_bottom_title);
        viewDelegate.viewHolder.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.icon1_active, R.drawable.icon1, mTitles[0]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.icon2_active, R.drawable.icon2, mTitles[1]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.icon3_active, R.drawable.icon3, mTitles[2]))
                .addItem(new BottomBarTab(_mActivity, R.drawable.icon4_active, R.drawable.icon4, mTitles[3]));
        viewDelegate.viewHolder.bottomBar.setCurrentItem(SECOND);
        viewDelegate.viewHolder.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (mFragments != null) {
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                if (position == THIRD && assetsFragment != null) {
                    assetsFragment.onRefresh();
                }
            }
        });
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}
