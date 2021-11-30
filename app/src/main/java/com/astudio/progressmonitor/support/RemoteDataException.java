package com.astudio.progressmonitor.support;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.modules.Validator;

public class RemoteDataException extends Exception{

    private String message;

    public RemoteDataException(String message) {
        this.message = message;
    }


    public RemoteDataException(int errorCode, String sourceMessage){
        Validator validator = App.getInstance().getValidator();
        int userId = App.getInstance().getCurrentUserId();
        this.message = "UserId: " +userId + " | Server code-" + errorCode + " | ErrorMessage: " + validator.cutMessage(cleanTags(sourceMessage), 300);
    }


    private String cleanTags(String message){
        String substring = "Page Not Found";
        if(message.contains(substring)){
            message = "Page Not Found";
        }
        return message;
    }


    //@Nullable
    @Override
    public String getMessage() {
        return message;
    }



    public static class GroupDeleted extends RemoteDataException{

        public GroupDeleted(String message) {
            super(message);
        }

        public GroupDeleted(int errorCode, String sourceMessage) {
            super(errorCode, sourceMessage);
        }
    }


}

