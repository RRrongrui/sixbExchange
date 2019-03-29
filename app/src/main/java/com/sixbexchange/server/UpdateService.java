package com.sixbexchange.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.BuildConfig;
import com.sixbexchange.R;
import com.sixbexchange.base.AppConst;
import com.sixbexchange.entity.bean.AppVersion;
import com.sixbexchange.server.listener.UpdateProgressListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class UpdateService extends Service {
    public static final String TAG = "UpdateService";
    public static final String ACTION = "me.shenfan.UPDATE_APP";
    public static final String STATUS = "status";
    public static final String PROGRESS = "progress";
    public static boolean DEBUG = BuildConfig.isLog;

    //下载大小通知频率
    public static final int UPDATE_NUMBER_SIZE = 4;
    public static final int DEFAULT_RES_ID = -1;

    public static final int UPDATE_PROGRESS_STATUS = 0;
    public static final int UPDATE_ERROR_STATUS = -1;
    public static final int UPDATE_SUCCESS_STATUS = 1;

    //params
    private static final String URL = "downloadUrl";
    private static final String ICO_RES_ID = "icoResId";
    private static final String ICO_SMALL_RES_ID = "icoSmallResId";
    private static final String UPDATE_PROGRESS = "updateProgress";
    private static final String STORE_DIR = "storeDir";
    private static final String DOWNLOAD_NOTIFICATION_FLAG = "downloadNotificationFlag";
    private static final String DOWNLOAD_SUCCESS_NOTIFICATION_FLAG = "downloadSuccessNotificationFlag";
    private static final String DOWNLOAD_ERROR_NOTIFICATION_FLAG = "downloadErrorNotificationFlag";
    private static final String IS_SEND_BROADCAST = "isSendBroadcast";
    private static final String APPVERSION = "appversion";


    private String downloadUrl;
    private int icoResId;             //default app ico
    private int icoSmallResId;
    private int updateProgress;   //update notification progress when it add number
    private String storeDir;          //default sdcard/Android/package/update
    private int downloadNotificationFlag;
    private int downloadSuccessNotificationFlag;
    private int downloadErrorNotificationFlag;
    private boolean isSendBroadcast;
    private AppVersion appVersion;

    private UpdateProgressListener updateProgressListener;
    private LocalBinder localBinder = new LocalBinder();

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        /**
         * set update progress call back
         *
         * @param listener
         */
        public void setUpdateProgressListener(UpdateProgressListener listener) {
            UpdateService.this.setUpdateProgressListener(listener);
        }
    }


    private boolean startDownload;//开始下载
    private int lastProgressNumber;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private int notifyId;
    private String appName;
    private LocalBroadcastManager localBroadcastManager;
    private Intent localIntent;
    private DownloadApk downloadApkTask;

    /**
     * whether debug
     */
    public static void debug() {
        DEBUG = true;
    }

    private static Intent installIntent(String path) {
        Intent install = null;
        if (path.endsWith(".apk")) {
            if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
                Uri apkUri = FileProvider.getUriForFile(GlobleContext.getInstance().getApplicationContext(), GlobleContext.getInstance().getApplicationContext().getPackageName() + ".FileProvider", new File(path));//在AndroidManifest中的android:authorities值
                install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        //        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        //            uri = Uri.fromFile(new File(path));
        //        } else {
        //            uri = FileProvider.getUriForFile(GlobleContext.getInstance().getApplicationContext(), GlobleContext.getInstance().getApplicationContext().getPackageName()+ ".FileProvider", new File(path));
        //        }
        //        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        //        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        return install;
    }

    private static Intent webLauncher(String downloadUrl) {
        Uri download = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, download);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "noName.apk";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    private static File getDownloadDir(UpdateService service) {
        File downloadDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (service.storeDir != null) {
                downloadDir = new File(Environment.getExternalStorageDirectory(), service.storeDir);
            } else {
                downloadDir = new File(service.getExternalCacheDir(), "update");
            }
        } else {
            downloadDir = new File(service.getCacheDir(), "update");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("id", AppConst.apkName, NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, "id").build();

            startForeground(0x123, notification);
        }
        appName = getApplicationName();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!startDownload && intent != null) {
            startDownload = true;
            downloadUrl = intent.getStringExtra(URL);
            icoResId = intent.getIntExtra(ICO_RES_ID, DEFAULT_RES_ID);
            icoSmallResId = intent.getIntExtra(ICO_SMALL_RES_ID, DEFAULT_RES_ID);
            storeDir = intent.getStringExtra(STORE_DIR);
            updateProgress = intent.getIntExtra(UPDATE_PROGRESS, UPDATE_NUMBER_SIZE);
            downloadNotificationFlag = intent.getIntExtra(DOWNLOAD_NOTIFICATION_FLAG, 0);
            downloadErrorNotificationFlag = intent.getIntExtra(DOWNLOAD_ERROR_NOTIFICATION_FLAG, 0);
            downloadSuccessNotificationFlag = intent.getIntExtra(DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, 0);
            appVersion = intent.getParcelableExtra(APPVERSION);
            isSendBroadcast = intent.getBooleanExtra(IS_SEND_BROADCAST, false);


            if (DEBUG) {
                Log.d(TAG, "downloadUrl: " + downloadUrl);
                Log.d(TAG, "icoResId: " + icoResId);
                Log.d(TAG, "icoSmallResId: " + icoSmallResId);
                Log.d(TAG, "storeDir: " + storeDir);
                Log.d(TAG, "updateProgress: " + updateProgress);
                Log.d(TAG, "downloadNotificationFlag: " + downloadNotificationFlag);
                Log.d(TAG, "downloadErrorNotificationFlag: " + downloadErrorNotificationFlag);
                Log.d(TAG, "downloadSuccessNotificationFlag: " + downloadSuccessNotificationFlag);
                Log.d(TAG, "isSendBroadcast: " + isSendBroadcast);
            }

            notifyId = startId;
            buildNotification();
            buildBroadcast();
            downloadApkTask = new DownloadApk(this);
            downloadApkTask.execute(downloadUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public UpdateService setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
        return this;
    }

    @Override
    public void onDestroy() {
        if (downloadApkTask != null) {
            downloadApkTask.cancel(true);
        }

        if (updateProgressListener != null) {
            updateProgressListener = null;
        }
        localIntent = null;
        builder = null;

        super.onDestroy();
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    private void buildBroadcast() {
        if (!isSendBroadcast) {
            return;
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localIntent = new Intent(ACTION);
    }

    private void sendLocalBroadcast(int status, int progress) {
        if (!isSendBroadcast || localIntent == null) {
            return;
        }
        localIntent.putExtra(STATUS, status);
        localIntent.putExtra(PROGRESS, progress);
        localBroadcastManager.sendBroadcast(localIntent);
    }

    private void buildNotification() {


        boolean isLollipop = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        int smallIcon = getResources().getIdentifier("artboard", "drawable", getPackageName());

        if (smallIcon <= 0 || !isLollipop) {
            smallIcon = getApplicationInfo().icon;
        }

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.update_app_model_prepare))
                .setWhen(System.currentTimeMillis())
                .setProgress(100, 1, false)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(), icoResId))
                .setDefaults(downloadNotificationFlag);

        manager.notify(notifyId, builder.build());
    }

    private void start() {
        builder.setContentTitle(appName);
        builder.setContentText(getString(R.string.update_app_model_prepare));
        manager.notify(notifyId, builder.build());
        sendLocalBroadcast(UPDATE_PROGRESS_STATUS, 1);
        if (updateProgressListener != null) {
            updateProgressListener.start();
        }
    }

    /**
     * @param progress download percent , max 100
     */
    private void update(int progress) {
        if (progress - lastProgressNumber > updateProgress) {
            lastProgressNumber = progress;
            builder.setProgress(100, progress, false);
            builder.setContentText(getString(R.string.update_app_model_progress, progress, "%"));
            manager.notify(notifyId, builder.build());
            sendLocalBroadcast(UPDATE_PROGRESS_STATUS, progress);
            if (updateProgressListener != null) {
                updateProgressListener.update(progress);
            }
        }
    }

    private void success(String path) {
        builder.setProgress(0, 0, false);
        builder.setContentText(getString(R.string.update_app_model_success));
        final Intent i = installIntent(path);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intent);
        builder.setDefaults(downloadSuccessNotificationFlag);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        sendLocalBroadcast(UPDATE_SUCCESS_STATUS, 100);
        if (updateProgressListener != null) {
            updateProgressListener.success();
        }
        appVersion.setLoadSuccess(true);
        EventBus.getDefault().post(appVersion);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //            if (getPackageManager().canRequestPackageInstalls()) {
        //                //已经同意权限在这里执行安装应用的代码
        //                startActivity(i);
        //            } else {
        //                //没有允许  需要去申请权限，由于这个权限不是运行时权限，所有需要用户手
        //
        //                //动去开启权限，可以给用户一个弹窗 提示用户去权限列表开启权限     开启设
        //
        //                //置的代码  8.0新的API
        //                ToastUtil.show(CommonUtils.getString(R.string.str_toast_install));
        //                Intent it = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        //                startActivity(it);
        //            }
        //
        //        } else {
        //            startActivity(i);
        //        }
        AndPermission.with(this)
                .install()
                .file(new File(path))
                .rationale(new Rationale<File>() {
                    @Override
                    public void showRationale(Context c, File f, RequestExecutor e) {
                        // 启动设置：e.execute();
                        // 取消启动：e.cancel();
                        e.execute();
                    }
                })
                .onGranted(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        //startActivity(i);
                    }
                })
                .onDenied(new Action<File>() {
                    @Override
                    public void onAction(File data) {
                        ToastUtil.show(CommonUtils.getString(R.string.no_install_permission));
                    }
                })
                .start();


        //        AppUtils.installApp(UpdateService.getDownloadDir(this),
        //                apkName);
    }

    private void error() {
        Intent i = webLauncher(downloadUrl);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentText(getString(R.string.update_app_model_error));
        builder.setContentIntent(intent);
        builder.setProgress(0, 0, false);
        builder.setDefaults(downloadErrorNotificationFlag);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        appVersion.setLoadSuccess(false);
        EventBus.getDefault().post(appVersion);
        sendLocalBroadcast(UPDATE_ERROR_STATUS, -1);
        if (updateProgressListener != null) {
            updateProgressListener.error();
        }
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String> {

        private WeakReference<UpdateService> updateServiceWeakReference;

        public DownloadApk(UpdateService service) {
            updateServiceWeakReference = new WeakReference<>(service);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                service.start();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            final String downloadUrl = params[0];

            final File file = new File(UpdateService.getDownloadDir(updateServiceWeakReference.get()),
                    AppConst.apkName);
            if (DEBUG) {
                Log.d(TAG, "download url is " + downloadUrl);
                Log.d(TAG, "download apk cache at " + file.getAbsolutePath());
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            int updateTotalSize = 0;
            java.net.URL url;
            try {
                url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(20000);
                httpConnection.setReadTimeout(20000);

                if (DEBUG) {
                    Log.d(TAG, "download status code: " + httpConnection.getResponseCode());
                }

                if (httpConnection.getResponseCode() != 200) {
                    return null;
                }

                updateTotalSize = httpConnection.getContentLength();

                if (file.exists()) {
                    if (updateTotalSize == file.length()) {
                        // 下载完成
                        return file.getAbsolutePath();
                    } else {
                        file.delete();
                    }
                }
                file.createNewFile();
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(file, false);
                byte buffer[] = new byte[4096];

                int readSize = 0;
                int currentSize = 0;

                while ((readSize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readSize);
                    currentSize += readSize;
                    publishProgress((currentSize * 100 / updateTotalSize));
                }
                // download success
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (DEBUG) {
                Log.d(TAG, "current progress is " + values[0]);
            }
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                service.update(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                if (s != null) {
                    service.success(s);
                } else {
                    service.error();
                }
            }
        }
    }


    /**
     * a builder class helper use UpdateService
     */
    public static class Builder {

        private String downloadUrl;
        private int icoResId = DEFAULT_RES_ID;             //default app ico
        private int icoSmallResId = DEFAULT_RES_ID;
        private int updateProgress = UPDATE_NUMBER_SIZE;   //update notification progress when it add number
        private String storeDir;          //default sdcard/Android/package/update
        private int downloadNotificationFlag;
        private int downloadSuccessNotificationFlag;
        private int downloadErrorNotificationFlag;
        private boolean isSendBroadcast;
        private AppVersion appVersion;

        public AppVersion getAppVersion() {
            return appVersion;
        }

        public Builder setAppVersion(AppVersion appVersion) {
            this.appVersion = appVersion;
            return this;
        }

        protected Builder(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public static Builder create(String downloadUrl) {
            if (downloadUrl == null) {
                throw new NullPointerException("downloadUrl == null");
            }
            return new Builder(downloadUrl);
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public int getIcoResId() {
            return icoResId;
        }

        public Builder setIcoResId(int icoResId) {
            this.icoResId = icoResId;
            return this;
        }

        public int getIcoSmallResId() {
            return icoSmallResId;
        }

        public Builder setIcoSmallResId(int icoSmallResId) {
            this.icoSmallResId = icoSmallResId;
            return this;
        }

        public int getUpdateProgress() {
            return updateProgress;
        }

        public Builder setUpdateProgress(int updateProgress) {
            if (updateProgress < 1) {
                throw new IllegalArgumentException("updateProgress < 1");
            }
            this.updateProgress = updateProgress;
            return this;
        }

        public String getStoreDir() {
            return storeDir;
        }

        public Builder setStoreDir(String storeDir) {
            this.storeDir = storeDir;
            return this;
        }

        public int getDownloadNotificationFlag() {
            return downloadNotificationFlag;
        }

        public Builder setDownloadNotificationFlag(int downloadNotificationFlag) {
            this.downloadNotificationFlag = downloadNotificationFlag;
            return this;
        }

        public int getDownloadSuccessNotificationFlag() {
            return downloadSuccessNotificationFlag;
        }

        public Builder setDownloadSuccessNotificationFlag(int downloadSuccessNotificationFlag) {
            this.downloadSuccessNotificationFlag = downloadSuccessNotificationFlag;
            return this;
        }

        public int getDownloadErrorNotificationFlag() {
            return downloadErrorNotificationFlag;
        }

        public Builder setDownloadErrorNotificationFlag(int downloadErrorNotificationFlag) {
            this.downloadErrorNotificationFlag = downloadErrorNotificationFlag;
            return this;
        }

        public boolean isSendBroadcast() {
            return isSendBroadcast;
        }

        public Builder setIsSendBroadcast(boolean isSendBroadcast) {
            this.isSendBroadcast = isSendBroadcast;
            return this;
        }

        public Builder build(Context context) {
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            Intent intent = new Intent();
            intent.setClass(context, UpdateService.class);
            intent.putExtra(URL, downloadUrl);

            if (icoResId == DEFAULT_RES_ID) {
                icoResId = getIcon(context);
            }

            if (icoSmallResId == DEFAULT_RES_ID) {
                icoSmallResId = icoResId;
            }
            intent.putExtra(ICO_RES_ID, icoResId);
            intent.putExtra(STORE_DIR, storeDir);
            intent.putExtra(ICO_SMALL_RES_ID, icoSmallResId);
            intent.putExtra(UPDATE_PROGRESS, updateProgress);
            intent.putExtra(DOWNLOAD_NOTIFICATION_FLAG, downloadNotificationFlag);
            intent.putExtra(DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, downloadSuccessNotificationFlag);
            intent.putExtra(DOWNLOAD_ERROR_NOTIFICATION_FLAG, downloadErrorNotificationFlag);
            intent.putExtra(IS_SEND_BROADCAST, isSendBroadcast);
            intent.putExtra(APPVERSION, appVersion);

            ContextCompat.startForegroundService(context, intent);

            return this;
        }

        private int getIcon(Context context) {

            final PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = null;
            try {
                appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (appInfo != null) {
                return appInfo.icon;
            }
            return 0;
        }
    }

}
