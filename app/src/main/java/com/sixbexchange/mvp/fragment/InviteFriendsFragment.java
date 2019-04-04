package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.databinder.InviteFriendsBinder;
import com.sixbexchange.mvp.delegate.InviteFriendsDelegate;

public class InviteFriendsFragment extends BaseDataBindFragment<InviteFriendsDelegate, InviteFriendsBinder> {

    @Override
    protected Class<InviteFriendsDelegate> getDelegateClass() {
        return InviteFriendsDelegate.class;
    }

    @Override
    public InviteFriendsBinder getDataBinder(InviteFriendsDelegate viewDelegate) {
        return new InviteFriendsBinder(viewDelegate);
    }

    String imgBg = "http://bicoin.oss-cn-beijing.aliyuncs.com/6b/img/6bsharebg.png";

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("邀请好友"));
        GlideUtils.loadImage(
                imgBg,
                viewDelegate.viewHolder.iv_piv,
                GlideUtils.getNoCacheRO()
        );
        GlideUtils.loadImage(
                "http://bicoin.oss-cn-beijing.aliyuncs.com/6b/img/6bsharebgtop.png",
                viewDelegate.viewHolder.iv_invite,
                GlideUtils.getNoCacheRO()
        );
        GlideUtils.loadImage(
                "http://bicoin.oss-cn-beijing.aliyuncs.com/6b/img/6bsharebgrule.png",
                viewDelegate.viewHolder.iv_activity_rules,
                GlideUtils.getNoCacheRO()
        );
        viewDelegate.viewHolder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHeplUtils.shareImgView(viewDelegate.getActivity(), share());
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        UserLoginInfo loginInfo = UserLoginInfo.getLoginInfo();
        viewDelegate.viewHolder.tv_invite_code.setText(loginInfo.getIcode());
        viewDelegate.viewHolder.tv_code.setText(loginInfo.getIcode());
        viewDelegate.viewHolder.tv_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHeplUtils.copy(getActivity(),
                        viewDelegate.viewHolder.tv_invite_code.getText().toString(), true);
            }
        });


    }

    public ImageView iv_piv;
    public TextView tv_invite_code;
    public ImageView iv_zxing;

    private View share() {
        View rootView = getLayoutInflater().inflate(R.layout.layout_share_friend, null);
        this.iv_piv = (ImageView) rootView.findViewById(R.id.iv_piv);
        this.iv_zxing = (ImageView) rootView.findViewById(R.id.iv_zxing);
        this.tv_invite_code = (TextView) rootView.findViewById(R.id.tv_invite_code);
        GlideUtils.loadImage(imgBg,
                iv_piv, GlideUtils.getNoCacheRO());
        tv_invite_code.setText(viewDelegate.viewHolder.tv_invite_code.getText().toString());
        tv_invite_code.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.getDimensionPixelSize(R.dimen.trans_54px));
        //iv_zxing.setImageBitmap(shareBitmap);
        return rootView;
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
