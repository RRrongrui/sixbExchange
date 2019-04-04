package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.CoinAddressBean;
import com.sixbexchange.entity.bean.WithdrawCoinBean;
import com.sixbexchange.mvp.databinder.WithdrawCoinBinder;
import com.sixbexchange.mvp.delegate.WithdrawCoinDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
*提现申请
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:36
* @Param
* @return
**/
public class WithdrawCoinFragment extends BaseDataBindFragment<WithdrawCoinDelegate, WithdrawCoinBinder> {

    @Override
    protected Class<WithdrawCoinDelegate> getDelegateClass() {
        return WithdrawCoinDelegate.class;
    }

    @Override
    public WithdrawCoinBinder getDataBinder(WithdrawCoinDelegate viewDelegate) {
        return new WithdrawCoinBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("提币"));
        viewDelegate.viewHolder.tv_set_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new FundPasswordFragment());
            }
        });
        viewDelegate.viewHolder.tv_set_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForResult(AddressManagementFragment.newInstance(typeStr, exchPosition), 0x123);
            }
        });
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(binder.mobile(WithdrawCoinFragment.this));
            }
        });
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 0x123) {
                addRequest(binder.extract(typeStr, this));
            }
        }
    }

    public static WithdrawCoinFragment newInstance(
            String typeStr,
            int exchPosition
    ) {
        WithdrawCoinFragment newFragment = new WithdrawCoinFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putInt("exchPosition", exchPosition);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    int exchPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putInt("exchPosition", exchPosition);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchPosition = this.getArguments().getInt("exchPosition");
        }
        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
        addRequest(binder.getAccountDetail(this));
        addRequest(binder.extract(typeStr, this));
        viewDelegate.viewHolder.tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectAddr == null) {
                    ToastUtil.show("请选择地址");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入提币数量");
                    return;
                }
                if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_num.getText().toString())) {
                    ToastUtil.show("请输入正确的数字");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_code.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                addRequest(binder.sendExtract(
                        selectAddr.getAddr(),
                        typeStr,
                        viewDelegate.viewHolder.tv_num.getText().toString(),
                        withdrawCoinBean.getFee() + "",
                        viewDelegate.viewHolder.tv_code.getText().toString(),
                        WithdrawCoinFragment.this
                ));
            }

        });
        viewDelegate.viewHolder.lin_select_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinAddressBeans == null) {
                    ToastUtil.show("未获取到地址");
                    return;
                }
                if (ListUtils.isEmpty(coinAddressBeans)) {
                    ToastUtil.show("您还没有当前币种的提币地址，请添加");
                    return;
                }
                addressDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                    @Override
                    public void onClick(View view, int position, Object item) {
                        selectAddr = coinAddressBeans.get(position);
                        viewDelegate.viewHolder.tv_addr.setText(
                                selectAddr.getRemark()
                        );
                    }
                });

                String select = "";
                if (selectAddr != null) {
                    select = selectAddr.getRemark();
                }
                List<String> address = new ArrayList<>();
                for (int i = 0; i < coinAddressBeans.size(); i++) {
                    address.add(
                            coinAddressBeans.get(i).getRemark()
                    );
                }
                addressDialog.showDilaog(select, address);
            }
        });
    }

    List<String> coins;
    LeverageDialog leverageDialog;
    List<CoinAddressBean> coinAddressBeans;
    CoinAddressBean selectAddr;

    LeverageDialog addressDialog;
    WithdrawCoinBean withdrawCoinBean;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                withdrawCoinBean = GsonUtil.getInstance().toObj(data, WithdrawCoinBean.class);
                viewDelegate.viewHolder.tv_num.setHint("最小提币单位" + withdrawCoinBean.getMin() + typeStr);
                coinAddressBeans = GsonUtil.getInstance().toList(data, "list", CoinAddressBean.class);
                viewDelegate.viewHolder.tv_content.setText("提币手续费" + withdrawCoinBean.getFee() + typeStr);
                break;
            case 0x124:
                List<String> list = GsonUtil.getInstance().toList(
                        data, String.class
                );
                Map<String, String> map = GsonUtil.getInstance().toMap(list.get(exchPosition),
                        new TypeReference<Map<String, String>>() {
                        });
                map.remove("name");
                map.remove("position");
                coins = new ArrayList<>();
                for (String key : map.keySet()) {
                    coins.add(key);
                }
                viewDelegate.viewHolder.lin_select_coin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (leverageDialog == null) {
                            leverageDialog = new LeverageDialog(getActivity(), new DefaultClickLinsener() {
                                @Override
                                public void onClick(View view, int position, Object item) {
                                    typeStr = coins.get(position);
                                    viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                                    viewDelegate.viewHolder.tv_num.setText("");
                                    viewDelegate.viewHolder.tv_content.setText("");
                                    viewDelegate.viewHolder.tv_addr.setText("");
                                    viewDelegate.viewHolder.tv_code.setText("");
                                    selectAddr = null;
                                    addRequest(binder.extract(typeStr, WithdrawCoinFragment.this));
                                }
                            });
                        }
                        leverageDialog.showDilaog(typeStr, coins);
                    }
                });
                break;
            case 0x126:
                addRequest(binder.vcode(data, this));
                break;
            case 0x125:
                addRequest(UiHeplUtils.getCode(viewDelegate.viewHolder.tv_get_code, 60));
                break;
        }
    }

}
