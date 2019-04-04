package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.JudgeNestedScrollView;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowOrderDetailsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow_order_details;
    }


    public static class ViewHolder {
        public View rootView;

        public CircleImageView iv_pic;
        public TextView tv_name;
        public RoundButton tv_sub;
        public TextView tv_rate;
        public TextView tv_days;
        public TextView tv_money;
        public TextView tv_unit;
        public IconFontTextview tv_rate_help;
        public IconFontTextview tv_days_help;
        public IconFontTextview tv_money_help;
        public TextView tv_progress_start;
        public View view_progress_end;
        public LinearLayout lin_progress;
        public IconFontTextview tv_all;
        public TextView tv_content_interest;
        public IconFontTextview tv_info1_help;
        public TextView tv_info1;
        public IconFontTextview tv_info2_help;
        public TextView tv_info2;
        public IconFontTextview tv_info3_help;
        public TextView tv_info3;
        public IconFontTextview tv_info4_help;
        public TextView tv_info4;
        public IconFontTextview tv_info5_help;
        public TextView tv_info5;
        public IconFontTextview tv_info6_help;
        public TextView tv_info6;
        public TextView tv_content_introduction;
        public TextView tv_content_risk;
        public JudgeNestedScrollView judgeNestedScrollView;

        public IconFontTextview tv_customer_service;
        public RoundButton tv_buy;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.iv_pic = (CircleImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_sub = (RoundButton) rootView.findViewById(R.id.tv_sub);
            this.tv_rate = (TextView) rootView.findViewById(R.id.tv_rate);
            this.tv_days = (TextView) rootView.findViewById(R.id.tv_days);
            this.tv_money = (TextView) rootView.findViewById(R.id.tv_money);
            this.tv_unit = (TextView) rootView.findViewById(R.id.tv_unit);
            this.tv_rate_help = (IconFontTextview) rootView.findViewById(R.id.tv_rate_help);
            this.tv_days_help = (IconFontTextview) rootView.findViewById(R.id.tv_days_help);
            this.tv_money_help = (IconFontTextview) rootView.findViewById(R.id.tv_money_help);
            this.tv_progress_start = (TextView) rootView.findViewById(R.id.tv_progress_start);
            this.view_progress_end = (View) rootView.findViewById(R.id.view_progress_end);
            this.lin_progress = (LinearLayout) rootView.findViewById(R.id.lin_progress);
            this.tv_all = (IconFontTextview) rootView.findViewById(R.id.tv_all);
            this.tv_content_interest = (TextView) rootView.findViewById(R.id.tv_content_interest);
            this.tv_info1_help = (IconFontTextview) rootView.findViewById(R.id.tv_info1_help);
            this.tv_info1 = (TextView) rootView.findViewById(R.id.tv_info1);
            this.tv_info2_help = (IconFontTextview) rootView.findViewById(R.id.tv_info2_help);
            this.tv_info2 = (TextView) rootView.findViewById(R.id.tv_info2);
            this.tv_info3_help = (IconFontTextview) rootView.findViewById(R.id.tv_info3_help);
            this.tv_info3 = (TextView) rootView.findViewById(R.id.tv_info3);
            this.tv_info4_help = (IconFontTextview) rootView.findViewById(R.id.tv_info4_help);
            this.tv_info4 = (TextView) rootView.findViewById(R.id.tv_info4);
            this.tv_info5_help = (IconFontTextview) rootView.findViewById(R.id.tv_info5_help);
            this.tv_info5 = (TextView) rootView.findViewById(R.id.tv_info5);
            this.tv_info6_help = (IconFontTextview) rootView.findViewById(R.id.tv_info6_help);
            this.tv_info6 = (TextView) rootView.findViewById(R.id.tv_info6);
            this.tv_content_introduction = (TextView) rootView.findViewById(R.id.tv_content_introduction);
            this.tv_content_risk = (TextView) rootView.findViewById(R.id.tv_content_risk);
            this.judgeNestedScrollView = (JudgeNestedScrollView) rootView.findViewById(R.id.judgeNestedScrollView);
            this.tv_customer_service = (IconFontTextview) rootView.findViewById(R.id.tv_customer_service);
            this.tv_buy = (RoundButton) rootView.findViewById(R.id.tv_buy);
        }

    }
}