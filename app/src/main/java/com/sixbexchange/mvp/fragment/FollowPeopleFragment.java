package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.sixbexchange.R;
import com.sixbexchange.adapter.FollowPeopleAdapter;
import com.sixbexchange.entity.bean.FollowPeopleBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
/*
*跟单列表
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:16
* @Param
* @return
**/
public class FollowPeopleFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

    @Override
    protected Class<BaseFragentPullDelegate> getDelegateClass() {
        return BaseFragentPullDelegate.class;
    }

    @Override
    public BaseFragmentPullBinder getDataBinder(BaseFragentPullDelegate viewDelegate) {
        return new BaseFragmentPullBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initList(new ArrayList<FollowPeopleBean>());
        onRefresh();
    }

    FollowPeopleAdapter adapter;

    private void initList(List<FollowPeopleBean> data) {
        if (adapter == null) {
            adapter = new FollowPeopleAdapter(getActivity(), data);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (getParentFragment().getParentFragment() instanceof MainFragment) {
                        ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                                FollowOrderDetailsFragment.newInstance(adapter.getDatas().get(position)
                                        .getId() + "")
                        );
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.setIsLoadMore(false);
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_bg));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, FollowPeopleBean.class));
                break;
        }
    }

    @Override
    protected void refreshData() {
        addRequest(binder.followlist(this));
    }
}
