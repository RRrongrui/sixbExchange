package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.adapter.OrdersAdapter;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;

import java.util.ArrayList;
import java.util.List;

public class TrOrderFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

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
        initList(new ArrayList<OrderBean>());
    }

    OrdersAdapter adapter;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        onRefresh();
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
    }

    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new OrdersAdapter(getActivity(), data);
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_bg));
            initHeader();
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    public void changeExch(String exch){

    }

    public IconFontTextview tv_coin;
    public IconFontTextview tv_statu;

    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_order_header, null);
        this.tv_coin = (IconFontTextview) rootView.findViewById(R.id.tv_coin);
        this.tv_statu = (IconFontTextview) rootView.findViewById(R.id.tv_statu);

        tv_coin.setText("ETH季度 " + CommonUtils.getString(R.string.ic_Down));
        tv_statu.setText("已成交 " + CommonUtils.getString(R.string.ic_Down));

        viewDelegate.viewHolder.fl_pull.addView(rootView,0);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
                break;
        }
    }

    @Override
    protected void refreshData() {
        addRequest(binder.accountgetOrders(
                "okef",
                "active",
                "okef/eth.usd.q",
                this));
    }
}
