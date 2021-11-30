package com.astudio.progressmonitor.modules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {


    public static String checkString(String param){
        return param;
    }


    public boolean checkPhoneNumberRu(String number){
        if(number != null){
            Pattern pattern = Pattern.compile("^[+]{1}[7]{1}[0-9]{10}$");
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        }
        return false;
    }


    public boolean checkPhoneNumber(String number){
        if(number != null){
            Pattern pattern = Pattern.compile("^[+]{1}[0-9]{10,15}$");
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        }
        return false;
    }


    public boolean checkEmail(String email) {
        if(email != null){
            Pattern pattern = Pattern.compile("^.+@.+\\..+$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        return false;
    }


    public boolean alternativeCheckEmail(String email){
        boolean result = true;
//        try {
//            InternetAddress emailAddress = new InternetAddress(email);
//            emailAddress.validate();
//        } catch (AddressException ex) {
//            result = false;
//        }
        return result;
    }


    public boolean checkSmsCode(String code){
        if(code != null){
            Pattern pattern = Pattern.compile("^[0-9]{6}$");
            Matcher matcher = pattern.matcher(code);
            return matcher.matches();
        }
        return false;
    }


    // RemoteDataTask
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkDataFormat(String sourceString){
        //String sourceString2 = new String("2020-07-07 12:29:19");
        //Pattern pattern = Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}\\s[0-9]{2}[:][0-9]{2}[:][0-9]{2}$");
        if (sourceString != null) {
            Pattern pattern = Pattern.compile("^[0-9]{4}[-][0-9]{2}[-][0-9]{2}\\s[0-9]{2}[:][0-9]{2}\\W[0-9]{2}$"); //непонятно почему последн. двоеточие вызывает ошибку
            Matcher matcher = pattern.matcher(sourceString);
            return matcher.matches();
        }
        return false; //TODO: mock
    }


    public boolean checkResponseCode(int code){
        return code >= 200 & code <= 301 ;
    }


    public String cutMessage(String sourceMessage, int limit){
        //int limit = 200;
        return sourceMessage.length() > limit ? sourceMessage.substring(0, limit) : sourceMessage;
    }


}
