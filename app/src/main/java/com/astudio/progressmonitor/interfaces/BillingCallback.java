package com.astudio.progressmonitor.interfaces;

import com.android.billingclient.api.Purchase;
import com.astudio.progressmonitor.support.MessageDecoder;

import org.json.JSONException;

public interface BillingCallback {


    interface Presenter{
        //void writePurchaseTokenRemoteDB(String token); // для проверки токена покупки на сервере, пока не использую 07-02-2021
        void setBillingClient(BillingCallback.BillingClient client);
        //void unblockGroup(int year, String productId, String purchaseToken);
        void resultUnblockGroup(Boolean result);
        void failUnblockGroup(MessageDecoder.Codes code);
        void processPurchase(Purchase purchase);

    }


    interface View{
        void failLaunchBillingFlow(MessageDecoder.Codes code);
        void successAlertDialog(MessageDecoder.Codes code);
        //void purchaseFlowUserCanceled(MessageDecoder.Codes code);
        void pendingPurchase();
        void successfulEarlyPaidPurchase();
        void showPaymentInterface();
    }


    interface BillingClient{
        void resultUnblockGroup(boolean result);
    }



}
