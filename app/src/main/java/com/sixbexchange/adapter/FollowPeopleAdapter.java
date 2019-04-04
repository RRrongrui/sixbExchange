package com.sixbexchange.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.FollowPeopleBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class FollowPeopleAdapter extends BaseAdapter<FollowPeopleBean> {


    private ImageView iv_piv;
    private TextView tv_name;
    private RoundButton tv_sub;
    private TextView tv_progress;
    private View view_start;
    private View view_end;
    private TextView tv_rate;
    private TextView tv_divided_into;
    private TextView tv_money;
    private RoundButton tv_commit;
    private TextView tv_rate_title;
    private TextView tv_divided_into_title;
    private TextView tv_money_title;

    public FollowPeopleAdapter(Context context, List<FollowPeopleBean> datas) {
        super(context, R.layout.adapter_follow_people, datas);

    }


    @Override
    protected void convert(ViewHolder holder, FollowPeopleBean s, final int position) {
        iv_piv = holder.getView(R.id.iv_piv);
        tv_name = holder.getView(R.id.tv_name);
        tv_sub = holder.getView(R.id.tv_sub);
        tv_progress = holder.getView(R.id.tv_progress);
        view_start = holder.getView(R.id.view_start);
        view_end = holder.getView(R.id.view_end);
        tv_rate = holder.getView(R.id.tv_rate);
        tv_divided_into = holder.getView(R.id.tv_divided_into);
        tv_money = holder.getView(R.id.tv_money);
        tv_commit = holder.getView(R.id.tv_commit);
        tv_money_title = holder.getView(R.id.tv_money_title);
        tv_divided_into_title = holder.getView(R.id.tv_divided_into_title);
        tv_rate_title = holder.getView(R.id.tv_rate_title);

        view_start.setBackground(new RadiuBg(
                CommonUtils.getColor(R.color.color_25A73F),
                999, 999, 999, 999
        ));

        GlideUtils.loadImage(s.getIcon(), iv_piv);
        tv_name.setText(s.getNickName());
        tv_sub.setText(s.getTitle());

        BigDecimal subtract = new BigDecimal(s.getAllAmount()).subtract(new BigDecimal(s.getRestAmount()));
        LinearLayout.LayoutParams view_startp = (LinearLayout.LayoutParams) view_start.getLayoutParams();
        view_startp.weight = subtract.floatValue();
        view_start.setLayoutParams(view_startp);

        tv_progress.setText("当前进度 " + subtract.multiply(new BigDecimal("100"))
                .divide(new BigDecimal(s.getAllAmount()), 0, RoundingMode.DOWN).intValue() + "%");

        LinearLayout.LayoutParams view_endp = (LinearLayout.LayoutParams) view_end.getLayoutParams();
        view_endp.weight = new BigDecimal(s.getRestAmount()).floatValue();
        view_end.setLayoutParams(view_endp);


        if (s.getStatus() == 0) {
            tv_commit.setEnabled(true);
            tv_commit.setText("跟单");
            tv_commit.setSolidColor(CommonUtils.getColor(R.color.color_FA8C16));

            tv_rate_title.setText("实盘收益率");
            tv_divided_into_title.setText("用户分成");
            tv_money_title.setText("起投金额");
            tv_rate.setText(Html.fromHtml(
                    s.getBicoinRate() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_divided_into.setText(Html.fromHtml(
                    s.getBenitfitRate() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_money.setText(Html.fromHtml(
                    (new BigDecimal(s.getAllMoney()).divide(
                            new BigDecimal(s.getAllAmount()), 4, RoundingMode.DOWN
                    ).stripTrailingZeros().toPlainString()) +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>" + s.getCurrency() + "</small></small></small>"
            ));


        } else if (s.getStatus() == 1) {
            tv_commit.setEnabled(false);
            tv_commit.setText("即将开始");
            tv_commit.setSolidColor(CommonUtils.getColor(R.color.base_mask));


            tv_rate_title.setText("实盘收益率");
            tv_divided_into_title.setText("投资周期");
            tv_money_title.setText("起投金额");
            tv_rate.setText(Html.fromHtml(
                    s.getBicoinRate() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_divided_into.setText(Html.fromHtml(
                    s.getCloseDay() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>天</small></small></small>"
            ));
            tv_money.setText(Html.fromHtml(
                    (new BigDecimal(s.getAllMoney()).divide(
                            new BigDecimal(s.getAllAmount()), 4, RoundingMode.DOWN
                    ).stripTrailingZeros().toPlainString()) +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>" + s.getCurrency() + "</small></small></small>"
            ));

        } else if (s.getStatus() == 3) {
            tv_commit.setEnabled(false);
            tv_commit.setText("已结束");
            tv_commit.setSolidColor(CommonUtils.getColor(R.color.base_mask));

            tv_rate_title.setText("实盘收益率");
            tv_divided_into_title.setText("用户分成");
            tv_money_title.setText("资金规模");
            tv_rate.setText(Html.fromHtml(
                    s.getBicoinRate() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_divided_into.setText(Html.fromHtml(
                    s.getBenitfitRate() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_money.setText(Html.fromHtml(
                    (new BigDecimal(s.getAllMoney()).stripTrailingZeros().toPlainString()) +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>" + s.getCurrency() + "</small></small></small>"
            ));

        } else if (s.getStatus() == 2) {
            tv_commit.setEnabled(false);
            tv_commit.setText("运行中");
            tv_commit.setSolidColor(CommonUtils.getColor(R.color.color_25A73F));

            tv_rate_title.setText("当前收益率");
            tv_divided_into_title.setText("用户分成");
            tv_money_title.setText("资金规模");
            tv_rate.setText(Html.fromHtml(
                    s.getWelfare() +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_divided_into.setText(Html.fromHtml(
                    (new BigDecimal(s.getWelfare())
                            .multiply(new BigDecimal("100"))
                            .divide(new BigDecimal(s.getAllMoney()), 4, RoundingMode.DOWN)
                            .stripTrailingZeros().toPlainString()) +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>%</small></small></small>"
            ));
            tv_money.setText(Html.fromHtml(
                    (new BigDecimal(s.getAllMoney()).stripTrailingZeros().toPlainString()) +
                            "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                            "\"<small><small><small>" + s.getCurrency() + "</small></small></small>"
            ));


        }

    }

}