package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.TrOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.ExchCurrencyPairBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.mvp.databinder.TrTransactionBinder;
import com.sixbexchange.mvp.delegate.TrTransactionDelegate;
import com.sixbexchange.mvp.popu.SelectCurrencyPairPopu;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrTransactionFragment extends BaseDataBindFragment<TrTransactionDelegate, TrTransactionBinder> {

    boolean isVisibility = false;
    String TAG = getClass().getSimpleName();
    int depthSize = 5;

    @Override
    protected Class<TrTransactionDelegate> getDelegateClass() {
        return TrTransactionDelegate.class;
    }

    @Override
    public TrTransactionBinder getDataBinder(TrTransactionDelegate viewDelegate) {
        return new TrTransactionBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.viewHolder.fl_opponent_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChange(1);
            }
        });
        viewDelegate.viewHolder.fl_limit_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChange(0);
            }
        });
        viewDelegate.viewHolder.tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeModeChange(1);
            }
        });
        viewDelegate.viewHolder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeModeChange(0);
            }
        });
        tradeTypeChange(0);
        tradeModeChange(0);
        viewDelegate.viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
        viewDelegate.viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initWs();
        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        TradeExchangeCache = CacheUtils.getInstance().getString(CacheName.TradeExchangeCache);
        TradeIdCache = CacheUtils.getInstance().getString(CacheName.TradeIdCache);
        onRefreshData();
        addRequest(binder.tradelist(TradeExchangeCache, this));
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        initList(new ArrayList<OrderBean>());
    }

    String TradeExchangeCache;
    String TradeIdCache;

    private void onRefreshData() {
        addRequest(binder.tradedetail(TradeExchangeCache,
                TradeIdCache,
                TrTransactionFragment.this
        ));
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
    }

    String contract = "";

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        sendWs();
        isVisibility = true;
    }

    private void sendWs() {
        if (WebSocketRequest.getInstance().getCallBack(TAG) != null && !TextUtils.isEmpty(contract)) {
            Map<String, String> map = new HashMap<>();
            map.put("uri", "subscribe-single-tick-verbose");
            map.put("contract", contract);
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isVisibility = false;
    }

    TrOrdersAdapter adapter;

    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new TrOrdersAdapter(getActivity(), data);
            viewDelegate.viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.recycler_view.setAdapter(adapter);
        } else {
            adapter.setData(data);
            viewDelegate.viewHolder.lin_no_order.setVisibility(
                    ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
        }
    }

    List<ExchCurrencyPairBean> selectLists;
    TradeDetailBean tradeDetailBean;
    SelectCurrencyPairPopu selectCurrencyPairPopu;


    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //订单列表
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
                break;
            case 0x124:
                tradeDetailBean = GsonUtil.getInstance().toObj(data, TradeDetailBean.class);
                initTradeDetail();
                break;
            case 0x125:
                selectLists = GsonUtil.getInstance().toList(data, ExchCurrencyPairBean.class);
                viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tradeDetailBean != null) {
                            if (selectCurrencyPairPopu == null) {
                                selectCurrencyPairPopu = new SelectCurrencyPairPopu(getActivity());
                            }
                        }
                        selectCurrencyPairPopu.showList(selectLists, tradeDetailBean,
                                viewDelegate.viewHolder.tv_order_type, new DefaultClickLinsener() {
                                    @Override
                                    public void onClick(View view, int position, Object item) {
                                        tradeDetailBean = selectLists.get((int) item).getList().get(position);
                                        initTradeDetail();
                                        selectCurrencyPairPopu.dismiss();
                                    }
                                });
                    }
                });
                break;
        }
    }

    public void changeExch(String exch) {
        //切换交易所
        CacheUtils.getInstance().put(CacheName.TradeExchangeCache, exch);
        CacheUtils.getInstance().put(CacheName.TradeIdCache, "");
        TradeExchangeCache = exch;
        TradeIdCache = "";
        addRequest(binder.tradelist(TradeExchangeCache, this));
        onRefreshData();
    }

    private void initTradeDetail() {
        CacheUtils.getInstance().put(CacheName.TradeExchangeCache, tradeDetailBean.getExchange());
        CacheUtils.getInstance().put(CacheName.TradeIdCache, tradeDetailBean.getId());
        CacheUtils.getInstance().put(CacheName.TradeCoinCache, tradeDetailBean.getCurrency());
        if (getParentFragment() instanceof TransactionFragment) {
            ((TransactionFragment) getParentFragment()).setLevel(tradeDetailBean.getTextName());
        }
        viewDelegate.viewHolder.tv_order_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewDelegate.viewHolder.tv_show_price_type.setText("价格(" + tradeDetailBean.getPriceUnit() + ")");
        viewDelegate.viewHolder.tv_order_num_unit.setText(tradeDetailBean.getAmountUnit());
        viewDelegate.viewHolder.tv_coin_num.setText("数量(" + tradeDetailBean.getAmountUnit() + ")");
        viewDelegate.viewHolder.tv_order_type.setText(tradeDetailBean.getCurrencyPairName());

        //设置深度 价格 数量 小数点长度
        asksAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));
        bidsAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));

        asksAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));
        bidsAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));

        //注册ws
        contract = tradeDetailBean.getCurrencyPair();
        Map<String, String> map = new HashMap<>();
        map.put("uri", "subscribe-single-tick-verbose");
        map.put("contract", contract);
        WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        //请求未完成订单
        addRequest(binder.accountgetOrders(
                tradeDetailBean.getExchange(),
                contract,
                this));
        //清除可用可开，重新获取
        viewDelegate.viewHolder.tv_buy_type_num.setText("--");
        viewDelegate.viewHolder.tv_buy_available.setText("--");
        viewDelegate.viewHolder.tv_sell_available.setText("--");
        viewDelegate.viewHolder.tv_sell_type_num.setText("--");
        addRequest(binder.accountopen(tradeDetailBean.getExchange(),
                tradeDetailBean.getCurrencyPair(), this));

    }

    TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (UiHeplUtils.isDouble(s.toString()) && tradeDetailBean != null) {
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()) < UiHeplUtils.numPointAfterLength(s.toString())) {
                    viewDelegate.viewHolder.tv_order_price.removeTextChangedListener(priceTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewDelegate.viewHolder.tv_order_price.setText(substring);
                    viewDelegate.viewHolder.tv_order_price.setSelection(substring.length());
                    viewDelegate.viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
                }
            }
        }
    };
    TextWatcher numTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (UiHeplUtils.isDouble(s.toString()) && tradeDetailBean != null) {
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()) < UiHeplUtils.numPointAfterLength(s.toString())) {
                    viewDelegate.viewHolder.tv_order_num.removeTextChangedListener(numTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewDelegate.viewHolder.tv_order_num.setText(substring);
                    viewDelegate.viewHolder.tv_order_num.setSelection(substring.length());
                    viewDelegate.viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
                }
            }
        }
    };
    DepthAdapter asksAdapter;
    DepthAdapter bidsAdapter;

    //开仓0  平仓1
    private void tradeModeChange(int type) {
        if (type == 0) {
            viewDelegate.viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.mark_color));

            viewDelegate.viewHolder.tv_buy.setText("买入开多(看涨)");
            viewDelegate.viewHolder.tv_sell.setText("卖出开空(看跌)");

        } else {
            viewDelegate.viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewDelegate.viewHolder.tv_buy.setText("买入平空");
            viewDelegate.viewHolder.tv_sell.setText("卖出平多");

        }
    }

    private void depthPrice(String price) {
        tradeTypeChange(0);
        viewDelegate.viewHolder.tv_order_price.setText(
                BigUIUtil.getinstance().bigPrice(price)
        );
    }

    //市价1 限价0切换
    private void tradeTypeChange(int type) {
        if (type == 0) {
            viewDelegate.viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_order_price.setText("");
            viewDelegate.viewHolder.tv_order_price.setEnabled(true);
        } else {
            viewDelegate.viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_order_price.setText("市价");
            viewDelegate.viewHolder.tv_order_price.setEnabled(false);
        }
    }

    private void initDepth(List<DepthBean> asks, List<DepthBean> bids) {
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(asksAdapter.getDatas().get(position).getPrice());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_buy.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_buy.setAdapter(asksAdapter);
        } else {
            asksAdapter.setData(asks);
        }
        if (bidsAdapter == null) {
            bidsAdapter = new DepthAdapter(getActivity(), bids, true);
            bidsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(bidsAdapter.getDatas().get(position).getPrice());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_sell.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_sell.setAdapter(bidsAdapter);
        } else {
            bidsAdapter.setData(bids);
        }
    }

    private void initWs() {
        WebSocketRequest.getInstance().addCallBack(TAG, new WebSocketRequest.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&
                        !WebSocketRequest.getInstance().isSend() && isVisibility
                        ) {
                    WebSocketDepthBean webSocketDepthBean =
                            GsonUtil.getInstance().toObj(data, "data", WebSocketDepthBean.class);
                    if (webSocketDepthBean != null &&
                            ObjectUtils.equals(contract, webSocketDepthBean.getContract()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getAsks()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getBids()) &&
                            !TextUtils.isEmpty(webSocketDepthBean.getLast())) {
                        HandlerHelper.getinstance().put(contract,
                                webSocketDepthBean);
                    }
                }
            }

            @Override
            public void onDataError(String name, String data, String info, int status) {

            }
        });
        HandlerHelper.getinstance().initHander(TAG,
                viewDelegate.viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
                        if (isVisibility) {
                            if (val instanceof WebSocketDepthBean && tradeDetailBean != null) {
                                showNowPrice(((WebSocketDepthBean) val).getLast());
                                changeDepth((WebSocketDepthBean) val);
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    String oldPrice = "";

    private void showNowPrice(String price) {
        viewDelegate.viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewDelegate.viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewDelegate.viewHolder.tv_now_price.setText(
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                        BigUIUtil.getinstance().bigPrice(price));
    }

    private void changeDepth(WebSocketDepthBean val) {
        if (ObjectUtils.equals(contract, ((WebSocketDepthBean) val).getContract())
                && val.getBids().size() >= depthSize &&
                val.getAsks().size() >= depthSize) {
            List<DepthBean> depthBeans = ((WebSocketDepthBean) val)
                    .getAsks().subList(0, depthSize);
            Collections.reverse(depthBeans);
            initDepth(
                    depthBeans,
                    ((WebSocketDepthBean) val).getBids().subList(0, depthSize));
        }
    }
}
