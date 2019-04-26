package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/15 0015.
 * 交易 可开 可用 信息
 */

public class TransactionBean {
    /**
     * usableOpenMore : 0
     * usableOpenSpace : 0
     * availableOpenMore : 0
     * availableOpenSpace : 0
     * availableflatMore : null
     * availableflatSpace : null
     */

    private String usableOpenMore="";
    private String usableOpenSpace="";
    private String availableOpenMore="";
    private String availableOpenSpace="";
    private String availableflatMore="";
    private String availableflatSpace="";

    public String getUsableOpenMore() {
        return usableOpenMore;
    }

    public void setUsableOpenMore(String usableOpenMore) {
        this.usableOpenMore = usableOpenMore;
    }

    public String getUsableOpenSpace() {
        return usableOpenSpace;
    }

    public void setUsableOpenSpace(String usableOpenSpace) {
        this.usableOpenSpace = usableOpenSpace;
    }

    public String getAvailableOpenMore() {
        return availableOpenMore;
    }

    public void setAvailableOpenMore(String availableOpenMore) {
        this.availableOpenMore = availableOpenMore;
    }

    public String getAvailableOpenSpace() {
        return availableOpenSpace;
    }

    public void setAvailableOpenSpace(String availableOpenSpace) {
        this.availableOpenSpace = availableOpenSpace;
    }

    public String getAvailableflatMore() {
        return availableflatMore;
    }

    public void setAvailableflatMore(String availableflatMore) {
        this.availableflatMore = availableflatMore;
    }

    public String getAvailableflatSpace() {
        return availableflatSpace;
    }

    public void setAvailableflatSpace(String availableflatSpace) {
        this.availableflatSpace = availableflatSpace;
    }
}
