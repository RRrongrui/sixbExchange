package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.sixbexchange.mvp.databinder.TrTransactionBinder;
import com.sixbexchange.mvp.delegate.TrTransactionDelegate;

public class TrTransactionFragment extends BaseDataBindFragment<TrTransactionDelegate, TrTransactionBinder> {

    @Override
    protected Class<TrTransactionDelegate> getDelegateClass() {
        return TrTransactionDelegate.class;
    }

    @Override
    public TrTransactionBinder getDataBinder(TrTransactionDelegate viewDelegate) {
        return new TrTransactionBinder(viewDelegate);
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
