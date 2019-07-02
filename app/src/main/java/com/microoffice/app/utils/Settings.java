package com.microoffice.app.utils;

import android.location.Location;

/**
 * Created by com.moffice.com.microoffice.app on 02/18/2017.
 */

public  class Settings {
    boolean regStatus;
    private int regReqStatus=0;
    boolean instTypeSet;
    int instType;
    String name;
    String mobile;
    String company;
    private String adminEmail;
    String email;
    String topic;
    private String secret;

    public int getRegReqStatus() {
        return regReqStatus;
    }

    public void setRegReqStatus(int regReqStatus) {
        this.regReqStatus = regReqStatus;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public class OfficeLocs{
        boolean enabled;
        int officeNum;
        String officeName;
        Location loc;
    }
    OfficeLocs [] offices = new OfficeLocs[5];

    public Settings(){
        for(int i=0;i<5;++i) {
            offices[i] = new OfficeLocs();
        }
    }
    public boolean isInstTypeSet() {
        return instTypeSet;
    }

    public void setInstTypeSet(boolean instTypeSet) {
        this.instTypeSet = instTypeSet;
    }

    public int getInstType() {
        return instType;
    }

    public void setInstType(int instType) {
        this.instType = instType;
    }

    String userUUID;



    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public boolean isRegStatus() {
        return regStatus;
    }

    public void setRegStatus(boolean regStatus) {
        this.regStatus = regStatus;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}


