package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.TrOkexOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TransactionBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OkexTrDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    String oldPrice = "";
    String TAG = getClass().getSimpleName();
    public TradeDetailBean tradeDetailBean;
    boolean isVisibility = false;
    public TransactionBean transactionBean = new TransactionBean();


    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    ResultDialogEntity resultDialogEntity;

    public void showTriggerDialog(String data) {
        if (ObjectUtils.equals("1", GsonUtil.getInstance().getValue(data, "need"))) {
            if (resultDialogEntity == null) {
                resultDialogEntity = new ResultDialogEntity();
                resultDialogEntity.setType("1");
                resultDialogEntity.setConfirmBtn("知道了");
            }
            resultDialogEntity.setTitle(GsonUtil.getInstance().getValue(data, "remark"));
            resultDialogEntity.setConfirmColor(CommonUtils.getStringColor(R.color.mark_color));
            ResultDialog.getInstence()
                    .ShowResultDialog(this.getActivity(), GsonUtil.getInstance().toJson(resultDialogEntity), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {

                        }
                    });
        }
    }

    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;
        CacheUtils.getInstance().put(CacheName.TradeIdOkexCache, tradeDetailBean.getId());
        CacheUtils.getInstance().put(CacheName.TradeCoinOkexCache, tradeDetailBean.getCurrency());

        viewHolder.tv_order_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewHolder.tv_show_price_type.setText("价格(" + tradeDetailBean.getPriceUnit() + ")");
        viewHolder.tv_order_num_unit.setText(tradeDetailBean.getAmountUnit());
        viewHolder.tv_coin_num.setText("数量(" + tradeDetailBean.getAmountUnit() + ")");
        viewHolder.tv_order_type.setText(tradeDetailBean.getCurrencyPairName());

        viewHolder.tv_fund_rate.setText(tradeDetailBean.getRate() + "%");
        ((View) viewHolder.tv_fund_rate.getParent()).setVisibility(
                TextUtils.isEmpty(tradeDetailBean.getRate()) ? View.GONE : View.VISIBLE
        );
        ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.GONE);

        //设置深度 价格 数量 小数点长度
        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        asksAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));
        bidsAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));

        asksAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));
        bidsAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));


        //清除可用可开 指数 价格 深度 订单，重新获取
        viewHolder.tv_buy_type_num.setText("--");
        viewHolder.tv_buy_available.setText("--");
        viewHolder.tv_sell_available.setText("--");
        viewHolder.tv_sell_type_num.setText("--");
        viewHolder.tv_latest_index.setText("--");
        viewHolder.tv_now_price.setText("--");
        viewHolder.tv_now_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.getDimensionPixelSize(R.dimen.text_trans_36px));

        transactionBean.setAvailableflatMore("");
        transactionBean.setAvailableflatSpace("");
        transactionBean.setAvailableOpenMore("");
        transactionBean.setAvailableOpenSpace("");
        transactionBean.setUsableOpenMore("");
        transactionBean.setUsableOpenSpace("");

        if (tradeDetailBean.getKline() == 1) {
            viewHolder.lin_to_kline.setEnabled(true);
            viewHolder.iv_to_kline.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lin_to_kline.setEnabled(false);
            viewHolder.iv_to_kline.setVisibility(View.GONE);
        }
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
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()) <
                        UiHeplUtils.numPointAfterLength(s.toString()) ||
                        (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()) == 0 &&
                                s.toString().contains(".")
                        )) {
                    viewHolder.tv_order_price.removeTextChangedListener(priceTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewHolder.tv_order_price.setText(substring);
                    viewHolder.tv_order_price.setSelection(substring.length());
                    viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
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
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()) <
                        UiHeplUtils.numPointAfterLength(s.toString())) {
                    viewHolder.tv_order_num.removeTextChangedListener(numTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewHolder.tv_order_num.setText(substring);
                    viewHolder.tv_order_num.setSelection(substring.length());
                    viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
                }
            }
        }
    };

    private void showNowPrice(String price) {
        viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewHolder.tv_now_price.setText(
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                        BigUIUtil.getinstance().bigPrice(price));
    }

    private void depthPrice(String price, String amount) {
        if (tradeMode == 0) {
            tradeTypeChangeOpen(3);
        } else {
            tradeTypeChangeClose(0);
        }
        viewHolder.tv_order_price.setText(
                BigUIUtil.getinstance().bigPrice(price));
        viewHolder.tv_order_num.setText(amount);
    }

    //市价1 限价0切换
    public void tradeTypeChangeClose(int type) {
        if (type == 0) {
            viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_order_price.setText("");
            viewHolder.tv_order_price.setEnabled(true);
        } else {
            viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("市价");
            viewHolder.tv_order_price.setEnabled(false);
        }
    }

    //对手价0 买1 1 卖1 2
    int openTradeType = 3;

    public void tradeTypeChangeOpen(int type) {
        if (openTradeType == type) {
            openTradeType = 3;
        } else {
            openTradeType = type;
        }
        if (openTradeType == 0) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("对手价");
            viewHolder.tv_order_price.setEnabled(false);
        } else if (openTradeType == 1) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("买一价");
            viewHolder.tv_order_price.setEnabled(false);
        } else if (openTradeType == 2) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_order_price.setText("卖一价");
            viewHolder.tv_order_price.setEnabled(false);
        } else {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("");
            viewHolder.tv_order_price.setEnabled(true);
        }
    }

    //订阅深度和指数
    public void sendWs(boolean isSubscribe) {
        if (!isVisibility) {
            return;
        }
        if (WebSocketRequest.getInstance().getCallBack(TAG) != null &&
                tradeDetailBean != null) {
            Map<String, String> map = new HashMap<>();
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract", tradeDetailBean.getDelivery());
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract", tradeDetailBean.getIndex());
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        }
        HandlerHelper.getinstance().initHander(TAG,
                viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
                        if (isVisibility) {
                            if (val instanceof WebSocketDepthBean && tradeDetailBean != null) {
                                if (ObjectUtils.equals(tradeDetailBean.getDelivery(),
                                        ((WebSocketDepthBean) val).getContract())) {
                                    showNowPrice(((WebSocketDepthBean) val).getLast());
                                    changeDepth((WebSocketDepthBean) val);
                                } else if (ObjectUtils.equals(tradeDetailBean.getIndex(),
                                        ((WebSocketDepthBean) val).getContract())) {
                                    ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.VISIBLE);
                                    viewHolder.tv_latest_index.setText(
                                            BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                                                    BigUIUtil.getinstance().bigPrice(((WebSocketDepthBean) val).getLast())
                                    );
                                }
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    public void initWs(int type) {
        tradeModeChange(type);
        TAG = TAG + tradeMode;
        WebSocketRequest.getInstance().addCallBack(TAG, new WebSocketRequest.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&
                        !WebSocketRequest.getInstance().isSend() && isVisibility
                        ) {
                    WebSocketDepthBean webSocketDepthBean =
                            GsonUtil.getInstance().toObj(data, "data", WebSocketDepthBean.class);
                    if (webSocketDepthBean != null &&
                            ObjectUtils.equals(tradeDetailBean.getDelivery(),
                                    webSocketDepthBean.getContract()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getAsks()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getBids()) &&
                            !TextUtils.isEmpty(webSocketDepthBean.getLast())) {
                        //深度
                        HandlerHelper.getinstance().put(tradeDetailBean.getDelivery(),
                                webSocketDepthBean);
                    } else if (webSocketDepthBean != null &&
                            ObjectUtils.equals(tradeDetailBean.getIndex(),
                                    webSocketDepthBean.getContract())) {
                        //指数
                        HandlerHelper.getinstance().put(tradeDetailBean.getIndex(),
                                webSocketDepthBean);
                    }
                }
            }

            @Override
            public void onDataError(String name, String data, String info, int status) {

            }
        });
    }

    public int depthSize = 6;

    private void changeDepth(WebSocketDepthBean val) {
        if (ObjectUtils.equals(tradeDetailBean.getDelivery(), ((WebSocketDepthBean) val).getContract())
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

    public DepthAdapter asksAdapter;
    public DepthAdapter bidsAdapter;

    //显示深度
    public void initDepth(List<DepthBean> asks, List<DepthBean> bids) {
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(asksAdapter.getDatas().get(position).getPrice(),
                            asksAdapter.getDatas().get(position).getVolume());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewHolder.rv_buy.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.rv_buy.setAdapter(asksAdapter);
        } else {
            asksAdapter.setData(asks);
        }
        if (bidsAdapter == null) {
            bidsAdapter = new DepthAdapter(getActivity(), bids, true);
            bidsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(bidsAdapter.getDatas().get(position).getPrice(),
                            bidsAdapter.getDatas().get(position).getVolume());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewHolder.rv_sell.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.rv_sell.setAdapter(bidsAdapter);
        } else {
            bidsAdapter.setData(bids);
        }
    }

    //开仓0  平仓1
    public int tradeMode = 0;
    public boolean isLimit = true;

    public void tradeTypeChange(boolean isLimit) {
        this.isLimit = isLimit;
        if (isLimit) {
            viewHolder.tv_trigger.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_limit.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_trigger.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_limit.setTextColor(CommonUtils.getColor(R.color.mark_color));
            if (tradeMode == 0) {
                viewHolder.tv_order_price_title.setText("价格");
                viewHolder.lin_trigger_price.setVisibility(View.GONE);
                viewHolder.lin_order_select_price_open.setVisibility(View.VISIBLE);
                ((LinearLayout.LayoutParams) viewHolder.lin_order_num.getLayoutParams()).topMargin = 0;
                viewHolder.lin_order_num.requestLayout();
            } else {
                viewHolder.tv_order_price_title.setText("价格");
                viewHolder.lin_trigger_price.setVisibility(View.GONE);
                viewHolder.lin_order_select_price_close.setVisibility(View.VISIBLE);
                tradeTypeChangeOpen(3);
            }
        } else {
            viewHolder.tv_limit.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_trigger.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_limit.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_trigger.setTextColor(CommonUtils.getColor(R.color.mark_color));

            if (tradeMode == 0) {
                ((LinearLayout.LayoutParams) viewHolder.lin_order_num.getLayoutParams()).topMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_20px);
                viewHolder.lin_order_num.requestLayout();
                viewHolder.tv_order_price_title.setText("委托价格");
                viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
                viewHolder.lin_order_select_price_open.setVisibility(View.GONE);
            } else {
                viewHolder.tv_order_price_title.setText("委托价格");
                tradeTypeChangeClose(0);
                viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
                viewHolder.lin_order_select_price_close.setVisibility(View.GONE);
            }

        }

    }

    public void tradeModeChange(int type) {
        tradeMode = type;
        if (type == 0) {
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.mark_color));

            viewHolder.tv_buy.setText("买入开多(看涨)");
            viewHolder.tv_sell.setText("卖出开空(看跌)");


            viewHolder.tv_buy_left.setText("可用");
            viewHolder.tv_buy_type.setText("可开多");
            viewHolder.tv_sell_left.setText("可用");
            viewHolder.tv_sell_type.setText("可开空");

            viewHolder.tv_buy_type_num.setVisibility(View.VISIBLE);
            viewHolder.tv_sell_type_num.setVisibility(View.VISIBLE);

            viewHolder.lin_order_select_price_open.setVisibility(View.VISIBLE);
            viewHolder.lin_order_select_price_close.setVisibility(View.GONE);

            ((LinearLayout.LayoutParams) viewHolder.lin_order_price.getLayoutParams())
                    .bottomMargin = 0;
            ((LinearLayout.LayoutParams) viewHolder.lin_order_price.getLayoutParams())
                    .topMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_10px);
            viewHolder.lin_order_price.requestLayout();
        } else {
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewHolder.tv_buy.setText("买入平空");
            viewHolder.tv_sell.setText("卖出平多");


            viewHolder.tv_buy_left.setText("可平空");
            viewHolder.tv_buy_type.setText("");
            viewHolder.tv_sell_left.setText("可平多");
            viewHolder.tv_sell_type.setText("");

            viewHolder.tv_buy_type_num.setVisibility(View.GONE);
            viewHolder.tv_sell_type_num.setVisibility(View.GONE);

            viewHolder.lin_order_select_price_open.setVisibility(View.GONE);
            viewHolder.lin_order_select_price_close.setVisibility(View.VISIBLE);

        }
        tradeTypeChange(isLimit);
        if (transactionBean != null && tradeDetailBean != null) {
            if (type == 0) {
                viewHolder.tv_buy_type_num.setText(transactionBean.getAvailableOpenMore() + tradeDetailBean.getAmountUnit());
                viewHolder.tv_buy_available.setText(transactionBean.getUsableOpenMore() + tradeDetailBean.getMarginUnit());

                viewHolder.tv_sell_type_num.setText(transactionBean.getAvailableOpenSpace() + tradeDetailBean.getAmountUnit());
                viewHolder.tv_sell_available.setText(transactionBean.getUsableOpenSpace() + tradeDetailBean.getMarginUnit());
            } else {
                viewHolder.tv_buy_available.setText(transactionBean.getAvailableflatSpace() + tradeDetailBean.getAmountUnit());
                viewHolder.tv_sell_available.setText(transactionBean.getAvailableflatMore() + tradeDetailBean.getAmountUnit());
            }
        }
    }

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        //平仓 交易模式
        viewHolder.fl_opponent_price_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeClose(1);
            }
        });
        viewHolder.fl_limit_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeClose(0);
            }
        });

        //开仓交易模式
        viewHolder.fl_opponent_price_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(0);
            }
        });
        viewHolder.fl_buy_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(1);
            }
        });
        viewHolder.fl_sell_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(2);
            }
        });

        viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
        viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
    }

    public TrOkexOrdersAdapter adapter;

    public void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new TrOkexOrdersAdapter(getActivity(), data);
            viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.recycler_view.setAdapter(adapter);
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            adapter.setData(data);
            viewHolder.lin_no_order.setVisibility(
                    ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
            viewHolder.lin_close_all.setVisibility(
                    !ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_okex_tr;
    }


    public static class ViewHolder {
        public View rootView;
        public IconFontTextview tv_order_type;
        public LinearLayout lin_order_type;
        public IconFontTextview tv_lever;
        public LinearLayout lin_lever;
        public SingleLineZoomTextView tv_now_price;
        public ImageView iv_to_kline;
        public LinearLayout lin_to_kline;
        public TextView tv_open;
        public TextView tv_close;
        public TextView tv_limit;
        public TextView tv_order_price_title;
        public TextView tv_trigger;
        public TextView tv_opponent_price;
        public FrameLayout fl_opponent_price_close;
        public TextView tv_limit_price;
        public FrameLayout fl_limit_price;
        public LinearLayout lin_order_select_price_close;
        public EditText tv_trigger_price;
        public TextView tv_trigger_price_unit;
        public LinearLayout lin_trigger_price;
        public EditText tv_order_price;
        public TextView tv_order_price_unit;
        public LinearLayout lin_order_price;
        public TextView tv_opponent_price_open;
        public FrameLayout fl_opponent_price_open;
        public TextView tv_buy_one_price;
        public FrameLayout fl_buy_one_price;
        public TextView tv_sell_one_price;
        public FrameLayout fl_sell_one_price;
        public LinearLayout lin_order_select_price_open;
        public EditText tv_order_num;
        public TextView tv_order_num_unit;
        public LinearLayout lin_order_num;
        public RoundButton tv_buy;
        public TextView tv_buy_left;
        public TextView tv_buy_available;
        public LinearLayout lin_buy_left;
        public TextView tv_buy_type;
        public TextView tv_buy_type_num;
        public RoundButton tv_sell;
        public TextView tv_sell_left;
        public TextView tv_sell_available;
        public LinearLayout lin_sell_left;
        public TextView tv_sell_type;
        public TextView tv_sell_type_num;
        public TextView tv_show_price_type;
        public TextView tv_coin_num;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public SingleLineZoomTextView tv_latest_index;
        public SingleLineZoomTextView tv_fund_rate;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;
        public LinearLayout lin_close_all;
        public SwipeRefreshLayout swipeRefreshLayout;
        public FrameLayout fl_left;
        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_order_type = (IconFontTextview) rootView.findViewById(R.id.tv_order_type);
            this.lin_order_type = (LinearLayout) rootView.findViewById(R.id.lin_order_type);
            this.tv_lever = (IconFontTextview) rootView.findViewById(R.id.tv_lever);
            this.lin_lever = (LinearLayout) rootView.findViewById(R.id.lin_lever);
            this.tv_now_price = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_now_price);
            this.iv_to_kline = (ImageView) rootView.findViewById(R.id.iv_to_kline);
            this.lin_to_kline = (LinearLayout) rootView.findViewById(R.id.lin_to_kline);
            this.tv_open = (TextView) rootView.findViewById(R.id.tv_open);
            this.tv_close = (TextView) rootView.findViewById(R.id.tv_close);
            this.tv_limit = (TextView) rootView.findViewById(R.id.tv_limit);
            this.tv_order_price_title = (TextView) rootView.findViewById(R.id.tv_order_price_title);
            this.tv_trigger = (TextView) rootView.findViewById(R.id.tv_trigger);
            this.tv_opponent_price = (TextView) rootView.findViewById(R.id.tv_opponent_price);
            this.fl_opponent_price_close = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price_close);
            this.tv_limit_price = (TextView) rootView.findViewById(R.id.tv_limit_price);
            this.fl_limit_price = (FrameLayout) rootView.findViewById(R.id.fl_limit_price);
            this.lin_order_select_price_close = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price_close);
            this.tv_trigger_price = (EditText) rootView.findViewById(R.id.tv_trigger_price);
            this.tv_trigger_price_unit = (TextView) rootView.findViewById(R.id.tv_trigger_price_unit);
            this.lin_trigger_price = (LinearLayout) rootView.findViewById(R.id.lin_trigger_price);
            this.tv_order_price = (EditText) rootView.findViewById(R.id.tv_order_price);
            this.tv_order_price_unit = (TextView) rootView.findViewById(R.id.tv_order_price_unit);
            this.lin_order_price = (LinearLayout) rootView.findViewById(R.id.lin_order_price);
            this.tv_opponent_price_open = (TextView) rootView.findViewById(R.id.tv_opponent_price_open);
            this.fl_opponent_price_open = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price_open);
            this.tv_buy_one_price = (TextView) rootView.findViewById(R.id.tv_buy_one_price);
            this.fl_buy_one_price = (FrameLayout) rootView.findViewById(R.id.fl_buy_one_price);
            this.tv_sell_one_price = (TextView) rootView.findViewById(R.id.tv_sell_one_price);
            this.fl_sell_one_price = (FrameLayout) rootView.findViewById(R.id.fl_sell_one_price);
            this.lin_order_select_price_open = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price_open);
            this.tv_order_num = (EditText) rootView.findViewById(R.id.tv_order_num);
            this.tv_order_num_unit = (TextView) rootView.findViewById(R.id.tv_order_num_unit);
            this.lin_order_num = (LinearLayout) rootView.findViewById(R.id.lin_order_num);
            this.tv_buy = (RoundButton) rootView.findViewById(R.id.tv_buy);
            this.tv_buy_left = (TextView) rootView.findViewById(R.id.tv_buy_left);
            this.tv_buy_available = (TextView) rootView.findViewById(R.id.tv_buy_available);
            this.lin_buy_left = (LinearLayout) rootView.findViewById(R.id.lin_buy_left);
            this.tv_buy_type = (TextView) rootView.findViewById(R.id.tv_buy_type);
            this.tv_buy_type_num = (TextView) rootView.findViewById(R.id.tv_buy_type_num);
            this.tv_sell = (RoundButton) rootView.findViewById(R.id.tv_sell);
            this.tv_sell_left = (TextView) rootView.findViewById(R.id.tv_sell_left);
            this.tv_sell_available = (TextView) rootView.findViewById(R.id.tv_sell_available);
            this.lin_sell_left = (LinearLayout) rootView.findViewById(R.id.lin_sell_left);
            this.tv_sell_type = (TextView) rootView.findViewById(R.id.tv_sell_type);
            this.tv_sell_type_num = (TextView) rootView.findViewById(R.id.tv_sell_type_num);
            this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
            this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
            this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
            this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
            this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
            this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.lin_no_order = (LinearLayout) rootView.findViewById(R.id.lin_no_order);
            this.lin_close_all = (LinearLayout) rootView.findViewById(R.id.lin_close_all);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
            this.fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}