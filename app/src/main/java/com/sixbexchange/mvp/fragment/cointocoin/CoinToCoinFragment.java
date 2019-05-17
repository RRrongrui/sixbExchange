package com.sixbexchange.mvp.fragment.cointocoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.view.FontTextview;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.LineTextView;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.sixbexchange.R;
import com.sixbexchange.adapter.MyFollowAdapter;
import com.sixbexchange.entity.bean.MyFollowBean;
import com.sixbexchange.mvp.databinder.BaseActivityPullBinder;
import com.sixbexchange.mvp.delegate.BaseActivityPullDelegate;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

/*
*我的跟单列表
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:17
* @Param
* @return
**/
public class CoinToCoinFragment extends BasePullFragment<BaseActivityPullDelegate, BaseActivityPullBinder> {

    @Override
    protected Class<BaseActivityPullDelegate> getDelegateClass() {
        return BaseActivityPullDelegate.class;
    }

    @Override
    public BaseActivityPullBinder getDataBinder(BaseActivityPullDelegate viewDelegate) {
        return new BaseActivityPullBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initList(new ArrayList<MyFollowBean>());
        onRefresh();
    }

    HeaderAndFooterWrapper headerAndFooterWrapper;
    MyFollowAdapter adapter;

    private void initList(List<MyFollowBean> data) {
        if (adapter == null) {
            adapter = new MyFollowAdapter(getActivity(), data);
            headerAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
            headerAndFooterWrapper.addHeaderView(initHeader());
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    public IconFontTextview tv_order_type;
    public LinearLayout lin_order_type;
    public SingleLineZoomTextView tv_now_price;
    public ImageView iv_to_kline;
    public LinearLayout lin_to_kline;
    public TextView tv_buy;
    public TextView tv_sell;
    public FontTextview tv_price;
    public EditText et_num;
    public TextView tv_valuation;
    public RoundButton tv_commit;
    public TextView tv_use;
    public LinearLayout lin_use;
    public LineTextView tv_transaction_title;
    public TextView tv_transaction;
    public LinearLayout lin_transaction;
    public TextView tv_show_price_type;
    public TextView tv_coin_num;
    public RecyclerView rv_buy;
    public RecyclerView rv_sell;
    public SingleLineZoomTextView tv_latest_index;
    public SingleLineZoomTextView tv_fund_rate;

    public View initHeader() {
        View rootView = getLayoutInflater().inflate(R.layout.layout_coin_to_coin, null);
        this.tv_order_type = (IconFontTextview) rootView.findViewById(R.id.tv_order_type);
        this.lin_order_type = (LinearLayout) rootView.findViewById(R.id.lin_order_type);
        this.tv_now_price = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_now_price);
        this.iv_to_kline = (ImageView) rootView.findViewById(R.id.iv_to_kline);
        this.lin_to_kline = (LinearLayout) rootView.findViewById(R.id.lin_to_kline);
        this.tv_buy = (TextView) rootView.findViewById(R.id.tv_buy);
        this.tv_sell = (TextView) rootView.findViewById(R.id.tv_sell);
        this.tv_price = (FontTextview) rootView.findViewById(R.id.tv_price);
        this.et_num = (EditText) rootView.findViewById(R.id.et_num);
        this.tv_valuation = (TextView) rootView.findViewById(R.id.tv_valuation);
        this.tv_commit = (RoundButton) rootView.findViewById(R.id.tv_commit);
        this.tv_use = (TextView) rootView.findViewById(R.id.tv_use);
        this.lin_use = (LinearLayout) rootView.findViewById(R.id.lin_use);
        this.tv_transaction_title = (LineTextView) rootView.findViewById(R.id.tv_transaction_title);
        this.tv_transaction = (TextView) rootView.findViewById(R.id.tv_transaction);
        this.lin_transaction = (LinearLayout) rootView.findViewById(R.id.lin_transaction);
        this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
        this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
        this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
        this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
        this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
        this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);

        setTlPosition(0);
        return rootView;
    }

    private void setTlPosition(int position) {
        if (position == 0) {
            tv_buy.setTextColor(CommonUtils.getColor(R.color.color_Primary));
            tv_sell.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            tv_buy.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                    5, 0, 0, 5
            ));
            tv_sell.setBackground(new RadiuBg(
                    CommonUtils.getColor(R.color.base_mask),
                    0, 5, 5, 0
            ));
            tv_commit.setSolidColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
            tv_commit.setText("买入");
            tv_transaction_title.setText("可买");
        } else {
            tv_sell.setTextColor(CommonUtils.getColor(R.color.color_Primary));
            tv_buy.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
            tv_buy.setBackground(new RadiuBg(
                    CommonUtils.getColor(R.color.base_mask),
                    5, 0, 0, 5
            ));
            tv_sell.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                    0, 5, 5, 0
            ));
            tv_commit.setSolidColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            tv_commit.setText("卖出");
            tv_transaction_title.setText("可卖");
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, MyFollowBean.class));
                break;
        }
    }

    @Override
    protected void refreshData() {

    }


}
