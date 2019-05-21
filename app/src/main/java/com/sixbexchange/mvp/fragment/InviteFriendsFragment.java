package com.sixbexchange.mvp.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.circledialog.CircleDialogHelper;
import com.circledialog.view.listener.OnInputClickListener;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.databinder.InviteFriendsBinder;
import com.sixbexchange.mvp.delegate.InviteFriendsDelegate;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                UiHeplUtils.shareImgViewBySdk(viewDelegate.getActivity(), share(), new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                });
            }
        });

    }

    UserLoginInfo loginInfo;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loginInfo = UserLoginInfo.getLoginInfo();
        viewDelegate.viewHolder.tv_invite_code.setText(loginInfo.getIcode());
        viewDelegate.viewHolder.tv_code.setText(loginInfo.getIcode());
        viewDelegate.viewHolder.tv_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UiHeplUtils.copy(getActivity(),
                        viewDelegate.viewHolder.tv_invite_code.getText().toString(), true);
            }
        });
        addRequest(binder.inviteState(this));
        Observable.create(
                new ObservableOnSubscribe<Bitmap>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(
                                "https://blz.bicoin.com.cn/web/modules/6BDownload.html?intiviteCode="
                                        + viewDelegate.viewHolder.tv_invite_code.getText(),
                                400,
                                CommonUtils.getColor(R.color.black),
                                ((BitmapDrawable) getResources().getDrawable(R.mipmap.artboard)).getBitmap()
                        );
                        e.onNext(bitmap);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(
                        new Consumer<Bitmap>() {
                            @Override
                            public void accept(Bitmap bitmap) throws Exception {
                                viewDelegate.viewHolder.iv_zxing.setImageBitmap(bitmap);
                                shareBitmap = bitmap;
                            }
                        }
                );
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
        iv_zxing.setImageBitmap(shareBitmap);
        return rootView;
    }

    Bitmap shareBitmap;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                viewDelegate.viewHolder.tv_input.setVisibility(View.VISIBLE);
                viewDelegate.viewHolder.tv_input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleDialogHelper.initDefaultInputDialog(viewDelegate.getActivity(),
                                "补填邀请码",
                                "请输入邀请码", "确定", new OnInputClickListener() {
                                    @Override
                                    public void onClick(String text, View v) {
                                        if (TextUtils.isEmpty(text)) {
                                            ToastUtil.show("请输入邀请码");
                                            return;
                                        }
                                        addRequest(binder.setInviteCode(text, InviteFriendsFragment.this));
                                    }
                                }).show();
                    }
                });
                break;
        }
    }

}
