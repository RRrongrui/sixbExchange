package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.WalletCoinBean;
import com.sixbexchange.mvp.databinder.TransferFundsBinder;
import com.sixbexchange.mvp.delegate.TransferFundsDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransferFundsFragment extends BaseDataBindFragment<TransferFundsDelegate, TransferFundsBinder> {

    @Override
    protected Class<TransferFundsDelegate> getDelegateClass() {
        return TransferFundsDelegate.class;
    }

    @Override
    public TransferFundsBinder getDataBinder(TransferFundsDelegate viewDelegate) {
        return new TransferFundsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }

    public static TransferFundsFragment newInstance(
            String typeStr,
            String exchName,
            int exchPosition
    ) {
        TransferFundsFragment newFragment = new TransferFundsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putString("exchName", exchName);
        bundle.putInt("exchPosition", exchPosition);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    String exchName = "";
    int exchPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putString("exchName", exchName);
        outState.putInt("exchPosition", exchPosition);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchName = savedInstanceState.getString("exchName", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchName = this.getArguments().getString("exchName", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }

        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outExch == null) {
                    ToastUtil.show("请选择转出钱包");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入转出数量");
                    return;
                }
                if (ObjectUtils.equals(inExch.get("name"), outExch.get("name"))) {
                    ToastUtil.show("不能选择相同交易所钱包");
                    return;
                }
                addRequest(binder.accounttrans(
                        outExch.get("position"),
                        exchPosition + 1 + "",
                        typeStr,
                        viewDelegate.viewHolder.tv_num.getText().toString(),
                        TransferFundsFragment.this
                ));
            }
        });
        exchNames = new ArrayList<>();
        exchList = new ArrayList<>();
        data = CacheUtils.getInstance().getString(CacheName.ExchWalletCache);
        initCoins();
        initExchList();
        getOutExchList();
        getInExchList();

    }

    Map<String, String> inExch;
    LeverageDialog inExchDialog;

    private void getInExchList() {
        inExch = exchList.get(exchPosition);
        viewDelegate.viewHolder.tv_in.setText(exchNames.get(exchPosition));
        viewDelegate.viewHolder.tv_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inExchDialog == null) {
                    inExchDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            initExchList();
                            inExch = exchList.get(position);
                            viewDelegate.viewHolder.tv_in.setText(exchNames.get(position));
                            initCoins();
                            exchPosition = position;
                        }
                    });
                    inExchDialog.setSize(15);
                }
                inExchDialog.showDilaog(inExch != null ? inExch.get("name") + "钱包" : "", exchNames);
            }
        });
    }

    String data;

    private void initCoins() {
        List<String> list = GsonUtil.getInstance().toList(
                data, String.class
        );
        Map<String, String> map = GsonUtil.getInstance().toMap(list.get(exchPosition),
                new TypeReference<Map<String, String>>() {
                });
        map.remove("name");
        map.remove("position");
        coins = new ArrayList<>();
        String coin = "";
        for (String key : map.keySet()) {
            coins.add(key);
            if (typeStr.equals(key)) {
                coin = typeStr;
            }
        }
        if (TextUtils.isEmpty(coin)) {
            typeStr = coins.get(0);
        }
        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
        viewDelegate.viewHolder.lin_select_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leverageDialog == null) {
                    leverageDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            typeStr = coins.get(position);
                            viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                            initExchList();
                            getInExchList();
                        }
                    });
                }
                leverageDialog.showDilaog(typeStr, coins);
            }
        });
    }

    List<Map<String, String>> exchList;
    Map<String, String> outExch;
    LeverageDialog outExchDialog;
    List<String> exchNames;

    private void initExchList() {
        List<String> list = GsonUtil.getInstance().toList(
                data, String.class
        );
        exchList.clear();
        exchNames.clear();
        boolean isHaveInExch = false;
        boolean isHaveOutExch = false;
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = GsonUtil.getInstance().toMap(list.get(i),
                    new TypeReference<Map<String, String>>() {
                    });
            if (map.containsKey(typeStr)) {
                if (inExch != null && ObjectUtils.equals(map.get("name"), inExch.get("name"))) {
                    isHaveInExch = true;
                }
                if (outExch != null && ObjectUtils.equals(map.get("name"), outExch.get("name"))) {
                    isHaveOutExch = true;
                }
                exchList.add(map);
                exchNames.add(map.get("name") + "钱包");
            }
        }
        if (!isHaveInExch) {
            inExch = exchList.get(0);
            exchPosition = 0;
        }
        if (!isHaveOutExch) {
            getOutExchList();
        } else {
            WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(outExch.get(typeStr), WalletCoinBean.class);
            viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                    new BigDecimal(walletCoinBean.getAvi())
                            .subtract(new BigDecimal(walletCoinBean.getLock()))
                            .stripTrailingZeros().toPlainString() + typeStr
            );
        }
    }

    private void getOutExchList() {
        outExch = null;
        viewDelegate.viewHolder.tv_num.setHint("请输入转出数量");
        viewDelegate.viewHolder.tv_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outExchDialog == null) {
                    outExchDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            outExch = exchList.get(position);
                            viewDelegate.viewHolder.tv_out.setText(exchNames.get(position));
                            WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(outExch.get(typeStr), WalletCoinBean.class);
                            viewDelegate.viewHolder.tv_num.setHint("当前可用" +
                                    new BigDecimal(walletCoinBean.getAvi())
                                            .subtract(new BigDecimal(walletCoinBean.getLock()))
                                            .stripTrailingZeros().toPlainString() + typeStr
                            );
                        }
                    });
                    outExchDialog.setSize(15);
                }
                outExchDialog.showDilaog(outExch != null ? outExch.get("name") + "钱包" : "", exchNames);
            }
        });
    }


    List<String> coins;
    LeverageDialog leverageDialog;


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:

                break;
        }
    }

}
