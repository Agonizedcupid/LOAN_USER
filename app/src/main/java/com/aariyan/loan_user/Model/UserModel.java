package com.aariyan.loan_user.Model;

public class UserModel {
    private String userId,userName,emailId,phoneNumber,Password;
    private String imageUrl;

    public UserModel(){}

    public UserModel(String userId, String userName, String emailId, String phoneNumber, String password, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        Password = password;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
