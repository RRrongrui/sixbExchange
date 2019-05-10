package com.sixbexchange.mvp.fragment.transaction.okex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.OkexTrBinder;
import com.sixbexchange.mvp.delegate.OkexTrDelegate;
import com.sixbexchange.mvp.fragment.CoinInfoFragment;
import com.sixbexchange.mvp.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class OkexTrCloseFragment extends BaseDataBindFragment<OkexTrDelegate, OkexTrBinder> {

    @Override
    protected Class<OkexTrDelegate> getDelegateClass() {
        return OkexTrDelegate.class;
    }

    @Override
    public OkexTrBinder getDataBinder(OkexTrDelegate viewDelegate) {
        return new OkexTrBinder(viewDelegate);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.initWs(1);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.tradeTypeChangeClose(0);
        viewDelegate.initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData();
            }
        });
        initList(new ArrayList<OrderBean>());
        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.checkOrder(viewDelegate.tradeMode == 0 ? 1 : 4, OkexTrCloseFragment.this);
            }
        });
        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binder.checkOrder(viewDelegate.tradeMode == 0 ? 2 : 3, OkexTrCloseFragment.this);
            }
        });
        if (viewDelegate.tradeDetailBean != null) {
            initTradeDetail(viewDelegate.tradeDetailBean);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.sendWs(true);
        viewDelegate.setVisibility(true);
        if (viewDelegate.tradeDetailBean != null) {
            onRefreshData(false);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.setVisibility(false);
    }

    public void initTradeDetail(TradeDetailBean s) {
        viewDelegate.sendWs(false);
        viewDelegate.setTradeDetailBean(s);
        initList(new ArrayList<OrderBean>());
        onRefreshData(true);
        //选择交易对
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OkexTrParentsFragment) getParentFragment()).showSelectCurrencyPair(v);
            }
        });
        //跳转k线
        viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转k线
                viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getParentFragment().getParentFragment().getParentFragment() instanceof MainFragment) {
                            ((MainFragment) getParentFragment().getParentFragment().getParentFragment()).startBrotherFragment(
                                    CoinInfoFragment.newInstance(
                                            viewDelegate.tradeDetailBean.getDelivery(),
                                            viewDelegate.tradeDetailBean.getCurrencyPairName()
                                    )
                            );
                        }
                    }
                });
            }
        });
    }


    private void initList(List<OrderBean> data) {
        viewDelegate.initList(data);
        viewDelegate.viewHolder.lin_close_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //撤销全部
                if (viewDelegate.tradeDetailBean != null) {
                    addRequest(binder.cancelAllOrder(
                            viewDelegate.tradeDetailBean.getExchange(),
                            viewDelegate.tradeDetailBean.getCurrencyPair(),
                            OkexTrCloseFragment.this
                    ));
                }
            }
        });
        viewDelegate.adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //撤单
                addRequest(binder.cancelOrder(
                        viewDelegate.tradeDetailBean.getExchange(),
                        viewDelegate.adapter.getDatas().get(position).getExchange_oid(),
                        OkexTrCloseFragment.this
                ));
            }
        });
    }

    public void onRefreshData(boolean isRefreshing) {
        if (viewDelegate.tradeDetailBean != null) {
            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(isRefreshing);
            addRequest(binder.accountclose(viewDelegate.tradeDetailBean.getExchange(),
                    viewDelegate.tradeDetailBean.getOnlykey(), this));
        }
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    public void setOrderList(List<OrderBean> data) {
        //订单列表
        initList(data);
        //订阅ws
        viewDelegate.sendWs(true);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x127:
                viewDelegate.transactionBean.setAvailableflatSpace(
                        GsonUtil.getInstance().getValue(data, "availableflatSpace", String.class));
                viewDelegate.transactionBean.setAvailableflatMore(
                        GsonUtil.getInstance().getValue(data, "availableflatMore", String.class));
                viewDelegate.tradeModeChange(1);
                //订阅ws
                viewDelegate.sendWs(true);
                break;
            case 0x125:
                //下单
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData();
                break;
            case 0x126:
                //撤单
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData();
                break;
        }
    }

}
