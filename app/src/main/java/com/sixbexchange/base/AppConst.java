package com.sixbexchange.base;


import com.sixbexchange.BuildConfig;

/**
 * 全局配置
 */
public class AppConst {

    //http://47.98.56.206:1913/symbol/info?name=testCoin.
    public static final boolean isSSL = BuildConfig.isSSL;

    public static final String app2BaseUrl = BuildConfig.app2BaseUrl;// "https://tblz.bicoin.com.cn";
    public static final boolean isTest = false;

//    public static final String app2BaseUrl = "http://test.6b.top";
//    public static final boolean isTest = true;


    //public static final boolean isSSL = false;
    //    public static final String app2BaseUrl = "http://ex.bicoin.info";
    //    public static final boolean isSSL = false;
    //    public static final String serviceId = BuildConfig.serviceId;// "KEFU151728371459995";
    //-javaagent:D:\android\ide\IntelliJ IDEA 2018.3.4\bin\JetbrainsCrack-2.6.9-release-enc.jar
    //           public static final String wsAddress = "ws:" + "//192.168.0.22:1903/ws/";// ;
    //    public static final String wsAddressRegisterkeys = "http://192.168.0.22:1903/ws/resubscript";// ;

    public static final String wsAddress = "wss://1token.trade/api/v1/ws/tick";//;


    public static final String pushId = BuildConfig.pushId;// "5afcd70bf43e483b6900038c";
    public static final String pushSecret = BuildConfig.pushSecret;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String xiaoMiPushId = BuildConfig.XiaoMiPushId;// "5afcd70bf43e483b6900038c";
    public static final String xiaoMiPushKey = BuildConfig.XiaoMiPushKey;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String meizuAppId = BuildConfig.meizuAppId;// BuildConfig.meizuAppId;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String meizuAppKey = BuildConfig.meizuAppKey;//

    public static final String CACHE_EXCHANGE_RATE = "cache_exchange_rate";//汇率缓存
    public static final String CACHE_CUSTOM_RATE = "cache_custom_rate";//自定义汇率
    public static final String CACHE_BITMEX = "cache_bitmex";//自定义汇率
    public static final String CACHE_SELECT_COIN = "cache_select_coin";//自定义汇率
    public static final String CACHE_SELECT_COIN_POSITION = "cache_select_coin_position";//自定义汇率


    public static final String httpsCer = "json/bm.cer";
    public static final String umS1 = "Bradar";
    public static final String aliasS1 = "bradar";
    public static final String packageName = "com.gqfbitmex";
    public static final String apkName = "币coin极速交易版.apk";
    public static final boolean isChinese = true;

    private static final String webUrl = "http://116.62.232.175:3002";


}
