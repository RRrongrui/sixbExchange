package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.HoldPositionBean;
import com.sixbexchange.utils.BigUIUtil;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/4/11 0011.
 * 持仓 页面 adapter
 */

public class HoldPositionAdapter extends BaseAdapter<HoldPositionBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_open;
    private TextView tv_rate;
    private TextView tv_income;
    private TextView tv_amount;
    private TextView tv_close_amount;
    private TextView tv_margin;
    private TextView tv_end_price;
    private TextView tv_change;
    private TextView tv_close;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public HoldPositionAdapter(Context context, List<HoldPositionBean> datas) {
        super(context, R.layout.adapter_hold_position, datas);

    }


    @Override
    protected void convert(ViewHolder holder, HoldPositionBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_type = holder.getView(R.id.tv_type);
        tv_open = holder.getView(R.id.tv_open);
        tv_rate = holder.getView(R.id.tv_rate);
        tv_income = holder.getView(R.id.tv_income);
        tv_amount = holder.getView(R.id.tv_amount);
        tv_close_amount = holder.getView(R.id.tv_close_amount);
        tv_margin = holder.getView(R.id.tv_margin);
        tv_end_price = holder.getView(R.id.tv_end_price);
        tv_change = holder.getView(R.id.tv_change);
        tv_close = holder.getView(R.id.tv_close);

        String[] info = s.getContract().split("\\.");
        //"contract": "eth.usd.t",##交易所/币种.市场.t(本周).n(次周).q(季度)
        tv_name.setText(info[0] + info[2]
                .replace("t", "本周")
                .replace("n", "次周")
                .replace("q", "季度")
        );

        tv_open.setText(BigUIUtil.getinstance().getSymbol(info[1]) +
                BigUIUtil.getinstance().bigPrice(s.getAverageOpenPrice()));
        tv_rate.setText(BigUIUtil.getinstance().bigAmount(s.getUnrealizedRate()) + "%");

        tv_income.setText(BigUIUtil.getinstance().bigAmount(s.getUnrealized()) +" "+ info[0]);
        tv_amount.setText(BigUIUtil.getinstance().bigAmount(s.getTotalAmount()) + " "+info[0]);
        tv_close_amount.setText(BigUIUtil.getinstance().bigAmount(s.getAvailable()) +" "+ info[0]);
        tv_margin.setText(BigUIUtil.getinstance().bigAmount(s.getUsedMargin()) + " "+info[0]);
        tv_end_price.setText(BigUIUtil.getinstance().getSymbol(info[1]) +" "+
                BigUIUtil.getinstance().bigPrice(s.getLiquidationPrice()));

        //#spot 现货 future 期货------------------BM都是future
        if (ObjectUtils.equals("future", s.getType())) {
            tv_type.setVisibility(View.VISIBLE);
        } else {
            tv_type.setVisibility(View.GONE);
        }

    }

}