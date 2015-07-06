package com.nyu.blife_app.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

/**
 * Created by Yeshwant on 01/04/2015.
 */
@ParseClassName("User")
public class User extends ParseObject {
    public User(){

    }
    /** getter methods */
    public int getUserId(){
        return getInt("userId");
    }

    public String getUsername(){
        return getString("username");
    }

    public String getPassword(){
        return getString("password");
    }

    public Long getPhoneNumber(){
        return getLong("phoneNumber");
    }

    public String getFirstName(){
        return getString("firstName");
    }

    public String getLastName(){
        return getString("lastName");
    }

    public String getCity(){
        return getString("city");
    }

    public int getZipCode(){
        return getInt("zipCode");
    }

    public boolean getIsDonor(){
        return getBoolean("isDonor");
    }

    public Date getDOB(){
        return getDate("DOB");
    }

    public String getBloodGroup(){
        return getString("bloodGroup");
    }

    public int getWeight(){
        return getInt("weight");
    }

    public boolean getHasDisease(){
        return getBoolean("hasDisease");
    }

    public String getGender(){
        return getString("gender");
    }

    public  boolean isPregnant(){
        return getBoolean("pregnant");
    }

    /** setter methods */

    public void setUserId(int userId){
        put("userID", userId);
    }

    public void setUsername(String username){
        put("username", username);
    }

    public void setPassword(String password){
        put("password", password);
    }

    public void setPhoneNumber(Long phoneNumber){
        put("phoneNumber", phoneNumber);
    }

    public void setFirstName(String firstName){
        put("firstName", firstName);
    }

    public void setLastName(String lastName){
        put("lastName", lastName);
    }

    public void setCity(String city){
        put("city", city);
    }

    public void setZipCode(int zipCode){
        put("zipCode", zipCode);
    }

    public void setIsDonor(Boolean isDonor){
        put("isDonor", isDonor);
    }

    public void setDOB(String DOB){
        put("DOB", DOB);
    }

    public void setBloodGroup(String bloodGroup){
        put("bloodGroup", bloodGroup);
    }

    public void setWeight(int weight){
        put("weight", weight);
    }

    public void setHasDisease(Boolean hasDisease){
        put("hasDisease", hasDisease);
    }

    public void setGender(String gender){
        put("gender", gender);
    }

    public void setIsPregnant(Boolean isPregnant){
        put("isPregnant", isPregnant);
    }

    public static ParseQuery<User> getQuery() {
        return ParseQuery.getQuery(User.class);
    }
}
