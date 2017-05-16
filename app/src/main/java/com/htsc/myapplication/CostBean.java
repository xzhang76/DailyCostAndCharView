package com.htsc.myapplication;

import java.io.Serializable;

/**
 *
 * Created by zhangxiaoting on 2017/3/22.
 */
public class CostBean implements Serializable{
    public String costTitle;
    public String costDate;
    public String costMoney;

    public String getCostTitle() {
        return costTitle;
    }

    public String getCostDate() {
        return costDate;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostTitle(String costTitle) {
        this.costTitle = costTitle;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }
}
