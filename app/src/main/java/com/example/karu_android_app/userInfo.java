package com.example.karu_android_app;

public class userInfo {
    String fullName,Email,phnNum,Dob;

    public userInfo() {
    }

    public userInfo(String fullName, String email, String phnNum, String dob) {
        this.fullName = fullName;
        Email = email;
        this.phnNum = phnNum;
        this.Dob = dob;
    }

    public userInfo(String fullName, String phnNum, String dob) {
        this.fullName = fullName;
        this.phnNum = phnNum;
        this.Dob = dob;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhnNum() {
        return phnNum;
    }

    public void setPhnNum(String phnNum) {
        this.phnNum = phnNum;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }
}
