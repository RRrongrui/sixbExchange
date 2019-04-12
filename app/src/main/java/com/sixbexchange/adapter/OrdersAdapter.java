package com.sixbexchange.adapter;

import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 * 用户订单
 */

public class OrdersAdapter extends BaseAdapter<OrderBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_open_amount;
    private TextView tv_open_amount_title;
    private TextView tv_hold_amount;
    private TextView tv_hold_amount_title;
    private TextView tv_open_price;
    private TextView tv_avg_price;
    private TextView tv_statu;
    private TextView tv_time;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public OrdersAdapter(Context context, List<OrderBean> datas) {
        super(context, R.layout.adapter_orders, datas);

    }


    @Override
    protected void convert(ViewHolder holder, OrderBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_type = holder.getView(R.id.tv_type);
        tv_open_amount = holder.getView(R.id.tv_open_amount);
        tv_open_amount_title = holder.getView(R.id.tv_open_amount_title);
        tv_hold_amount = holder.getView(R.id.tv_hold_amount);
        tv_hold_amount_title = holder.getView(R.id.tv_hold_amount_title);
        tv_open_price = holder.getView(R.id.tv_open_price);
        tv_avg_price = holder.getView(R.id.tv_avg_price);
        tv_statu = holder.getView(R.id.tv_statu);
        tv_time = holder.getView(R.id.tv_time);

        String[] info = s.getContract().split("/")[1].split("\\.");
        tv_name.setText(info[0] + info[2]
                .replace("t", "本周")
                .replace("n", "次周")
                .replace("q", "季度")
        );
        if (ObjectUtils.equals("b", s.getBs())) {
            tv_type.setText("买入/做多");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                    5, 5, 5, 5
            ));
        } else if (ObjectUtils.equals("s", s.getBs())) {
            tv_type.setText("卖出/开空");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                    5, 5, 5, 5
            ));
        }

        tv_open_amount.setText(BigUIUtil.getinstance().bigAmount(s.getEntrust_amount()));
        tv_hold_amount.setText(BigUIUtil.getinstance().bigAmount(s.getDealt_amount()));

        tv_open_amount_title.setText("委托数量（" + info[1] + "）");
        tv_hold_amount_title.setText("已成交数量（" + info[1] + "）");

        tv_open_price.setText(BigUIUtil.getinstance().getSymbol(info[1]) + " " +
                BigUIUtil.getinstance().bigPrice(s.getEntrust_price()));
        tv_avg_price.setText(BigUIUtil.getinstance().getSymbol(info[1]) + " " +
                BigUIUtil.getinstance().bigPrice(s.getAverage_dealt_price()));

        tv_statu.setText(
                s.getStatus()
                        .replace("active", "激活中")
                        .replace("waiting", "已下单")
                        .replace("pending", "等待成交")
                        .replace("part-deal-pending", "部分成交")
                        .replace("withdrawing", "等待撤单")
                        .replace("part-deal-withdrawing", "等待撤单")
                        .replace("end", "已结束")
                        .replace("dealt", "完全成交")
                        .replace("withdrawn", "已撤销")
                        .replace("part-deal-withdrawn", "已撤单")
                        .replace("error-order", "错误订单")
        );
        tv_time.setText(s.getEntrust_time());
    }

}