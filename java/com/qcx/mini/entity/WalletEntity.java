package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class WalletEntity extends Entity {
    private String aliPay;
    private double moneyCard;
    private int count;
    private double moneyEnchashment;
    private List<MoneyEntity> waterBill;
    private double highMoney;
    private double moneyIncome;
    private int status;
    private double discountMoney;
    private String bankcard;
    private String bank;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public double getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getAliPay() {
        return aliPay == null ? "" : aliPay;
    }

    public void setAliPay(String aliPay) {
        this.aliPay = aliPay;
    }

    public double getMoneyCard() {
        return moneyCard;
    }

    public void setMoneyCard(double moneyCard) {
        this.moneyCard = moneyCard;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMoneyEnchashment() {
        return moneyEnchashment;
    }

    public void setMoneyEnchashment(double moneyEnchashment) {
        this.moneyEnchashment = moneyEnchashment;
    }

    public List<MoneyEntity> getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(List<MoneyEntity> waterBill) {
        this.waterBill = waterBill;
    }

    public double getHighMoney() {
        return highMoney;
    }

    public void setHighMoney(double highMoney) {
        this.highMoney = highMoney;
    }

    public double getMoneyIncome() {
        return moneyIncome;
    }

    public void setMoneyIncome(double moneyIncome) {
        this.moneyIncome = moneyIncome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class MoneyEntity extends Entity {
        private double billMoney;
        private String billTime;
        private int enchshmentState;
        private int mark;

        public double getBillMoney() {
            return billMoney;
        }

        public void setBillMoney(double billMoney) {
            this.billMoney = billMoney;
        }

        public String getBillTime() {
            return billTime;
        }

        public void setBillTime(String billTime) {
            this.billTime = billTime;
        }

        public int getEnchshmentState() {
            return enchshmentState;
        }

        public void setEnchshmentState(int enchshmentState) {
            this.enchshmentState = enchshmentState;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }
    }
}
