package com.astudio.progressmonitor.support;

import android.content.Context;

import com.astudio.progressmonitor.R;

public class MessageDecoder {

    private Context context;
    private MessageDecoder.Codes code;

    public MessageDecoder(Context context, MessageDecoder.Codes code) {
        this.context = context;
        this.code = code;
    }


    public String decode(){
        switch (code){
            case SUCCESS: return context.getResources().getString(R.string.alert_dialog_mess_success_non_details);
            case FAIL_CUT: return context.getResources().getString(R.string.alert_dialog_mess_fail_cut);
            case FAIL_FULL: return context.getResources().getString(R.string.alert_dialog_mess_fail_full);
            case CONNECT_FAIL: return context.getResources().getString(R.string.alert_dialog_mess_connect_fail);
            case USER_CREATE_SUCCESS: return context.getResources().getString(R.string.alert_dialog_mess_success_add_user);
            case USER_DELETE_SUCCESS: return context.getResources().getString(R.string.alert_dialog_mess_success_delete_user);
            case USER_DOUBLE: return context.getResources().getString(R.string.alert_dialog_mess_double_add_user);
            case USER_NOT_FOUND: return context.getResources().getString(R.string.alert_dialog_mess_user_not_found);
            case YOU_DELETED: return context.getResources().getString(R.string.alert_dialog_mess_you_deleted);
            case GROUP_BLOCKED: return context.getResources().getString(R.string.alert_dialog_mess_group_blocked);
            case GROUP_NOT_FOUND: return context.getResources().getString(R.string.alert_dialog_mess_group_not_found);
            case USER_FAIL_PHONE_OR_PASSWORD: return context.getResources().getString(R.string.alert_dialog_mess_fail_phone_or_password);
            case BILLING_SUCCESS: return context.getResources().getString(R.string.billing_success);
            case BILLING_FAIL: return context.getResources().getString(R.string.billing_fail);
            case BILLING_USER_CANCELED: return context.getResources().getString(R.string.billing_user_canceled);
            case BILLING_FAIL_REFUND: return context.getResources().getString(R.string.billing_fail_refund);
            case SUCCESS_SYNCHRONIZED: return context.getResources().getString(R.string.success_synchronized);
            case FAIL_SYNCHRONIZED: return context.getResources().getString(R.string.fail_synchronized);
            case NEW_TASK: return context.getResources().getString(R.string.new_task_notification);
            case SELECT_NEW_PARENT: return context.getResources().getString(R.string.prompt_change_new_superior);
            case SELECT_EMPLOYEES: return context.getResources().getString(R.string.post_checkbox_change_employees);
            case REQUEST_TOO_LARGE: return context.getResources().getString(R.string.promo_request_too_large);

            default: return "";
        }
    }

    
    public enum Codes {

        SUCCESS, FAIL_CUT, FAIL_FULL, CONNECT_FAIL,
        USER_CREATE_SUCCESS, USER_DELETE_SUCCESS, USER_DOUBLE,
        USER_NOT_FOUND, USER_FAIL_PHONE_OR_PASSWORD,
        YOU_DELETED, GROUP_BLOCKED, GROUP_NOT_FOUND, BILLING_SUCCESS, BILLING_FAIL, BILLING_USER_CANCELED, BILLING_FAIL_REFUND,
        SUCCESS_SYNCHRONIZED, FAIL_SYNCHRONIZED, NEW_TASK, SELECT_NEW_PARENT, SELECT_EMPLOYEES, REQUEST_TOO_LARGE

    }


}
