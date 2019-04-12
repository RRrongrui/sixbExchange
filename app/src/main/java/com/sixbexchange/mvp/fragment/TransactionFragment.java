package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.ExchangeListBean;
import com.sixbexchange.mvp.databinder.TransactionBinder;
import com.sixbexchange.mvp.delegate.TransactionDelegate;
import com.sixbexchange.mvp.popu.SelectExchPopu;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends BaseDataBindFragment<TransactionDelegate, TransactionBinder> {

    @Override
    protected Class<TransactionDelegate> getDelegateClass() {
        return TransactionDelegate.class;
    }

    @Override
    public TransactionBinder getDataBinder(TransactionDelegate viewDelegate) {
        return new TransactionBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("").setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
        addRequest(binder.exchangeList(this));
        String string = CacheUtils.getInstance().getString(CacheName.ExchangeListCache);
        if (!TextUtils.isEmpty(string)) {
            initList(string);
        }
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    TrTransactionFragment trTransactionFragment;
    TrOrderFragment trOrderFragment;
    TrPositionFragment trPositionFragment;
    int showFragmentPosition = 0;

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(trTransactionFragment = new TrTransactionFragment());
        fragments.add(trOrderFragment = new TrOrderFragment());
        fragments.add(trPositionFragment = new TrPositionFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_transaction_title);
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < stringArray.length; i++) {
                mTabEntities.add(new TabEntity(stringArray[i], 0, 0));
            }
            viewDelegate.viewHolder.tl_2.setTabData(mTabEntities);
            viewDelegate.viewHolder.tl_2.setTextSelectColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tl_2.setTextUnselectColor(CommonUtils.getColor(R.color.color_font2));
            viewDelegate.viewHolder.tl_2.setIndicatorColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.vp_root.setOffscreenPageLimit(3);
            innerPagerAdapter = new InnerPagerAdapter(getChildFragmentManager(), fragments, stringArray);
            viewDelegate.viewHolder.tl_2.setViewPager(innerPagerAdapter, viewDelegate.viewHolder.vp_root);
        } else {
            innerPagerAdapter.setDatas(fragments);
        }
        viewDelegate.viewHolder.tl_2.setCurrentTab(showFragmentPosition);
        viewDelegate.viewHolder.vp_root.setCurrentItem(showFragmentPosition);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                CacheUtils.getInstance().put(CacheName.ExchangeListCache, data);
                initList(data);
                break;
        }
    }

    public void changeCoin(String coin) {
        //切换币种

    }

    public void changeContract(String contract) {
        //切换交易对

    }

    public void setLevel(String data) {
        viewDelegate.viewHolder.tv_level.setText(data);
    }

    List<ExchangeListBean> list;
    SelectExchPopu selectExchPopu;
    int position = 0;

    private void initList(String data) {
        list = GsonUtil.getInstance().toList(data, ExchangeListBean.class);
        String string = CacheUtils.getInstance().getString(CacheName.TradeExchangeCache);
        if (TextUtils.isEmpty(string)) {
            GlideUtils.loadImage(list.get(0).getExchangeImg(), viewDelegate.viewHolder.iv_exch);
            viewDelegate.viewHolder.tv_name.setText(list.get(0).getName());
            position = 0;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (ObjectUtils.equals(string, list.get(i).getCode())) {
                    GlideUtils.loadImage(list.get(i).getExchangeImg(), viewDelegate.viewHolder.iv_exch);
                    viewDelegate.viewHolder.tv_name.setText(list.get(i).getName());
                    position = i;
                }
            }
        }
        viewDelegate.viewHolder.lin_exch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectExchPopu == null) {
                    selectExchPopu = new SelectExchPopu(getActivity(), position);
                }
                selectExchPopu.showList(list, viewDelegate.viewHolder.lin_exch, new DefaultClickLinsener() {
                    @Override
                    public void onClick(View view, int position, Object item) {
                        if (view.getId() == R.id.tv_help) {
                            CircleDialogHelper.initDefaultDialog(getActivity(),
                                    list.get(position).getDiscountInfo(), null
                            ).show();
                        } else {
                            //选择交易所
                            GlideUtils.loadImage(list.get(position).getExchangeImg(), viewDelegate.viewHolder.iv_exch);
                            viewDelegate.viewHolder.tv_name.setText(list.get(position).getName());
                            trTransactionFragment.changeExch(list.get(position).getCode());
                            trOrderFragment.changeExch(list.get(position).getCode());
                            trPositionFragment.changeExch(list.get(position).getCode());
                        }
                        selectExchPopu.dismiss();
                    }
                });
            }
        });
    }

}
