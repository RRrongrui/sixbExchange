package com.sixbexchange.mvp.fragment.transaction.bitmex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.adapter.BMHoldPositionAdapter;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.HoldPositionBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.sixbexchange.mvp.dialog.ChangeMArginDialog;
import com.sixbexchange.mvp.dialog.ClosePositionDialog;
import com.sixbexchange.utils.BigUIUtil;

import java.util.ArrayList;
import java.util.List;

public class BMTrPositionFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

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
        initList(new ArrayList<HoldPositionBean>());
    }

    BMHoldPositionAdapter adapter;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (adapter != null && tradeDetailBean != null) {
            onRefresh();
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    int selectPosition = 0;
    ClosePositionDialog closePositionDialog;
    ChangeMArginDialog changeMArginDialog;

    private void initList(List<HoldPositionBean> data) {
        if (adapter == null) {
            adapter = new BMHoldPositionAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int p, Object item) {
                    selectPosition = p;
                    if (view.getId() == R.id.tv_change) {
                        if (changeMArginDialog == null) {
                            changeMArginDialog = new ChangeMArginDialog(getActivity(), new DefaultClickLinsener() {
                                @Override
                                public void onClick(View view, int position, Object item) {
                                    if (!TextUtils.isEmpty((String) item)) {
                                        if (UiHeplUtils.isDouble((String) item)) {
                                            addRequest(binder.transferMargin(
                                                    tradeDetailBean.getExchange(),
                                                    adapter.getDatas().get(position).getContract(),
                                                    (changeMArginDialog.isAdd() ? "" : "-") +
                                                            (String) item,
                                                    BMTrPositionFragment.this));
                                        }
                                    }
                                }
                            });
                        }
                        changeMArginDialog.showDialog(BigUIUtil.getinstance().getSymbol("XBT"));
                    } else {
                        if (closePositionDialog == null) {
                            closePositionDialog = new ClosePositionDialog(getActivity(), new DefaultClickLinsener() {
                                @Override
                                public void onClick(View view, int position, Object item) {
                                    if (position == 0) {
                                        CircleDialogHelper.initDefaultDialog(
                                                getActivity(),
                                                "确定市价全平" + "\n" +
                                                        adapter.getDatas().get(selectPosition).getDetail().getContractName() +
                                                        (adapter.getDatas().get(selectPosition).getTotalAmount().contains("-")
                                                                ? "空头" : "多头") +
                                                        (adapter.getDatas().get(selectPosition).getDetail().getLever_rate() + "X"),
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        addRequest(binder.placeOrder(
                                                                tradeDetailBean.getExchange(),
                                                                "1",
                                                                "",
                                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? 6 : 5,
                                                                adapter.getDatas().get(selectPosition).getContract(),
                                                                adapter.getDatas().get(selectPosition).getContract(),
                                                                adapter.getDatas().get(selectPosition).getAvailable().replace("-", ""),
                                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? "b" : "s",
                                                                BMTrPositionFragment.this
                                                        ));
                                                    }
                                                }
                                        ).show();
                                    } else if (position == 1) {
                                        String[] info = ((String) item).split("/");
                                        addRequest(binder.placeOrder(
                                                tradeDetailBean.getExchange(),
                                                "0",
                                                info[0],
                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? 4 : 3,
                                                adapter.getDatas().get(selectPosition).getContract(),
                                                adapter.getDatas().get(selectPosition).getContract(),
                                                info[1],
                                                adapter.getDatas().get(selectPosition).getTotalAmount().contains("-") ? "b" : "s",
                                                BMTrPositionFragment.this
                                        ));
                                    }
                                }
                            });
                        }
                        closePositionDialog.showDialog(adapter.getDatas().get(p));
                    }
                }
            });
            initHeader();
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    public IconFontTextview tv_coin;


    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_position_header, null);
        this.tv_coin = (IconFontTextview) rootView.findViewById(R.id.tv_coin);
        tv_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下拉选择币种
                ((BMTrParentsFragment) getParentFragment()).showSelectCoins(v,
                        selectPositionBean.getName());
            }
        });
        viewDelegate.viewHolder.fl_pull.addView(rootView, 0);
    }

    ExchSelectPositionBean selectPositionBean;
    TradeDetailBean tradeDetailBean;

    public TradeDetailBean getTradeDetailBean() {
        return tradeDetailBean;
    }

    public void initTradeDetail(ExchSelectPositionBean s, TradeDetailBean t) {
        //请求新数据 获取新的币种列表
        selectPositionBean = s;
        tradeDetailBean = t;
        tv_coin.setText(s.getName() + " " + CommonUtils.getString(R.string.ic_Down));
        initList(new ArrayList<HoldPositionBean>());
        onRefresh();
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                List<HoldPositionBean> holdPositionBeans = GsonUtil.getInstance().toList(data, HoldPositionBean.class);
                initList(holdPositionBeans);
                break;
            case 0x125:
                //平仓
                tv_coin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 500);
                break;
            case 0x127:

                break;
        }
    }

    @Override
    protected void refreshData() {
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
        addRequest(binder.getCoinPosition(
                tradeDetailBean.getExchange(),
                selectPositionBean.getCurrency(),
                this));
    }
}
