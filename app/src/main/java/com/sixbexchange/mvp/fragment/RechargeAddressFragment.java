package com.sixbexchange.mvp.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.RechargeAddressBinder;
import com.sixbexchange.mvp.delegate.RechargeAddressDelegate;
import com.sixbexchange.mvp.dialog.LeverageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
*充值地址
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:17
* @Param
* @return
**/
public class RechargeAddressFragment extends BaseDataBindFragment<RechargeAddressDelegate, RechargeAddressBinder> {

    @Override
    protected Class<RechargeAddressDelegate> getDelegateClass() {
        return RechargeAddressDelegate.class;
    }

    @Override
    public RechargeAddressBinder getDataBinder(RechargeAddressDelegate viewDelegate) {
        return new RechargeAddressBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }


    public static RechargeAddressFragment newInstance(
            String typeStr,
            int exchPosition
    ) {
        RechargeAddressFragment newFragment = new RechargeAddressFragment();
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
        addRequest(binder.addrinfo(typeStr, exchPosition, this));
        addRequest(binder.getAccountDetail(this));
        viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
    }

    List<String> coins;
    String addr;
    String remark;
    String notice;
    LeverageDialog leverageDialog;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                addr = GsonUtil.getInstance().getValue(data, "addr");
                remark = GsonUtil.getInstance().getValue(data, "remark");
                notice = GsonUtil.getInstance().getValue(data, "notice");
                viewDelegate.viewHolder.tv_content.setText(notice);
                viewDelegate.viewHolder.lin_memo.setVisibility(TextUtils.isEmpty(remark) ? View.GONE : View.VISIBLE);
                viewDelegate.viewHolder.tv_address.setText(addr + "");
                viewDelegate.viewHolder.tv_memo.setText(remark + "");
                viewDelegate.viewHolder.tv_copy_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiHeplUtils.copy(getActivity(), addr, false);
                    }
                });
                viewDelegate.viewHolder.tv_copy_memo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UiHeplUtils.copy(getActivity(), remark, false);
                    }
                });
                Observable.create(
                        new ObservableOnSubscribe<Bitmap>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Bitmap> e) throws Exception {
                                Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(
                                        addr,
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
                                        viewDelegate.viewHolder.iv_address.setImageBitmap(bitmap);
                                    }
                                }
                        );
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
                                    viewDelegate.viewHolder.iv_address.setImageDrawable(null);
                                    viewDelegate.viewHolder.tv_content.setText("");
                                    viewDelegate.viewHolder.tv_address.setText("");
                                    viewDelegate.viewHolder.tv_memo.setText("");
                                    viewDelegate.viewHolder.tv_select_coins.setText(typeStr);
                                    addRequest(binder.addrinfo(typeStr, exchPosition, RechargeAddressFragment.this));
                                }
                            });
                        }
                        leverageDialog.showDilaog(typeStr, coins);
                    }
                });
                break;
            case 0x125:

                break;
        }
    }

}
