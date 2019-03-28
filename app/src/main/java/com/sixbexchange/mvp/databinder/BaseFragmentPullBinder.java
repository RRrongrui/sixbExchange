package com.sixbexchange.mvp.databinder;


import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;

/**
 * Created by 郭青枫 on 2017/9/27.
 * 统一的 fragment列表接口 代理
 */

public class BaseFragmentPullBinder extends BaseDataBind<BaseFragentPullDelegate> {
    public BaseFragmentPullBinder(BaseFragentPullDelegate viewDelegate) {
        super(viewDelegate);
    }


}