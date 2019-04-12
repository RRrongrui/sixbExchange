package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/12 0012.
 */

public class TradeDetailBean {

    /**
     * id : 1
     * exchange : okef
     * exchangeName : OKEx交割合约
     * currencyPair : okef/btc.usd.t
     * currency : BTC
     * currencyIcon : http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BTC.png
     * exchangeIcon : http://topcoin.oss-cn-hangzhou.aliyuncs.com/exchange/exchange_okex.png
     * currencyPairName : BTC当周
     * priceUnit : USD
     * amountUnit : 张
     * minPriceUnit : 0.01
     * minAmountUnit : 1
     * marginUnit : BTC
     * index : okef/btc.usd.i
     * textName : 全仓 20X
     * fee : 0.7
     * secondType : 当周
     */

    private String id;
    private String exchange;
    private String exchangeName;
    private String currencyPair;
    private String currency;
    private String currencyIcon;
    private String exchangeIcon;
    private String currencyPairName;
    private String priceUnit;
    private String amountUnit;
    private String minPriceUnit;
    private String minAmountUnit;
    private String marginUnit;
    private String index;
    private String textName;
    private String fee;
    private String secondType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyIcon() {
        return currencyIcon;
    }

    public void setCurrencyIcon(String currencyIcon) {
        this.currencyIcon = currencyIcon;
    }

    public String getExchangeIcon() {
        return exchangeIcon;
    }

    public void setExchangeIcon(String exchangeIcon) {
        this.exchangeIcon = exchangeIcon;
    }

    public String getCurrencyPairName() {
        return currencyPairName;
    }

    public void setCurrencyPairName(String currencyPairName) {
        this.currencyPairName = currencyPairName;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getMinPriceUnit() {
        return minPriceUnit;
    }

    public void setMinPriceUnit(String minPriceUnit) {
        this.minPriceUnit = minPriceUnit;
    }

    public String getMinAmountUnit() {
        return minAmountUnit;
    }

    public void setMinAmountUnit(String minAmountUnit) {
        this.minAmountUnit = minAmountUnit;
    }

    public String getMarginUnit() {
        return marginUnit;
    }

    public void setMarginUnit(String marginUnit) {
        this.marginUnit = marginUnit;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTextName() {
        return textName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }
}
