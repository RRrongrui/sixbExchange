package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.adapter.HoldPositionAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.HoldPositionBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrPositionFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

    @Override
    protected Class<BaseFragentPullDelegate> getDelegateClass() {
        return BaseFragentPullDelegate.class;
    }

    @Override
    public BaseFragmentPullBinder getDataBinder(BaseFragentPullDelegate viewDelegate) {
        return new BaseFragmentPullBinder(viewDelegate);
    }

    String currency;
    String exchange="okef";

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initList(new ArrayList<HoldPositionBean>());
        currency = CacheUtils.getInstance().getString(CacheName.TradeCoinCache);
        exchange = CacheUtils.getInstance().getString(CacheName.TradeExchangeCache);
        addRequest(binder.allCoins(
                exchange,
                this
        ));
    }

    HoldPositionAdapter adapter;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        onRefresh();
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
    }

    private void initList(List<HoldPositionBean> data) {
        if (adapter == null) {
            adapter = new HoldPositionAdapter(getActivity(), data);
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_bg));
            initHeader();
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    public IconFontTextview tv_coin;
    DropDownPopu dropDownPopu;
    List<String> coins;


    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_order_header, null);
        this.tv_coin = (IconFontTextview) rootView.findViewById(R.id.tv_coin);
        tv_coin.setText(currency + " " + CommonUtils.getString(R.string.ic_Down));
        tv_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下拉选择币种
                if (dropDownPopu == null) {
                    dropDownPopu = new DropDownPopu();
                }
                dropDownPopu.show(
                        coins, viewDelegate.getmToolbarRightImg1(), getActivity(),
                        new MultiItemTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                currency = coins.get(position);
                                tv_coin.setText(currency + " " + CommonUtils.getString(R.string.ic_Down));
                                //通知其他页面 切换 币种
                                if (getParentFragment() instanceof TransactionFragment) {
                                    ((TransactionFragment) getParentFragment()).changeCoin(currency);
                                }
                            }

                            @Override
                            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                return false;
                            }
                        }
                );
            }
        });
        viewDelegate.viewHolder.fl_pull.addView(rootView, 0);
    }

    public void changeExch(String exch) {
        //请求新数据 获取新的币种列表
        addRequest(binder.allCoins(
                exch,
                this
        ));
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, "position", HoldPositionBean.class));
                break;
        }
    }

    @Override
    protected void refreshData() {
        addRequest(binder.accountgetAccount(exchange, this));
    }
}
