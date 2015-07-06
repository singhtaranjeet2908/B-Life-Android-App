package com.nyu.blife_app.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;


import java.util.Date;

/**
 * Created by Yeshwant on 01/04/2015.
 */

@ParseClassName("BloodRequest")
public class BloodRequest extends ParseObject {
    public BloodRequest(){

    }

    /**getter methods */

    public String getrequestorName(){
        return getString("requestorName");
    }

    public String getBloodGroup(){
        return getString("bloodGroup");
    }

    public String getLocation(){
        return getString("location");
    }

    public String getCity(){
        return getString("city");
    }

    public String getMessage(){
        return getString("message");
    }

    public Date getRequiredBefore(){
        return getDate("requiredBefore");
    }

    public String getCellNumber(){
        return getString("cellNumber");
    }

    public String getRequestStatus(){
        return getString("requestStatus");
    }

    public String getUserName(){
        return getString("username");

    }

    public int getVerificationCode(){
        return getInt("verificationCode");
    }

    /**setter methods */
    public void setrequestorName (String requestorName){
        put("requestorName", requestorName);
    }

    public void setBloodGroup(String bloodGroup){
        put("bloodGroup", bloodGroup);
    }

    public void setLocation(String location){
        put("location", location);
    }

    public void setCity(String city){
        put("city", city);
    }

    public void setMessage(String message){
        put("message", message);
    }

    public void setRequiredBefore(Date requiredBefore){
        put("requiredBefore", requiredBefore);
    }

    public void setCellNumber(String cellNumber){
        put("cellNumber", cellNumber);
    }

    public void setRequestStatus(String requestStatus){
        put("requestStatus", requestStatus);
    }

    public void setUserName(String username){
        put("username", username);
    }

    public void setVerificationCode(int verificationCode){
        put("verificationCode",verificationCode);
    }
}

