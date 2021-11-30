package com.astudio.progressmonitor.support;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class PurchaseCustom {

    @SerializedName("group_token")
    @Expose
    public String groupToken;

    @SerializedName("order_id")
    @Expose
    public String orderId;

    @SerializedName("product_id")
    @Expose
    public String productId;

    @SerializedName("purchase_time")
    @Expose
    public Long purchaseTime;

    @SerializedName("purchase_state")
    @Expose
    public int purchaseState;

    @SerializedName("purchase_token")
    @Expose
    public String purchaseToken;

    @SerializedName("acknowledgement_state")
    @Expose
    public int acknowledgementState;

    @SerializedName("consumption_state")
    @Expose
    public int consumptionState;


//    public PurchaseCustom(String groupToken, String orderId, String productId, Long purchaseTime,
//                          int purchaseState, String purchaseToken, int acknowledgementState, int consumptionState) {
//        this.groupToken = groupToken;
//        this.orderId = orderId;
//        this.productId = productId;
//        this.purchaseTime = purchaseTime;
//        this.purchaseState = purchaseState;
//        this.purchaseToken = purchaseToken;
//        this.acknowledgementState = acknowledgementState;
//        this.consumptionState = consumptionState;
//    }


    @NonNull
    @Override
    public String toString() {
        return "PurchaseCustom{" +
                "groupToken='" + groupToken + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", purchaseTime=" + purchaseTime +
                ", purchaseState=" + purchaseState +
                ", purchaseToken='" + purchaseToken + '\'' +
                ", acknowledgementState=" + acknowledgementState +
                ", consumptionState=" + consumptionState +
                '}';
    }

}
