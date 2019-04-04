package com.sixbexchange.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/*
* 资产行情数据处理
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:48
* @Param
* @return
**/
public class BigUIUtil {

    private static class helper {
        private static BigUIUtil exchangeRateUtil = new BigUIUtil();
    }

    public static BigUIUtil getinstance() {
        return helper.exchangeRateUtil;
    }

    private BigUIUtil() {
        initSymbol();
    }

    LinkedHashMap<String, String> symbol;

    private void initSymbol() {
        symbol = new LinkedHashMap<>();
        //        （CNY）¥
        //（USD）$
        //（EUR）€
        //（GBP）￡
        //（JPY）円
        //（KRW）₩
        //（LTC）Ł
        //                (BTC)฿
        //                (ETH)E
        symbol.put("CNY", "¥");
        symbol.put("USD", "$");
        symbol.put("EUR", "€");
        symbol.put("GBP", "￡");
        symbol.put("JPY", "円");
        symbol.put("KRW", "₩");
        symbol.put("LTC", "Ł");
        symbol.put("BTC", "฿");
        symbol.put("ETH", "E");
        symbol.put("USDT", "₮");
    }

    public String getSymbol(String unit) {
        if (symbol == null) {
            return unit;
        }
        if (TextUtils.isEmpty(unit)) {
            return unit;
        }
        for (Map.Entry<String, String> entry : symbol.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k.equals(unit)) {
                return v;
            }
        }
        return unit;
    }

    public String bigEnglishNum(String change, int size) {
        if (TextUtils.isEmpty(change)) {
            return "";
        }
        if ("null".equals(change)) {
            return "";
        }
        if (!UiHeplUtils.isDouble(change)) {
            return change;
        }
        StringBuffer stringBuffer = new StringBuffer(",###,###,###,###,###,###");
        if (size > 0) {
            stringBuffer.append(".");
            for (int i = 0; i < size; i++) {
                stringBuffer.append("0");
            }
        }
        BigDecimal bigDecimal = new BigDecimal(change);
        DecimalFormat df = new DecimalFormat(stringBuffer.toString());
        String format = df.format(bigDecimal);
        if (ObjectUtils.equals(format.charAt(0), '.')) {
            format = "0" + format;
        }
        return format;
    }

}
