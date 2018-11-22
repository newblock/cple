package com.qcx.mini.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/8/14.
 *
 */

public class BankAccountListEntity extends Entity {
    /**
     * {"allBankCard":[{"bank":"ICBC","bankCard":"6212261102785485649","bankCardSub":"5649"}],
     * "status":200}
     */
    private List<BankAccount> allBankCard;
    @SerializedName(value = "BankInfo")
    private BankAccount bankInfo;
    private int status;

    public List<BankAccount> getAllBankCard() {
        return allBankCard;
    }

    public void setAllBankCard(List<BankAccount> allBankCard) {
        this.allBankCard = allBankCard;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BankAccount getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankAccount bankInfo) {
        this.bankInfo = bankInfo;
    }

    public static class BankAccount implements Parcelable{
        private String bank;
        private String bankCard;
        private String bankCardSub;
        private boolean isChecked;
        private boolean isDeleted;
        private boolean isShowDelete;

        public BankAccount(){}

        protected BankAccount(Parcel in) {
            bank = in.readString();
            bankCard = in.readString();
            bankCardSub = in.readString();
        }

        public static final Creator<BankAccount> CREATOR = new Creator<BankAccount>() {
            @Override
            public BankAccount createFromParcel(Parcel in) {
                return new BankAccount(in);
            }

            @Override
            public BankAccount[] newArray(int size) {
                return new BankAccount[size];
            }
        };

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        public boolean isShowDelete() {
            return isShowDelete;
        }

        public void setShowDelete(boolean showDelete) {
            isShowDelete = showDelete;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankCard() {
            return bankCard;
        }

        public void setBankCard(String bankCard) {
            this.bankCard = bankCard;
        }

        public String getBankCardSub() {
            return bankCardSub;
        }

        public void setBankCardSub(String bankCardSub) {
            this.bankCardSub = bankCardSub;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(bank);
            dest.writeString(bankCard);
            dest.writeString(bankCardSub);
        }
    }
}
