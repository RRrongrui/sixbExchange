package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.sixbexchange.mvp.delegate.BaseActivityPullDelegate;


/**
 * Created by 郭青枫 on 2017/9/27.
 * 统一的 activity列表页面 接口代理
 */

public class BaseActivityPullBinder<T extends BaseActivityPullDelegate> extends BaseDataBind<T> {
    public BaseActivityPullBinder(T viewDelegate) {
        super(viewDelegate);
    }


}