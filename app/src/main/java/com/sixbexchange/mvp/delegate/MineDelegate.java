package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }


    public static class ViewHolder {
        public View rootView;

        public CircleImageView iv_pic;
        public TextView tv_name;
        public TextView tv_uid;
        public TextView tv_type;
        public LinearLayout lin1;
        public LinearLayout lin2;
        public TextView tv_version;
        public LinearLayout lin3;
        public LinearLayout lin4;
        public LinearLayout lin5;
        public LinearLayout lin6;
        public TextView tv_logout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.iv_pic = (CircleImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_uid = (TextView) rootView.findViewById(R.id.tv_uid);
            this.tv_type = (TextView) rootView.findViewById(R.id.tv_type);
            this.lin1 = (LinearLayout) rootView.findViewById(R.id.lin1);
            this.lin2 = (LinearLayout) rootView.findViewById(R.id.lin2);
            this.tv_version = (TextView) rootView.findViewById(R.id.tv_version);
            this.lin3 = (LinearLayout) rootView.findViewById(R.id.lin3);
            this.lin4 = (LinearLayout) rootView.findViewById(R.id.lin4);
            this.lin5 = (LinearLayout) rootView.findViewById(R.id.lin5);
            this.lin6 = (LinearLayout) rootView.findViewById(R.id.lin6);
            this.tv_logout = (TextView) rootView.findViewById(R.id.tv_logout);
        }

    }
}