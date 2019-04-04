package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.sixbexchange.mvp.databinder.TransferFundsBinder;
import com.sixbexchange.mvp.delegate.TransferFundsDelegate;

public class TransferFundsFragment extends BaseDataBindFragment<TransferFundsDelegate, TransferFundsBinder> {

    @Override
    protected Class<TransferFundsDelegate> getDelegateClass() {
        return TransferFundsDelegate.class;
    }

    @Override
    public TransferFundsBinder getDataBinder(TransferFundsDelegate viewDelegate) {
        return new TransferFundsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
