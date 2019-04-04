package com.sixbexchange.mvp.fragment;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.AppUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.AppVersion;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.MineBinder;
import com.sixbexchange.mvp.delegate.MineDelegate;
import com.sixbexchange.mvp.dialog.UpdateDialog;
import com.sixbexchange.server.UpdateService;
import com.sixbexchange.utils.UserSet;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

/*
* 用户主页
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:13
* @Param
* @return
**/

public class MineFragment extends BaseDataBindFragment<MineDelegate, MineBinder> {

    @Override
    protected Class<MineDelegate> getDelegateClass() {
        return MineDelegate.class;
    }

    @Override
    public MineBinder getDataBinder(MineDelegate viewDelegate) {
        return new MineBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("个人中心")
                //.setmRightImg1(CommonUtils.getString(R.string.ic_envelope) + " 通知中心")
                .setShowBack(false));
        GlideUtils.loadImage("http://bicoin.oss-cn-beijing.aliyuncs.com/6b/img/userinvite.png",
                viewDelegate.viewHolder.iv_invite, GlideUtils.getNoCacheRO());
        viewDelegate.viewHolder.iv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment()).startBrotherFragment(new InviteFriendsFragment());
                }
            }
        });


        viewDelegate.viewHolder.lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bcoin2018.mikecrm.com/K8BBMl1");
            }
        });
        viewDelegate.viewHolder.lin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/jiarushequn.html");
            }
        });
        viewDelegate.viewHolder.lin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivityActivity.startAct(getActivity(),
                        "https://bicoin.oss-cn-beijing.aliyuncs.com/6b/userweb/changjianwenti.html");
            }
        });
        viewDelegate.viewHolder.lin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest(binder.getlatestversion(
                        AppUtils.getAppVersionName(), MineFragment.this
                ));
            }
        });

    }

    UserLoginInfo userLoginInfo;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        userLoginInfo = UserLoginInfo.getLoginInfo();
        viewDelegate.viewHolder.tv_name.setText(userLoginInfo.getMobile());
        viewDelegate.viewHolder.tv_uid.setText("UID " + userLoginInfo.getUid());
        if (UiHeplUtils.compareVersion(UserSet.getinstance().getSystemVersion(),
                AppUtils.getAppVersionName()) == 1) {
            viewDelegate.viewHolder.tv_version.setText("有新版本");
        } else {
            viewDelegate.viewHolder.tv_version.setText("当前版本 V" + AppUtils.getAppVersionName());
        }
        GlideUtils.loadImage(userLoginInfo.gethImg(),
                viewDelegate.viewHolder.iv_pic);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.viewHolder.tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginAndRegisteredActivity.class));
                getActivity().finish();
            }
        });
        viewDelegate.viewHolder.lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment()).startBrotherFragment(new SecurityCenterFragment());
                }
            }
        });
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //版本更新
                appVersion = GsonUtil.getInstance().toObj(data, AppVersion.class);
                version();
                break;
        }
    }

    AppVersion appVersion;

    private void version() {
        if (UiHeplUtils.compareVersion(appVersion.getSystemVersion(), AppUtils.getAppVersionName()) == 1) {
            new UpdateDialog(viewDelegate.getActivity())
                    .setAppVersion(appVersion)
                    .setDefaultClickLinsener(new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            if (position == 1) {
                                if (AppUtil.isWifi(viewDelegate.getActivity())) {
                                    updataApp();
                                } else {
                                    CircleDialogHelper.initDefaultDialog(viewDelegate.getActivity(), CommonUtils.getString(R.string.isinstall_in_no_wifi), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            updataApp();
                                        }
                                    }).show();
                                }
                            }
                        }
                    }).showDialog();
        } else {
            ToastUtil.show(CommonUtils.getString(R.string.str_no_new_version));
        }

    }

    private void updataApp() {
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        UpdateService.
                                Builder.create(appVersion.getDownloadAddr())
                                .setStoreDir("update")
                                .setIcoResId(R.drawable.artboard)
                                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                .setAppVersion(appVersion)
                                .build(viewDelegate.getActivity());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        ToastUtil.show(CommonUtils.getString(R.string.str_permission_read_write));
                    }
                }).start();

    }

}
