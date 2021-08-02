package com.aariyan.loan_user.Model;

import java.util.List;

public class UserDetailsModel {
    private String id,userId,userName,permanentAddress,currentAddress,aadharNumber,panNumber,BankName,branch,code,bankAccount;
    private String dob,gender;
    private String memberId;
    private List<ImageModel> list;

    public UserDetailsModel(){}

    public UserDetailsModel(String id, String userId, String userName, String permanentAddress, String currentAddress, String aadharNumber, String panNumber, String bankName, String branch, String code, String bankAccount, String dob, String gender, List<ImageModel> list,String memberId) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.permanentAddress = permanentAddress;
        this.currentAddress = currentAddress;
        this.aadharNumber = aadharNumber;
        this.panNumber = panNumber;
        BankName = bankName;
        this.branch = branch;
        this.code = code;
        this.bankAccount = bankAccount;
        this.dob = dob;
        this.gender = gender;
        this.list = list;
        this.memberId = memberId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<ImageModel> getList() {
        return list;
    }

    public void setList(List<ImageModel> list) {
        this.list = list;
    }
}
