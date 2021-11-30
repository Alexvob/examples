package com.astudio.progressmonitor.support;

import com.google.gson.annotations.Expose;
import java.util.Arrays;


public class ErrorMessage {

    private int userId;
    private String source;
    private String message;

    @Expose
    private String descError;

    public ErrorMessage(String source, int userId, String message){
        this.userId = userId;
        this.source = source;
        this.message = message;
        setDescError();
    }

    public ErrorMessage(StackTraceElement[] stackTrace, int userId, String message){
        this.userId = userId;
        this.source = Arrays.toString(stackTrace);
        this.message = message;
        setDescError();
    }

    public String getDescError() {
        return descError;
    }

    private void setDescError() {
        this.descError = " UserId: " + userId + "| Source: " + source + "| Message: " + message;
    }

    public int getUserId() {
        return userId;
    }


}
